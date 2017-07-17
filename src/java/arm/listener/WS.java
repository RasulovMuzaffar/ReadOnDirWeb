/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arm.listener;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author Muzaffar
 */
@ServerEndpoint("/ws")
public class WS {

    public static final Set<Session> PEERS = Collections.synchronizedSet(new HashSet<Session>());
    public static final Map<String, Session> p = Collections.synchronizedMap(new HashMap<>());

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("ws.WS_server.onMessage()   " + message + "   " + session.getId());
        String[] str = message.split("|");
        String[] zprs = str[1].split(",");
        switch (str[0]) {
            case "spr":
                System.out.println("spr byl");
                break;
            case "qwerty":
                System.out.println("qwerty byl");
                break;
        }
//        WsSendMessage(message, session);
    }

    @OnOpen
    public void onOpen(Session peer) {
        PEERS.add(peer);
        p.put("user", peer);
        System.out.println("======>" + peer.getId());
        System.out.println("======>" + p.get("user").getId());
    }

    @OnError
    public void onError(Throwable t) {
    }

    @OnClose
    public void onClose(Session peer) {
        PEERS.remove(peer);
    }

    public void WsSendMessage(String param, Session session) {

        //рассылка всем подключённым, кроме того кто отправил это сообщение
//        try {
//            for (Session peer : PEERS) {
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
