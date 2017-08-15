
package arm.tableutils.sprtemplates;

import arm.tableutils.HtmlTable;
import arm.tableutils.tablereaders.TableReaderInterface;
import arm.tableutils.tablereaders.utils.TextReplace;
import arm.wr.ReadOnDir;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Spravka5072Reader  implements TableReaderInterface {

    final static String regexDocHead = "[A-ZА-Я]{2}\\s+"
            + "[A-ZА-Я]{3}\\s+\\d{4}\\s+\\d{2}.\\d{2}\\s+\\d{2}-\\d{2}\\s"
            + "[A-ZА-Я]{2}\\s\\d{2}\\s+[A-ZА-Я]{8}\\s\"[A-ZА-Я]{5}\"\\s"
            + "[A-ZА-Я]{7}\\s[A-ZА-Я]{3}.[A-ZА-Я]{5}\\s[A-ZА-Я]{2}\\s"
            + "[A-ZА-Я]{7}\\s[A-ZА-Я]{3}.\\s[A-ZА-Я]{2}\\s[A-ZА-Я]{6}\\s+"
            + "[A-ZА-Я]{2,10}-{0,1}[A-ZА-Я]{0,5}\\d{0,4}\\s+,[A-ZА-Я]{5}\\s"
            + "[A-ZА-Я]{3}.[A-ZА-Я]{6}\\s\\d{0,3}\\s[A-ZА-Я]{3}.";

    final static String regexTHead = "(?<nvag>[A-ZА-Я]{1}\\s[A-ZА-Я]{6}):"
            + "(?<stn>[A-ZА-Я]{2}.[A-ZА-Я]{2}):"
            + "(?<gr>[A-ZА-Я]{4})\\s:"
            + "(?<oper>[A-ZА-Я]{4}):"
            + "(?<dt1>[A-ZА-Я]{4}\\s[A-ZА-Я]{5})\\s:"
            + "(?<stp>[A-ZА-Я]{2}.[A-ZА-Я]{2}):"
            + "(?<dt2>[A-ZА-Я]{4}\\s[A-ZА-Я]{5})\\s:"
            + "(?<dor>[A-ZА-Я]{3})\\s:"
            + "(?<gos>[A-ZА-Я]{3})\\s:"
            + "(?<sto>[A-ZА-Я]{2}.[A-ZА-Я]{2})";

//    final static String regexTBody = "((?<tbpoluch>\\d{0,4})\\D{0,}(?<tbnvag>\\d{8})\\s+"
//            + "(?<tbves>\\d{1,2}))|"
//            + "((?<= )((?<tbgruz>\\d{5})\\s+(?<tbnp>\\d{4})\\s+(?<tbidx>\\d{4}\\+\\d{3}\\+\\d{4})\\s+"
//            + "(?<tbdisl>\\d{5})\\s+(?<tboper>[A-ZА-Я]{2,4})\\s+(?<tbvroper>\\d{2}\\s\\d{2}-\\d{2})))";
    final static String regexTBody = "((?<tbsobst>[A-ZА-Я]{11}\\s[A-ZА-Я]{1,4})\\s+){0,1}"
            + "((?<tbnvag>\\d{8})\\s+"
            + "(?<tbstn>[A-ZА-Я]{0,8}-{0,1}[A-ZА-Я]{0,5}\\d{0,5})\\s+"
            + "(?<tbgr>\\d{5})\\s+"
            + "(?<tboper>[A-ZА-Я]{3,5}\\d{0,3})\\s+"
            + "(?<tbdt1>\\d{2}.\\d{2}\\s\\d{2}-\\d{2})\\s+"
            + "(?<tbstp>[A-ZА-Я]{0,8}-{0,1}[A-ZА-Я]{0,5}\\d{0,3})\\s+"
            + "(?<tbdt2>\\d{2}.\\d{2}\\s\\d{2}-\\d{2})\\s+"
            + "(?<tbdor>\\d{1,3})\\s+"
            + "(?<tbgos>\\d{1,3})\\s+"
            + "(?<tbsto>[A-ZА-Я]{0,8}-{0,1}[A-ZА-Я]{0,5}\\d{0,3})\\s+){0,1}"
            + "(?<tbvkv>[A-ZА-Я]{5}:[A-ZА-Я]{10}\\s[A-ZА-Я]{7}\\s+\\d{1,4}\\s+){0,1}";

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

            str = str = new String(new String(buffer, "CP1251").getBytes(), "CP866");

            f1 = TextReplace.getText(str);
            f = TextReplace.getSha(f1);

        } catch (IOException ex) {
            Logger.getLogger(Spravka93Reader.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("exception in Spravka93Reader : " + ex);
        }

        HtmlTable result = new HtmlTable();

        pattern = Pattern.compile(regexDocHead);
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

        pattern = Pattern.compile(regexTHead);
        matcher = pattern.matcher(f);
        tableHeaderProcessed = false;

        while (matcher.find()) {

            result.addCell("№");
            for (int i = 1; i <= matcher.groupCount(); i++) {
                result.addCell(matcher.group(i));
            }
//            result.addCell("ТГНЛ");

            if (!tableHeaderProcessed) {
                tableHeaderProcessed = true;
                result.markCurrentRowAsHeader();
            }

            tHead = true;
            result.advanceToNextRow();
        }

        pattern = Pattern.compile(regexTBody);
        matcher = pattern.matcher(f);

        int n = 1;
        while (matcher.find()) {
            result.addCell("" + n++);
            
//            String bidx = "";
            for (int i = 1; i <= matcher.groupCount(); i++) {
                result.addCell(matcher.group(i));
//                bidx = matcher.group("tbidx");
            }
//            result.addCell("<button type='button' class='btn btn-default' onclick='getTGNL(\"" + bidx + "\");'>Показать</button>");
            if (!tableHeaderProcessed) {
                tableHeaderProcessed = true;
                result.markCurrentRowAsHeader();
            }
            reading = true;
            tBody = true;
            result.advanceToNextRow();
        }

        System.out.println("docHead === " + docHead);
        System.out.println("tHead === " + tHead);
        System.out.println("tBody === " + tBody);
        if (reading == true && (docHead == true && tHead == true && tBody == true)) {
            System.out.println("can reading SPR5072 " + result);
            ReadOnDir.spr = "sprDefault";
            return result;
        } else {
            System.out.println("can not reading SPR5072 " + result);
            return null;
        }
    }

}
