
package arm.ws;

import arm.ent.Users;
import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;


public class WsServerConfigurator extends ServerEndpointConfig.Configurator {

    public static HttpSession hs;

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        HttpSession session = (HttpSession) request.getHttpSession();
        Users u = (Users) session.getAttribute("usrname");
        hs = session;
        sec.getUserProperties().put("usrname", u);
    }
}
