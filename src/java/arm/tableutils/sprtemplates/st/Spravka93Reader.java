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

public class Spravka93Reader implements TableReaderInterface {

//regexDocHead
    final static String RDH = "(?<dhvc>[A-ZА-Я]{2})\\s+(?<dhdor>[A-ZА-Я]{3,4})\\s+"
            + "(?<dhcode>93)\\s+"
            + "(?<dhdate>\\d{2}.\\d{2})\\s+"
            + "(?<dhtime>\\d{2}-\\d{2})\\s+"
            + "(?<dhvc73>[А-ЯA-Z]{2}\\s\\d{2})\\s+"
            + "(?<dhnpsst>[A-ZА-Я]{7}\\s[A-ZА-Я]{7}\\s[A-ZА-Я]{11}\\s[A-ZА-Я]{2}\\s[A-ZА-Я]{2}.)\\s+"
            + "(?<dhst>[А-ЯA-Z\\d+]{2,8})";

//    regexTHead
    final static String RTH = "(?<hnum>[А-ЯA-Z]{5})\\s+"
            + "(?<hidx>[А-ЯA-Z]{6})\\s+"
            + "(?<hstate>[А-ЯA-Z]{4})\\s+"
            + "(?<hdate>[А-ЯA-Z]{4})\\s+"
            + "(?<htime>[А-ЯA-Z]{5})\\s+"
            + "(?<hpark>[А-ЯA-Z]{4})\\s+"
            + "(?<hvag>[А-ЯA-Z]{3})\\s+"
            + "(?<hudl>[А-ЯA-Z]{3})\\s+"
            + "(?<hbrutt>[А-ЯA-Z]{5})";

//    regexTBody
    final static String RTB = "(?<bnum>\\d{4})\\s+"
            + "(?<bidx>\\d{4}\\s+\\d{2,3}\\s\\d{4})\\s+"
            + "(?<bstate>[А-ЯA-Z]{2,4})\\s+"
            + "(?<bdate>\\d{2}.\\d{2})\\s+"
            + "(?<btime>\\d{2}-\\d{2})\\s+"
            + "(?<bpark>\\d{2}\\/\\d{2})\\s+"
            + "(?<bvag>\\d{1,2})\\s+"
            + "(?<budl>\\d{1,2})\\s+"
            + "(?<bbrutt>\\d{2,4})";

    final HistoryInterface hi = new WriteToHist();

    @Override
    public HtmlTable processFile(String fileName, Users u) {
//        String str = null;
//        String f = null;
        Pattern pattern;
        Matcher matcher;
        boolean reading = false;
        boolean docHead = false;
        boolean tHead = false;
        boolean tBody = false;
        String doroga = "";

        String f = TextReplace.getText(fileName);
        pattern = Pattern.compile("\\s+93\\s+\\d{2}.\\d{2}\\s+");
        matcher = pattern.matcher(f);

        int count = 0;
        while (matcher.find()) {
            count++;
        }
        System.out.println("COUNT--->>> " + count);
        if (count == 1) {
            HtmlTable result = new HtmlTable();

            pattern = Pattern.compile(RDH);
            matcher = pattern.matcher(f);

            boolean tableHeaderProcessed = false;

            String obj = "";
            while (matcher.find()) {
                for (int i = 1; i <= matcher.groupCount(); i++) {
                    result.addCell(matcher.group(i));
                }

                obj = matcher.group("dhst");
                doroga = matcher.group("dhdor");

                if (!tableHeaderProcessed) {
                    tableHeaderProcessed = true;
                    result.markCurrentRowAsDocHeader();
                }
                docHead = true;
                result.advanceToNextRow();
//            break;
            }

            if (docHead == false) {
                System.out.println("fignya v 93 docHead!!!");
                return null;
            } else if (fromDB != true) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM HH:mm");
                Date currDate = new Date();
                History h = new History();
                h.setSprN("93");
                h.setDate("" + dateFormat.format(currDate));
                h.setTime("");
                h.setObj(obj);
                hi.infoFromSpr(h);
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
            if (tHead == false) {
                System.out.println("fignya v 93 tHead!!!");
                return null;
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
            if (tBody == false) {
                System.out.println("fignya v 93 tBody!!!");
                return null;
            }

            System.out.println("docHead93 === " + docHead);
            System.out.println("tHead93 === " + tHead);
            System.out.println("tBody93 === " + tBody);
            if (reading == true && (docHead == true && tHead == true && tBody == true)) {
                System.out.println("can reading SPR93 ");
                ReadOnDir.spr = "sprDefault";
                return result;
            } else {
                System.out.println("can not reading SPR93 ");
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public List<HtmlTable> readersResult() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
