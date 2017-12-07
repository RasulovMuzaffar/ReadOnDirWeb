package arm.test;

import arm.tableutils.sprtemplates.Spravka902Reader;
import arm.tableutils.sprtemplates.nod.*;
import arm.tableutils.sprtemplates.pzd.*;
import arm.tableutils.sprtemplates.st.*;
import arm.tableutils.sprtemplates.vag.*;
import arm.tableutils.tablereaders.CompositeReader;
import arm.wr.ReadOnDir;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Singleton {

    private static Singleton ourInstance;
    public static ReadOnDir rod;

    public static void getOurInstance() {
        if (ourInstance == null) {
            ourInstance = new Singleton();
            rod = new ReadOnDir();
            System.out.println("---------------------------");
            rod.setDaemon(true);
            rod.start();
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Singleton.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("+++++++++++++++++++++++++++");
        }
    }
    
//        public static void pathListener() {
//// init composite reader - register all reader types
////        tableReader.registerReader(new Spravka02Reader());
//        tableReader.registerReader(new Spravka1296Reader());
//        tableReader.registerReader(new Spravka3290Reader());
//        tableReader.registerReader(new Spravka5065Reader());
//        tableReader.registerReader(new Spravka5072Reader());
//        tableReader.registerReader(new Spravka57Reader());
//        tableReader.registerReader(new Spravka64Reader());
//        tableReader.registerReader(new Spravka7401Reader());
//        tableReader.registerReader(new Spravka91Reader());
//        tableReader.registerReader(new Spravka92Reader());
//        tableReader.registerReader(new Spravka93Reader());
//        tableReader.registerReader(new Spravka95Reader());
//        ////////Работа с Поездами
//        tableReader.registerReader(new Spravka12Reader());
//        tableReader.registerReader(new Spravka104Reader());
//        tableReader.registerReader(new Spravka216Reader());
//        tableReader.registerReader(new Spravka42Reader());
//        tableReader.registerReader(new Spravka60Reader());
//        tableReader.registerReader(new Spravka902Reader());
//        ////////НОДы
//        tableReader.registerReader(new Spravka62Reader());
//        tableReader.registerReader(new Spravka4060Reader());
//        tableReader.registerReader(new Spravka1574Reader());
//        ///////ВАГОНЫ
//        tableReader.registerReader(new Spravka2610Reader());
//        tableReader.registerReader(new Spravka215vReader());
//        tableReader.registerReader(new Spravka2790Reader());
//        }
//        
//        static final CompositeReader tableReader = new CompositeReader();
}
