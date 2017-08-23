package arm.tableutils.sprtemplates.pzd;

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

public class Spravka104Reader implements TableReaderInterface {

//    regexDocHead
    final static String RDH = "([A-ZА-Я]{2,4})\\s+([A-ZА-Я]{2,4})\\s+"
            + "(104)\\s+(\\d{2}\\.\\d{2})\\s+(\\d{2}\\-\\d{2})\\s+"
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
    final static String RTB2 = "([A-ZА-Я]{11}\\s(?<tbsobst>[A-ZА-Я]{2,4})\\s+)?"
            + "(?<tbnv>\\d{8})\\s+"
            + "(?<tbstn>[A-ZА-Я]{0,6}\\-?\\.?[A-ZА-Я]{0,5}\\d{0,6}\\.?)\\s+"
            + "(?<tbgr>\\d{5})\\s+"
            + "(?<tbdor>\\d{1,2})\\s+"
            + "(?<tbgos>\\d{1,2})";

    @Override
    public HtmlTable processFile(String fileName) {
        String str = null;
        String f = null;
        String f1 = "";
        Pattern pattern;
        Matcher matcher;
        boolean reading = false;
        boolean docHead = false;
        boolean tHead = false;
        boolean tBody = false;

        /*
        * пока условно будем считать что файл всегда есть!
         */
        try (FileInputStream fis = new FileInputStream(fileName)) {

            System.out.println("Размер файла: " + fis.available() + " байт(а)");

            byte[] buffer = new byte[fis.available()];

            // считаем файл в буфер
            fis.read(buffer, 0, fis.available());

            str = new String(new String(buffer, "CP1251").getBytes(), "CP866");

            f1 = TextReplace.getText(str);
            f = TextReplace.getSha(f1);

        } catch (IOException ex) {
            Logger.getLogger(Spravka104Reader.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("exception in Spravka104Reader : " + ex);
        }

        HtmlTable result = new HtmlTable();

        pattern = Pattern.compile(RDH);
        matcher = pattern.matcher(f);

        boolean tableHeaderProcessed = false;

        while (matcher.find()) {

            for (int i = 1; i <= matcher.groupCount(); i++) {
                result.addCell(matcher.group(i));
            }

            if (!tableHeaderProcessed) {
                tableHeaderProcessed = true;
                result.markCurrentRowAsDocHeader();
            }

            docHead = true;
            result.advanceToNextRow();
        }

        pattern = Pattern.compile(RTH1);
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

        pattern = Pattern.compile(RTB1);
        matcher = pattern.matcher(f);

        int n = 1;
        while (matcher.find()) {
            result.addCell("" + n++);
            for (int i = 1; i <= matcher.groupCount(); i++) {
                result.addCell(matcher.group(i));
            }

            if (!tableHeaderProcessed) {
                tableHeaderProcessed = true;
                result.markCurrentRowAsHeader();
            }
            reading = true;
            tBody = true;
            result.advanceToNextRow();
        }
        System.out.println("result.markNextTable();");
        result.advanceToNextTable();
        System.out.println("result.markNextTable();");
        
        pattern = Pattern.compile(RTH2);
        matcher = pattern.matcher(f);
        tableHeaderProcessed = false;

        while (matcher.find()) {

            result.addCell("№");
            result.addCell("СОБСТВЕННИК");
            for (int i = 1; i <= matcher.groupCount(); i++) {
                result.addCell(matcher.group(i));
            }
            result.addCell("КОЛИЧЕСТВО");

            if (!tableHeaderProcessed) {
                tableHeaderProcessed = true;
                result.markCurrentRowAsHeader();
            }

            tHead = true;
            result.advanceToNextRow();
        }

        pattern = Pattern.compile(RTB2);
        matcher = pattern.matcher(f);

        int m = 1;
        int k=0;
        while (matcher.find()) {
            if (matcher.group("tbsobst") != null) {
                k = 0;
            }
                k++;
            
                result.addCell("" + m++);

                result.addCell("<b>" + delNull(matcher.group("tbsobst")) + "</b>");
                result.addCell(delNull(matcher.group("tbnv")));
                result.addCell(delNull(matcher.group("tbstn")));
                result.addCell(delNull(matcher.group("tbgr")));
                result.addCell(delNull(matcher.group("tbdor")));
                result.addCell(delNull(matcher.group("tbgos")));
//            String column = htmlParse(k);
                result.addCell("Вагонов " + k);

                if (!tableHeaderProcessed) {
                    tableHeaderProcessed = true;
                    result.markCurrentRowAsHeader();
                }
//            if (!column.equals("")) {
//                result.markCurrentRowAsRegularUnderlining();
//            }
            reading = true;
            tBody = true;
            result.advanceToNextRow();
        }

        System.out.println("docHead104 === " + docHead);
        System.out.println("tHead104 === " + tHead);
        System.out.println("tBody104 === " + tBody);
        if (reading == true && (docHead == true && tHead == true && tBody == true)) {
            System.out.println("can reading SPR104 " + result);
            ReadOnDir.spr = "sprDefault";
            return result;
        } else {
            System.out.println("can not reading SPR104 " + result);
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
            System.out.println("<b>" + s1 + " <div style='color:red'>" + s2 + "</div></b>");
            return "<b>" + s1 + " <div style='color:red'>" + s2 + "</div></b>";
        } else {
            return "";
        }
    }

    @Override
    public List<HtmlTable> readersResult() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
