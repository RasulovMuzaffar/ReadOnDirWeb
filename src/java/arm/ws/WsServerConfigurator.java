/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arm.ws;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

/**
 *
 * @author Muzaffar
 */
public class WsServerConfigurator extends ServerEndpointConfig.Configurator {

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        System.out.println("request.getHttpSession()).getAttribute(usrname) " + 
                ((HttpSession) request.getHttpSession()).getAttribute("usrname"));
//        sec.getUserProperties().put("usrname", (String) ((HttpSession) request.getHttpSession()).getAttribute("usrname"));
    }
}
