
package arm.test;

import arm.wr.ReadOnDir;
import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Muzaffar
 */
@WebServlet(name = "Start", urlPatterns = {"/start"})
public class Start extends HttpServlet {

    private static final String CONTENT_TYPE = "text/html; charset=UTF-8";

    ReadOnDir rod;// = new ReadOnDir();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        rod = new ReadOnDir();
//        System.out.println("---------------------------");
        rod.setDaemon(true);
        rod.start();
//        System.out.println("ReadOnDir "+rod);
//        System.out.println("+++++++rod.getName()++++++++++++++++++++"+rod.getName());
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE);
        request.getSession();
        
        request.getRequestDispatcher("index.html").forward(request, response);
    }
}
