package arm.tableutils.sprtemplates.st;

import arm.ent.History;
import arm.ent.Users;
import arm.tableutils.HtmlTable;
import arm.tableutils.tablereaders.TableReaderInterface;
import arm.tableutils.tablereaders.utils.TextReplace;
import arm.wr.HistoryInterface;
import arm.wr.ReadOnDir;
import static arm.wr.Write.fromDB;
import arm.wr.WriteToHist;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Spravka64Reader implements TableReaderInterface {

//    regexDocHead
    final static String RDH = "([A-ZА-Я]{2}\\s+[A-ZА-Я]{2,4})\\s+"
            + "(?<spr>64)\\s+(?<date>\\d{2}.\\d{2})\\s+(?<time>\\d{2}\\-\\d{2})\\s+"
            + "([A-ZА-Я]{2}\\s\\d{2})\\s+([A-ZА-Я]{6})\\s+([A-ZА-Я]{7})\\s+"
            + "(?<st>[A-ZА-Я]{1,6}\\-?\\.?[A-ZА-Я]{0,4}\\.?\\d{0,2}\\.?)\\s+"
            + "([A-ZА-Я]{1}\\s[A-ZА-Я]{8})";

//    regexTHead
    final static String RTH = "(?<thnum>[A-ZА-Я]{5})\\s+"
            + "(?<thidx>[A-ZА-Я]{6})\\s+"
            + "(?<thstate>[A-ZА-Я]{4})\\s+"
            + "(?<thst>[A-ZА-Я]{4})\\s+"
            + "(?<thdate>[A-ZА-Я]{4})\\s+"
            + "(?<thtime>[A-ZА-Я]{5})";

//    regexTBody
    final static String RTB = "(?<tbnp>\\d{4})\\s+(?<tbidx>\\d{4}\\+\\d{3}\\+\\d{4})\\s((?<tbstate1>[A-ZА-Я]{3,4}\\-?\\d{0,2})\\s+(?<tbtime1>\\d{2}\\-\\d{2}))?\\s+((?<tbstate2>[A-ZА-Я]{3,4}\\-?\\d{0,2})\\s+(?<tbtime2>\\d{2}\\-{1}\\d{2}))?\\s{0,14}((?<tbxz1>\\d{1}\\/\\d{2})\\s(?<tbxz2>\\d{4})\\s(?<tbxz3>\\d{0,3}))?(\\s(?<tbxz4>[A-ZА-Я]?\\d?)\\s{3,7}(?<tbxz5>\\d{1,4}))?(\\s{11}(?<tbxz6>\\d{4}))?";

    final HistoryInterface hi = new WriteToHist();

    @Override
    public HtmlTable processFile(String fileName, Users u) {
        Pattern pattern;
        Matcher matcher;
        boolean reading = false;
        boolean docHead = false;
        boolean tHead = false;
        boolean tBody = false;

        String f = (TextReplace.getText(fileName)).replace("\r\n\r\n", "\r\n");
        HtmlTable result = new HtmlTable();

        pattern = Pattern.compile(RDH);
        matcher = pattern.matcher(f);

        boolean tableHeaderProcessed = false;

        String obj = "";
        while (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                result.addCell(matcher.group(i));
            }

            if ("С-АГЧ".equals(matcher.group("st"))) {
                obj = "САРЫ-АГАЧ";
            } else {
                obj = matcher.group("st");
            }

            if (!tableHeaderProcessed) {
                tableHeaderProcessed = true;
                result.markCurrentRowAsDocHeader();
            }

            docHead = true;
            result.advanceToNextRow();
        }
        if (docHead == false) {
            return null;
        } else if (fromDB != true) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM HH:mm");
            Date currDate = new Date();
            History h = new History();
            h.setSprN("64");
            h.setDate("" + dateFormat.format(currDate));
            h.setTime("");
            h.setObj(obj);
            hi.infoFromSpr(h);
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
            result.addCell("п/п");
            result.addCell("брутта");
            result.addCell("кв");
            result.addCell("псл");
            result.addCell("нл");
            result.addCell("нп");

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


            if (!tableHeaderProcessed) {
                tableHeaderProcessed = true;
                result.markCurrentRowAsHeader();
            }
            reading = true;
            tBody = true;
            result.advanceToNextRow();
        }

        System.out.println("docHead64 === " + docHead);
        System.out.println("tHead64 === " + tHead);
        System.out.println("tBody64 === " + tBody);
        if (reading == true && (docHead == true && tHead == true && tBody
                == true)) {
            System.out.println("can reading SPR64 ");
            ReadOnDir.spr = "sprDefault";
            return result;
        } else {
            System.out.println("can not reading SPR64 ");
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
