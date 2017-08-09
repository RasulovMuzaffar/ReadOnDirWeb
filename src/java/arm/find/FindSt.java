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

public class FindSt {

    private static final String URL = "jdbc:mysql://localhost:3306/arm";
    private static final String USER = "test";
    private static final String PASS = "test";

    String autoNo;

    public void getSt(Session userSession, String str) throws SQLException {
        Users u = (Users) userSession.getUserProperties().get("usrname");
        autoNo = u.getAutoNo();
        System.out.println("-------------==find==---------");
        String[] s = str.split("\u0003");
//        String[] zprs = s[1].split(",");
        Station st = null;
        List<Station> lst = new ArrayList<>();
        StringBuilder string = new StringBuilder();

        try {

//            String sql = "select * FROM spr_stations ss WHERE ss.name_station LIKE '%" + s[1] + "%' limit 5";
            String[] st1 = s[1].split(" ");
            StringBuilder sy = new StringBuilder();
            sy.append("SELECT concat_ws(' | ', ss.code_station, ss.name_station) AS nn, code_station FROM spr_stations ss WHERE ");

            for (String sf : st1) {
                sy.append(" CONCAT(ss.code_station,ss.name_station) like '%").append(sf).
                        append("%' and ");
            }
            sy.setLength(sy.length() - 4);
            sy.append(" limit 10");

            string.append("getSt\u0003");

            try (Connection con = (Connection) DriverManager.getConnection(URL, USER, PASS);
                    PreparedStatement pstmt = con.prepareStatement(sy.toString());
                    ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    string.append("<tr  data-stcod='").append(rs.getString("code_station")).append("'><td>").append(rs.getString("nn")).append("</td></tr>");
                }
            }

            armUsers.stream().forEach((Session x) -> {
                if (x.getUserProperties().containsValue(u)) {
                    try {
                        x.getBasicRemote().sendText(string.toString());
                        return;
                    } catch (IOException ex) {
                        Logger.getLogger(WS.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        } catch (SQLException ex) {
            System.out.println("exexexexex " + ex);
            Logger.getLogger(Auth.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            System.out.println("**** " + ex);
            Logger.getLogger(Auth.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
