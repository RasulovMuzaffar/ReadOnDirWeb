package arm.test;

import arm.ent.Users;
import arm.wr.WriteToHist;
import static arm.ws.WS.armUsers;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.xml.sax.SAXException;

@WebServlet(name = "Auth", urlPatterns = {"/auth"})
public class Auth extends HttpServlet {

    public static String sessionTimeoutFromWebXml;
    private static final String URL = "jdbc:mysql://localhost:3306/arm";
    private static final String USER = "test";
    private static final String PASS = "test";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String login = null;
        String password = null;
        try {
            login = request.getParameter("login");
            password = request.getParameter("password");
        } catch (Exception e) {
            System.out.println("проблема в авторизации " + e);
        }
        System.out.println("looooogin: " + login);
        System.out.println("passsssss: " + password);
        try {
            sessionTimeoutFromWebXml = XPathFactory.newInstance().newXPath().compile("web-app/session-config/session-timeout").evaluate(DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(getServletContext().getResourceAsStream("/WEB-INF/web.xml")));
        } catch (XPathExpressionException | ParserConfigurationException | SAXException ex) {
            Logger.getLogger(Auth.class.getName()).log(Level.SEVERE, null, ex);
        }

        Users u = null;
        try {

            String sql = "select * from users where login='" + login + "' and password='" + password + "'";
            try (Connection con = (Connection) DriverManager.getConnection(URL, USER, PASS);
                    PreparedStatement pstmt = con.prepareStatement(sql);
                    ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    u = new Users(rs.getLong("id"), rs.getString("firstname"),
                            rs.getString("lastname"), rs.getInt("id_role"),
                            rs.getInt("id_org"), rs.getString("auto_no"),
                            rs.getString("login"), rs.getString("password"));
                }
            }

        } catch (SQLException ex) {
            System.out.println("exexexexex " + ex);
            Logger.getLogger(Auth.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (u != null) {
            if (!checkUser(u)) {
                System.out.println("this person is online!");
                response.sendRedirect("errorPage.html");
            } else {
                System.out.println("new person!");
                HttpSession httpSession = request.getSession(true);
                System.out.println("auth httpSession ==> " + login + " " + httpSession.getId());
                httpSession.setAttribute("user", u);
                httpSession.setAttribute("usrname", u);

                request.getRequestDispatcher("index2.jsp").forward(request, response);
            }
        } else {
            response.sendRedirect("auth.html");
        }
    }

    private boolean checkUser(Users u) {
        boolean b = true;
        Users user = u;
        if (!armUsers.isEmpty()) {
            for (Session armUser : armUsers) {
                if (armUser.getUserProperties().containsValue(user)) {
                    b = false;
                    try {
                        armUser.getBasicRemote().sendText("warning\u0003Попытка войти под Вашим логином!");
                    } catch (IOException ex) {
                        Logger.getLogger(Auth.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                }
                break;
            }
        }
        return b;
    }
}
