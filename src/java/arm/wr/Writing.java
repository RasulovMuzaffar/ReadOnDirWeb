/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arm.wr;

import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Muzaffar
 */
@WebServlet(name = "Writing", urlPatterns = {"/writing"})
public class Writing extends HttpServlet {

    private static final String CONTENT_TYPE = "text/html; charset=UTF-8";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE);
//        String mess = null;
//        mess = request.getParameter("mess");
        String kod = null;
        String q = null;
        String kodDok = null;
        String object = null;
        kod = request.getParameter("kod");
        q = request.getParameter("q");
        kodDok = request.getParameter("kodDok");
        object = request.getParameter("object");
        createFile("(:" + kod + " " + q + " " + object + ":" + kodDok + ":)");
    }

    static String path = "c:\\testFolder\\out";

    public void createFile(String text) {
        System.out.println("--------------->>> " + text);
        writingFile(path + "\\" + reading(text) + ".txt", text);
        System.out.println("Message is sending!");
    }

    private static void writingFile(String pth, String text) {
        try (FileWriter writer = new FileWriter(pth, false)) {
            writer.write(text);

        } catch (Exception e) {
            System.out.println("Exception in writingFile " + e);
        }
    }

    public static String reading(String txt) {
        String fileName = null;
        Pattern p = Pattern.compile("\\D*:(\\d+).*");
        Matcher m = p.matcher(txt);
        if (m.find()) {
            fileName = m.group(1);
        }
        return fileName;
    }
}
