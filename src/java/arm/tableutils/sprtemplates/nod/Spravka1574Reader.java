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
import java.util.ArrayList;
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

        pattern = Pattern.compile(RTH);
        matcher = pattern.matcher(f);
        tableHeaderProcessed = false;

        while (matcher.find()) {
            result.addCell("СТАНЦИИ");
            result.addCell("ВСЕГО");
            result.addCell("КР");
            result.addCell("ПЛ");
            result.addCell("ПВ");
            result.addCell("ЦС");
            result.addCell("ЦСС");
            result.addCell("РФ");
            result.addCell("ПР");
            result.addCell("ЦМВ");
            result.addCell("ЗРВ");
            result.addCell("ФТГ");
            result.addCell("МВЗ");
            result.addCell("ТР");

            if (!tableHeaderProcessed) {
                tableHeaderProcessed = true;
                result.markCurrentRowAsHeader();
            }
            tHead = true;
            result.advanceToNextRow();
            break;
        }

        Pattern pattern1 = Pattern.compile(RTB1);
        Matcher matcher1 = pattern1.matcher(f);

        List<String> row1 = new ArrayList<>();

        while (matcher1.find()) {
            for (int i = 1; i < matcher1.groupCount(); i++) {
                if (matcher1.group(i) != null) {
//                    System.out.println(i + " matcher1 ===> " + matcher1.group(i));
                    row1.add(matcher1.group(i));
                }
            }
        }
        for (String s : row1) {
            System.out.println(" - " + s + " - ");
        }
        System.out.println("==============");
//        System.out.println("j =====>>>> " + row1.size());

///////////////
        pattern = Pattern.compile(RTB);
        matcher = pattern.matcher(f);

//        }
//        List<Spr1574Ent> lspr = new ArrayList<>();
        List<String> lst = new ArrayList<>();
        int badline = 0;
        while (matcher.find()) {
            if (badline == 0) {
                badline++;
                continue;
            }
//            Spr1574Ent se = new Spr1574Ent();
            for (int j = 1; j <= matcher.groupCount(); j++) {
                lst.add(matcher.group(j));
//            se.setVsg(matcher.group("vsg"));
//            se.setSt1(matcher.group("st1"));
//            se.setSt2(matcher.group("st2"));
//            se.setSt3(matcher.group("st3"));
//            se.setSt4(matcher.group("st4"));
//            se.setSt5(matcher.group("st5"));
//            se.setSt6(matcher.group("st6"));
//            se.setSt7(matcher.group("st7"));
//            se.setSt8(matcher.group("st8"));
//            se.setSt9(matcher.group("st9"));
//            lspr.add(se);
            }
        }
        for (int i = 0; i < lst.size(); i++) {
            System.out.println(i + " ----->>>> " + lst.get(i));
        }

        String[][] m = new String[row1.size()][14];
//        while (matcher.find()) {
int a=0;
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[i].length; j++) {
                if (j == 0) {
                    m[i][j] = row1.get(i);
                } else  {
                    m[i][j] = lst.get(a++);
                    
//                } else {
////                        for (int k = 1; k < matcher.groupCount(); k++) {
////                            System.out.println("======? " + matcher.group(k));
////                            m[i][j] = matcher.group(k);
////                        }
//                    m[i][j] = "";
                }
            }
        }

//        for (int t = 0; t < m.length; t++) {
//            for (int j = 0; j < m[t].length; j++) {
//                System.out.print(m[t][j] + " ");
//            }
//            System.out.println("");
//        }
/////////////////
//        pattern = Pattern.compile(RTB);
//        matcher = pattern.matcher(f);
//
//        while (matcher.find()) {
//            for (int i = 1; i < m.length; i++) {
//                for (int j = 1; j <= matcher.groupCount(); j++) {
//                    m[i][j] = matcher.group(j);
//                }
//            }
//        }
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[i].length; j++) {
                result.addCell(m[i][j]);
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
