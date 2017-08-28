package arm.wr;

import arm.ent.Users;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.websocket.Session;

public class Write {
public static boolean forPopup=false;
//    static String path = "c:\\testFolder\\out";
    static String path = "c:\\soob\\out";
    String autoNo;

    public void getWrite(Session userSession, String str) {
        Users u = (Users) userSession.getUserProperties().get("usrname");
        autoNo = u.getAutoNo();
        String[] s = str.split("\u0003");
//        String[] zprs = s[1].split(",");
        switch (s[0]) {
            case "spr":
//                String kodOrg = zprs[0];
//                String numMess = zprs[1];
//                String numSpr = zprs[2];
//                String object = cod4(zprs[3]);
//                String id_user = zprs[4];
//                createFile("(:" + numMess + " " + kodOrg + " " + object + ":" + numSpr + ":)");
                String text = s[1];
                createFile(text);
//                 {
//                    try {
//                        createFile(new String(text.getBytes(), "Cp866"));
//                    } catch (UnsupportedEncodingException ex) {
//                        Logger.getLogger(Write.class.getName()).log(Level.SEVERE, null, ex);
//                        System.out.println("UnsupportedEncodingException in Write : " + ex);
//                    }
//                }
                break;
            case "getTGNL":
                System.out.println("TELEGRAMMA NATURNIY LIST");
//                (:213 0:7200 89 7258 902:)
                forPopup = true;
                createFile("(:213 0:" + s[1] + " 902:)");
                break;
            case "getRS":
                System.out.println("Full SPR");
//                (:213 0: ' + idx + ' 12 42 60 902 104:)
                forPopup = true;
                createFile("(:213 0:" + s[1] + " 12 42 60 902 104:)");
                break;
        }

    }

    private void createFile(String text) {
        writingFile(path + "\\" + autoNo + "a0" + reading(text) + ".txt", text);
        System.out.println("Message is sending!");
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

    private void writingFile(String pth, String text) {

        try (OutputStream outputStream = new FileOutputStream(pth);
                Writer outputStreamWriter = new OutputStreamWriter(outputStream, "Cp866");) {
        outputStreamWriter.write(text);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Write.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Write.class.getName()).log(Level.SEVERE, null, ex);
        }

//
//        try (FileWriter writer = new FileWriter(pth, false)) {
//            writer.write(text);
////            writer.write(new String(text.getBytes(), "Cp866"));
//        } catch (Exception e) {
//            System.out.println("Exception in writingFile " + e);
//        }
    }

    private String cod4(String zpr) {
        return zpr.substring(0, 4);
    }
}
