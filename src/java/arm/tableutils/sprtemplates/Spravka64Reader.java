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

public class Spravka64Reader implements TableReaderInterface {

    final static String regexDocHead = "([A-ZА-Я]{2}\\s[A-ZА-Я]{3})\\s+"
            + "(\\d{2})\\s+(\\d{2}.\\d{2})\\s+(\\d{2}-\\d{2})\\s+"
            + "([A-ZА-Я]{2})\\s+(\\d{2})\\s+([A-ZА-Я]{6})\\s+"
            + "([A-ZА-Я]{7})\\s+([A-ZА-Я]{0,10}-{0,1}[A-ZА-Я]{0,5}\\d{0,3})\\s+"
            + "([A-ZА-Я]{1})\\s+([A-ZА-Я]{8})";

    final static String regexTHead = "(?<thnum>[A-ZА-Я]{5})\\s+"
            + "(?<thidx>[A-ZА-Я]{6})\\s+"
            + "(?<thstate>[A-ZА-Я]{4})\\s+"
            + "(?<thst>[A-ZА-Я]{4})\\s+"
            + "(?<thdate>[A-ZА-Я]{4})\\s+"
            + "(?<thtime>[A-ZА-Я]{5})";

    final static String regexTBody = "(?<tbnp>\\d{4})\\s+"
            + "(?<tbidx>\\d{4}\\+\\d{3}\\+\\d{4})\\s"
            + "((?<tbstate1>[A-ZА-Я]{4}\\-{0,1}\\d{0,2})\\s+"
            + "(?<tbtime1>\\d{2}\\-\\d{2})){0,1}\\s+"
            + "((?<tbstate2>[A-ZА-Я]{4}\\-{0,1}\\d{0,2})\\s"
            + "(?<tbtime2>\\d{2}\\-{1}\\d{2})){0,1}\\s{0,14}"
            + "((?<tbxz1>\\d{1}\\/\\d{2})\\s(?<tbxz2>\\d{4})\\s"
            + "(?<tbxz3>\\d{0,3})){0,1}"
            + "(\\s(?<tbxz4>[A-ZА-Я]{0,1}\\d{0,1})\\s{4,7}"
            + "(?<tbxz5>\\d{1,4})){0,1}"
            + "(\\s{11}(?<tbxz6>\\d{4})){0,1}";
//    final static String regexTBody = "(?<tbnp>\\d{4})\\s+"
//            + "(?<tbidx>\\d{4}\\+\\d{3}\\+\\d{4})\\s"
//            + "((?<tbstate1>[A-ZА-Я]{4}\\-{0,1}\\d{0,2})\\s+"
//            + "(?<tbtime1>\\d{2}\\-\\d{2})){0,1}\\s{0,}"
//            + "((?<tbstate2>[A-ZА-Я]{4}\\-{0,1}\\d{0,2})\\s"
//            + "(?<tbtime2>\\d{2}\\-\\d{2})){0,1}\\s+"
//            + "((?<tbxz1>\\d{1}\\/\\d{2})\\s(?<tbxz2>\\d{4})\\s(?<tbxz3>\\d{0,3})){0,1}\\s{0,}"
//            + "((?<tbxz4>[A-ZА-Я]{0,1}\\d{0,1})\\s+(?<tbxz5>\\d{0,4})){0,1}";

    @Override
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

        tableHeaderProcessed = false;

        {

            result.addCell("№");
            result.addCell("№ поезда");
            result.addCell("Индекс поезда");
            result.addCell("Сост1");
            result.addCell("Время1");
            result.addCell("Сост2");
            result.addCell("Время2");
            result.addCell("хз1");
            result.addCell("хз2");
            result.addCell("хз3");
            result.addCell("хз4");
            result.addCell("хз5");
            result.addCell("хз6");
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

            result.addCell(matcher.group("tbnp"));
            result.addCell(plusToSpace(matcher.group("tbidx")));
            result.addCell(delNull(matcher.group("tbstate1")));
            result.addCell(delNull(matcher.group("tbtime1")));
            result.addCell(delNull(matcher.group("tbstate2")));
            result.addCell(delNull(matcher.group("tbtime2")));
            result.addCell(delNull(matcher.group("tbxz1")));
            result.addCell(delNull(matcher.group("tbxz2")));
            result.addCell(delNull(matcher.group("tbxz3")));
            result.addCell(delNull(matcher.group("tbxz4")));
            result.addCell(delNull(matcher.group("tbxz5")));
            result.addCell(delNull(matcher.group("tbxz6")));


            bidx = plusToSpace(matcher.group("tbidx"));

            result.addCell("<button type='button' class='btn btn-default' onclick='getTGNL(\"" + bidx + "\");'>Показать</button>");

            if (!tableHeaderProcessed) {
                tableHeaderProcessed = true;
                result.markCurrentRowAsHeader();
            }
            reading = true;
            tBody = true;
            result.advanceToNextRow();
        }

        if (reading == true && (docHead == true && tHead == true && tBody
                == true)) {
            System.out.println("can reading SPR64 " + result);
            ReadOnDir.spr = "sprDefault";
            return result;
        } else {
            System.out.println("can not reading SPR64 " + result);
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
