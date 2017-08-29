package arm.tableutils.sprtemplates.st;

import arm.ent.History;
import arm.tableutils.HtmlTable;
import arm.tableutils.tablereaders.TableReaderInterface;
import arm.tableutils.tablereaders.utils.TextReplace;
import arm.wr.ReadOnDir;
import arm.wr.WriteToHist;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Spravka1296Reader implements TableReaderInterface {

//    regexDocHead
    final static String RDH = "(?<vc>[A-ZА-Я]{2})\\s+"
            + "(?<dor>[A-ZА-Я]{2,4})\\s+(?<nspr>1296)\\s+"
            + "(?<date>\\d{2}\\.\\d{2})\\s+"
            + "(?<time>\\d{1,2}\\-\\d{1,2})\\s+"
            + "(?<vc73>[A-ZА-Я]{2}\\s\\d{2})\\s+"
            + "(?<ppispps>[A-ZА-Я]{8}\\s+[A-ZА-Я]{8}\\s+[A-ZА-Я]{1}\\s+[A-ZА-Я]{7}\\s+[A-ZА-Я]{7}\\s+[A-ZА-Я]{2}\\s+[A-ZА-Я]{4,8})\\s+"
            + "(?<st>[A-ZА-Я]{0,6}-?.?[A-ZА-Я]{0,5}\\d{0,2}.?)\\s+"
            + "(?<per>[A-ZА-Я]{6}\\:)\\s?"
            + "(?<pertime>\\d{1,2}\\-\\d{2}\\s[A-ZА-Я]{3})";

//    regexTHead
    final static String RTH = "(?<thnum>[A-ZА-Я]{5})\\s+"
            + "(?<thidx>[A-ZА-Я]{6})\\s+"
            + "(?<thstate>[A-ZА-Я]{4})\\s+"
            + "(?<thst>[A-ZА-Я]{4})\\s+"
            + "(?<thdate>[A-ZА-Я]{4})\\s+"
            + "(?<thtime>[A-ZА-Я]{5})";

//regexTBody1
    final static String RTB1 = "\\n(?<tdnp1>\\d{4})\\s+"
            + "(?<tdidx1>\\d{4,6}\\+\\d{3}\\+\\d{4,6})\\s+"
            + "(?<tdkv1>\\d{2})\\s+"
            + "(?<tddate1>\\d{2}\\.\\d{2})\\s+"
            + "(?<tdtime1>\\d{1,2}\\-\\d{1,2})";
//regexTBody2
    final static String RTB2 = " (?<tdnp2>\\d{4})\\s+"
            + "(?<tdidx2>\\d{4,6}\\+\\d{3}\\+\\d{4,6})\\s+"
            + "(?<tdkv2>\\d{2})\\s+"
            + "(?<tddate2>\\d{2}\\.\\d{2})\\s+"
            + "(?<tdtime2>\\d{1,2}\\-\\d{1,2})";

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
        boolean tBody1 = false;
        boolean tBody2 = false;


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
//            Logger.getLogger(Spravka1296Reader.class.getName()).log(Level.SEVERE, null, ex);
//            System.out.println("exception in Spravka1296Reader : " + ex);
//        }
        String f = TextReplace.getText(fileName);
        HtmlTable result = new HtmlTable();

        pattern = Pattern.compile(RDH);
        matcher = pattern.matcher(f);

        boolean tableHeaderProcessed = false;

        while (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                result.addCell(matcher.group(i));
            }
            
            History h = new History();
            h.setSprN(matcher.group("nspr"));
            h.setDate(matcher.group("date"));
            h.setTime(matcher.group("time"));
            h.setObj(matcher.group("st"));
            hist.infoFromSpr(h);
            
            if (!tableHeaderProcessed) {
                tableHeaderProcessed = true;
                result.markCurrentRowAsDocHeader();
            }

            docHead = true;
            result.advanceToNextRow();
        }

        tableHeaderProcessed = false;

        {
            result.addCell("Состояние");
            result.addCell("№");
            result.addCell("№ поезда");
            result.addCell("Индекс поезда");
            result.addCell("КВ");
            result.addCell("Дата");
            result.addCell("Время");

            if (!tableHeaderProcessed) {
                tableHeaderProcessed = true;
                result.markCurrentRowAsHeader();
            }

            tHead = true;
            result.advanceToNextRow();
        }

        pattern = Pattern.compile(RTB1);
        matcher = pattern.matcher(f);

        int n = 1;
        while (matcher.find()) {
            if (n == 1) {
                result.addCell("<b style='color:#51697E'>ПРИЕМ</b>");
            } else {
                result.addCell("");
            }
            result.addCell("" + n++);
            result.addCell(matcher.group("tdnp1"));
            result.addCell(plusToSpace(matcher.group("tdidx1")));
            result.addCell(delNull(matcher.group("tdkv1")));
            result.addCell(delNull(matcher.group("tddate1")));
            result.addCell(delNull(matcher.group("tdtime1")));

            if (!tableHeaderProcessed) {
                tableHeaderProcessed = true;
                result.markCurrentRowAsHeader();
            }
            reading = true;
            tBody1 = true;
            result.advanceToNextRow();
        }

        tableHeaderProcessed = false;

//        {
//            result.addCell("Состояние");
//            result.addCell("№");
//            result.addCell("№ поезда");
//            result.addCell("Индекс поезда");
//            result.addCell("КВ");
//            result.addCell("Дата");
//            result.addCell("Время");
//
//            if (!tableHeaderProcessed) {
//                tableHeaderProcessed = true;
//                result.markCurrentRowAsHeader();
//            }
//
//            tHead = true;
//            result.advanceToNextRow();
//        }
        pattern = Pattern.compile(RTB2);
        matcher = pattern.matcher(f);
        int m = 1;
        while (matcher.find()) {
            if (m == 1) {
                result.addCell("<b style='color:#51697E'>СДАЧА</b>");
                result.markCurrentRowAsRegularUnderscore();
            } else {
                result.addCell("");
            }

            result.addCell("" + m++);
            result.addCell(matcher.group("tdnp2"));
            result.addCell(plusToSpace(matcher.group("tdidx2")));
            result.addCell(delNull(matcher.group("tdkv2")));
            result.addCell(delNull(matcher.group("tddate2")));
            result.addCell(delNull(matcher.group("tdtime2")));

//            if (!tableHeaderProcessed) {
//                tableHeaderProcessed = true;
//            }
            reading = true;
            tBody2 = true;
            result.advanceToNextRow();
        }

        System.out.println("docHead1296 === " + docHead);
        System.out.println("tHead1296 === " + tHead);
        System.out.println("tBody1296-1 === " + tBody1);
        System.out.println("tBody1296-2 === " + tBody2);
        if (reading == true && (docHead == true && tHead == true && (tBody1
                == true || tBody2 == true))) {
            System.out.println("can reading SPR1296 ");
            ReadOnDir.spr = "sprDefault";
            return result;
        } else {
            System.out.println("can not reading SPR1296 ");
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
