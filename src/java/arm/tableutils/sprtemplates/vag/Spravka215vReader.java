/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

/**
 *
 * @author Muzaffar
 */
public class Spravka215vReader  implements TableReaderInterface {
//regexDocHead

    final static String RDH = "(?<sp>\\S{3})\\s+"
            + "(?<sprn>215:)\\s+"
            + "(?<dt>\\d{2}\\/\\d{2}\\/\\d{2}\\-\\d{2}\\:\\d{2}\\:\\d{2})\\s+"
            + "(?<idx>\\d{4,6}\\s?\\+\\d{2,3}\\+\\d{4,6})?\\s+"
            + "(?<ao>\\(\\d{2}\\s\\d{4}\\s\\d{1,2}\\)\\d{1,3})";

//    RTH
    final static String RTH = "";

//    RTB
    final static String RTB = "(?<nv>\\d{8})?\\s?\\-?\\>?\\s"
            + "(?<sbst>\\d{2})?\\s+"
            + "(?<idx>\\d{4,5}\\+\\s?\\d{2,3}\\+\\d{4,5})\\s+\\:\\s+"
            + "(?<date>\\d{2}\\/\\d{2}\\/\\d{2})\\s+\\:\\s+"
            + "(?<stp>\\d{4,6})\\s+\\:\\s+"
            + "(?<stn>\\d{4,6})\\s+\\:\\s+"
            + "(?<dsd>\\d{2})\\s"
            + "(?<dpr>\\d{2})";

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
            h.setSprN("215 : Вагон");
            h.setDate("" + dateFormat.format(currDate));
            h.setTime("");
            h.setObj("");
            hi.infoFromSpr(h);
        }

        //меняем ДокХидер на пробел
//        delStr = matcher.group(0);
//        f = f.replace(delStr, "");

//        pattern = Pattern.compile(RTH);
//        matcher = pattern.matcher(f);
        tableHeaderProcessed = false;

        result.addCell("№");
        result.addCell("№ вагона");
        result.addCell("Собственник");
        result.addCell("Индекс поезда");
        result.addCell("Дата");
        result.addCell("Ст. приема");
        result.addCell("Ст. назн");
        result.addCell("Дорога сд.");
        result.addCell("Дорога пр.");
//        while (matcher.find()) {
//            delStr = matcher.group(0);
//            result.addCell("№");
//
//            for (int i = 1; i <= matcher.groupCount(); i++) {
////                if (matcher.group(i) != null) {
//                result.addCell(matcher.group(i));
////                }
//            }
            if (!tableHeaderProcessed) {
                tableHeaderProcessed = true;
                result.markCurrentRowAsHeader();
            }
//
            tHead = true;
            result.advanceToNextRow();
//        }

        //меняем ТабХидер на пробел
//        delStr = matcher.group(0);
//        f = f.replace(delStr, "");

        pattern = Pattern.compile(RTB);
        matcher = pattern.matcher(f);

        int n = 1;
        while (matcher.find()) {
            if (matcher.group("nv") != null) {
                result.addCell("" + n++);
            }else{
                result.addCell("");
            }
            for (int i = 1; i <= matcher.groupCount(); i++) {
                if (matcher.group("nv") != null) {
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

        System.out.println("docHead215v === " + docHead);
        System.out.println("tHead215v === " + tHead);
        System.out.println("tBody215v === " + tBody);
        if (reading == true && (docHead == true && tHead == true && tBody == true)) {
            System.out.println("can reading SPR215v ");
            ReadOnDir.spr = "sprDefault";
            return result;
        } else {
            System.out.println("can not reading SPR215v ");
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
