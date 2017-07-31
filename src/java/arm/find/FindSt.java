/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arm.find;

import arm.ent.Station;
import arm.ent.Users;
import arm.test.Auth;
import arm.ws.WS;
import static arm.ws.WS.armUsers;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.Session;

/**
 *
 * @author Muzaffar
 */
public class FindSt {

    private static final String URL = "jdbc:mysql://localhost:3306/arm";
    private static final String USER = "test";
    private static final String PASS = "test";

    String autoNo;

    public void getSt(Session userSession, String str) throws SQLException {
        Users u = (Users) userSession.getUserProperties().get("usrname");
        autoNo = u.getAutoNo();
        System.out.println("-------------====---------");
        String[] s = str.split("\u0003");
//        String[] zprs = s[1].split(",");
        Station st = null;
        List<Station> lst = new ArrayList<>();
        String string = "";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String sql = "select * FROM spr_stations ss WHERE ss.name_station LIKE '" + s[1] + "%' limit 5";
            try (Connection con = (Connection) DriverManager.getConnection(URL, USER, PASS);
                    PreparedStatement pstmt = con.prepareStatement(sql);
                    ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    st = new Station(rs.getLong("id"), rs.getString("code_station"), rs.getString("name_station"));
                    lst.add(st);
                }
            }
            string+="getSt\u0003";
            for (Station ss : lst) {
//                System.out.println(""+ss.getCodeSt());
                string+="<option data-value = '" + ss.getCodeSt() + "' value='" + ss.getNameSt() + "'/>";
            }
//            System.out.println("" + sb.toString());
            for (Session armUser : armUsers) {
                        armUser.getBasicRemote().sendText(string);
//                System.out.println("" + sb.toString());
//                System.out.println("armUsers : " + armUser.getUserProperties());
            }

//            armUsers.stream().forEach((Session x) -> {
//                        System.out.println("x.getUserProperties() --> " + x.getUserProperties());
//                        System.out.println("x.getUserProperties().containsValue(user) ===>> "
//                                + x.getUserProperties().containsValue(u));
//                if (x.getUserProperties().containsValue(u)) {
//                    try {
//                        x.getBasicRemote().sendText(sb.toString());
//                        return;
//                    } catch (IOException ex) {
//                        Logger.getLogger(WS.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
//
//            });
        } catch (SQLException ex) {
            System.out.println("exexexexex " + ex);
            Logger.getLogger(Auth.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            System.out.println("**** "+ex);
            Logger.getLogger(Auth.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
