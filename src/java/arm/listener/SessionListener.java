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
static ReadOnDir rod;
    @Override
    public void sessionCreated(HttpSessionEvent event) {
        session = event.getSession();
        totalSess++;
        System.out.println("Total Session ----> " + totalSess);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        session = event.getSession();
        rod.interrupt();
        totalSess--;
        System.out.println("Total Session ----> " + totalSess);
    }

}
