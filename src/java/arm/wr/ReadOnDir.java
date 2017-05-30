/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arm.wr;

import java.io.BufferedReader;
import java.io.File;
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
    static String p = "c:\\testFolder\\in";

    public void run() {
        System.out.println("thread!!!");
        pathListener();
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

        String str = null;
        Matcher m = null;
//        File file = new File(path);
//        System.out.println(">>>>> " + file.exists());
        try (FileReader reader = new FileReader(path)) {
//            int c;
//            while ((c = reader.read()) != -1) {
//                System.out.print((char) c);
//            }
//            System.out.println("");
            LineNumberReader lnr = new LineNumberReader(new BufferedReader(reader));
            Pattern p1 = Pattern.compile("\\D*:(\\d+)(.*)");

//            System.out.format("00000  %s \n",lnr.readLine());
//            String line = lnr.readLine();
            while ((str = lnr.readLine()) != null) {
//                System.out.println("-------------------------------------");
                m = p1.matcher(str);
//                m = p1.matcher(line);
                if (m.find()) {
                    System.out.println("Message Code : " + m.group(1));
                    System.out.println("" + m.group());
                } else {
                    System.out.println("" + str);
                }
            }

        } catch (IOException ex) {
//            Logger.getLogger(ReadOnDir.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }

        deletingFile(path);
    }

    private static void deletingFile(String path) {
        System.out.println("File " + path + " deleting!!!");
        File file = new File(path);
        file.delete();
        System.out.println("File deleting!!!");
    }
}
