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

public class Spravka216Reader implements TableReaderInterface {

//    regexDocHead
    final static String RDH = "(?<sav>[A-ZА-Я]{6}\\s[A-ZА-Я]{12}\\s[A-ZА-Я]{7})\\s+"
            + "\\:\\s+(?<idx>\\d{4,6}\\s+\\d{1,3}\\s+\\d{4,6})";

//    regexTHead
    final static String RTH = "(?<thnv>[A-ZА-Яa-zа-я]{5})\\s{0,}\\:\\s{0,}"
            + "(?<thva>[A-ZА-Яa-zа-я]{3})\\s{0,}\\:\\s{0,}"
            + "(?<tha>[A-ZА-Яa-zа-я]{6})\\/\\s{0,}\\:\\s{0,}"
            + "(?<thdates>[A-ZА-Яa-zа-я]{4})\\s{0,}\\:\\s{0,}"
            + "(?<thdatee>[A-ZА-Яa-zа-я]{4})\\s{0,}\\:\\s{0,}"
            + "(?<thtlgrm>[A-ZА-Яa-zа-я]{10})\\s{0,}\\:\\s{0,}"
            + "(?<thst>[A-ZА-Яa-zа-я]{7})\\s{0,}\\:\\s{0,}"
            + "(?<tharend>[A-ZА-Яa-zа-я]{9})";

//    regexTBody
    final static String RTB = "(?<tdnv>\\d{8})\\s{0,}\\:\\s{0,}"
            + "(?<tdva>\\d{4})\\s{0,}\\:\\s{0,}"
            + "(?<tda>[A-ZА-Я]{6})\\s{0,}\\:\\s{0,}"
            + "(?<tddates>\\d{2}\\/\\d{2}\\/\\d{2})\\s{0,}\\:\\s{0,}"
            + "(?<tddatee>[A-ZА-Яa-zа-я]{0,12}\\d{0,2}\\/?\\d{0,2}\\/?\\d{0,2})\\s{0,}\\:\\s{0,}"
            + "(?<tdtlgrm>\\d{2,6})\\s{0,}\\:\\s{0,}"
            + "(?<tdst>\\d{4,6})\\s{0,}\\:\\s{0,}"
            + "(?<tdarend>[A-ZА-Я]{1,4}\\s+[A-ZА-Я]{2,5}\\s+[A-ZА-Я]{0,10})";

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
            Logger.getLogger(Spravka216Reader.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("exception in Spravka216Reader : " + ex);
        }

        HtmlTable result = new HtmlTable();

        pattern = Pattern.compile(RDH);
        matcher = pattern.matcher(f);

        boolean tableHeaderProcessed = false;

        while (matcher.find()) {
//            for (int i = 1; i <= matcher.groupCount(); i++) {
//                result.addCell(matcher.group(i));
//            }
            result.addCell(matcher.group("sav")+": ");
            result.addCell("<b>" + matcher.group("idx") + "</b>");

            if (!tableHeaderProcessed) {
                tableHeaderProcessed = true;
                result.markCurrentRowAsDocHeader();
            }

            docHead = true;
            result.advanceToNextRow();
        }

//        pattern = Pattern.compile(RTH);
//        matcher = pattern.matcher(f);
        tableHeaderProcessed = false;

//        while (matcher.find()) {

            result.addCell("№");            
            result.addCell("Номер вагона");
            result.addCell("Вид аренды");
            result.addCell("Аренда/Собств.");
            result.addCell("Дата начала");
            result.addCell("Дата конца");
            result.addCell("Телеграмма согласования");
            result.addCell("Станция приписки");
            result.addCell("Арендатор или собственник");

            if (!tableHeaderProcessed) {
                tableHeaderProcessed = true;
                result.markCurrentRowAsHeader();
            }

            tHead = true;
            result.advanceToNextRow();
//        }

        pattern = Pattern.compile(RTB);
        matcher = pattern.matcher(f);

        int n = 1;
        while (matcher.find()) {
            result.addCell("" + n++);
            for (int i = 1; i <= matcher.groupCount(); i++) {
                result.addCell(matcher.group(i));
            }

            if (!tableHeaderProcessed) {
                tableHeaderProcessed = true;
                result.markCurrentRowAsHeader();
            }
            reading = true;
            tBody = true;
            result.advanceToNextRow();
        }

        System.out.println("docHead216 === " + docHead);
        System.out.println("tHead216 === " + tHead);
        System.out.println("tBody216 === " + tBody);
        if (reading == true && (docHead == true && tHead == true && tBody == true)) {
            System.out.println("can reading SPR216 ");
            if (forPopup == true) {
                ReadOnDir.spr = "sprPopup";
            } else {
                ReadOnDir.spr = "sprDefault";
            }
            forPopup=false;
            return result;
        } else {
            System.out.println("can not reading SPR216 " );
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
