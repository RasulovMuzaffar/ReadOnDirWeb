/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arm.ws;

import arm.ent.Users;
import static arm.ws.WS.userHttpSess;
import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

/**
 *
 * @author Muzaffar
 */
public class WsServerConfigurator extends ServerEndpointConfig.Configurator {
//public static HttpSession hs;

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
//        sec.getUserProperties().put("usrname", (String) ((HttpSession) request.getHttpSession()).getAttribute("usrname"));
        HttpSession session = (HttpSession) request.getHttpSession();
//        System.out.println("Handshake httpSession " + session.getId());
//        Object attribute = session.getAttribute("usrname");
        Users u = (Users) session.getAttribute("usrname");

        userHttpSess.put(u, session);
//        String name = (String) attribute;
        sec.getUserProperties().put("usrname", u);
    }
}
