/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arm.ws;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    public void onMessage(String message, Session userSession/*, HttpServletRequest request*/) {
////        System.out.println("ws http session id >> "+request.getSession().getId());
//        System.out.println("ws.WS_server.onMessage()   " + message + "   " + session.getId());
//        String[] str = message.split("|");
//        String[] zprs = str[1].split(",");
//        switch (str[0]) {
//            case "spr":
//                System.out.println("spr byl");
//                break;
//            case "qwerty":
//                System.out.println("qwerty byl");
//                break;
//        }
////        WsSendMessage(message, session);
        String usrname = (String) userSession.getUserProperties().get("usrname");
        System.out.println("cse >> usrname " + usrname);

        if (usrname != null) {
            armUsers.stream().forEach((Session x) -> {

                if (x.getUserProperties().containsValue(usrname)) {
                    try {
                        x.getBasicRemote().sendText(message);
                        return;
                    } catch (IOException ex) {
                        Logger.getLogger(WS.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            });
        }
    }

    @OnOpen
    public void onOpen(EndpointConfig endpointConfig, Session userSession) {
        System.out.println("usid --> " + userSession.getId());
        
        userSession.getUserProperties().put("usrname", endpointConfig.getUserProperties().get("usrname"));
        
        String usrname = (String) userSession.getUserProperties().get("usrname");
        System.out.println("usrname " + usrname);
        
        armUsers.add(userSession);
        
        System.out.println("isEmpty? : " + armUsers.isEmpty());
        System.out.println("size = " + armUsers.size());
        
        for (Session s : armUsers) {
            System.out.println("+----->> " + s.getUserProperties().get("usrname"));
            System.out.println("id---> " + s.getId());
        }
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
