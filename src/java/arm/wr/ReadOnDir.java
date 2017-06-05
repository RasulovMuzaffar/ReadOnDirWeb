package arm.wr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

                    readingFile(eventDir + "\\" + eventPath);

                }
            } while (watchKey.reset());
        } catch (Exception e) {
//            System.out.println("exception on WatchService " + e);
            e.printStackTrace();
        }
    }

    private static void readingFile(String path) {

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
////                    System.out.println(new String(new String(str.getBytes("Cp866"),"Cp1251").getBytes(),"utf-8"));
//                    System.out.println(str);
//                }
//            }
//
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
        try (FileInputStream fin = new FileInputStream(path)) {
            System.out.println("Размер файла: " + fin.available() + " байт(а)");

            byte[] buffer = new byte[fin.available()];
// считаем файл в буфер
            fin.read(buffer, 0, fin.available());

            System.out.println("Содержимое файла:");
            for (int i = 0; i < buffer.length; i++) {
                System.out.print(new String(new String(buffer,"cp866").getBytes("cp1251"),"UTF-8"));
            }
        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }

//        deletingFile(path);
    }

    private static void deletingFile(String path) {
        System.out.println("File " + path + " deleting!!!");
        File file = new File(path);
        file.delete();
        System.out.println("File deleting!!!");
    }
}
