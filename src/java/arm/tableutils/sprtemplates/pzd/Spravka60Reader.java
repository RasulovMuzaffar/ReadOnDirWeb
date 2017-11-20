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

public class Spravka60Reader implements TableReaderInterface {

//    regexDocHead
    final static String RDH = "(?<dh>[A-ZА-Я]{2}\\s+[A-ZА-Я]{2,4}\\s+(?<spr>60)\\s+(?<date>\\d{2}\\.\\d{2})\\s+(?<time>\\d{2}\\-\\d{2})\\s+[A-ZА-Я]{2}\\s+\\d{4})\\s+"
            + "\\((?<idx>\\d{4,6}\\s?\\+\\s?\\d{3,4}\\s?\\+\\s?\\d{4,6})\\)\\s+"
            + "(?<pros>[A-ZА-Я]{4}\\s?\\-?\\s?\\d{4,6}\\s{0,}\\d{0,2}\\.?\\d{0,2}\\s?\\d{0,2}\\-?\\d{0,2}\\s?[A-ZА-Я]{0,20})?\\s+"
            + "(?<napr>[A-ZА-Я]{4}\\s?\\-?\\s?\\d{4,6}\\s{0,}\\d{0,2}\\.?\\d{0,2}\\s?\\d{0,2}\\-?\\d{0,2}\\s?[A-ZА-Я]{0,20})?\\s?"
            + "(?<nppv>[A-ZА-Я]{4}\\s?\\-?\\s?\\d{4,6}\\s{0,}\\d{0,2}\\.?\\d{0,2}\\s?\\d{0,2}\\-?\\d{0,2}\\s?[A-ZА-Я]{0,20})?\\s?"
            + "(?<ippv>[A-ZА-Я]{4}\\s?[A-ZА-Я]{0,20}\\-?\\s?\\d{4,6}\\s{0,}\\d{0,2}\\.?\\d{0,2}\\s?\\d{0,2}\\-?\\d{0,2}\\s?)?\\s+"
            + "(?<sppv>[A-ZА-Я]{4}\\s?\\-?\\s?\\d{4,6}\\s{0,}\\d{0,2}\\.?\\d{0,2}\\s?\\d{0,2}\\-?\\d{0,2}\\s?)?(?<otstv>[A-ZА-Я]{0,20})?";

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

        pattern = Pattern.compile(RDH);
        matcher = pattern.matcher(f);

        boolean tableHeaderProcessed = false;

        String obj = "";

        while (matcher.find()) {

            result.addCell(matcher.group("dh"));
            result.addCell("<b>" + plusToSpace(matcher.group("idx")) + "</b>");
            result.addCell("<br/><div class='smallH3'>" + delNull(matcher.group("pros")));
            result.addCell("<br/>" + delNull(matcher.group("napr")));
            result.addCell("<br/>" + delNull(matcher.group("nppv")));
            result.addCell("<br/>" + delNull(matcher.group("ippv")));
            result.addCell("<br/>" + delNull(matcher.group("sppv")));
            result.addCell(" " + delNull(matcher.group("otstv")) + "</div>");

            obj = plusToSpace(matcher.group("idx").replace("+0", "+"));

            if (!tableHeaderProcessed) {
                tableHeaderProcessed = true;
                result.markCurrentRowAsDocHeader();
            }
            reading = true;
            docHead = true;
            result.advanceToNextRow();
        }

        if (docHead == false) {
            return null;
        } else if (fromDB != true) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM HH:mm");
            Date currDate = new Date();
            History h = new History();
            h.setSprN("60");
            h.setDate("" + dateFormat.format(currDate));
            h.setTime("");
            h.setObj(obj);
            hi.infoFromSpr(h);
        }

        System.out.println("docHead60 === " + docHead);
        if (reading == true && docHead == true) {
            System.out.println("can reading SPR60 ");
            if (forPopup == true) {
                ReadOnDir.spr = "sprPopup";
            } else {
                ReadOnDir.spr = "sprDefault";
            }
//            forPopup=false;

            return result;
        } else {
            System.out.println("can not reading SPR60 ");
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
