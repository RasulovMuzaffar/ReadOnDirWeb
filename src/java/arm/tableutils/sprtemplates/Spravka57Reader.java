package arm.tableutils.sprtemplates;

import arm.tableutils.HtmlTable;
import arm.tableutils.tablereaders.TableReaderInterface;
import arm.tableutils.tablereaders.utils.TextReplace;
import arm.wr.ReadOnDir;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Spravka57Reader implements TableReaderInterface {

    final static String regexDocHead = "([A-ZА-Я]{2})\\s+"
            + "(?<dhdor>[A-ZА-Я]{1,4})\\s+(57)\\s+"
            + "(\\d{2}\\.\\d{2})\\s+(\\d{2}\\-\\d{2})\\s+"
            + "([A-ZА-Я]{2})\\s+(\\d{1,2})\\s+([A-ZА-Я]{6})\\s+"
            + "([A-ZА-Я]{7})\\s+([A-ZА-Я]{1})\\s+([A-ZА-Я]{2}\\.)\\s+"
            + "([A-ZА-Я]{0,15}-{0,1}.{0,1}[A-ZА-Я]{0,5}\\d{0,2}.{0,1})";

    final static String regexTHead = "([A-ZА-Я]{1})\\s+([A-ZА-Я]{4}\\.)\\s+"
            + "([A-ZА-Я]{0,15}-{0,1}.{0,1}[A-ZА-Я]{0,5}\\d{0,2}.{0,1})";

    final static String regexTBody = "((?<snapr>[A-ZА-Я]{1}\\s[A-ZА-Я]{4}\\.)\\s"
            + "(?<tbst>[A-ZА-Я]{0,15}-{0,1}.{0,1}[A-ZА-Я]{0,5}\\d{0,2}.{0,1}))?"
            + "\\s+((?<tbnp>\\d{4})\\s?\\"
            + "((?<tbidx>\\d{4,6}\\+\\s?\\d{1,3}\\+\\d{4,6})\\)"
            + "(?<tbstate>[A-ZА-Я]{2,4})\\s+"
            + "(?<tbxz1>\\d{1,2})\\s+(?<tbtime>\\d{2}\\-\\d{2}))";

    String str = null;
    String f = null;
    Pattern pattern;
    Matcher matcher;
    boolean reading = false;
    boolean docHead = false;
    boolean tHead = false;
    boolean tBody = false;
    String doroga = "";

    @Override
    public HtmlTable processFile(String fileName) {

        /*
        * пока условно будем считать что файл всегда есть!
         */
        try (FileInputStream fis = new FileInputStream(fileName)) {

            System.out.println("File size: " + fis.available() + " bytes");

            byte[] buffer = new byte[fis.available()];

            // считаем файл в буфер
            fis.read(buffer, 0, fis.available());

            str = str = new String(new String(buffer, "CP1251").getBytes(), "CP866");

            f = TextReplace.getSha(TextReplace.getText(str));

        } catch (IOException ex) {
            Logger.getLogger(Spravka93Reader.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("exception in Spravka57Reader : " + ex);
        }
//        String[] lines = f.split("С НАПР.");
//
//        HtmlTable result = new HtmlTable();
//        if (lines.length > 1) {
//            for (String l : lines) {
//                if (l.trim().length() > 2) {
//                    result = getResult("С НАПР. " + l.trim());
//                    break;
//                } else {
//                    result = null;
//                }
//            }
//            return result;
//        } else {
        return getResult(f);
//        }
    }

    private HtmlTable getResult(String str) {
        HtmlTable result = new HtmlTable();

        pattern = Pattern.compile(regexDocHead);
        matcher = pattern.matcher(str);

        boolean tableHeaderProcessed = false;

        while (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                result.addCell(matcher.group(i));

            }

            doroga = matcher.group("dhdor");

            if (!tableHeaderProcessed) {
                tableHeaderProcessed = true;
                result.markCurrentRowAsDocHeader();
            }

            docHead = true;
            result.advanceToNextRow();
        }
        if (docHead == false) {
            System.out.println("fignya v 57 docHead!!!");
            return null;
        }
//        pattern = Pattern.compile(regexTHead);
//        matcher = pattern.matcher(str);
//        tableHeaderProcessed = false;
//
//        while (matcher.find()) {
//
//            result.addCell("№");
////            for (int i = 1; i <= matcher.groupCount(); i++) {
////                result.addCell(matcher.group(i));
////            }
//
//            if (doroga.equals("УТИ")) {
//                result.addCell("ТГНЛ");
//            }
//
//            if (!tableHeaderProcessed) {
//                tableHeaderProcessed = true;
//                result.markCurrentRowAsHeader();
//            }
//
        tHead = true;
//            result.advanceToNextRow();
//            break;
//        }
//
//        if (tHead == false) {
//            System.out.println("fignya v 57 tHead!!!");
//            return null;
//        }
        pattern = Pattern.compile(regexTBody);
        matcher = pattern.matcher(str);
        System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
//        int n = 1;
        while (matcher.find()) {
            String column1 = delNull(matcher.group("snapr"));
            String column2 = delNull(htmlParse(matcher.group("tbst")));
            result.addCell(column1 + " " + column2);
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
//            System.out.println("can reading SPR57 " + result);
            ReadOnDir.spr = "sprDefault";
//            System.out.println("+++++++++++++++++++++++++57++++++++++++++++++++++++");
//            System.out.println("" + result.generateHtml());
//            System.out.println("-------------------------57------------------------");
            return result;
        } else {
//            System.out.println("can not reading SPR57 " + result);
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
//            String[] str = s.split("\\s+");
//            String s1 = str[0];
//            String s2 = str[1];
//            System.out.println("<b>" + s1 + " <div style='color:#E1C3C3'>" + s2 + "</div></b>");
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
