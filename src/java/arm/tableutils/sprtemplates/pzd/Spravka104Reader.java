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

public class Spravka104Reader implements TableReaderInterface {

//    regexDocHead
    final static String RDH = "([A-ZА-Я]{2,4})\\s+([A-ZА-Я]{2,4})\\s+"
            + "(?<spr>104)\\s+(?<date>\\d{2}\\.\\d{2})\\s+(?<time>\\d{2}\\-\\d{2})\\s+"
            + "([A-ZА-Я]{2}\\s+[A-ZА-Я]{0,6}\\d{0,3})\\s+([A-ZА-Я]{8})\\s+"
            + "(\\\"[A-ZА-Я]{5}\\\")\\s+([A-ZА-Я]{7})\\s+"
            + "([A-ZА-Я]{3}\\.[A-ZА-Я]{5})\\s+([A-ZА-Я]{1})\\s+([A-ZА-Я]{6})";

//    regexTHead
    final static String RTH1 = "(?<thnp>[A-ZА-Я]{4})\\s+(?<thidx>[A-ZА-Я]{6}\\s+[A-ZА-Я]{6})\\s+"
            + "(?<thst>[A-ZА-Я]{5})\\s+(?<thoper>[A-ZА-Я]{4})\\s+"
            + "(?<thdate>[A-ZА-Я]{4})\\s+(?<thtime>[A-ZА-Я]{5})";

//    regexTBody
    final static String RTB1 = "(?<tbnp>\\d{4})\\s+"
            + "(?<tbidx>\\d{4,6}\\+?\\s?\\d{2,3}\\+?\\s?\\d{4,6})\\s+"
            + "(?<tbst>[A-ZА-Я]{0,6}\\-?\\.?[A-ZА-Я]{0,5}\\d{0,2}\\.?)\\s+"
            + "(?<tboper>[A-ZА-Я]{2,4}\\d{0,2}\\-?\\.?)\\s+"
            + "(?<tbdate>\\d{2}\\.\\d{2})\\s+(?<tbtime>\\d{2}\\-\\d{2})";

//    regexTHead
    final static String RTH2 = "(?<thnv>[A-ZА-Я]{1}\\s+[A-ZА-Я]{6})\\s?\\:\\s?"
            + "(?<thstn>[A-ZА-Я]{2}\\.[A-ZА-Я]{3})\\s?\\:\\s?"
            + "(?<thgr>[A-ZА-Я]{4})\\s?\\:\\s?"
            + "(?<thdor>[A-ZА-Я]{3})\\s?\\:\\s?"
            + "(?<thgos>[A-ZА-Я]{3})";

//    regexTBody
//    final static String RTB2 = "([A-ZА-Я]{11}\\s(?<tbsobst>[A-ZА-Я]{2,4})\\s+)?"
//            + "(?<tbnv>\\d{8})\\s+"
//            + "(?<tbstn>[A-ZА-Я]{0,6}\\-?\\.?[A-ZА-Я]{0,5}\\d{0,6}\\.?)\\s+"
//            + "(?<tbgr>\\d{5})\\s+"
//            + "(?<tbdor>\\d{1,2})\\s+"
//            + "(?<tbgos>\\d{1,2})";
    final static String RTB2 = "([A-ZА-Я]{11}\\s(?<tbsobst>[A-ZА-Я]{2,4})\\s+)?"
            + "(?<tbnv>\\d{8})\\s+"
            + "(?<tbstn>[A-ZА-Я]{0,6}\\-?\\.?[A-ZА-Я]{0,5}\\d{0,6}\\.?)\\s+"
            + "(?<tbgr>\\d{5})\\s+"
            + "(?<tbdor>\\d{0,2})\\s+"
            + "(?<tbgos>\\d{0,2})\\s"
            + "(\\s+[A-ZА-Я]{5}\\s[A-ZА-Я]{7}\\s[A-ZА-Я]{2}\\s[A-ZА-Я]{2,4}\\s+"
            + "(?<tbiv>\\d{1,3}))?";
    //https://regex101.com/r/JCkg1B/1

    final HistoryInterface hi = new WriteToHist();

    @Override
    public HtmlTable processFile(String fileName, Users u) {
        Pattern pattern;
        Matcher matcher;
        boolean reading = false;
        boolean docHead = false;
        boolean tHead = false;
        boolean tBody = false;

        String f = TextReplace.getSha(TextReplace.getText(fileName)).replace("-----------------------------", "");
        HtmlTable result1 = new HtmlTable();
        HtmlTable result2 = new HtmlTable();
        HtmlTable resultT = new HtmlTable();

        

        pattern = Pattern.compile(RDH);
        matcher = pattern.matcher(f);

        boolean tableHeaderProcessed = false;
        
        String obj = "";

        while (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                result1.addCell(matcher.group(i));
            }
            
            if (!tableHeaderProcessed) {
                tableHeaderProcessed = true;
                result1.markCurrentRowAsDocHeader();
            }

            docHead = true;
            result1.advanceToNextRow();
        }
        if (docHead == false) {
            return null;
        }
        pattern = Pattern.compile(RTH1);
        matcher = pattern.matcher(f);
        tableHeaderProcessed = false;

        while (matcher.find()) {
            result1.addCell("№");

            for (int i = 1; i <= matcher.groupCount(); i++) {
                result1.addCell(matcher.group(i));
            }

            if (!tableHeaderProcessed) {
                tableHeaderProcessed = true;
                result1.markCurrentRowAsHeader();
            }

            tHead = true;
            result1.advanceToNextRow();
        }

        if (tHead == false) {
            return null;
        }

        pattern = Pattern.compile(RTB1);
        matcher = pattern.matcher(f);

        int n = 1;
        while (matcher.find()) {
            result1.addCell("" + n++);
            for (int i = 1; i <= matcher.groupCount(); i++) {
                if (matcher.group("tbidx").equals(matcher.group(i))) {
                    result1.addCell(plusToSpace(matcher.group(i)));
                } else {
                    result1.addCell(matcher.group(i));
                }
            }
            obj = plusToSpace(matcher.group("tbidx").replace("+0", "+"));

            if (!tableHeaderProcessed) {
                tableHeaderProcessed = true;
                result1.markCurrentRowAsHeader();
            }
            reading = true;
            tBody = true;
            result1.advanceToNextRow();
        }

        if (docHead == false) {
            return null;
        } else if (fromDB != true) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM HH:mm");
            Date currDate = new Date();
            History h = new History();
            h.setSprN("104");
            h.setDate("" + dateFormat.format(currDate));
            h.setTime("");
            h.setObj(obj);
            hi.infoFromSpr(h);
        }
        
        if (tBody == false) {
            return null;
        }

        resultT.addTable(result1);
        resultT.advanceToNextTable();

        pattern = Pattern.compile(RTH2);
        matcher = pattern.matcher(f);
        tableHeaderProcessed = false;

        while (matcher.find()) {

            result2.addCell("№");
            result2.addCell("СОБСТВЕННИК");
            for (int i = 1; i <= matcher.groupCount(); i++) {
                result2.addCell(matcher.group(i));
            }
            result2.addCell("КОЛИЧЕСТВО");

            if (!tableHeaderProcessed) {
                tableHeaderProcessed = true;
                result2.markCurrentRowAsHeader();
            }

            tHead = true;
            result2.advanceToNextRow();
        }

        if (tHead == false) {
            return null;
        }

        pattern = Pattern.compile(RTB2);
        matcher = pattern.matcher(f);

        int m = 1;
        while (matcher.find()) {
            result2.addCell("" + m++);

            result2.addCell("<b>" + delNull(matcher.group("tbsobst")) + "</b>");
            result2.addCell(delNull(matcher.group("tbnv")));
            result2.addCell(delNull(matcher.group("tbstn")));
            result2.addCell(delNull(matcher.group("tbgr")));
            result2.addCell(delNull(matcher.group("tbdor")));
            result2.addCell(delNull(matcher.group("tbgos")));
            if (matcher.group("tbiv") != null) {
                result2.addCell(htmlParse("Вагонов " + delNull(matcher.group("tbiv"))));
                result2.markCurrentRowAsRegularUnderlining();
            } else {
                result2.addCell("");
            }

            if (!tableHeaderProcessed) {
                tableHeaderProcessed = true;
                result2.markCurrentRowAsHeader();
            }

            reading = true;
            tBody = true;
            result2.advanceToNextRow();
        }

        if (tBody == false) {
            return null;
        }
        resultT.addTable(result2);
//        resultT.advanceToNextTable();

        System.out.println("docHead104 === " + docHead);
        System.out.println("tHead104 === " + tHead);
        System.out.println("tBody104 === " + tBody);
        if (reading == true && (docHead == true && tHead == true && tBody == true)) {
//            System.out.println("can reading SPR104 result1 --- " + result1);
//            System.out.println("can reading SPR104 result2 --- " + result2);
            System.out.println("can reading SPR104 ");
            if (forPopup == true) {
                ReadOnDir.spr = "sprPopup";
            } else {
                ReadOnDir.spr = "sprDefault";
            }
//            forPopup=false;
            return resultT;
        } else {
            System.out.println("can not reading SPR104 ");
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
