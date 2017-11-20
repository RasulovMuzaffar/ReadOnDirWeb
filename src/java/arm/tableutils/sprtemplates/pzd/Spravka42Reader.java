package arm.tableutils.sprtemplates.pzd;

import arm.ent.History;
import arm.ent.Users;
import arm.tableutils.HtmlTable;
import arm.tableutils.tablereaders.TableReaderInterface;
import arm.tableutils.tablereaders.utils.TextReplace;
import arm.wr.HistoryInterface;
import arm.wr.ReadOnDir;
import static arm.wr.Write.forPopup;
import static arm.wr.Write.fromDB;
import arm.wr.WriteToHist;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Spravka42Reader implements TableReaderInterface {

//    regexDocHead
    final static String RDH = "(?<vcdor>[A-ZA-Я]{2} [A-ZA-Я]{2,4})\\s+"
            + "(?<spr>42)\\s+"
            + "(?<date>\\d{2}.\\d{2})\\s+"
            + "(?<time>\\d{2}-\\d{2})\\s+\\S+\\s+\\S+\\s+\\d{4}\\("
            + "(?<idx>\\d{4}\\+\\s?\\d{2,3}\\+\\d{4})\\)";

//    regexTHead
    final static String RTH = "";

//    regexTBody
    final static String RTB = "";

    final HistoryInterface hi = new WriteToHist();

    @Override
    public HtmlTable processFile(String fileName, Users u) {

        Pattern pattern;
        Matcher matcher;
        boolean reading = false;
        boolean docHead = false;
        boolean tHead = false;
        boolean tBody = false;


        String f = TextReplace.getSha(TextReplace.getText(fileName));
        HtmlTable result = new HtmlTable();

        boolean tableHeaderProcessed = false;

        String vcdor = "";
        StringBuilder spr42 = new StringBuilder();

        pattern = Pattern.compile(RDH);
        matcher = pattern.matcher(f);

        String obj = "";
        
        while (matcher.find()) {
            vcdor = matcher.group("vcdor");

            spr42.append(vcdor).append(" ");
            String[] arrDoc = f.split("[ВЦ]{2} [A-ZA-Я]{2,4}\\s+");
            for (String s : arrDoc) {
                if ("42".equals(s.substring(0, 2))) {
                    result.addCell(spr42.append(s).toString().replace("\r\n", "<br/>").replace("42", "<b>42</b>"));
                    break;
                }
            }

            obj = plusToSpace(matcher.group("idx").replace("+ ", "+"));

            if (!tableHeaderProcessed) {
                tableHeaderProcessed = true;
                result.markCurrentRowAsDocHeader();
            }

            reading = true;
            docHead = true;
        }

        if (docHead == false) {
            return null;
        } else if (fromDB != true) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM HH:mm");
            Date currDate = new Date();
            History h = new History();
            h.setSprN("42");
            h.setDate("" + dateFormat.format(currDate));
            h.setTime("");
            h.setObj(obj);
            hi.infoFromSpr(h);
        }
        
        System.out.println("docHead42 === " + docHead);
        if (reading == true && docHead == true) {
            System.out.println("can reading SPR42 ");
            if (forPopup == true) {
                ReadOnDir.spr = "sprPopup";
            } else {
                ReadOnDir.spr = "sprDefault";
            }
//            forPopup=false;
            return result;
        } else {
            System.out.println("can not reading SPR42 ");
            return null;
        }
    }

    private String delNull(String s) {
        if (s != null) {
            return s;
        } else {
            return "";
        }
    }

    private String htmlParse(String s) {
        if (!"".equals(s)) {
            String[] str = s.split("\\s+");
            String s1 = str[0];
            String s2 = str[1];
            return "<b>" + s1 + " <div style='color:red'>" + s2 + "</div></b>";
        } else {
            return "";
        }
    }

    private String plusToSpace(String s) {
        return s.replace("+", " ");
    }

    @Override
    public List<HtmlTable> readersResult() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
