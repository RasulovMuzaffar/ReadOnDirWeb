/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arm.test;

import arm.wr.ReadOnDir;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.Map;
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
        rod.interrupt();
        System.out.println("---------------------------");
        rod.start();
        System.out.println("ReadOnDir "+rod);
        System.out.println("+++++++rod.getName()++++++++++++++++++++"+rod.getName());
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE);
        request.getSession();
        
        request.getRequestDispatcher("index.html").forward(request, response);
    }
}
