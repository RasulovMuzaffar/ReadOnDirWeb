package arm.tableutils.sprtemplates.st;

import arm.tableutils.HtmlTable;
import arm.tableutils.tablereaders.TableReaderInterface;
import arm.tableutils.tablereaders.utils.TextReplace;
import arm.wr.ReadOnDir;
import arm.wr.WriteToHist;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Spravka95Reader implements TableReaderInterface {

//regexDocHead
    final static String RDH = "(?<dhvc>[A-ZА-Я]{2})\\s+(?<dhdor>[A-ZА-Я]{3,4})\\s+"
            + "(?<dhcode>\\d{2})\\s+"
            + "(?<dhdate>\\d{2}.\\d{2})\\s+"
            + "(?<dhtime>\\d{2}-\\d{2})\\s+"
            + "(?<dhvc73>[А-ЯA-Z]{2}\\s\\d{2})\\s+"
            + "(?<dhnpsst>[A-ZА-Я]{6}\\s[A-ZА-Я]{7}\\s+[A-ZА-Я]{1}\\s[A-ZА-Я]{7})\\s+"
            //            + "(?<dhst>[А-ЯA-Z\\d+]{2,8})";
            + "(?<dhst>[A-ZА-Я]{0,6}-{0,1}.{0,1}[A-ZА-Я]{0,5}\\d{0,2}.{0,1})";

//    regexTHead
    final static String RTH = "(?<hnum>[А-ЯA-Z]{5})\\s+"
            + "(?<hidx>[А-ЯA-Z]{6})\\s+"
            + "(?<hstate>[А-ЯA-Z]{4})\\s+"
            + "(?<hst>[А-ЯA-Z]{4})\\s+"
            + "(?<hdate>[А-ЯA-Z]{4})\\s+"
            + "(?<htime>[А-ЯA-Z]{5})";

//    regexTBody
    final static String RTB = "(?<bnum>\\d{4})\\s+"
            + "(?<bidx>\\d{4}\\s+\\d{2,3}\\s\\d{4})\\s+"
            + "(?<bstate>[А-ЯA-Z]{2,4})\\s+"
            + "(?<bst>[A-ZА-Я]{0,6}-{0,1}.{0,1}[A-ZА-Я]{0,5}\\d{0,2}.{0,1})\\s+"
            + "(?<bdate>\\d{2}.\\d{2})\\s+"
            + "(?<btime>\\d{2}-\\d{2})";

    final WriteToHist hist = new WriteToHist();

    @Override
    public HtmlTable processFile(String fileName) {
//        String str = null;
//        String f = null;
        Pattern pattern;
        Matcher matcher;
        boolean reading = false;
        boolean docHead = false;
        boolean tHead = false;
        boolean tBody = false;
        String doroga = "";
        /*
        * пока условно будем считать что файл всегда есть!
         */
//        try (FileInputStream fis = new FileInputStream(fileName)) {
//
//            System.out.println("Размер файла: " + fis.available() + " байт(а)");
//
//            byte[] buffer = new byte[fis.available()];
//
//            // считаем файл в буфер
//            fis.read(buffer, 0, fis.available());
//
//            str = new String(new String(buffer, "CP1251").getBytes(), "CP866");
//
//            f = TextReplace.getText(str);
//
//        } catch (IOException ex) {
//            Logger.getLogger(Spravka93Reader.class.getName()).log(Level.SEVERE, null, ex);
//        }
        String f = TextReplace.getSha(TextReplace.getText(fileName));
        HtmlTable result = new HtmlTable();

        pattern = Pattern.compile(RDH);
        matcher = pattern.matcher(f);

        StringBuilder sb = new StringBuilder();

        boolean tableHeaderProcessed = false;
        while (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                String s = matcher.group("dhnpsst");

                if (matcher.group(i).equals(s)) {
                    result.addCell(matcher.group(i).replaceAll("\\s+", " "));
                } else {
                    result.addCell(matcher.group(i));
                }
            }

//            sb.append(matcher.group("dhcode")).append(" : ").append(matcher.group("dhdate")).append(" : ")
//                    .append(matcher.group("dhtime")).append(" : ").append(matcher.group("dhst"));
//            hist.infoFromSpr(sb.toString());

            doroga = matcher.group("dhdor");

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
        }

        pattern = Pattern.compile(RTB);
        matcher = pattern.matcher(f);

        int n = 1;
        while (matcher.find()) {
            result.addCell("" + n++);
            String bidx = "";
            for (int i = 1; i <= matcher.groupCount(); i++) {
                result.addCell(matcher.group(i));
                bidx = matcher.group("bidx");
            }
            result.addCell("<button type='button' class='btn btn-default' onclick='getTGNL(\"" + bidx + "\");'>Показать</button>");
            result.addCell("<button type='button' class='btn btn-default' onclick='getRS(\"" + bidx + "\");'>Показать</button>");

            if (!tableHeaderProcessed) {
                tableHeaderProcessed = true;
                result.markCurrentRowAsHeader();
            }
            tBody = true;
            reading = true;
            result.advanceToNextRow();
        }

        System.out.println("docHead95 === " + docHead);
        System.out.println("tHead95 === " + tHead);
        System.out.println("tBody95 === " + tBody);
        if (reading == true && (docHead == true && tHead == true && tBody == true)) {
            System.out.println("can reading SPR95 ");
            ReadOnDir.spr = "sprDefault";
            return result;
        } else {
            System.out.println("can not reading SPR95 ");
            return null;
        }

    }

    @Override
    public List<HtmlTable> readersResult() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
