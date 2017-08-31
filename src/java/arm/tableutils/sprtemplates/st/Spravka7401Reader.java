package arm.tableutils.sprtemplates.st;

import arm.ent.History;
import arm.tableutils.HtmlTable;
import arm.tableutils.tablereaders.TableReaderInterface;
import arm.tableutils.tablereaders.utils.TextReplace;
import arm.wr.HistoryInterface;
import arm.wr.ReadOnDir;
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

public class Spravka7401Reader implements TableReaderInterface {

//    regexDocHead
    final static String RDH = "([A-ZА-Я]{2}\\s[A-ZА-Я]{3})\\s+"
            + "(?<spr>\\d{4})\\s+"
            + "(?<date>\\d{2}.\\d{2})\\s+"
            + "(?<time>\\d{2}-\\d{2})\\s+"
            + "([A-ZА-Я]{2}\\s\\d{2})\\s+"
            + "([A-ZА-Я]{7})\\s+"
            + "([A-ZА-Я]{8})\\s+"
            + "([A-ZА-Я]{6})\\s+"
            + "(?<st>[A-ZА-Я]{6})";

//    regexTHead
    final static String RTH = "(?<thpoluch>[A-ZА-Я]{5})"
            + "-\\d{4,5}\\s+"
            + "(?<thnvag>[A-ZА-Я]{2}.[A-ZА-Я]{3})\\s+"
            + "(?<thves>[A-ZА-Я]{3})\\s+"
            + "(?<thgruz>[A-ZА-Я]{4})\\s+"
            + "(?<thnp>[A-ZА-Я]{2})\\s+"
            + "(?<thidx>[A-ZА-Я]{6})\\s+"
            + "(?<thdisl>[A-ZА-Я]{4})\\s+"
            + "(?<thoper>[A-ZА-Я]{4})\\s+"
            + "(?<thvroper>[A-ZА-Я]{2}.[A-ZА-Я]{4})\\s+"
            + "(?<thvroj>[A-ZА-Я]{2}.[A-ZА-Я]{2})";

//    regexTBody
    final static String RTB = "((?<poluch>\\d{4}){0,1}\\s+)(\\D+\\s){0,1}(?<vg>\\d{8})\\s+(?<ves>\\d{1,2})(\\s(?<gr>\\d{5})\\s+(?<np>\\d{4})\\s+(?<idx>\\d{4}\\+\\d{3}\\+\\d{4})\\s+(?<disl>\\d{5})\\s+(?<oper>[A-ZА-Я]{4})\\s+(?<vrop>\\d{2}\\s+\\d{2}\\-\\d{2})(?<vroj>\\s+\\d{2}\\s+\\d{2}\\-\\d{2}){0,1}){0,1}";

    final HistoryInterface hi = new WriteToHist();

    @Override
    public HtmlTable processFile(String fileName) {
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
            for (int i = 1; i <= matcher.groupCount(); i++) {
                result.addCell(matcher.group(i));
            }

            obj = matcher.group("st");

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
            h.setSprN("7401");
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
            result.addCell(delNull(matcher.group("poluch")));
            result.addCell(delNull(matcher.group("vg")));
            result.addCell(delNull(matcher.group("ves")));
            result.addCell(delNull(matcher.group("gr")));
            result.addCell(delNull(matcher.group("np")));
            result.addCell(delNull(matcher.group("idx")));
            result.addCell(delNull(matcher.group("disl")));
            result.addCell(delNull(matcher.group("oper")));
            result.addCell(delNull(matcher.group("vrop")));
            result.addCell(delNull(matcher.group("vroj")));

            if (!tableHeaderProcessed) {
                tableHeaderProcessed = true;
                result.markCurrentRowAsHeader();
            }
            reading = true;
            tBody = true;
            result.advanceToNextRow();
        }

        System.out.println("docHead7401 === " + docHead);
        System.out.println("tHead7401 === " + tHead);
        System.out.println("tBody7401 === " + tBody);
        if (reading == true && (docHead == true && tHead == true && tBody == true)) {
            System.out.println("can reading SPR7401 ");
            ReadOnDir.spr = "sprDefault";
            return result;
        } else {
            System.out.println("can not reading SPR7401 ");
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
