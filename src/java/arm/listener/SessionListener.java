package arm.listener;

import arm.ent.Users;
import arm.wr.ReadOnDir;
import arm.ws.WS;
import static arm.ws.WS.armUsers;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.websocket.Session;

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
        session = event.getSession();
        Users u = (Users) event.getSession().getAttribute("user");

        armUsers.stream().forEach((Session x) -> {
            if (x.getUserProperties().containsValue(u)) {
                try {
                    x.getBasicRemote().sendText("logoff\u0003auth.html");
                    x.close();
                    return;
                } catch (IOException ex) {
                    Logger.getLogger(WS.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });

        totalSess--;
        System.out.println("Total Session ----> " + totalSess);
    }

}
