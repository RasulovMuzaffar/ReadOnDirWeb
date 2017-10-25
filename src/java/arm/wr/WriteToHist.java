package arm.wr;

import arm.ent.History;
import arm.ent.InMessages;
import arm.ent.Users;
import arm.tableutils.HtmlTable;
import arm.tableutils.tablereaders.CompositeReader;
import arm.tableutils.tablereaders.MultipleResultsException;
import static arm.wr.ReadOnDir.tableReader;
import arm.ws.WS;
import static arm.ws.WS.armUsers;
import static arm.wr.Write.fromDB;
import static arm.ws.WS.armUsers;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.Session;

public class WriteToHist implements HistoryInterface {

    private static final String URL = "jdbc:mysql://localhost:3306/armasoup";
    private static final String USER = "root";
    private static final String PASS = "123456";

    final static List<String> lspr = new ArrayList<>();
    final static Set<String> sinf = new HashSet<>();

    @Override
    public void infoFromSpr(History h) {
        lspr.add(h.getSprN());
        sinf.add(h.toString());
    }

    @Override
    public void writeToDB(Users user, String str) {
        String sprs = "";
        for (String l : lspr) {
            sprs += " <b>" + l + "</b>";
        }
        String inf = "";
        for (String l : sinf) {
            inf += l;
        }
        System.out.println("history --->>> " + inf);
        System.out.println("history --->>> " + inf + sprs);
        lspr.clear();
        sinf.clear();
        if (!"".equals(inf) && !"".equals(sprs)) {
        System.out.println("writing to db!");
            String sql = "INSERT INTO in_messages (header, body, id_user) VALUES(?,?,?);";
            try (Connection con = (Connection) DriverManager.getConnection(URL, USER, PASS);
                    PreparedStatement pstmt = con.prepareStatement(sql);) {
                pstmt.setString(1, inf + sprs);
                pstmt.setString(2, str);
                pstmt.setLong(3, user.getId());
                pstmt.executeUpdate();

            } catch (SQLException ex) {
                System.out.println("exexexexex " + ex);
                Logger.getLogger(WriteToHist.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        createHistList(user);
    }

    public static void createHistList(Users user) {
        String sql = "SELECT * FROM in_messages WHERE id_user=" + user.getId() + " ORDER BY id DESC LIMIT 20";
        List<InMessages> lim = new ArrayList<>();
        InMessages im = new InMessages();
        try (Connection con = (Connection) DriverManager.getConnection(URL, USER, PASS);
                PreparedStatement pstmt = con.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                im = new InMessages(rs.getLong("id"), rs.getString("header"),
                        rs.getString("body"), rs.getTimestamp("curr_date"),
                        rs.getLong("id_user"));
                lim.add(im);
            }
        } catch (SQLException ex) {
            System.out.println("exexexexex " + ex);
            Logger.getLogger(WriteToHist.class.getName()).log(Level.SEVERE, null, ex);
        }
        StringBuilder sb = new StringBuilder();
        if (user.getOrg().equalsIgnoreCase("едц")) {
            for (InMessages i : lim) {
//                <li tabIndex="0" class="hist" data-idmess="${h.id}" onclick="getHist(this);">${h.header}</li>
                sb.append("<li tabIndex='0' class='hist' data-idmess=")
                        .append(i.getId()).append(" onclick='getHist(this);'>")
                        .append(i.getHeader()).append("</li>");
            }
        } else {
            for (InMessages i : lim) {
                sb.append("<li class='c-menu__item'>").append("<a href='#' class='c-menu__link' ")
                        .append("data-idmess='").append(i.getId()).append("' onclick='getHist(this);'>")
                        .append(i.getHeader()).append("</a></li>");
            }
        }
        

        armUsers.stream().forEach((Session x) -> {
            if (x.getUserProperties().containsValue(user)) {
                try {
                    x.getBasicRemote().sendText("histList\u0003" + sb.toString());
                } catch (IOException ex) {
                    Logger.getLogger(WS.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    @Override
    public void sendHist(Users user, int id) {

        System.out.println("User-->>> " + user.getLogin() + " messId -->" + id);
        try {

            String sql = "SELECT * FROM in_messages WHERE id=" + id;

            InMessages im = new InMessages();
            try (Connection con = (Connection) DriverManager.getConnection(URL, USER, PASS);
                    PreparedStatement pstmt = con.prepareStatement(sql);
                    ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    im = new InMessages(rs.getLong("id"), rs.getString("header"),
                            rs.getString("body"), rs.getTimestamp("curr_date"),
                            rs.getLong("id_user"));
                }
            } catch (SQLException ex) {
                System.out.println("exexexexex " + ex);
                Logger.getLogger(WriteToHist.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("im.getHead " + im.getHeader());
            System.out.println("im.getBody " + im.getBody());

            HtmlTable result = tableReader.processFile(im.getBody());

            if (result != null) {
                String answer = result.generateHtml();

                System.out.println("" + answer);
                armUsers.stream().forEach((Session x) -> {
                    if (x.getUserProperties().containsValue(user)) {
                        try {
                            x.getBasicRemote().sendText("sprDefault\u0003" + answer);
                        } catch (IOException ex) {
                            Logger.getLogger(WS.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                });
            } else {
                StringBuilder moreSprs = new StringBuilder();
                List<HtmlTable> list = tableReader.readersResult();
                if (list.size() != 0) {
                    for (HtmlTable l : list) {
                        moreSprs.append(l.generateHtml());
                        moreSprs.append("<br/>");
                    }
                    armUsers.stream().forEach((Session x) -> {
                        if (x.getUserProperties().containsValue(user)) {
                            try {
                                x.getBasicRemote().sendText("sprDefault\u0003" + moreSprs);
                                CompositeReader.lht.removeAll(list);
                            } catch (IOException ex) {
                                Logger.getLogger(WS.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    });
                } else {
                    String answer = im.getBody();
                    armUsers.stream().forEach((Session x) -> {
                        if (x.getUserProperties().containsValue(user)) {
                            try {
                                x.getBasicRemote().sendText("sprDefault\u0003<label><h3>" + answer + "</h3></label>");
                            } catch (IOException ex) {
                                Logger.getLogger(WS.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    });
                }
            }

        } catch (MultipleResultsException ex) {
            Logger.getLogger(WriteToHist.class.getName()).log(Level.SEVERE, null, ex);
        }
        fromDB = false;
    }
}
