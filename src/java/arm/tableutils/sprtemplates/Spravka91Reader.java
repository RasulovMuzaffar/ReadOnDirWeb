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

public class Spravka91Reader implements TableReaderInterface {

//    final static String regexDocHead = "(?<dhvcuty>[А-ЯA-Z]{2} [А-ЯA-Z]{3})\\s+"
//            + "(?<dhcode>\\d{2})\\s+"
//            + "(?<dhdate>\\d{2}.\\d{2})\\s+"
//            + "(?<dhtime>\\d{2}-\\d{2})\\s+"
//            + "(?<dhvc73>[А-ЯA-Z]{2} \\d{2})\\s+"
//            + "(?<dhnpsst>[А-ЯA-Z]{7} [А-ЯA-Z]{7} [А-ЯA-Z]{14} [А-ЯA-Z]{2}.)\\s+"
//            + "(?<dhst>[А-ЯA-Z]{5})";
    final static String regexDocHead = "(?<dhvc>[A-ZА-Я]{2})\\s+(?<dhdor>[A-ZА-Я]{3})\\s+"
            //            + "(?<dhcode>\\d{2})\\s+"
            + "(?<dhcode>91)\\s+"
            + "(?<dhdate>\\d{2}.\\d{2})\\s+"
            + "(?<dhtime>\\d{2}-\\d{2})\\s+"
            + "(?<dhvc73>[A-ZА-Я]{2}\\s\\d{2})\\s+"
            //            + "(?<dhnpsst>HAЛИЧИE\\s+ПOEЗДOB\\s+CФOPMИPOBAHHЫX\\s+CT.)\\s+"
            + "(?<dhnpsst>[A-ZА-Я]{7}\\s[A-ZА-Я]{7}\\s[A-ZА-Я]{14}\\s[A-ZА-Я]{2}.)\\s+"
            + "(?<dhst>[A-ZА-Я]{2,10}-{0,1}[A-ZА-Я]{0,5}\\d{0,4})";

    final static String regexTHead = "(?<thnum>[A-ZА-Я]{5})\\s+"
            + "(?<thidx>[A-ZА-Я]{6})\\s+"
            + "(?<thstate>[A-ZА-Я]{4})\\s+"
            + "(?<thst>[A-ZА-Я]{4})\\s+"
            + "(?<thdate>[A-ZА-Я]{4})\\s+"
            + "(?<thtime>[A-ZА-Я]{5})";

    final static String regexTBody = "(?<tbnum>\\d{4})\\s+"
            + "(?<tbidx>\\d{4,6}\\s+\\d{2,3}\\s+\\d{4,6})\\s+"
            + "(?<tbstate>[A-ZА-Я]{2,4})\\s+"
            + "(?<tbst>[A-ZА-Я]{0,6}-{0,1}.{0,1}[A-ZА-Я]{0,5}\\d{0,2}.{0,1})\\s+"
            + "(?<tbdate>\\d{2}.\\d{2})\\s+"
            + "(?<tbtime>\\d{2}-\\d{2})";

    String str = null;
    String f = null;
    Pattern pattern;
    Matcher matcher;
    boolean reading = false;
    boolean docHead = false;
    boolean tHead = false;
    boolean tBody = false;
    String doroga = "";

    @Override
    public HtmlTable processFile(String fileName) {

        /*
        * пока условно будем считать что файл всегда есть!
         */
        try (FileInputStream fis = new FileInputStream(fileName)) {

            System.out.println("File size: " + fis.available() + " bytes");

            byte[] buffer = new byte[fis.available()];

            // считаем файл в буфер
            fis.read(buffer, 0, fis.available());

            str = str = new String(new String(buffer, "CP1251").getBytes(), "CP866");

            f = TextReplace.getText(str);

        } catch (IOException ex) {
            Logger.getLogger(Spravka93Reader.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("exception in Spravka91Reader : " + ex);
        }
        String vc = "(?<vc>ВЦ УТИ\\s+\\d{2}\\s)";
        String[] lines = f.split("\\r\\n\\r\\nВЦ");
        HtmlTable result = new HtmlTable();
        for (String l : lines) {
            pattern = Pattern.compile(vc);
            matcher = pattern.matcher(l);
            while (matcher.find()) {
                if (matcher.group("vc") != null) {
                    result = getResult(l);
                } else {
                    result = getResult("ВЦ " + l);
                }
            }
        }
        
        if (result != null) {
            return result;
        } else {
            return null;
        }
    }

    private HtmlTable getResult(String f) {

        HtmlTable result = new HtmlTable();

        pattern = Pattern.compile(regexDocHead);
        matcher = pattern.matcher(f);

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
            System.out.println("fignya v 91 docHead!!!");
            return null;
        }
        pattern = Pattern.compile(regexTHead);
        matcher = pattern.matcher(f);
        tableHeaderProcessed = false;

        while (matcher.find()) {

            result.addCell("№");
            for (int i = 1; i <= matcher.groupCount(); i++) {
                result.addCell(matcher.group(i));
            }
            if (doroga.equals("УТИ")) {
                result.addCell("ТГНЛ");
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
            System.out.println("fignya v 91 tHead!!!");
            return null;
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
            if (doroga.equals("УТИ")) {
                result.addCell("<button type='button' class='btn btn-default' onclick='getTGNL(\"" + bidx + "\");'>Показать</button>");
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
            System.out.println("fignya v 91 tBody!!!");
            return null;
        }
        System.out.println("docHead91 === " + docHead);
        System.out.println("tHead91 === " + tHead);
        System.out.println("tBody91 === " + tBody);
        if (reading == true && (docHead == true && tHead == true && tBody == true)) {
//            System.out.println("can reading SPR91 " + result);
            ReadOnDir.spr = "sprDefault";
//            System.out.println("+++++++++++++++++++++++++91++++++++++++++++++++++++");
//            System.out.println("" + result.generateHtml());
//            System.out.println("-------------------------91------------------------");
            return result;
        } else {
//            System.out.println("can not reading SPR91 " + result);
            return null;
        }
    }

    @Override
    public List<HtmlTable> readersResult() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
