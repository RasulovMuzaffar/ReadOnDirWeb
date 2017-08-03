/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

@ServerEndpoint(value = "/ws", configurator = WsServerConfigurator.class)
//@ServerEndpoint(value = "/ws")
public class WS {

    public static final Set<Session> armUsers = Collections.synchronizedSet(new HashSet<Session>());
    public static final Map<Session, String> userHttpSess = new HashMap<>();
    public static final Map<Session, HttpSession> uhs = new HashMap<>();

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
            userHttpSess.put(userSession, str[1]);
            uhs.put(userSession, hs);
        } else {
            Write w = new Write();
            w.getWrite(userSession, message);
        }

        prolongSess(userSession);
//        if (userHttpSess.containsValue(userSession)) {
//        }
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
        System.out.println("--------------------------------");
        for (Map.Entry<Session, HttpSession> u : uhs.entrySet()) {
            Session k = u.getKey();
            HttpSession v = u.getValue();
            System.out.println(k + " -+- " + v);
        }
        System.out.println("usess " + userSession);
        System.out.println("===============================");
        if (uhs.containsKey(userSession)) {
            hs.setMaxInactiveInterval(1 * 60);
        }
    }
}
