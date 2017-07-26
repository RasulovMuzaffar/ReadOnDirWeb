package arm.wr;

import arm.ent.Users;
import arm.tableutils.HtmlTable;
import arm.tableutils.tablereaders.CompositeReader;
import arm.tableutils.tablereaders.MultipleResultsException;
import arm.tableutils.sprtemplates.Spravka02Reader;
import arm.tableutils.sprtemplates.Spravka902Reader;
import arm.tableutils.sprtemplates.Spravka93Reader;
import arm.tableutils.sprtemplates.Spravka95Reader;
import arm.test.Auth;
import arm.ws.WS;
import static arm.ws.WS.armUsers;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.websocket.Session;

/**
 *
 * @author Muzaffar
 */
public class ReadOnDir extends Thread {

    /**
     * @param args the command line arguments
     */
    static String p = "c:\\testFolder\\in";
//    static String p = "C:\\soob\\in";

    private static final String URL = "jdbc:mysql://localhost:3306/armasoup";
    private static final String USER = "root";
    private static final String PASS = "123456";

    @Override
    public void run() {
        System.out.println("thread!!!");
//        if (Thread.currentThread().isInterrupted()) {
        pathListener();
//        }
    }

    private static void pathListener() {
// init composite reader - register all reader types
        tableReader.registerReader(new Spravka93Reader());
        tableReader.registerReader(new Spravka95Reader());
        tableReader.registerReader(new Spravka02Reader());
        tableReader.registerReader(new Spravka902Reader());

        ///////////////////////////////////////////
        try (WatchService service = FileSystems.getDefault().newWatchService()) {
            Map<WatchKey, Path> keyMap = new HashMap<>();
            Path path = Paths.get(p);

            keyMap.put(path.register(service,
                    StandardWatchEventKinds.ENTRY_CREATE
            //                  ,StandardWatchEventKinds.ENTRY_DELETE
            //                  ,StandardWatchEventKinds.ENTRY_MODIFY
            ), path);

            WatchKey watchKey;
            do {
                watchKey = service.take();
                Path eventDir = keyMap.get(watchKey);

                for (WatchEvent<?> event : watchKey.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();
                    Path eventPath = (Path) event.context();
                    System.out.println(eventDir + " : " + kind + " : " + eventPath);
                    File f = new File(eventDir + "\\" + eventPath);

                    boolean b;
                    do {
                        b = true;
                        try (FileInputStream fis = new FileInputStream(f)) {
                            while (f.canRead() && f.canWrite() && f.exists()) {
                                readingFile(eventDir + "\\" + eventPath, eventPath);
                                break;
                            }
                        } catch (IOException ex) {
                            System.out.println("Файл занят " + ex);
                            b = false;
                        }
                    } while (!b);
                }
            } while (watchKey.reset());
        } catch (Exception e) {
            System.out.println("exception on WatchService " + e);
        }
        ///////////////////////////////////////////           
    }

    static final CompositeReader tableReader = new CompositeReader();

    private static void readingFile(String path, Path fName) {
        // test
        String filePath = path;
        String fileName = fName.toString();
        System.out.println("Using file name " + filePath);
        System.out.println("Using file name " + fileName);
        /////////////////////////////////
        String usrAutoN = null;
        String rx = "^\\d{4}";
        final Pattern pattern = Pattern.compile(rx);
        final Matcher matcher = pattern.matcher(fileName);

        while (matcher.find()) {
            usrAutoN = matcher.group(0);
//            System.out.println("Full match: " + matcher.group(0));
//            for (int i = 1; i <= matcher.groupCount(); i++) {
//                usrAutoN = matcher.group(i);
//            }
        }
        System.out.println("usrAutoN = " + usrAutoN);

        Users u = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");

            String sql = "select * from users where auto_no='" + usrAutoN + "'";
            try (Connection con = (Connection) DriverManager.getConnection(URL, USER, PASS);
                    PreparedStatement pstmt = con.prepareStatement(sql);
                    ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    u = new Users(rs.getLong("id"), rs.getString("firstname"),
                            rs.getString("lastname"), rs.getInt("id_role"),
                            rs.getInt("id_org"), rs.getString("auto_no"),
                            rs.getString("login"), rs.getString("password"));
//                ul.add(u);
                }
            }

        } catch (SQLException ex) {
            System.out.println("exexexexex " + ex);
            Logger.getLogger(Auth.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Auth.class.getName()).log(Level.SEVERE, null, ex);
        }

        String gLogin = u.getLogin();
        System.out.println("gLogin --->>> " + gLogin);
////////////////////////
        try {
            File f = new File(filePath);
//            System.out.println("fileNameToTest ---?> " + filePath);
            if (f.exists()) {
                HtmlTable result = tableReader.processFile(filePath);
                if (result != null) {
                    String answer = result.generateHtml();

                    armUsers.stream().forEach((Session x) -> {
                        System.out.println("x.getUserProperties() --> " + x.getUserProperties());
                        if (x.getUserProperties().containsValue(gLogin)) {
                            try {
                                x.getBasicRemote().sendText(answer);
                                return;
                            } catch (IOException ex) {
                                Logger.getLogger(WS.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                    });

//                    for (Session armUser : armUsers) {
//                        armUser.getBasicRemote().sendText(answer);
//                        System.out.println("armUsers : " + armUser.getUserProperties());
//                    }
                } else {
                    for (Session armUser : armUsers) {
                        armUser.getBasicRemote().sendText("Could not detect input file type");
                    }
                    System.out.println("Could not detect input file type");
                }
            } else {
                System.out.println("File not found");
            }
        } catch (MultipleResultsException ex) {
            System.out.println("Error: multiple results");
//            for (HtmlTable result : ex.multipleResults) {
//                System.out.println(result);
//            }
        } catch (IOException ex) {
            Logger.getLogger(ReadOnDir.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void deletingFile(String path) {
        System.out.println("File " + path + " deleting!!!");
        File file = new File(path);
        file.delete();
        System.out.println("File deleting!!!");
    }

}
