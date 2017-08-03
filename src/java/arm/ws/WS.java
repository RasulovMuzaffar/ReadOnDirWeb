/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arm.ws;

import arm.ent.Users;
import arm.find.FindSt;
import arm.wr.Write;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
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
    public static final Map<Users, HttpSession> userHttpSess = new HashMap<Users, HttpSession>();

    @OnMessage
    public void onMessage(String message, Session userSession) {
//        userHttpSess.get(userSession.getUserProperties());
        int i = 0;
        System.out.println("----------onMessage------------");
        for (Map.Entry<Users, HttpSession> entry : userHttpSess.entrySet()) {
            i++;
            Users key = entry.getKey();
            HttpSession value = entry.getValue();
            if ((key).equals(userSession.getUserProperties().get("usrname"))) {
                System.out.println("true!!!!!");
            }
            System.out.println(i + ". " + key + " " + value.getId());
        }

        System.out.println("==========onMessage==========");
//        System.out.println("us "+);
//        System.out.println("hs ==>> " + userHttpSess..getLastAccessedTime());
//        System.out.println("created time CreationTime ==>> " + new Date(hs.getCreationTime()));
//        System.out.println("created time LastAccessedTime ==>> " + new Date(hs.getLastAccessedTime()));
//        System.out.println("created time MaxInactiveInterval ==>> " + new Date(hs.getMaxInactiveInterval()));
        String[] str = message.split("\u0003");
        String[] zprs = str[1].split(",");
//        System.out.println("str[0] " + str[0]);
//        System.out.println("str[1] " + str[1]);
//        System.out.println("zprs " + Arrays.toString(zprs));
//        System.out.println("message WS " + message);
        if (str[0].equals("getSt")) {
            FindSt find = new FindSt();
            try {
                find.getSt(userSession, message);
//            System.out.println("getSt");
            } catch (SQLException ex) {
                Logger.getLogger(WS.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            Write w = new Write();
            w.getWrite(userSession, message);
        }

//        switch (str[0]) {
//            case "spr":
//                w.getWrite(userSession, message /*str[1]*/);
//                break;
//            case "getTGNL":
//                System.out.println("TELEGRAMM NATURNIY LIST");
//                break;
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
        int i = 0;
        for (Map.Entry<Users, HttpSession> entry : userHttpSess.entrySet()) {
            Users key = entry.getKey();
            HttpSession value = entry.getValue();
            if ((key).equals(userSession.getUserProperties().get("usrname"))) {
                userHttpSess.remove(key);
                System.out.println("on deleted!!!!!");
                break;
            }
            System.out.println(i++ + ". " + key + " " + value.getId());
        }
    }
}
