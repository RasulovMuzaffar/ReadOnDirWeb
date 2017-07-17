package arm.wr;

import static arm.listener.WS.PEERS;
import arm.tableutils.HtmlTable;
import arm.tableutils.tablereaders.CompositeReader;
import arm.tableutils.tablereaders.MultipleResultsException;
import arm.tableutils.sprtemplates.Spravka02Reader;
import arm.tableutils.sprtemplates.Spravka902Reader;
import arm.tableutils.sprtemplates.Spravka93Reader;
import arm.tableutils.sprtemplates.Spravka95Reader;
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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.Session;

/**
 *
 * @author Muzaffar
 */
public class ReadOnDir extends Thread {

    /**
     * @param args the command line arguments
     */
//    static String p = "c:\\testFolder\\in";
    static String p = "C:\\soob\\in";

    private static final String URL = "jdbc:mysql://localhost:3306/arm";
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
                                readingFile(eventDir + "\\" + eventPath);
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

    private static void readingFile(String path) {
        // test
        String fileNameToTest;
        fileNameToTest = path;
        System.out.println("Using file name " + fileNameToTest);

        try {
            File f = new File(fileNameToTest);
            if (f.exists()) {
                HtmlTable result = tableReader.processFile(fileNameToTest);
                if (result != null) {
                    String s = result.generateHtml();
                    for (Session peer : PEERS) {
                        System.out.println("peeeeeeer " + peer.getId());
                        peer.getBasicRemote().sendText(s);
                    }
                    System.out.println(s);
                } else {
                    for (Session peer : PEERS) {
                        System.out.println("peeeeeeer " + peer.getId());
                        peer.getBasicRemote().sendText("Could not detect input file type");
                    }
                    System.out.println("Could not detect input file type");
                }
            } else {
                System.out.println("File not found");
            }
        } catch (MultipleResultsException ex) {
            System.out.println("Error: multiple results");
            for (HtmlTable result : ex.multipleResults) {
                System.out.println(result);
            }
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
