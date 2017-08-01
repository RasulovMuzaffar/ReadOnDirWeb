package arm.util;

import static arm.ws.WS.armUsers;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(name = "logOff", urlPatterns = {"/logOff"})
public class logOff extends HttpServlet {

    
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("Exit!!! " + request.getSession().getAttribute("user"));
        request.getSession().invalidate();
//        armUsers.remove(userSession);
        response.sendRedirect("auth.html");
    }
}
