/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arm.listener;

import arm.ent.Users;
import arm.wr.ReadOnDir;
import static arm.ws.WS.armUsers;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.websocket.Session;

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
        Users user = (Users) session.getAttribute("usrname");
        System.out.println("dest -- > " + user.getLogin());

        session = event.getSession();

        
        for (Session armUser : armUsers) {
            if (armUser.getUserProperties().containsValue(user)) {
                try {
                    armUser.getBasicRemote().sendText("logoff\u0003auth.html");
                    armUser.close();                    
                } catch (IOException ex) {
                    Logger.getLogger(SessionListener.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        totalSess--;
        System.out.println("Total Session ----> " + totalSess);
    }

}
