//package arm.wr;
//
//import arm.ent.Users;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//
//@WebServlet(name = "Writing", urlPatterns = {"/writing"})
//public class Writing extends HttpServlet {
//
//    private static final String CONTENT_TYPE = "text/html; charset=UTF-8";
//    static private String autoNo;
//
//    @Override
//    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        response.setContentType(CONTENT_TYPE);
//        String kod = null;
//        String q = null;
//        String kodDok = null;
//        String object = null;
//        int id_user = 0;
//        kod = request.getParameter("kod");
//        q = request.getParameter("q");
//        kodDok = request.getParameter("kodDok");
//        object = request.getParameter("object");
//        id_user = Integer.parseInt(request.getParameter("id_user"));
////        autoNo = request.getSession().getAttributeNames();
//        System.out.println("autoNo " + request.getSession().getAttributeNames().nextElement());
//
//        Users u = (Users) request.getSession().getAttribute("user");
//        autoNo = u.getAutoNo();
//
//        createFile("(:" + kod + " " + q + " " + object + ":" + kodDok + ":)");
////        createFile("(:140:)");
//    }
//
//    static String path = "c:\\testFolder\\out";
////    static String path = "d:\\soob\\out";
//
//    public void createFile(String text) {
//        System.out.println("--------------->>> " + text);
//
//        writingFile(path + "\\" + autoNo + "a0" + reading(text) + ".txt", text);
//        System.out.println("Message is sending!");
//    }
//
//    private static void writingFile(String pth, String text) {
//        try (FileWriter writer = new FileWriter(pth, false)) {
////            writer.write(text);
//            writer.write(new String (text.getBytes(),"Cp866"));
//        } catch (Exception e) {
//            System.out.println("Exception in writingFile " + e);
//        }
//    }
//
//    public static String reading(String txt) {
//        String fileName = null;
//        Pattern p = Pattern.compile("\\D*:(\\d+).*");
//        Matcher m = p.matcher(txt);
//        if (m.find()) {
//            fileName = m.group(1);
//        }
//        return fileName;
//    }
//}
