/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arm.ws;

import arm.find.FindSt;
import arm.test.Singleton;
import arm.wr.Write;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
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

    @OnMessage
    public void onMessage(String message, Session userSession) {
//        HttpSession hs = (HttpSession) userSession;
        System.out.println("hs ==>> " + userSession.getId());
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
    }

    public void WsSendMessage(String param, Session session) {

        //рассылка всем подключённым, кроме того кто отправил это сообщение
//        try {
//            for (Session peer : armUsers) {
//                if (!peer.equals(session)) {
//
//                    peer.getBasicRemote().sendText("--" + param + "--<br>");
//
//                }
//            }
//        } catch (IOException ex) {
//            Logger.getLogger(WS.class.getName()).log(Level.SEVERE, null, ex);
//        }
        //отправка ответа только автору сообщения
        try {
            session.getBasicRemote().sendText("--" + param + "--<br>");
        } catch (IOException ex) {
            Logger.getLogger(WS.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
