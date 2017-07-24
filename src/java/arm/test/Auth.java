/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arm.test;

import arm.ent.Users;
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

/**
 *
 * @author Muzaffar
 */
@WebServlet(name = "Auth", urlPatterns = {"/auth"})
public class Auth extends HttpServlet {

    private static final String URL = "jdbc:mysql://localhost:3306/armasoup";
    private static final String USER = "root";
    private static final String PASS = "123456";

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
        Users u = null;
//        List<Users> ul = new ArrayList<>();
        try {
            Class.forName("com.mysql.jdbc.Driver");

            String sql = "select * from users where login='" + login + "' and password='" + password + "'";
            try (Connection con = (Connection) DriverManager.getConnection(URL, USER, PASS);
                    PreparedStatement pstmt = con.prepareStatement(sql);
                    ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    u = new Users(rs.getLong("id"), rs.getString("firstname"),
                            rs.getString("lastname"), rs.getInt("id_role"),
                            rs.getInt("id_org"), rs.getString("auto_no"),
                            rs.getString("login"), rs.getString("password"));
//                ul.add(u);
                }
            }

        } catch (SQLException ex) {
            System.out.println("exexexexex " + ex);
            Logger.getLogger(Auth.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Auth.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (u != null) {
            HttpSession httpSession = request.getSession(true);
            httpSession.setAttribute("user", u);
            request.getSession().setAttribute("usrname", u.getLogin());
//            httpSession.setAttribute("usrname", u.getLogin());
            request.getRequestDispatcher("index.jsp").forward(request, response);
        } else {
            response.sendRedirect("auth.html");
        }
    }
}
