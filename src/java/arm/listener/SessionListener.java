/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arm.listener;

import arm.wr.ReadOnDir;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 *
 * @author Muzaffar
 */
public class SessionListener implements HttpSessionListener {

    private HttpSession session = null;
    private int totalSess = 0;
    ReadOnDir rod;

    @Override
    public void sessionCreated(HttpSessionEvent event) {
//        System.out.println("+++++++++++++++++++++");
//        rod.start();
//        System.out.println("+++++++++++++++++++++");

        session = event.getSession();
        totalSess++;
        System.out.println("Total Session ----> " + totalSess);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        session = event.getSession();

        System.out.println("----------------------");
//        System.out.println("rod.getName()--- "+rod.getName());
        System.out.println("ReadOnDir list"+rod);
//        rod.interrupt();
        System.out.println("----------------------");
        totalSess--;
        System.out.println("Total Session ----> " + totalSess);
    }

}
