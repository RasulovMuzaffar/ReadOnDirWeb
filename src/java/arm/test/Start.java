
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

//    private static Singleton ourInstance;
//    static ReadOnDir rod;
//    
//    public static void getOurInstance() {
//        if (ourInstance == null) {
//            ourInstance = new Singleton();
//            rod = new ReadOnDir();
//            System.out.println("---------------------------");
//            rod.setDaemon(true);
//            rod.start();
//            System.out.println("+++++++++++++++++++++++++++");
//        }
//    }
}
