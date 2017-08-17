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

public class Spravka92Reader implements TableReaderInterface {

//    final static String regexDocHead = "(?<dhvcuty>[А-ЯA-Z]{2} [А-ЯA-Z]{3})\\s+"
//            + "(?<dhcode>\\d{2})\\s+"
//            + "(?<dhdate>\\d{2}.\\d{2})\\s+"
//            + "(?<dhtime>\\d{2}-\\d{2})\\s+"
//            + "(?<dhvc73>[А-ЯA-Z]{2} \\d{2})\\s+"
//            + "(?<dhnpsst>[А-ЯA-Z]{7} [А-ЯA-Z]{7} [А-ЯA-Z]{14} [А-ЯA-Z]{2}.)\\s+"
//            + "(?<dhst>[А-ЯA-Z]{5})";
    final static String regexDocHead = "(?<dhvcuty>[А-ЯA-Z]{2}\\s[А-ЯA-Z]{3})\\s+"
            + "(?<dhcode>\\d{2})\\s+"
            + "(?<dhdate>\\d{2}.\\d{2})\\s+"
            + "(?<dhtime>\\d{2}-\\d{2})\\s+"
            + "(?<dhvc73>[А-ЯA-Z]{2}\\s\\d{2})\\s+"
//            + "(?<dhnpsst>HAЛИЧИE\\s+ПOEЗДOB\\s+HAЗHAЧEHИEM\\s+HA\\s+CT.)\\s+"
            + "(?<dhnpsst>[A-ZА-Я]{7}\\s[A-ZА-Я]{7}\\s[A-ZА-Я]{11}\\s[A-ZА-Я]{2}\\s[A-ZА-Я]{2}.)\\s+"
            + "(?<dhst>[А-ЯA-Z\\d+]{2,8})";

    final static String regexTHead = "(?<thnum>[A-ZА-Я]{5})\\s+"
            + "(?<thidx>[A-ZА-Я]{6})\\s+"
            + "(?<thstate>[A-ZА-Я]{4})\\s+"
            + "(?<thst>[A-ZА-Я]{4})\\s+"
            + "(?<thdate>[A-ZА-Я]{4})\\s+"
            + "(?<thtime>[A-ZА-Я]{5})";

    final static String regexTBody = "(?<tbnum>\\d{4})\\s+"
            + "(?<tbidx>\\d{4}\\s+\\d{2,3}\\s+\\d{4})\\s+"
            + "(?<tbstate>[A-ZА-Я]{2,4})\\s+"
            + "(?<tbst>[A-ZА-Я]{0,6}-{0,1}.{0,1}[A-ZА-Я]{0,5}\\d{0,2}.{0,1})\\s+"
            + "(?<tbdate>\\d{2}.\\d{2})\\s+"
            + "(?<tbtime>\\d{2}-\\d{2})";

    public HtmlTable processFile(String fileName) {
        String str = null;
        String f = null;
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

            f = TextReplace.getText(str);

        } catch (IOException ex) {
            Logger.getLogger(Spravka93Reader.class.getName()).log(Level.SEVERE, null, ex);
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
            result.addCell("ТГНЛ");

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
            String bidx = "";
            for (int i = 1; i <= matcher.groupCount(); i++) {
                result.addCell(matcher.group(i));
                bidx = matcher.group("tbidx");
            }
            result.addCell("<button type='button' class='btn btn-default' onclick='getTGNL(\"" + bidx + "\");'>Показать</button>");

            if (!tableHeaderProcessed) {
                tableHeaderProcessed = true;
                result.markCurrentRowAsHeader();
            }
            tBody = true;
            reading = true;
            result.advanceToNextRow();
        }

        if (reading == true && (docHead == true && tHead == true && tBody == true)) {
            System.out.println("can reading SPR92 " + result);
            ReadOnDir.spr = "sprDefault";
            return result;
        } else {
            System.out.println("can not reading SPR92 " + result);
            return null;
        }
    }

}
