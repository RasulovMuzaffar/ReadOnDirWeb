package arm.ws;

import static arm.ws.WsServerConfigurator.hs;
import arm.find.FindSt;
import arm.wr.Write;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import static arm.test.Auth.sessionTimeoutFromWebXml;

@ServerEndpoint(value = "/ws", configurator = WsServerConfigurator.class)
public class WS {

    public static final Set<Session> armUsers = Collections.synchronizedSet(new HashSet<Session>());
    public static final Map<Session, HttpSession> usersSession = new HashMap<>();

    @OnMessage
    public void onMessage(String message, Session userSession) {

        String[] str = message.split("\u0003");
        if (str[0].equals("getSt")) {
            FindSt find = new FindSt();
            try {
                find.getSt(userSession, message);
            } catch (SQLException ex) {
                Logger.getLogger(WS.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (str[0].equals("sessions")) {
            usersSession.put(userSession, hs);
        } else {
            Write w = new Write();
            w.getWrite(userSession, message);
        }

        prolongSess(userSession);
    }

    @OnOpen
    public void onOpen(EndpointConfig endpointConfig, Session userSession) {
        userSession.getUserProperties().put("usrname", endpointConfig.getUserProperties().get("usrname"));
        armUsers.add(userSession);
    }

    @OnError
    public void onError(Throwable t) {
    }

    @OnClose
    public void onClose(Session userSession) {
        armUsers.remove(userSession);
    }

    private void prolongSess(Session userSession) {
        if (usersSession.containsKey(userSession)) {
            usersSession.get(userSession).setMaxInactiveInterval(Integer.parseInt(sessionTimeoutFromWebXml.trim()) * 60);
        }
    }
}
