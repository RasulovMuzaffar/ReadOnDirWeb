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

        session = event.getSession();
        totalSess++;
        System.out.println("Total Session ----> " + totalSess);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        System.out.println("event "+event.getSession().getId());
        Users user;
        System.out.println("----------sessionDestroyed-----------");
        for (Session armUser : armUsers) {
            System.out.println("before remove " + armUser.getId());
            System.out.println("armUser.getUserProperties() " + armUser.getUserProperties());
        }
        try {
            user = (Users) session.getAttribute("usrname");
            System.out.println("dest -- > " + user.getLogin() + " " + event.getSession().getId());

//            for (Session armUser : armUsers) {
//                if (armUser.getUserProperties().containsValue(user)) {
//                    try {
//                        System.out.println("udalyaem user - " + user.getLogin() + " " + event.getSession().getId());
//                        Users usr = (Users) armUser.getUserProperties().get("usrname");
//                        System.out.println("udalyaem armUser - " + usr.getLogin());
//                        session = event.getSession();
//                        armUser.getBasicRemote().sendText("logoff\u0003auth.html");
//                        armUser.close();
//                        return;
//                    } catch (IOException ex) {
//                        Logger.getLogger(SessionListener.class.getName()).log(Level.SEVERE, null, ex);
//                        System.out.println("neponyatnaya problema!!!");
//                    }
//                }
//            }

armUsers.stream().forEach((Session x) -> {
                        System.out.println("x.getUserProperties() --> " + x.getId());
//                        System.out.println("x.getUserProperties().containsValue(user) ===>> "
//                                + x.getUserProperties().containsValue(user));
                        if (x.getUserProperties().containsValue(user)) {
//                        if (x.getUserProperties().containsValue(gLogin)) {
                            try {
                                x.getBasicRemote().sendText("logoff\u0003auth.html");
                                x.close();
                                return;
                            } catch (IOException ex) {
//                                Logger.getLogger(WS.class.getName()).log(Level.SEVERE, null, ex);
                                System.out.println("ex "+ex);
                            }
                        }

                    });
        } catch (Exception e) {
            System.out.println("e--------->>> " + e);
        }
        for (Session armUser : armUsers) {
            System.out.println("after remove " + armUser.getId());
        }

        totalSess--;
        System.out.println("Total Session ----> " + totalSess);
        System.out.println("=============sessionDestroyed================");
    }

}
