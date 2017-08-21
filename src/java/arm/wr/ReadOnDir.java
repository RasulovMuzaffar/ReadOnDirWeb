package arm.wr;

import arm.ent.Users;
import arm.tableutils.HtmlTable;
import arm.tableutils.tablereaders.CompositeReader;
import arm.tableutils.tablereaders.MultipleResultsException;
import arm.tableutils.sprtemplates.*;
//import static arm.tableutils.tablereaders.CompositeReader.lht;
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
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.websocket.Session;

public class ReadOnDir extends Thread {

    /**
     * @param args the command line arguments
     */
    public static String spr;
//    static String p = "c:\\testFolder\\in";
    static String p = "C:\\soob\\in";

    private static final String URL = "jdbc:mysql://localhost:3306/arm";
    private static final String USER = "test";
    private static final String PASS = "test";

    @Override
    public void run() {
        System.out.println("thread!!!!");
//        if (Thread.currentThread().isInterrupted()) {
        pathListener();
//        }
    }

    private static void pathListener() {
// init composite reader - register all reader types
        tableReader.registerReader(new Spravka02Reader());
        tableReader.registerReader(new Spravka1296Reader());
        tableReader.registerReader(new Spravka3290Reader());
        tableReader.registerReader(new Spravka5065Reader());
        tableReader.registerReader(new Spravka5072Reader());
        tableReader.registerReader(new Spravka57Reader());
        tableReader.registerReader(new Spravka64Reader());
        tableReader.registerReader(new Spravka7401Reader());
        tableReader.registerReader(new Spravka902Reader());
        tableReader.registerReader(new Spravka91Reader());
        tableReader.registerReader(new Spravka92Reader());
        tableReader.registerReader(new Spravka93Reader());
        tableReader.registerReader(new Spravka95Reader());

        ///////////////////////////////////////////
        try (WatchService service = FileSystems.getDefault().newWatchService()) {
            Map<WatchKey, Path> keyMap = new HashMap<>();
            Path path = Paths.get(p);
            System.out.println("blablablablabla");

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

                    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);
                    ScheduledFuture scheduledFuture
                            = scheduledExecutorService.schedule(new Callable() {
                                public Object call() throws Exception {
                                    System.out.println("Executed!");
                                    return "Called!";
                                }
                            }, 2, TimeUnit.SECONDS);
//                    readingFile(eventDir + "\\" + eventPath, eventPath);
                    /////////////////////////////

                    boolean b;
                    do {
                        b = true;
                        try (FileInputStream fis = new FileInputStream(f)) {
                            while (f.canRead() && f.canWrite() && f.exists()) {
                                readingFile(eventDir + "\\" + eventPath, eventPath);
                                break;
                            }
                        } catch (IOException ex) {
                            System.out.println("File take another process " + ex);
                            b = false;
                        }
                    } while (!b);

                    scheduledExecutorService.shutdown();
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
        /////////////////////////////////
        String usrAutoN = null;
//        String rx = "^\\d{4}";
        String rx = "01[\\dA-Fa-f\\d]{2}";
        final Pattern pattern = Pattern.compile(rx);
        final Matcher matcher = pattern.matcher(fileName);

        while (matcher.find()) {
            usrAutoN = matcher.group(0);
        }

        Users u = null;
        try {

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
        }

////////////////////////
        Users user = u;
        try {
            File f = new File(filePath);
            if (f.exists()) {
                HtmlTable result = tableReader.processFile(filePath);

                StringBuilder s = new StringBuilder();
                if (result != null) {
                    String answer = result.generateHtml();
                    armUsers.stream().forEach((Session x) -> {
                        if (x.getUserProperties().containsValue(user)) {
                            try {
                                x.getBasicRemote().sendText(spr + "\u0003" + answer);
                                return;
                            } catch (IOException ex) {
                                Logger.getLogger(WS.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                    });

                } else {
                    StringBuilder moreSprs = new StringBuilder();
                    List<HtmlTable> list = tableReader.readersResult();
//                    System.out.println("list.size() ---->>>> "+list.size());
                    if (list.size() != 0) {

                        for (HtmlTable l : list) {
                            moreSprs.append(l.generateHtml());
                            moreSprs.append("<br/>");
                        }
                        armUsers.stream().forEach((Session x) -> {
                            if (x.getUserProperties().containsValue(user)) {
                                try {
                                    x.getBasicRemote().sendText("sprDefault\u0003" + moreSprs);
                                    CompositeReader.lht.removeAll(list);
                                    return;
                                } catch (IOException ex) {
                                    Logger.getLogger(WS.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        });
                    } else {
                        System.out.println("mi tuda zawli!!!");
                        String answer = readNotDetectedFile(filePath);
                        armUsers.stream().forEach((Session x) -> {
                            if (x.getUserProperties().containsValue(user)) {
                                try {
                                    x.getBasicRemote().sendText("sprDefault\u0003<label><h3>" + answer+"</h3></label>");
                                    return;
                                } catch (IOException ex) {
                                    Logger.getLogger(WS.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        });
                        System.out.println("Could not detect input file type : " + answer);
                    }
                }
            } else {
                System.out.println("File not found");
            }
        } catch (MultipleResultsException ex) {
            System.out.println("Error: multiple results");
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

            int i = -1;
            while ((i = fis.read()) != -1) {
                System.out.print((char) i);
            }

        } catch (IOException ex) {
            Logger.getLogger(Spravka93Reader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return f;
    }
}
