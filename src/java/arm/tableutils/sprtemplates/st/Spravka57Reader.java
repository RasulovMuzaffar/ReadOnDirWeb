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

public class Spravka57Reader implements TableReaderInterface {

//    regexDocHead
    final static String RDH = "([A-ZА-Я]{2})\\s+"
            + "(?<dhdor>[A-ZА-Я]{1,4})\\s+(?<spr>57)\\s+"
            + "(?<date>\\d{2}\\.\\d{2})\\s+(?<time>\\d{2}\\-\\d{2})\\s+"
            + "([A-ZА-Я]{2})\\s+(\\d{1,2})\\s+([A-ZА-Я]{6})\\s+"
            + "([A-ZА-Я]{7})\\s+([A-ZА-Я]{1})\\s+([A-ZА-Я]{2}\\.)\\s+"
            + "(?<st>[A-ZА-Я]{0,15}-{0,1}.{0,1}[A-ZА-Я]{0,5}\\d{0,2}.{0,1})";

//    regexTHead
    final static String RTH = "([A-ZА-Я]{1})\\s+([A-ZА-Я]{4}\\.)\\s+"
            + "([A-ZА-Я]{0,15}-{0,1}.{0,1}[A-ZА-Я]{0,5}\\d{0,2}.{0,1})";

//    regexTBody
    final static String RTB = "((?<snapr>[A-ZА-Я]{1}\\s[A-ZА-Я]{4}\\.)\\s"
            + "(?<tbst>[A-ZА-Я]{0,15}-{0,1}.{0,1}[A-ZА-Я]{0,5}\\d{0,2}.{0,1}))?"
            + "\\s+((?<tbnp>\\d{4})\\s?\\"
            + "((?<tbidx>\\d{4,6}\\+\\s?\\d{1,3}\\+\\d{4,6})\\)"
            + "(?<tbstate>[A-ZА-Я]{2,4})\\s+"
            + "(?<tbxz1>\\d{1,2})\\s+(?<tbtime>\\d{2}\\-\\d{2}))";

    final HistoryInterface hi = new WriteToHist();

    @Override
    public HtmlTable processFile(String fileName) {

        String f = TextReplace.getSha(TextReplace.getText(fileName));
        return getResult(f);
    }

    private HtmlTable getResult(String str) {
        Pattern pattern;
        Matcher matcher;
        boolean reading = false;
        boolean docHead = false;
        boolean tHead = false;
        boolean tBody = false;
        String doroga = "";

        HtmlTable result = new HtmlTable();

        pattern = Pattern.compile(RDH);
        matcher = pattern.matcher(str);

        boolean tableHeaderProcessed = false;

        String obj = "";
        while (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                result.addCell(matcher.group(i));

            }

            doroga = matcher.group("dhdor");

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
            h.setSprN("57");
            h.setDate("" + dateFormat.format(currDate));
            h.setTime("");
            h.setObj(obj);
            hi.infoFromSpr(h);
        }

//        tableHeaderProcessed = false;
        {
            result.addCell("НАПР");
            result.addCell("№ поезда");
            result.addCell("Индекс поезда");
            result.addCell("Сост");
            result.addCell("Дата");
            result.addCell("Время");

//            if (!tableHeaderProcessed) {
//                tableHeaderProcessed = true;
            result.markCurrentRowAsHeader();
//            }

            tHead = true;
            result.advanceToNextRow();
        }

        pattern = Pattern.compile(RTB);
        matcher = pattern.matcher(str);

        while (matcher.find()) {
            String column1 = delNull(matcher.group("snapr"));
            String column2 = delNull(htmlParse(matcher.group("tbst")));
            result.addCell(column2);
            result.addCell(matcher.group("tbnp"));
            result.addCell(matcher.group("tbidx"));
            result.addCell(matcher.group("tbstate"));
            result.addCell(matcher.group("tbxz1"));
            result.addCell(matcher.group("tbtime"));

            if (!tableHeaderProcessed) {
                tableHeaderProcessed = true;
                result.markCurrentRowAsHeader();
            }

            if (!column1.equals("")) {
                result.markCurrentRowAsRegularUnderscore();
            }
            reading = true;
            tBody = true;
            result.advanceToNextRow();
        }

        if (tBody == false) {
            System.out.println("fignya v 57 tBody!!!");
            return null;
        }
        System.out.println("docHead57 === " + docHead);
        System.out.println("tHead57 === " + tHead);
        System.out.println("tBody57 === " + tBody);
        if (reading == true && (docHead == true && tHead == true && tBody == true)) {
            System.out.println("can reading SPR57 ");
            ReadOnDir.spr = "sprDefault";
            return result;
        } else {
            System.out.println("can not reading SPR57 ");
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
        if (s != null) {
            return "<b>" + s + "</b>";
        } else {
            return null;
        }
    }

    @Override
    public List<HtmlTable> readersResult() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
