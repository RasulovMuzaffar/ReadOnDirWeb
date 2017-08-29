package arm.wr;

import arm.ent.Users;
import arm.test.Auth;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WriteToHist implements HistoryInterface {

    private static final String URL = "jdbc:mysql://localhost:3306/arm";
    private static final String USER = "test";
    private static final String PASS = "test";
    final static String SPR = "\\s+(?<spr>\\d{2,4})\\s+(?<date>\\d{2}.\\d{2})\\s+(?<time>\\d{2}-\\d{2})\\s+\\D";

    String[] info;
    final static List<String> ls = new ArrayList<>();

    @Override
    public String infoFromSpr(String str) {
//        System.out.println("++++++++++ " + str);
        info = str.split(" : ");
        for (int i = 0; i < info.length; i++) {
            ls.add(info[i]);
        }
        return null;
    }

    public static void writeToDB(Users user) {
        for (String l : ls) {
            System.out.println("******* " + l);
        }
//        Pattern pattern;
//        Matcher matcher;
//
//        StringBuilder sb = new StringBuilder();
//        pattern = Pattern.compile(SPR);
//        matcher = pattern.matcher(str);
//
//        while (matcher.find()) {
//            for (int i = 1; i <= matcher.groupCount(); i++) {
//                sb.append(matcher.group(i)).append(" : ");
//            }
//        }
//
//        System.out.println("-------------------" + sb.toString());
//        String sql = "insert into ";
//        try (Connection con = (Connection) DriverManager.getConnection(URL, USER, PASS);
//                PreparedStatement pstmt = con.prepareStatement(sql);) {
////            pstmt.set
//            pstmt.executeQuery();
//
//        } catch (SQLException ex) {
//            System.out.println("exexexexex " + ex);
//            Logger.getLogger(Auth.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    public void createHistList() {

    }
}
