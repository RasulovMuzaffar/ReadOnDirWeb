/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arm.test;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Muzaffar
 */
@WebServlet(name = "Auth", urlPatterns = {"/auth"})
public class Auth extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String user = null;
        String password = null;
        try {
            user = request.getParameter("user");
            password = request.getParameter("password");
        } catch (Exception e) {
            System.out.println("проблема в авторизации " + e);
        }

        request.getSession(true);
        request.getSession().setAttribute("user", user);
        System.out.println(request.getSession().getId());
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

}
