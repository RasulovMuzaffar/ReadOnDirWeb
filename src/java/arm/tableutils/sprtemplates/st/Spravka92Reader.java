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

public class Spravka92Reader implements TableReaderInterface {

//regexDocHead
    final static String RDH = "(?<dhvc>[A-ZА-Я]{2})\\s+(?<dhdor>[A-ZА-Я]{3})\\s+"
            + "(?<dhcode>92)\\s+"
            + "(?<dhdate>\\d{2}.\\d{2})\\s+"
            + "(?<dhtime>\\d{2}-\\d{2})\\s+"
            + "(?<dhvc73>[А-ЯA-Z]{2}\\s\\d{2})\\s+"
            + "(?<dhnpsst>[A-ZА-Я]{7}\\s[A-ZА-Я]{7}\\s[A-ZА-Я]{11}\\s[A-ZА-Я]{2}\\s[A-ZА-Я]{2}.)\\s+"
            + "(?<dhst>[A-ZА-Я]{0,6}-{0,1}.{0,1}[A-ZА-Я]{0,5}\\d{0,2}.{0,1})";

//    regexTHead
    final static String RTH = "(?<thnum>[A-ZА-Я]{5})\\s+"
            + "(?<thidx>[A-ZА-Я]{6})\\s+"
            + "(?<thstate>[A-ZА-Я]{4})\\s+"
            + "(?<thst>[A-ZА-Я]{4})\\s+"
            + "(?<thdate>[A-ZА-Я]{4})\\s+"
            + "(?<thtime>[A-ZА-Я]{5})";

//    regexTBody
    final static String RTB = "(?<tbnum>\\d{4})\\s+"
            + "(?<tbidx>\\d{4}\\s+\\d{2,3}\\s+\\d{4})\\s+"
            + "(?<tbstate>[A-ZА-Я]{2,4})\\s+"
            + "(?<tbst>[A-ZА-Я]{0,6}-{0,1}.{0,1}[A-ZА-Я]{0,5}\\d{0,2}.{0,1})\\s+"
            + "(?<tbdate>\\d{2}.\\d{2})\\s+"
            + "(?<tbtime>\\d{2}-\\d{2})";

    @Override
    public HtmlTable processFile(String fileName) {

        String str = null;
        String f = null;
        /*
        * пока условно будем считать что файл всегда есть!
         */
        try (FileInputStream fis = new FileInputStream(fileName)) {

            System.out.println("File size: " + fis.available() + " bytes");

            byte[] buffer = new byte[fis.available()];

            // считаем файл в буфер
            fis.read(buffer, 0, fis.available());

            str = new String(new String(buffer, "CP1251").getBytes(), "CP866");

            f = TextReplace.getSha(TextReplace.getText(str));
            //asdasdasd

        } catch (IOException ex) {
            Logger.getLogger(Spravka92Reader.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("exception in Spravka92Reader : " + ex);
        }

        String[] lines = f.split("ВЦ УТИ");

        HtmlTable result = new HtmlTable();
        if (lines.length > 1) {
            for (String l : lines) {
                if (l.trim().length() > 0 && l.trim().substring(0, 2).equals("92")) {
                    result = getResult("ВЦ УТИ" + l);
                    break;
                } else {
                    result = null;
                }
            }
            return result;
        } else {
            return getResult(f);
        }
    }

    private HtmlTable getResult(String text) {
        Pattern pattern;
        Matcher matcher;
        boolean reading = false;
        boolean docHead = false;
        boolean tHead = false;
        boolean tBody = false;
        String doroga = "";
        
        HtmlTable result = new HtmlTable();

        pattern = Pattern.compile(RDH);
        matcher = pattern.matcher(text);

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
            System.out.println("fignya v 92 docHead!!!");
            return null;
        }
        pattern = Pattern.compile(RTH);
        matcher = pattern.matcher(text);
        tableHeaderProcessed = false;

        while (matcher.find()) {

            result.addCell("№");
            for (int i = 1; i <= matcher.groupCount(); i++) {
                result.addCell(matcher.group(i));
            }
            if (doroga.equals("УТИ")) {
                result.addCell("ТГНЛ");
                result.addCell("Расш. Спр.");
            }

            if (!tableHeaderProcessed) {
                tableHeaderProcessed = true;
                result.markCurrentRowAsHeader();
            }

            tHead = true;
            result.advanceToNextRow();
            break;
        }

        if (tHead == false) {
            System.out.println("fignya v 92 tHead!!!");
            return null;
        }
        pattern = Pattern.compile(RTB);
        matcher = pattern.matcher(text);

        int n = 1;
        while (matcher.find()) {
            result.addCell("" + n++);
            String bidx = "";
            for (int i = 1; i <= matcher.groupCount(); i++) {
                result.addCell(matcher.group(i));
                bidx = matcher.group("tbidx");
            }
            if (doroga.equals("УТИ")) {
                result.addCell("<button type='button' class='btn btn-default' onclick='getTGNL(\"" + bidx + "\");'>Показать</button>");
                result.addCell("<button type='button' class='btn btn-default' onclick='getRS(\"" + bidx + "\");'>Показать</button>");
            }
            if (!tableHeaderProcessed) {
                tableHeaderProcessed = true;
                result.markCurrentRowAsHeader();
            }
            reading = true;
            tBody = true;
            result.advanceToNextRow();
        }

        if (tBody == false) {
            System.out.println("fignya v 92 tBody!!!");
            return null;
        }
        System.out.println("docHead92 === " + docHead);
        System.out.println("tHead92 === " + tHead);
        System.out.println("tBody92 === " + tBody);
        if (reading == true && (docHead == true && tHead == true && tBody == true)) {
            System.out.println("can reading SPR92 " );
            ReadOnDir.spr = "sprDefault";
            return result;
        } else {
            System.out.println("can not reading SPR92 " );
            return null;
        }
    }

    @Override
    public List<HtmlTable> readersResult() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
