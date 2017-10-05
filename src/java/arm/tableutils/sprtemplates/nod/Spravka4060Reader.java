package arm.tableutils.sprtemplates.nod;

import arm.ent.History;
import arm.tableutils.HtmlTable;
import arm.tableutils.tablereaders.TableReaderInterface;
import arm.tableutils.tablereaders.utils.TextReplace;
import arm.wr.HistoryInterface;
import arm.wr.ReadOnDir;
import static arm.wr.Write.forPopup;
import static arm.wr.Write.fromDB;
import arm.wr.WriteToHist;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Spravka4060Reader implements TableReaderInterface {

//    RDH
    final static String RDH = "(?<vc>\\S{2,3}\\s+\\S{2,4})\\s+(?<spr>4060)\\s+(?<date>\\d{2}.\\d{2})\\s+(?<time>\\d{2}-\\d{2})\\s+(?<brp>\\S{2,3}\\s+\\S{2,4})\\s+(?<per>\\S+\\s+\\S+\\s+\\S+\\s+\\S+\\s+\\S+\\s+\\S+)\\s+(?<nod>\\S+)";

//    RTH
    final static String RTH = "(?<np>[A-ZА-Я]{4})\\s+(?<idx>[A-ZА-Я]{6})\\s+(?<vag>[A-ZА-Я]{3})\\s+(?<ud>[A-ZА-Я]{3})\\s+(?<brt>[A-ZА-Я]{5})\\s+(?<st>[A-ZА-Я]{5})\\s+(?<date>[A-ZА-Я]{4})\\s+(?<time>[A-ZА-Я]{5})\\s+(?<pr>[A-ZА-Я]{2})\\s+(?<otv>[A-ZА-Я]{3})\\s+(?<ott>[A-ZА-Я]{3})";

//    RTB
    final static String RTB = "(?<np>\\d{4})\\s+(?<idx>\\d{4,5}\\+\\s?\\d{2,3}\\+\\d{4,5})\\s+(?<vag>\\d{2})\\s+(?<ud>\\d{2})\\s+(?<brt>\\d{1,5})\\s+(?<st>\\S+)\\s+(?<date>\\d{2}.\\d{2})\\s+(?<time>\\d{2}.\\d{2})\\s+(?<pr>\\S+)\\s{0,4}(?<otv>\\d{0,3})\\s{0,4}(?<ott>\\d{0,4})";

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
                if (matcher.group(i).equals(matcher.group("nod"))) {
                    result.addCell(htmlParse(matcher.group(i)));
                } else {
                    result.addCell(matcher.group(i));
                }

                obj = matcher.group("nod");

                if (!tableHeaderProcessed) {
                    tableHeaderProcessed = true;
                    result.markCurrentRowAsDocHeader();
                }

                docHead = true;
                result.advanceToNextRow();
            }
        }

        if (docHead == false) {
            return null;
        } else if (fromDB != true) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM HH:mm");
            Date currDate = new Date();
            History h = new History();
            h.setSprN("4060");
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
        int n = 0;
        while (matcher.find()) {
            result.addCell("" + n++);
            for (int i = 1; i <= matcher.groupCount(); i++) {
                // if (matcher.group(i)==matcher.group("otv")||matcher.group(i)==matcher.group("ott")) {
                //     result.addCell(matcher.group(i));
                // }else{
                result.addCell(matcher.group(i));
                // }

            }

            if (!tableHeaderProcessed) {
                tableHeaderProcessed = true;
                result.markCurrentRowAsHeader();
            }
            reading = true;
            tBody = true;
            result.advanceToNextRow();
        }

        System.out.println("docHead4060 === " + docHead);
        System.out.println("tHead4060 === " + tHead);
        System.out.println("tBody4060 === " + tBody);
        if (reading == true && (docHead == true && tHead == true && tBody == true)) {
            System.out.println("can reading SPR4060 ");
            if (forPopup == true) {
                ReadOnDir.spr = "sprPopup";
            } else {
                ReadOnDir.spr = "sprDefault";
            }
            forPopup = false;
            return result;
        } else {
            System.out.println("can not reading SPR4060 ");
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
