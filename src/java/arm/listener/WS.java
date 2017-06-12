/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arm.listener;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
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
private static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());

    @OnMessage
    public String onMessage(String message) {
        String str = "your are writing: " + message;
        return str;
    }

    @OnOpen
    public void OnOpen() {
        System.out.println("client is connected...");
    }

    @OnError
    public void onError(Throwable t) {
    }

    @OnClose
    public void onClose() {
    }   
}
