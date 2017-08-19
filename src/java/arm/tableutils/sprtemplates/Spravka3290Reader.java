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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Muzaffar
 */
public class Spravka3290Reader implements TableReaderInterface {

    final static String regexDocHead = "((?<dhvc>[A-ZА-Я]{2})\\s+"
            + "(?<dhdor>[A-ZА-Я]{3})\\s+"
            + "(?<dhcode>3290)\\s+"
            + "(?<dhdate>\\d{2}.\\d{2})\\s+"
            + "(?<dhtime>\\d{2}-\\d{2})\\s+"
            + "(?<dhvc73>[А-ЯA-Z]{2}\\s\\d{2})\\s+"
            + "(?<dhpvps>[A-ZА-Я]{8}\\s[A-ZА-Я]{7}\\s[A-ZА-Я]{2}\\s[A-ZА-Я]{5}\\s\\([A-ZА-Я]{2}\\s[A-ZА-Я]{3}\\s[A-ZА-Я]{1}\\s[A-ZА-Я]{13}\\))\\s+"
            + "(?<dhst>[A-ZА-Я]{0,6}\\-?\\.?[A-ZА-Я]{0,5}\\d{0,2}\\.?)\\s+"
            + "(?<dhrp>[A-ZА-Я]{1,4})\\s+"
            + "(?<dhper>[A-ZА-Я]{6}\\:\\d{2}\\-\\d{2}\\s+[A-ZА-Я]{3}\\.)";

    final static String regexTHead = "(?<thsbst>[A-ZА-Я]{6}.)\\s+:\\s+"
            + "(?<thvsg>[A-ZА-Я]{3})\\s+"
            + "(?<thkr>[A-ZА-Я]{2})\\s+"
            + "(?<thpl>[A-ZА-Я]{2})\\s+"
            + "(?<thpv>[A-ZА-Я]{2})\\s+"
            + "(?<thcs>[A-ZА-Я]{2})\\s+"
            + "(?<thrf>[A-ZА-Я]{2})\\s+"
            + "(?<thpr>[A-ZА-Я]{2})\\s+"
            + "(?<thcmv>[A-ZА-Я]{3})\\s+"
            + "(?<th94>\\d{2})\\s+"
            + "(?<thzvg>[A-ZА-Я]{3})\\s+"
            + "(?<thftg>[A-ZА-Я]{3})\\s+"
            + "(?<thmvz>[A-ZА-Я]{3})";

    final static String regexTBody = "((?<poluch>\\d{4}){0,1}\\s+)(\\D+\\s){0,1}(?<vg>\\d{8})\\s+(?<ves>\\d{1,2})(\\s(?<gr>\\d{5})\\s+(?<np>\\d{4})\\s+(?<idx>\\d{4}\\+\\d{3}\\+\\d{4})\\s+(?<disl>\\d{5})\\s+(?<oper>[A-ZА-Я]{4})\\s+(?<vrop>\\d{2}\\s+\\d{2}\\-\\d{2})(?<vroj>\\s+\\d{2}\\s+\\d{2}\\-\\d{2}){0,1}){0,1}";

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
            if (matcher.group("poluch") != null) {
                result.addCell(matcher.group("poluch"));
            } else {
                result.addCell("");
            }
            if (matcher.group("vg") != null) {
                result.addCell(matcher.group("vg"));
            } else {
                result.addCell("");
            }
            if (matcher.group("ves") != null) {
                result.addCell(matcher.group("ves"));
            } else {
                result.addCell("");
            }
            if (matcher.group("gr") != null) {
                result.addCell(matcher.group("gr"));
            } else {
                result.addCell("");
            }
            if (matcher.group("np") != null) {
                result.addCell(matcher.group("np"));
            } else {
                result.addCell("");
            }
            if (matcher.group("idx") != null) {
                result.addCell(matcher.group("idx"));
            } else {
                result.addCell("");
            }
            if (matcher.group("disl") != null) {
                result.addCell(matcher.group("disl"));
            } else {
                result.addCell("");
            }
            if (matcher.group("oper") != null) {
                result.addCell(matcher.group("oper"));
            } else {
                result.addCell("");
            }
            if (matcher.group("vrop") != null) {
                result.addCell(matcher.group("vrop"));
            } else {
                result.addCell("");
            }
            if (matcher.group("vroj") != null) {
                result.addCell(matcher.group("vroj"));
            } else {
                result.addCell("");
            }

//            String bidx = "";
//            for (int i = 1; i <= matcher.groupCount(); i++) {
//                result.addCell(matcher.group(i));
////                bidx = matcher.group("tbidx");
//            }
//            result.addCell("<button type='button' class='btn btn-default' onclick='getTGNL(\"" + bidx + "\");'>Показать</button>");
            if (!tableHeaderProcessed) {
                tableHeaderProcessed = true;
                result.markCurrentRowAsHeader();
            }
            reading = true;
            tBody = true;
            result.advanceToNextRow();
        }

        System.out.println("docHead3290 === " + docHead);
        System.out.println("tHead3290 === " + tHead);
        System.out.println("tBody3290 === " + tBody);
        if (reading == true && (docHead == true && tHead == true && tBody == true)) {
            System.out.println("can reading SPR5065 " + result);
            ReadOnDir.spr = "sprDefault";
            return result;
        } else {
            System.out.println("can not reading SPR5065 " + result);
            return null;
        }
    }

    @Override
    public List<HtmlTable> readersResult() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
