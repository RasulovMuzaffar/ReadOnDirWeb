/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arm.test;

import arm.wr.ReadOnDir;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Muzaffar
 */
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
    
//    private Singleton() {
//    }
//    
//    public static Singleton getInstance() {
//        return SingletonHolder.INSTANCE;
//    }
//    
//    private static class SingletonHolder {
//
//        private static final Singleton INSTANCE = new Singleton();
//    }
}
