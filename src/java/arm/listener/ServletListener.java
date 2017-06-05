/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arm.listener;

import arm.test.Singleton;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author Muzaffar
 */
public class ServletListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Singleton.getOurInstance();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("================contextDestroyed======================");
//        System.out.println("rod.isInterrupted()---- " + rod.isInterrupted());
        Singleton.rod.interrupt();
        System.out.println("================rod.interrupt()======================");
    }
}
