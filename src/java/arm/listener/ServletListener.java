
package arm.listener;

import arm.test.Singleton;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


public class ServletListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Singleton.getOurInstance();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("================contextDestroyed======================");
        Singleton.rod.interrupt();
        System.out.println("================rod.interrupt()======================");
    }
}
