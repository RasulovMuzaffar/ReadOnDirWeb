package arm.wr;

import arm.ent.Users;
import arm.tableutils.HtmlTable;
import arm.tableutils.tablereaders.CompositeReader;
import arm.tableutils.tablereaders.MultipleResultsException;
import arm.tableutils.sprtemplates.Spravka02Reader;
import arm.tableutils.sprtemplates.Spravka902Reader;
import arm.tableutils.sprtemplates.Spravka93Reader;
import arm.tableutils.sprtemplates.Spravka95Reader;
import arm.tableutils.tablereaders.utils.TextReplace;
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
    public static String spr;
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
//        String rx = "^\\d{4}";
        String rx = "01[\\dA-Fa-f\\d]{2}";
        final Pattern pattern = Pattern.compile(rx);
        final Matcher matcher = pattern.matcher(fileName);

        while (matcher.find()) {
            usrAutoN = matcher.group(0);
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
                }
            }

        } catch (SQLException ex) {
            System.out.println("exexexexex " + ex);
            Logger.getLogger(Auth.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Auth.class.getName()).log(Level.SEVERE, null, ex);
        }

//        String gLogin = u.getLogin();
//        System.out.println("gLogin --->>> " + gLogin);
////////////////////////
        Users user = u;
        try {
            File f = new File(filePath);
            if (f.exists()) {
                HtmlTable result = tableReader.processFile(filePath);
                if (result != null) {
                    String answer = result.generateHtml();

        System.out.println("spr---->> "+spr);
                    System.out.println("user ------>>> " + user);
                    armUsers.stream().forEach((Session x) -> {
                        System.out.println("x.getUserProperties() --> " + x.getUserProperties());
                        System.out.println("x.getUserProperties().containsValue(user) ===>> "
                                + x.getUserProperties().containsValue(user));
                        if (x.getUserProperties().containsValue(user)) {
//                        if (x.getUserProperties().containsValue(gLogin)) {
                            try {
                                x.getBasicRemote().sendText(spr+"\u0003"+answer);
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
                    String answer = readNotDetectedFile(filePath);
                    armUsers.stream().forEach((Session x) -> {
                        if (x.getUserProperties().containsValue(user)) {
                            try {
//                                x.getBasicRemote().sendText("Could not detect input file type");
                                x.getBasicRemote().sendText(answer);
                                return;
                            } catch (IOException ex) {
                                Logger.getLogger(WS.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    });
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
        }
    }

    private static void deletingFile(String path) {
        System.out.println("File " + path + " deleting!!!");
        File file = new File(path);
        file.delete();
        System.out.println("File deleting!!!");
    }

    private static String readNotDetectedFile(String filePath) {
        String f = null;
        try (FileInputStream fis = new FileInputStream(filePath)) {

            byte[] buffer = new byte[fis.available()];

            // считаем файл в буфер
            fis.read(buffer, 0, fis.available());

            String str = new String(new String(buffer, "CP1251").getBytes(), "CP866");

            f = TextReplace.getText(str);
            
            StringBuffer sb;
            int i=-1;
            while((i=fis.read())!=-1){  
                System.out.print((char)i);
            }   

        } catch (IOException ex) {
            Logger.getLogger(Spravka93Reader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return f;
    }
}
