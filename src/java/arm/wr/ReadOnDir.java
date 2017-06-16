package arm.wr;

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
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Muzaffar
 */
public class ReadOnDir extends Thread {

    /**
     * @param args the command line arguments
     */
//    static String p = "c:\\testFolder\\in";
    static String p = "d:\\soob\\in";

    private static final String URL = "jdbc:mysql://localhost:3306/armasoup";
    private static final String USER = "test";
    private static final String PASS = "test";

    @Override
    public void run() {
        System.out.println("thread!!!");
//        if (Thread.currentThread().isInterrupted()) {
        pathListener();
//        }
    }

    private static void pathListener() {
        try (WatchService service = FileSystems.getDefault().newWatchService()) {
            Map<WatchKey, Path> keyMap = new HashMap<>();
            Path path = Paths.get(p);
            keyMap.put(path.register(service,
                    StandardWatchEventKinds.ENTRY_CREATE
            //                    ,StandardWatchEventKinds.ENTRY_DELETE
            //                    ,StandardWatchEventKinds.ENTRY_MODIFY
            ), path);

            WatchKey watchKey;

            do {
                System.out.println("POTOK");
                watchKey = service.take();
                Path eventDir = keyMap.get(watchKey);

                for (WatchEvent<?> event : watchKey.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();
                    Path eventPath = (Path) event.context();
                    System.out.println(eventDir + " : " + kind + " : " + eventPath);

                    readingFile(eventDir + "\\" + eventPath, "" + eventPath);

                }
            } while (watchKey.reset());
        } catch (Exception e) {
//            System.out.println("exception on WatchService " + e);
            e.printStackTrace();
        }
    }

    private static void readingFile(String path, String fileName) throws ClassNotFoundException {

//        String str = null;
//        Matcher m = null;
//        try (FileReader reader = new FileReader(path)) {
//            LineNumberReader lnr = new LineNumberReader(new BufferedReader(reader));
//            Pattern p1 = Pattern.compile("\\D*:(\\d+)(.*)");
//            while ((str = lnr.readLine()) != null) {
//                m = p1.matcher(str);
//                if (m.find()) {
//                    System.out.println("Message Code : " + m.group(1));
//                    System.out.println("" + m.group());
//                } else {
////                    System.out.println(new String(str.getBytes("Cp1251"), "CP866"));
//                    
//                }
//            }
//
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//try (FileInputStream fin = new FileInputStream("C:\\testFolder\\01020000.00I")) {
//            byte[] buffer = new byte[fin.available()];
//            fin.read(buffer, 0, fin.available());
//            String sss = new String(new String(buffer, "CP1251").getBytes(), "CP866");
//            System.out.println(sss);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//        try (FileReader reader = new FileReader("C:\\testFolder\\01020000.00I")) {
//            LineNumberReader lnr = new LineNumberReader(new BufferedReader(reader));
//            String str;
//            while ((str = lnr.readLine()) != null) {
//                System.out.println(new String(str.getBytes("Cp1251"), "CP866"));
//            }
//
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
        boolean d;
        do {
            try (FileInputStream fin = new FileInputStream(path)) {
                d = true;
                System.out.println("Размер файла: " + fin.available() + " байт(а)");
                char[] fn = fileName.toCharArray();
                String s = "";
                for (int i = 0; i < 4; i++) {
                    s += fn[i];
                }
                System.out.println("111   " + s);
                byte[] buffer = new byte[fin.available()];
// считаем файл в буфер
                fin.read(buffer, 0, fin.available());

                System.out.println("Содержимое файла:");
                String sss = new String(new String(buffer, "CP1251").getBytes(), "CP866");
                System.out.println(sss);
//            for (int i = 0; i < buffer.length; i++) {
//                System.out.print(new String(new String(buffer,"cp866").getBytes("cp1251"),"UTF-8"));
//            }

                String[] text = sss.split("\\u000d\\u000a\\u000d\\u000a");

                Class.forName("com.mysql.jdbc.Driver");
                try (Connection con = (Connection) DriverManager.getConnection(URL, USER, PASS);
                        CallableStatement proc = con.prepareCall("{call insertMessage('" + text[0] + "','" + text[1] + "','" + s + "')}");) {

                    proc.execute();

                } catch (SQLException ex) {
                    System.out.println("ошибка!!! в SQLException!!!");
                    System.out.println("" + ex);
                    System.out.println(Arrays.toString(ex.getStackTrace()));
                }
            } catch (IOException ex) {
                System.out.println("файл занят");
                d = false;
            }
        } while (!d);

//        deletingFile(path);
    }

    private static void deletingFile(String path) {
        System.out.println("File " + path + " deleting!!!");
        File file = new File(path);
        file.delete();
        System.out.println("File deleting!!!");
    }
}
