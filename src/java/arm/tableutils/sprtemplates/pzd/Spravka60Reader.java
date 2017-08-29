package arm.tableutils.sprtemplates.pzd;

import arm.tableutils.HtmlTable;
import arm.tableutils.tablereaders.TableReaderInterface;
import arm.tableutils.tablereaders.utils.TextReplace;
import arm.wr.ReadOnDir;
import static arm.wr.Write.forPopup;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Spravka60Reader implements TableReaderInterface {

//    regexDocHead
    final static String RDH = "(?<dh>[A-ZА-Я]{2}\\s+[A-ZА-Я]{2,4}\\s+60\\s+\\d{2}\\.\\d{2}\\s+\\d{2}\\-\\d{2}\\s+[A-ZА-Я]{2}\\s+\\d{4})\\s+"
            + "\\((?<idx>\\d{4,6}\\s?\\+\\s?\\d{3,4}\\s?\\+\\s?\\d{4,6})\\)\\s+"
            + "(?<pros>[A-ZА-Я]{4}\\s?\\-?\\s?\\d{4,6}\\s{0,}\\d{0,2}\\.?\\d{0,2}\\s?\\d{0,2}\\-?\\d{0,2}\\s?[A-ZА-Я]{0,20})?\\s+"
            + "(?<napr>[A-ZА-Я]{4}\\s?\\-?\\s?\\d{4,6}\\s{0,}\\d{0,2}\\.?\\d{0,2}\\s?\\d{0,2}\\-?\\d{0,2}\\s?[A-ZА-Я]{0,20})?\\s?"
            + "(?<nppv>[A-ZА-Я]{4}\\s?\\-?\\s?\\d{4,6}\\s{0,}\\d{0,2}\\.?\\d{0,2}\\s?\\d{0,2}\\-?\\d{0,2}\\s?[A-ZА-Я]{0,20})?\\s?"
            + "(?<ippv>[A-ZА-Я]{4}\\s?[A-ZА-Я]{0,20}\\-?\\s?\\d{4,6}\\s{0,}\\d{0,2}\\.?\\d{0,2}\\s?\\d{0,2}\\-?\\d{0,2}\\s?)?\\s+"
            + "(?<sppv>[A-ZА-Я]{4}\\s?\\-?\\s?\\d{4,6}\\s{0,}\\d{0,2}\\.?\\d{0,2}\\s?\\d{0,2}\\-?\\d{0,2}\\s?)?(?<otstv>[A-ZА-Я]{0,20})?";

//    regexTHead
    final static String RTH = "";

//    regexTBody
    final static String RTB = "";

    @Override
    public HtmlTable processFile(String fileName) {
//        String str = null;
//        String f = null;
//        String f1 = "";
        Pattern pattern;
        Matcher matcher;
        boolean reading = false;
        boolean docHead = false;
        boolean tHead = false;
        boolean tBody = false;
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
//            f1 = TextReplace.getText(str);
//            f = TextReplace.getSha(f1);
//        } catch (IOException ex) {
//            Logger.getLogger(Spravka60Reader.class.getName()).log(Level.SEVERE, null, ex);
//            System.out.println("exception in Spravka60Reader : " + ex);
//        }
        String f = TextReplace.getSha(TextReplace.getText(fileName));
        HtmlTable result = new HtmlTable();

        pattern = Pattern.compile(RDH);
        matcher = pattern.matcher(f);

        boolean tableHeaderProcessed = false;

        while (matcher.find()) {
//            for (int i = 1; i <= matcher.groupCount(); i++) {
//                result.addCell(matcher.group(i));
//            }
            result.addCell(matcher.group("dh"));
            result.addCell("<b>" + plusToSpace(matcher.group("idx")) + "</b>");
            result.addCell("<br/><div class='smallH3'>" + delNull(matcher.group("pros")));
            result.addCell("<br/>" + delNull(matcher.group("napr")));
            result.addCell("<br/>" + delNull(matcher.group("nppv")));
            result.addCell("<br/>" + delNull(matcher.group("ippv")));
            result.addCell("<br/>" + delNull(matcher.group("sppv")));
            result.addCell(" " + delNull(matcher.group("otstv")) + "</div>");

            if (!tableHeaderProcessed) {
                tableHeaderProcessed = true;
                result.markCurrentRowAsDocHeader();
            }
            reading = true;
            docHead = true;
            result.advanceToNextRow();
        }

        System.out.println("docHead60 === " + docHead);
        if (reading == true && docHead == true) {
            System.out.println("can reading SPR60 ");
            if (forPopup == true) {
                ReadOnDir.spr = "sprPopup";
            } else {
                ReadOnDir.spr = "sprDefault";
            }
//            forPopup=false;

            return result;
        } else {
            System.out.println("can not reading SPR60 ");
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

    private String htmlParse(String s) {
        if (!"".equals(s)) {
            String[] str = s.split("\\s+");
            String s1 = str[0];
            String s2 = str[1];
            return "<b>" + s1 + " <div style='color:red'>" + s2 + "</div></b>";
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
