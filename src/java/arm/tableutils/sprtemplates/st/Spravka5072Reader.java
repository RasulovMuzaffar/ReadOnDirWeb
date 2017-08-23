package arm.tableutils.sprtemplates.st;

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

public class Spravka5072Reader implements TableReaderInterface {

//    regexDocHead
    final static String RDH = "([A-ZА-Я]{2}\\s+[A-ZА-Я]{3})\\s+"
            + "(\\d{4})\\s+(\\d{2}.\\d{2}\\s+\\d{2}-\\d{2})\\s"
            + "([A-ZА-Я]{2}\\s\\d{2})\\s+([A-ZА-Я]{8})\\s"
            + "(\\\"[A-ZА-Я]{5}\\\")\\s([A-ZА-Я]{7})\\s([A-ZА-Я]{3}.[A-ZА-Я]{5})\\s"
            + "([A-ZА-Я]{2}\\s[A-ZА-Я]{7}\\s[A-ZА-Я]{3}.\\s[A-ZА-Я]{2}\\s[A-ZА-Я]{6})\\s+"
            + "([A-ZА-Я]{2,10}-{0,1}[A-ZА-Я]{0,5}\\d{0,4})\\s+"
            + "(,[A-ZА-Я]{5}\\s[A-ZА-Я]{3}.[A-ZА-Я]{6}\\s\\d{0,3}\\s[A-ZА-Я]{3}.)";

//    regexTHead
    final static String RTH = "(?<nvag>[A-ZА-Я]{1}\\s[A-ZА-Я]{6}):"
            + "(?<stn>[A-ZА-Я]{2}.[A-ZА-Я]{2}):"
            + "(?<gr>[A-ZА-Я]{4})\\s:"
            + "(?<oper>[A-ZА-Я]{4}):"
            + "(?<dt1>[A-ZА-Я]{4}\\s[A-ZА-Я]{5})\\s:"
            + "(?<stp>[A-ZА-Я]{2}.[A-ZА-Я]{2}):"
            + "(?<dt2>[A-ZА-Я]{4}\\s[A-ZА-Я]{5})\\s:"
            + "(?<dor>[A-ZА-Я]{3})\\s:"
            + "(?<gos>[A-ZА-Я]{3})\\s:"
            + "(?<sto>[A-ZА-Я]{2}.[A-ZА-Я]{2})";

//    regexTBody
    
    final static String RTB = "([A-ZА-Я]{11}\\s(?<tbsobst>[A-ZА-Я]{1,4})){0,1}\\s{0,}"
            + "(?<tbnvag>\\d{8})\\s+"
            + "(?<tbstn>[A-ZА-Я]{0,8}-{0,1}[A-ZА-Я]{0,5}\\d{0,5})\\s+"
            + "(?<tbgr>\\d{5})\\s+"
            + "(?<tboper>[A-ZА-Я]{3,5}\\d{0,3})\\s+"
            + "(?<tbdt1>\\d{2}.\\d{2}\\s\\d{2}-\\d{2})\\s+"
            + "(?<tbstp>[A-ZА-Я]{0,8}-{0,1}[A-ZА-Я]{0,5}\\d{0,3})\\s+"
            + "(?<tbdt2>\\d{2}.\\d{2}\\s\\d{2}-\\d{2})\\s+"
            + "(?<tbdor>\\d{1,3})\\s+(?<tbgos>\\d{1,3})\\s+"
            + "(?<tbsto>[A-ZА-Я]{0,8}-{0,1}[A-ZА-Я]{0,5}\\d{0,3})\\s{0,}([A-ZА-Я]{5}:[A-ZА-Я]{10}\\s"
            + "(?<tbvkv>[A-ZА-Я]{7}\\s+\\d{1,4})){0,1}(\\s+"
            + "(?<tbobwk>[A-ZА-Я]{5}:[A-ZА-Я]{10}\\s[A-ZА-Я]{7}\\s+\\d{1,4})){0,1}";

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
            Logger.getLogger(Spravka5072Reader.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("exception in Spravka5072Reader : " + ex);
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

        pattern = Pattern.compile(RTH);
        matcher = pattern.matcher(f);
        tableHeaderProcessed = false;

        while (matcher.find()) {

            result.addCell("№");
            result.addCell("СОБСТВЕННИК");
            for (int i = 1; i <= matcher.groupCount(); i++) {
                result.addCell(matcher.group(i));
            }
            result.addCell("КОЛИЧЕСТВО");
//            result.addCell("ТГНЛ");

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

            result.addCell("<b>"+delNull(matcher.group("tbsobst"))+"</b>");
            result.addCell(delNull(matcher.group("tbnvag")));
            result.addCell(delNull(matcher.group("tbstn")));
            result.addCell(delNull(matcher.group("tbgr")));
            result.addCell(delNull(matcher.group("tboper")));
            result.addCell(delNull(matcher.group("tbdt1")));
            result.addCell(delNull(matcher.group("tbstp")));
            result.addCell(delNull(matcher.group("tbdt2")));
            result.addCell(delNull(matcher.group("tbdor")));
            result.addCell(delNull(matcher.group("tbgos")));
            result.addCell(delNull(matcher.group("tbsto")));
            String column = htmlParse(delNull(matcher.group("tbvkv")));
            result.addCell(column);

            if (!tableHeaderProcessed) {
                tableHeaderProcessed = true;
                result.markCurrentRowAsHeader();
            }
            if (!column.equals("")) {
                result.markCurrentRowAsRegularUnderlining();
            }
            
            reading = true;
            tBody = true;
            result.advanceToNextRow();
        }
        System.out.println("docHead5072 === " + docHead);
        System.out.println("tHead5072 === " + tHead);
        System.out.println("tBody5072 === " + tBody);
        if (reading == true && (docHead == true && tHead == true && tBody == true)) {
            System.out.println("can reading SPR5072 " + result);
            ReadOnDir.spr = "sprDefault";
            return result;
        } else {
            System.out.println("can not reading SPR5072 " + result);
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
