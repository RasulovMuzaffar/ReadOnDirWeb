/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arm.wr;

import javax.websocket.Session;

/**
 *
 * @author Muzaffar
 */
public class Write {

//    public void getWrite(String[] zprs) {
//        System.out.println("1111111111111 " +zprs);
//    }
    public void getWrite(Session userSession) {
        String usrname = (String) userSession.getUserProperties().get("usrname");
        System.out.println("qwerty!!! " + usrname);
    }
}
