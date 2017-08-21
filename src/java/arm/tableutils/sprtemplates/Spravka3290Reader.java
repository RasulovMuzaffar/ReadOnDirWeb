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

public class Spravka3290Reader implements TableReaderInterface {

    final static String regexDocHead = "(?<dhvc>[A-ZА-Я]{2})\\s+"
            + "(?<dhdor>[A-ZА-Я]{3})\\s+"
            + "(?<dhcode>3290)\\s+"
            + "(?<dhdate>\\d{2}.\\d{2})\\s+"
            + "(?<dhtime>\\d{2}-\\d{2})\\s+"
            + "(?<dhvc73>[А-ЯA-Z]{2}\\s\\d{2})\\s+"
            + "(?<dhpvps>[A-ZА-Я]{8}\\s[A-ZА-Я]{7}\\s[A-ZА-Я]{2}\\s[A-ZА-Я]{5}\\s\\([A-ZА-Я]{2}\\s[A-ZА-Я]{3}\\s[A-ZА-Я]{1}\\s[A-ZА-Я]{13}\\))\\s+"
            + "(?<dhst>[A-ZА-Я]{0,6}\\-?\\.?[A-ZА-Я]{0,5}\\d{0,2}\\.?)\\s+"
            + "(?<dhrp>[A-ZА-Я]{1,4})\\s+"
            + "(?<dhper>[A-ZА-Я]{6}\\:)"
            + "(?<dhperiod>\\d{2}\\-\\d{2}\\s+[A-ZА-Я]{3}\\.)";

    final static String regexTHead = "(?<thtype>[A-ZА-Я]{4,7})\\:\\s+(?<thsbst>[A-ZА-Я]{6}\\.)\\s+\\:\\s+"
            + "(?<thvsg>[A-ZА-Я]{3})\\s+(?<thkr>[A-ZА-Я]{2})\\s+"
            + "(?<thpl>[A-ZА-Я]{2})\\s+(?<thpv>[A-ZА-Я]{2})\\s+"
            + "(?<thcs>[A-ZА-Я]{2})\\s+(?<thrf>[A-ZА-Я]{2})\\s+"
            + "(?<thpr>[A-ZА-Я]{2})\\s+(?<thcmv>[A-ZА-Я]{3})\\s+"
            + "(?<th94>\\d{2})\\s+(?<thzvg>[A-ZА-Я]{3})\\s+"
            + "(?<thftg>[A-ZА-Я]{3})\\s+(?<thmvz>[A-ZА-Я]{3})";

//    final static String regexTBody = "(?<tdvcggrzvg>[A-ZА-Я]{2,3}\\s[A-ZА-Я]{0,3}\\.?[A-ZА-Я]{0,2})\\s{0,}\\:\\s{1,4}"
//            + "(?<tdvsg>\\d{1,3})?\\s{1,4}(?<tdkr>\\d{1,3})?\\s{1,4}"
//            + "(?<tdpl>\\d{1,3})?\\s{1,4}(?<tdpv>\\d{1,3})?\\s{1,4}"
//            + "(?<tdcs>\\d{1,3})?\\s{1,4}(?<tdrf>\\d{1,3})?\\s{1,4}"
//            + "(?<tdpr>\\d{1,3})?\\s{1,4}(?<tdcmv>\\d{1,3})?\\s{1,4}"
//            + "(?<td94>\\d{1,3})?\\s{1,4}(?<tdzrv>\\d{1,3})?\\s{1,4}"
//            + "(?<tdftg>\\d{1,3})?\\s{1,4}(?<tdmvz>\\d{1,3})?";
    final static String regexTBody = "(?<tdvcggrzvg>[A-ZА-Я]{3,5}\\s[A-ZА-Я]{0,3}\\s?[A-ZА-Я]{0,5}\\.?[A-ZА-Я]{0,3})\\s{0,}\\:(\\s{1,5}(?<tdvsg>\\d{1,3})?\\s{1,5}(?<tdkr>\\d{1,3})?\\s{1,5}(?<tdpl>\\d{1,3})?\\s{1,5}(?<tdpv>\\d{1,3})?\\s{1,5}(?<tdcs>\\d{1,3})?\\s{1,5}(?<tdrf>\\d{1,3})?\\s{1,5}(?<tdpr>\\d{1,3})?\\s{1,4}(?<tdcmv>\\d{1,3})?\\s{1,4}(?<td94>\\d{1,3})?\\s{1,4}(?<tdzrv>\\d{1,3})?\\s{1,4}(?<tdftg>\\d{1,3})?\\s{1,4}(?<tdmvz>\\d{1,3})?)?";

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
        String park = "";
        String period = "";
        String type = "";

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
            Logger.getLogger(Spravka3290Reader.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("exception in Spravka3290Reader : " + ex);
        }

        HtmlTable result = new HtmlTable();

        pattern = Pattern.compile(regexDocHead);
        matcher = pattern.matcher(f);

        boolean tableHeaderProcessed = false;

        while (matcher.find()) {
            result.addCell(matcher.group("dhvc"));
            result.addCell(matcher.group("dhdor"));
            result.addCell(matcher.group("dhcode"));
            result.addCell(matcher.group("dhdate"));
            result.addCell(matcher.group("dhtime"));
            result.addCell(matcher.group("dhvc73"));
            result.addCell(matcher.group("dhpvps"));
            result.addCell("<br/><b>" + matcher.group("dhst") + "</b>");

            park = matcher.group("dhrp");

            result.addCell(matcher.group("dhper"));
            result.addCell(matcher.group("dhperiod"));

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

//            result.addCell("№");
            result.addCell("ПАРК");
//            result.addCell("ПЕРИОД");
            result.addCell("ТИП");

            /*(?<thtype>[A-ZА-Я]{4,7})\\:\\s+(?<thsbst>[A-ZА-Я]{6}\\.)\\s+\\:\\s+"
            + "(?<thvsg>[A-ZА-Я]{3})\\s+(?<thkr>[A-ZА-Я]{2})\\s+"
            + "(?<thpl>[A-ZА-Я]{2})\\s+(?<thpv>[A-ZА-Я]{2})\\s+"
            + "(?<thcs>[A-ZА-Я]{2})\\s+(?<thrf>[A-ZА-Я]{2})\\s+"
            + "(?<thpr>[A-ZА-Я]{2})\\s+(?<thcmv>[A-ZА-Я]{3})\\s+"
            + "(?<th94>\\d{2})\\s+(?<thzvg>[A-ZА-Я]{3})\\s+"
            + "(?<thftg>[A-ZА-Я]{3})\\s+(?<thmvz>[A-ZА-Я]{3})*/
//            result.addCell(matcher.group("thsbst"));
//            result.addCell(matcher.group("thvsg"));
//            result.addCell(matcher.group("thkr"));
//            result.addCell(matcher.group("thpl"));
//            result.addCell(matcher.group("thpv"));
//            result.addCell(matcher.group("thcs"));
//            result.addCell(matcher.group("thsbst"));
//            result.addCell(matcher.group("thsbst"));
//            result.addCell(matcher.group("thsbst"));
//            result.addCell(matcher.group("thsbst"));
//            result.addCell(matcher.group("thsbst"));
//            result.addCell(matcher.group("thsbst"));
//            result.addCell(matcher.group("thsbst"));
            for (int i = 2; i <= matcher.groupCount(); i++) {
                result.addCell(matcher.group(i));
            }

            if (!tableHeaderProcessed) {
                tableHeaderProcessed = true;
                result.markCurrentRowAsHeader();
            }

            tHead = true;
            result.advanceToNextRow();
            break;
        }

        pattern = Pattern.compile(regexTBody);
        matcher = pattern.matcher(f);

        int tip = 1;
        int prk = 1;
        while (matcher.find()) {

            if ("ВСГ ГРЗ.ВГ".equals(matcher.group("tdvcggrzvg"))) {
                if (tip==1 || tip==2) {
                    park="<b>РП</b>";
                }else{
                    park="<b>НРП</b>";
                }
                if (tip % 2 != 0) {
                    type = "<b>СДАЧА</b>";
                }else{
                    type = "<b>ПРИЕМ</b>";
                }
                
                result.addCell(park);
//                result.addCell("<b>" + period + "</b>");
                result.addCell(type);
                result.addCell("<b>" + delNull(matcher.group("tdvcggrzvg")) + "</b>");
                result.addCell(delNull(matcher.group("tdvsg")));
                result.addCell(delNull(matcher.group("tdkr")));
                result.addCell(delNull(matcher.group("tdpl")));
                result.addCell(delNull(matcher.group("tdpv")));
                result.addCell(delNull(matcher.group("tdcs")));
                result.addCell(delNull(matcher.group("tdrf")));
                result.addCell(delNull(matcher.group("tdpr")));
                result.addCell(delNull(matcher.group("tdcmv")));
                result.addCell(delNull(matcher.group("td94")));
                result.addCell(delNull(matcher.group("tdzrv")));
                result.addCell(delNull(matcher.group("tdftg")));
                result.addCell(delNull(matcher.group("tdmvz")));

                result.markCurrentRowAsRegularUnderlining();
            } else {
                result.addCell("");
//                result.addCell("");
                result.addCell("");
                if ("НИХ\r\nСОБСТ.ВАГ".equals(matcher.group("tdvcggrzvg"))) {
                    result.addCell("<b>ИЗ " + delNull(matcher.group("tdvcggrzvg")) + "</b>");
                } else {
                    result.addCell(delNull(matcher.group("tdvcggrzvg")));
                }
                result.addCell(delNull(matcher.group("tdvsg")));
                result.addCell(delNull(matcher.group("tdkr")));
                result.addCell(delNull(matcher.group("tdpl")));
                result.addCell(delNull(matcher.group("tdpv")));
                result.addCell(delNull(matcher.group("tdcs")));
                result.addCell(delNull(matcher.group("tdrf")));
                result.addCell(delNull(matcher.group("tdpr")));
                result.addCell(delNull(matcher.group("tdcmv")));
                result.addCell(delNull(matcher.group("td94")));
                result.addCell(delNull(matcher.group("tdzrv")));
                result.addCell(delNull(matcher.group("tdftg")));
                result.addCell(delNull(matcher.group("tdmvz")));
            }
            tip++;

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
            System.out.println("can reading SPR3290 " + result);
            ReadOnDir.spr = "sprDefault";
            return result;
        } else {
            System.out.println("can not reading SPR3290 " + result);
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
