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

public class Spravka1574Reader implements TableReaderInterface {

//    RDH
    final static String RDH = "(?<vc>\\S{2,3}\\s+\\S{2,4})\\s+"
            + "(?<spr>1574)\\s+(?<date>\\d{2}.\\d{2})\\s+"
            + "(?<time>\\d{2}-\\d{2})\\s+(?<brp>\\S{2,3}\\s+\\S{2,4})\\s+"
            + "(?<per>\\S+\\s+\\S+\\s+\\S+\\s+\\S+\\s+\\S+\\s+\\S+)\\s+(?<nod>\\S+)";

//    RTH
    final static String RTH = "\\s{2}(?<vsg>[A-ZА-Я]{2,3})\\s{3,7}";

//stations column 1
    final static String RTB1 = "(?<vsg>[A-ZА-Я]{4,6})\\s{1,3}"
            + "(?<st1>\\S{4,6})?\\s{1,3}(?<st2>\\S{4,6})?\\s{1,3}"
            + "(?<st3>\\S{4,6})?\\s{1,3}(?<st4>\\S{4,6})?\\s{1,3}"
            + "(?<st5>\\S{4,6})?\\s{1,3}(?<st6>\\S{4,6})?\\s{1,3}"
            + "(?<st7>\\S{4,6})?\\s{1,3}(?<st8>\\S{4,6})?\\s{1,3}"
            + "(?<st9>\\S{4,6})?(?<st10>\\S{4,6})?";

//    RTB column not 1
    final static String RTB = "[A-ZА-Я]\\s{3,9}(?<vsg>\\d{0,4})\\s{1,6}"
            + "(?<st1>\\d{0,4})\\s{1,6}(?<st2>\\d{0,4})\\s{1,6}"
            + "(?<st3>\\d{0,4})\\s{1,6}(?<st4>\\d{0,4})\\s{1,6}"
            + "(?<st5>\\d{0,4})\\s{1,6}(?<st6>\\d{0,4})\\s{1,6}"
            + "(?<st7>\\d{0,4})\\s{1,6}(?<st8>\\d{0,4})\\s{1,6}(?<st9>\\d{0,4})";

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
            }

//			doroga = matcher.group("dhdor");
            obj = matcher.group("nod");

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
            h.setSprN("1574");
            h.setDate("" + dateFormat.format(currDate));
            h.setTime("");
            h.setObj(obj);
            hi.infoFromSpr(h);
        }

        pattern = Pattern.compile(RTB);
        matcher = pattern.matcher(f);

        Pattern pattern1 = Pattern.compile(RTB1);
        Matcher matcher1 = pattern1.matcher(f);

        String[][] m = new String[11][matcher1.groupCount()];
        System.out.println("i =====>>>> " + m.length);
        int j = 0;
        while (matcher1.find()) {
            for (int i = 0; i < matcher1.groupCount(); i++) {
                if (matcher1.group(i) != null || !"null".equals(matcher1.group(i)) || !" ".equals(matcher1.group(i)) || !"".equals(matcher1.group(i))) {
                    j++;
                }
                System.out.println("RTB1 ---> +" + matcher1.group(i) != null + " - "
                        + !"null".equals(matcher1.group(i)) + " - "
                        + !" ".equals(matcher1.group(i)) + " - "
                        + !"".equals(matcher1.group(i)));
            }
        }
        System.out.println("j =====>>>> " + j);

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
                // if (matcher.group(i)==matcher.group("otv")||matcher.group(i)==matcher.group("ott")) {
                //     result.addCell(matcher.group(i));
                // }else{
                result.addCell(matcher.group(i));
                System.out.println("---->>>>" + matcher.group(i));
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

        System.out.println("docHead1574 === " + docHead);
        System.out.println("tHead1574 === " + tHead);
        System.out.println("tBody1574 === " + tBody);
        if (reading == true && (docHead == true && tHead == true && tBody == true)) {
            System.out.println("can reading SPR1574 ");
            if (forPopup == true) {
                ReadOnDir.spr = "sprPopup";
            } else {
                ReadOnDir.spr = "sprDefault";
            }
            forPopup = false;
            return result;
        } else {
            System.out.println("can not reading SPR1574 ");
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
