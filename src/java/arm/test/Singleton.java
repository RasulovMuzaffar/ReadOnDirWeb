/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arm.test;

import arm.wr.ReadOnDir;

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
