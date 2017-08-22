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

public class Spravka5065Reader implements TableReaderInterface {

//regexDocHead
    final static String RDH = "([A-ZА-Я]{2}\\s+[A-ZА-Я]{3})\\s+"
            + "(\\d{4})\\s+(\\d{2}.\\d{2})\\s+(\\d{2}-\\d{2})\\s+([A-ZА-Я]{2})\\s+"
            + "(\\d{2})\\s+([A-ZА-Я]{10})\\s+([A-ZА-Я]{7})\\s+([A-ZА-Я]{7})\\s+"
            + "([A-ZА-Я]{3,8}-{0,1}[A-ZА-Я]{0,8}\\d{0,2})\\s{0,5}"
            + "([A-ZА-Я]{0,8}-{0,1}[A-ZА-Я]{0,3})\\s{0,5}"
            + "([A-ZА-Я]{0,3}={0,1}[A-ZА-Я]{0,4}\\s{0,5}\\({0,1}\\d{0,2}\\){0,1})\\s{0,5}([A-ZА-Я]{0,4}={0,1}[A-ZА-Я]{0,3})";

//    RTH
    final static String RTH = "(?<thnv>[A-ZА-Я]{1}\\s+[A-ZА-Я]{6})\\s+"
            + "(?<thnazn>[A-ZА-Я]{4})\\s+"
            + "(?<thves>[A-ZА-Я]{3})\\s+"
            + "(?<thgruz>[A-ZА-Я]{4})\\s+"
            + "(?<thpoluch>[A-ZА-Я]{5})\\s+"
            + "(?<thoper>[A-ZА-Я]{4})\\s+"
            + "(?<thdate>[A-ZА-Я]{4})\\s+"
            + "(?<thtime>[A-ZА-Я]{5})\\s+"
            + "(?<thsobst>[A-ZА-Я]{3}){0,1}\\s+"
            + "(?<thidx>[A-ZА-Я]{6}\\s+[A-ZА-Я]{6})";

//    RTB
    final static String RTB = "(?<tbnv>\\d{8})\\s+"
            + "(?<tbnazn>\\d{5})\\s+(?<tbves>\\d{3})\\s+"
            + "(?<tbgruz>\\d{5})\\s+(?<tbpoluch>\\d{5})\\s+"
            + "(?<tboper>[A-ZА-Я]{2,4}\\d{0,3})\\s+(?<tbdate>\\d{2}\\s\\d{2})\\s+(?<tbtime>\\d{2}\\s\\d{2}\\s\\d{2})\\s{0,}"
            + "(?<tbsobst>\\d{2}){0,1}\\s+(?<tbidx>\\d{0,5}\\+{0,1}\\s{0,5}\\d{0,3}\\+{0,1}\\s{0,1}\\d{0,6})";

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
            Logger.getLogger(Spravka5065Reader.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("exception in Spravka93Reader : " + ex);
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

            for (int i = 1; i <= matcher.groupCount(); i++) {
                if (matcher.group(i) != null) {
                    result.addCell(matcher.group(i));
                }
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
                if (matcher.group(i) != null) {
                    result.addCell(matcher.group(i));
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

        System.out.println("docHead5065 === " + docHead);
        System.out.println("tHead5065 === " + tHead);
        System.out.println("tBody5065 === " + tBody);
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
