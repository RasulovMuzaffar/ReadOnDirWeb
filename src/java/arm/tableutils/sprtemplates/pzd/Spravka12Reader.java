
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


public class Spravka12Reader implements TableReaderInterface {

//    regexDocHead
    final static String RDH = "(?<title>[A-ZА-Я]{2,3}\\s[A-ZА-Я]{2,4}\\s+12\\s+"
            + "\\d{2}\\.\\d{2}\\s+\\d{2}\\-\\d{2}\\s+[A-ZА-Я]{1,4}\\s+"
            + "[A-ZА-Я]{6}\\s+[A-ZА-Я]{1}\\s+[A-ZА-Я]{7})\\s+"
            + "\\((?<idx>\\d{4,6}\\s?\\+?\\s?\\d{1,3}\\s?\\+?\\s?\\d{4,6})\\)";

//    regexTHead
    final static String RTH = "(?<thst>[A-ZА-Я]{4})\\s+"
            + "(?<thoper>[A-ZА-Я]{4})\\s+"
            + "(?<thdate>[A-ZА-Я]{4})\\s+"
            + "(?<thtime>[A-ZА-Я]{5})\\s+"
            + "(?<thnapr>[A-ZА-Я]{4})\\s+"
            + "(?<thnp>[A-ZА-Я]{5})";

//    regexTBody
    final static String RTB = "(?<tbst>[A-ZА-Я]{0,6}\\-?\\.?[A-ZА-Я]{0,5}\\d{0,2}\\.?)\\s+"
            + "(?<tboper>[A-ZА-Я]{3,4}\\-?\\d{0,2})\\s+"
            + "(?<tbdate>\\d{2}\\.\\d{2})\\s+"
            + "(?<tbtime>\\d{2}\\-\\d{2})\\s+"
            + "(?<tbnapr>[A-ZА-Я]{0,6}\\-?\\.?[A-ZА-Я]{0,5}\\d{0,2}\\.?)\\s+"
            + "(?<tbnp>\\d{4}\\s?\\+?\\-?\\d{0,3})";

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
            Logger.getLogger(Spravka12Reader.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("exception in Spravka12Reader : " + ex);
        }

        HtmlTable result = new HtmlTable();

        pattern = Pattern.compile(RDH);
        matcher = pattern.matcher(f);

        boolean tableHeaderProcessed = false;

        while (matcher.find()) {
//            for (int i = 1; i <= matcher.groupCount(); i++) {
//                result.addCell(matcher.group(i));
//            }
            result.addCell(matcher.group("title"));
            result.addCell("<b>" + matcher.group("idx") + "</b>");

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

        System.out.println("docHead12 === " + docHead);
        System.out.println("tHead12 === " + tHead);
        System.out.println("tBody12 === " + tBody);
        if (reading == true && (docHead == true && tHead == true && tBody == true)) {
            System.out.println("can reading SPR12 " + result);
            ReadOnDir.spr = "sprDefault";
            return result;
        } else {
            System.out.println("can not reading SPR12 " + result);
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

    private String plusToSpace(String s) {
        return s.replace("+", " ");
    }

    @Override
    public List<HtmlTable> readersResult() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
