package arm.tableutils.sprtemplates.vag;

import arm.ent.History;
import arm.tableutils.HtmlTable;
import arm.tableutils.tablereaders.TableReaderInterface;
import arm.tableutils.tablereaders.utils.TextReplace;
import arm.wr.HistoryInterface;
import arm.wr.ReadOnDir;
import static arm.wr.Write.fromDB;
import arm.wr.WriteToHist;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Spravka2610Reader implements TableReaderInterface {
//regexDocHead

    final static String RDH = "([A-ZА-Я]{2,3}\\s[A-ZА-Я]{2,3})\\s+"
            + "(?<spr>2610)\\s+(?<date>\\d{2}\\.\\d{2})\\s+"
            + "(?<time>\\d{2}\\-\\d{2})\\s+([A-ZА-Я]{2,4})\\s+"
            + "([A-ZА-Я]{11})\\s+([A-ZА-Я]{6})\\s+([A-ZА-Я]{1})\\s+([A-ZА-Я]{7})";

//    RTH
    final static String RTH = "(?<nvag>[A-ZА-Я]{3}\\.[A-ZА-Я]{3}\\.)\\s{0,6}\\:\\s{0,6}"
            + "(?<sobs>[A-ZА-Я]{5})\\s{0,6}\\:\\s{0,6}"
            + "(?<prip>[A-ZА-Я]{8})\\s{0,6}\\:\\s{0,6}"
            + "(?<gp>[A-ZА-Я]{2})\\s{0,6}\\:\\s{0,6}"
            + "(?<rem>[A-ZА-Я]{6})\\s{0,6}\\:\\s{0,6}"
            + "(?<prbg>[A-ZА-Я]{7})\\s{0,6}\\:\\s{0,6}"
            + "(?<prznk>[A-ZА-Я]{8})";

//    RTB
//    final static String RTB = "(?<nvag>\\d{8})\\s+"
//            + "(?<sobs>\\d{2}[A-ZА-Я]{2,4})\\s+"
//            + "(?<prip>[A-ZА-Я]{2,4})\\s+"
//            + "(?<gp>\\d{0,2})\\s+"
//            + "(?<rem>[A-ZА-Я]{0,2}\\d{2}\\.\\d{2}\\.\\d{2})\\s+"
//            + "(?<prbg>[A-ZА-Я]{0,2}\\s?\\d{1,6})\\s+"
//            + "(?<prznk>[A-ZА-Я]{0,4}\\s+\\d{0,7})";
//    final static String RTB = "(?<nvag>\\d{8})\\s+"
//            + "(?<sobs>\\d{2}[A-ZА-Я]{2,4})\\s+"
//            + "(?<prip>[A-ZА-Я]{2,4}\\s{0,10}[A-ZА-Я]{0,12})?\\s+"
//            + "(?<gp>\\d{0,2})\\s+"
//            + "(?<rem>[A-ZА-Я]{0,2}\\d{2}\\.\\d{2}\\.\\d{2})\\s+"
//            + "(?<prbg>[A-ZА-Я]{0,2}\\s?\\d{1,6})?\\s+"
//            + "(?<p1>[A-ZА-Я]{2,8}\\s{0,6}\\d{0,7})?\\s+"
//            + "(?<p2>[A-ZА-Я]{2,8}\\.?\\s?[A-ZА-Я]{0,5})?\\s+"
//            + "(?<p3>[A-ZА-Я]{2,8}\\.?\\s?[A-ZА-Я]{0,5})?";
//надо удалить RDH и RTH
    final static String RTB = "(?<nvag>\\d{8})?\\s{1,2}"
                + "(?<sobs>\\d{2}[A-ZА-Я]{2,4})?\\s{1,3}"
                + "(?<prip>[A-ZА-Я]{2,4}\\ {0,10}[A-ZА-Я]{0,12})?\\s+"
                + "(?<gp>\\d{0,2})?\\s+"
                + "(?<rem>[A-ZА-Я]{0,2}\\d{2}\\.\\d{2}\\.\\d{2})?\\s+"
                + "(?<prbg>[A-ZА-Я]{0,2}\\s?\\d{1,6})?\\s+"
                + "(?<p1>[A-ZА-Я]{2,8}\\.?\\ {0,6}\\d{0,7}[A-ZА-Я]{0,5})";

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

        String sost = "";
        String obj = "";
        String delStr = "";
        while (matcher.find()) {
            delStr = matcher.group(0);
            for (int i = 1; i <= matcher.groupCount(); i++) {
                result.addCell(matcher.group(i));
            }

//            sost = matcher.group("sost");
//            obj = matcher.group("st");
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
            h.setSprN("2610 : Вагоны");
            h.setDate("" + dateFormat.format(currDate));
            h.setTime("");
            h.setObj("");
            hi.infoFromSpr(h);
        }

        //меняем ДокХидер на пробел
//        delStr = matcher.group(0);
        f = f.replace(delStr, "");

        pattern = Pattern.compile(RTH);
        matcher = pattern.matcher(f);
        tableHeaderProcessed = false;

        while (matcher.find()) {
            delStr = matcher.group(0);
            result.addCell("№");

            for (int i = 1; i <= matcher.groupCount(); i++) {
//                if (matcher.group(i) != null) {
                result.addCell(matcher.group(i));
//                }
            }
            if (!tableHeaderProcessed) {
                tableHeaderProcessed = true;
                result.markCurrentRowAsHeader();
            }

            tHead = true;
            result.advanceToNextRow();
        }

        //меняем ТабХидер на пробел
//        delStr = matcher.group(0);
        f = f.replace(delStr, "");

        pattern = Pattern.compile(RTB);
        matcher = pattern.matcher(f);

        int n = 1;
        while (matcher.find()) {
            if (matcher.group("nvag") != null) {
                result.addCell("" + n++);
            }else{
                result.addCell("");
            }
            for (int i = 1; i <= matcher.groupCount(); i++) {
                if (matcher.group("nvag") != null) {
                    result.addCell(delNull(matcher.group(i)));
                    result.markCurrentRowAsRegularUnderscore();
                } else {
                    result.addCell(delNull(matcher.group(i)));
                }
            }

            if (!tableHeaderProcessed) {
                tableHeaderProcessed = true;
                result.markCurrentRowAsHeader();
            }
            reading = true;
            tBody = true;
            result.advanceToNextRow();
        }

        System.out.println("docHead2610 === " + docHead);
        System.out.println("tHead2610 === " + tHead);
        System.out.println("tBody2610 === " + tBody);
        if (reading == true && (docHead == true && tHead == true && tBody == true)) {
            System.out.println("can reading SPR2610 ");
            ReadOnDir.spr = "sprDefault";
            return result;
        } else {
            System.out.println("can not reading SPR2610 ");
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

    @Override
    public List<HtmlTable> readersResult() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
