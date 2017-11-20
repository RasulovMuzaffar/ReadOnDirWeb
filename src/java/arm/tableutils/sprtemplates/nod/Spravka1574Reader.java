package arm.tableutils.sprtemplates.nod;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
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
//    final static String RTB1 = "(?<vsg>[A-ZА-Я]{4,6})\\s{1,3}"
//            + "(?<st1>\\S{4,6})?\\s{1,3}(?<st2>\\S{4,6})?\\s{1,3}"
//            + "(?<st3>\\S{4,6})?\\s{1,3}(?<st4>\\S{4,6})?\\s{1,3}"
//            + "(?<st5>\\S{4,6})?\\s{1,3}(?<st6>\\S{4,6})?\\s{1,3}"
//            + "(?<st7>\\S{4,6})?\\s{1,3}(?<st8>\\S{4,6})?\\s{1,3}"
//            + "(?<st9>\\S{4,6})?(?<st10>\\S{4,6})?";
    final static String RTB1 = "\\s(?<vsg>[A-ZА-Я]{4,6}-?.?[A-ZА-Я]{0,3}\\d{0,2}.?)\\s{1,3}"
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

        pattern = Pattern.compile(RTH);
        matcher = pattern.matcher(f);
        tableHeaderProcessed = false;
        int gCount = 0;
        int colSum = 1;
        Set<String> th = new LinkedHashSet<>();
        while (matcher.find()) {
            th.add("СТАНЦИИ");
            th.add("ВСЕГО");
            for (int i = 1; i <= matcher.groupCount(); i++) {
                th.add(matcher.group(i));
            }
        }

        for (String s : th) {
            result.addCell(s);
        }

        if (!tableHeaderProcessed) {
            tableHeaderProcessed = true;
            result.markCurrentRowAsHeader();
        }
        tHead = true;
        result.advanceToNextRow();
//            break;

//            result.addCell("СТАНЦИИ");
//            if (gCount == 0) {
//                for (int i = 1; i <= matcher.groupCount(); i++) {
//                    result.addCell(matcher.group(i));
//                }
//                gCount++;
//                colSum++;
//            }
//            result.addCell("ВСЕГО");
//            result.addCell("КР");
//            result.addCell("ПЛ");
//            result.addCell("ПВ");
//            result.addCell("ЦС");
//            result.addCell("ЦСС");
//            result.addCell("РФ");
//            result.addCell("ПР");
//            result.addCell("ЦМВ");
//            result.addCell("ЗРВ");
//            result.addCell("ФТГ");
//            result.addCell("МВЗ");
//            result.addCell("ТР");
//            if (!tableHeaderProcessed) {
//                tableHeaderProcessed = true;
//                result.markCurrentRowAsHeader();
//            }
//            tHead = true;
//            result.advanceToNextRow();
//            break;
//        }
        Pattern pattern1 = Pattern.compile(RTB1);
        Matcher matcher1 = pattern1.matcher(f);

        List<String> row1 = new ArrayList<>();
        int tCount = 0;
        while (matcher1.find()) {
            tCount++;
            for (int i = 1; i < matcher1.groupCount(); i++) {

//                if (matcher1.group(i) != null) {
//                    System.out.println(i + " matcher1 ===> " + matcher1.group(i));
                row1.add(matcher1.group(i));
//                }
            }
        }
        String endElem = "";
        for (String s : row1) {
//            System.out.println(" - " + s + " - ");
            if (s != null) {
                endElem = s;
            }
        }
        System.out.println("endElem ===>>> " + endElem);
        f = f.replace(endElem, endElem + ".");
        System.out.println("==============");
//        System.out.println("j =====>>>> " + row1.size());

///////////////
        pattern = Pattern.compile(RTB);
        matcher = pattern.matcher(f);

//        }
        List<String> lst = new ArrayList<>();
        int badline = 0;

        while (matcher.find()) {
            if (badline == 0) {
                badline++;
                continue;
            }
            for (int j = 1; j <= matcher.groupCount(); j++) {
                lst.add(matcher.group(j));
            }
        }

        int row = row1.size();
//        int col = 14;
//        int col = colSum + 1;
        int col = th.size();

        String[][] m = new String[row][col];
        System.out.println("tCount = " + tCount);
        System.out.println("row1.size = " + row1.size());
        System.out.println("lst.size = " + lst.size());
//        for (int i = 0; i < lst.size(); i++) {
//            if (i % 10 != 0 || i == 0) {
//                System.out.print("".equals(lst.get(i)) ? "0 " : lst.get(i) + " ");
//            } else {
//                System.out.print("".equals(lst.get(i)) ? "0 " : lst.get(i) + " ");
//                System.out.println("");
//            }
//        }
        int a = 0;
        int b = 0;
        int q = 0;
        while (q < tCount) {
//            System.out.println("q");
            for (int j = 0; j < col; j++) {
//                System.out.println("f1");
                for (int i = 10 * q; i < (q + 1) * 10; i++) {
//                    System.out.println("f2");
                    if (j == 0) {
                        m[i][j] = row1.get(a++);
                    } else {
                        m[i][j] = lst.get(b);
                        b++;
                    }
                }
            }
            q++;
        }

        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[i].length; j++) {
                if (m[i][0] == null) {
                    break;
                }
                if (j == 0) {
                    result.addCell("<b>" + m[i][j] + "</b>");
                } else {
                    result.addCell(m[i][j]);
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
