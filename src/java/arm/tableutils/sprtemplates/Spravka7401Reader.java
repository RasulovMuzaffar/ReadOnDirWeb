/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

/**
 *
 * @author Muzaffar
 */
public class Spravka7401Reader  implements TableReaderInterface {

    final static String regexDocHead = "([A-ZА-Я]{2}\\s[A-ZА-Я]{3})\\s+"
            + "(\\d{4})\\s+"
            + "(\\d{2}.\\d{2})\\s+"
            + "(\\d{2}-\\d{2})\\s+"
            + "([A-ZА-Я]{2}\\s\\d{2})\\s+"
            + "([A-ZА-Я]{7})\\s+"
            + "([A-ZА-Я]{8})\\s+"
            + "([A-ZА-Я]{6})\\s+"
            + "([A-ZА-Я]{6})";

    final static String regexTHead = "(?<thpoluch>[A-ZА-Я]{5})"
            + "-\\d{4,5}\\s+"
            + "(?<thnvag>[A-ZА-Я]{2}.[A-ZА-Я]{3})\\s+"
            + "(?<thves>[A-ZА-Я]{3})\\s+"
            + "(?<thgruz>[A-ZА-Я]{4})\\s+"
            + "(?<thnp>[A-ZА-Я]{2})\\s+"
            + "(?<thidx>[A-ZА-Я]{6})\\s+"
            + "(?<thdisl>[A-ZА-Я]{4})\\s+"
            + "(?<thoper>[A-ZА-Я]{4})\\s+"
            + "(?<thvroper>[A-ZА-Я]{2}.[A-ZА-Я]{4})\\s+"
            + "(?<thvroj>[A-ZА-Я]{2}.[A-ZА-Я]{2})";

    final static String regexTBody = "(?<tbnv>\\d{8})\\s+"
            + "(?<tbnazn>\\d{5})\\s+"
            + "(?<tbves>\\d{3})\\s+"
            + "(?<tbgruz>\\d{5})\\s+"
            + "(?<tbpoluch>\\d{5})\\s+"
            + "(?<tboper>[A-ZА-Я]{2,4}\\d{0,3})\\s+"
            + "(?<tbdate>\\d{2}\\s\\d{2})\\s+"
            + "(?<tbtime>\\d{2}\\s\\d{2}\\s\\d{2})\\s+"
            + "(?<tbidx>\\d{0,6}\\+{0,1}\\s{0,5}\\d{0,3}\\+{0,1}\\s\\d{0,6})";

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

            System.out.println("docHead === "+docHead);
            System.out.println("tHead === "+tHead);
            System.out.println("tBody === "+tBody);
        if (reading == true && (docHead == true && tHead == true && tBody == true)) {
            System.out.println("can reading SPR5065 " + result);
            ReadOnDir.spr = "sprDefault";
            return result;
        } else {
            System.out.println("can not reading SPR5065 " + result);
            return null;
        }
    }

}
