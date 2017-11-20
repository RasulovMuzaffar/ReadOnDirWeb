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

public class Spravka12Reader implements TableReaderInterface {

//    regexDocHead
    final static String RDH = "(?<title>[A-ZА-Я]{2,3}\\s[A-ZА-Я]{2,4}\\s+(?<spr>12)\\s+"
            + "(?<date>\\d{2}\\.\\d{2})\\s+(?<time>\\d{2}\\-\\d{2})\\s+[A-ZА-Я]{1,4}\\s+"
            + "[A-ZА-Я]{6}\\s+[A-ZА-Я]{1}\\s+[A-ZА-Я]{7})\\s+"
            + "\\((?<idx>\\d{4,6}\\s?\\+?\\s?\\d{1,3}\\s?\\+?\\s?\\d{4,6})\\)";

//    regexTHead
    final static String RTH = "(?<thst>[A-ZА-Я]{4})\\s+"
            + "(?<thoper>[A-ZА-Я]{4})\\s+"
            + "(?<thdate>[A-ZА-Я]{4})\\s+"
            + "(?<thtime>[A-ZА-Я]{5})\\s+"
            + "(?<thnapr>[A-ZА-Я]{4})\\s+"
            + "(?<thnp>[A-ZА-Я]{5})";

//    regexTBody
    final static String RTB = "(?<tbst>[A-ZА-Я]{0,6}\\-?\\.?[A-ZА-Я]{0,5}\\d{0,2}\\.?)\\s+"
            + "(?<tboper>[A-ZА-Я]{3,4}\\-?\\d{0,2})\\s+"
            + "(?<tbdate>\\d{2}\\.\\d{2})\\s+"
            + "(?<tbtime>\\d{2}\\-\\d{2})\\s+"
            + "(?<tbnapr>[A-ZА-Я]{0,6}\\-?\\.?[A-ZА-Я]{0,5}\\d{0,2}\\.?)\\s+"
            + "(?<tbnp>\\d{4}\\s?\\+?\\-?\\d{0,3})";

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

            result.addCell(matcher.group("title"));
            result.addCell("<b>" + plusToSpace(matcher.group("idx")) + "</b>");

            obj = plusToSpace(matcher.group("idx"));

            if (!tableHeaderProcessed) {
                tableHeaderProcessed = true;
                result.markCurrentRowAsDocHeader();
            }

            docHead = true;
            result.advanceToNextRow();
        }

        if (docHead == false) {
            return null;
        } else if (fromDB != true) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM HH:mm");
            Date currDate = new Date();
            History h = new History();
            h.setSprN("12");
            h.setDate("" + dateFormat.format(currDate));
            h.setTime("");
            h.setObj(obj);
            hi.infoFromSpr(h);
        }

        pattern = Pattern.compile(RTH);
        matcher = pattern.matcher(f);
        tableHeaderProcessed = false;

        while (matcher.find()) {

            result.addCell("№");
            for (int i = 1; i <= matcher.groupCount(); i++) {
                result.addCell(matcher.group(i));
            }

            if (!tableHeaderProcessed) {
                tableHeaderProcessed = true;
                result.markCurrentRowAsHeader();
            }

            tHead = true;
            result.advanceToNextRow();
        }

        pattern = Pattern.compile(RTB);
        matcher = pattern.matcher(f);

        int n = 1;
        while (matcher.find()) {
            result.addCell("" + n++);
            for (int i = 1; i <= matcher.groupCount(); i++) {
                result.addCell(matcher.group(i));
            }

            if (!tableHeaderProcessed) {
                tableHeaderProcessed = true;
                result.markCurrentRowAsHeader();
            }
            reading = true;
            tBody = true;
            result.advanceToNextRow();
        }

        System.out.println("docHead12 === " + docHead);
        System.out.println("tHead12 === " + tHead);
        System.out.println("tBody12 === " + tBody);
        if (reading == true && (docHead == true && tHead == true && tBody == true)) {
            System.out.println("can reading SPR12 ");
            if (forPopup == true) {
                ReadOnDir.spr = "sprPopup";
            } else {
                ReadOnDir.spr = "sprDefault";
            }
//            forPopup=false;
            return result;
        } else {
            System.out.println("can not reading SPR12 ");
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

    private String plusToSpace(String s) {
        return s.replace("+", " ");
    }

    @Override
    public List<HtmlTable> readersResult() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
