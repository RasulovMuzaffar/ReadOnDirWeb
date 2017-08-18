
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

public class Spravka902Reader implements TableReaderInterface {

    final static String regexDocHead = "(?<dcode>\\d{3})\\s+"
            + "(?<dst>\\d{4,5})\\s+"
            + "(?<dnp>\\d{4})\\s+"
            + "(?<dindx>\\d{4}\\s\\d{2,3}\\s\\d{4})\\s+"
            + "(?<dediz>\\d{1})\\s+"
            + "(?<ddate>\\d{2} \\d{2})\\s+"
            + "(?<dtime>\\d{2} \\d{2})\\s+"
            + "(?<duddl>\\d{2,5})\\s+"
            + "(?<dxz1>\\d{4,5})\\s+"
            + "(?<dxz2>\\d \\d{4} \\d \\d)";

    final static String regexTHead = "(?<hnum>[А-ЯA-Z]{5})\\s+"
            + "(?<hidx>[А-ЯA-Z]{6})\\s+"
            + "(?<hstate>[А-ЯA-Z]{4})\\s+"
            + "(?<hst>[А-ЯA-Z]{4})\\s+"
            + "(?<hdate>[А-ЯA-Z]{4})\\s+"
            + "(?<htime>[А-ЯA-Z]{5})";

    final static String regexTBody = "(?<thnum>\\d{2})\\s+"
            + "(?<thnvag>\\d{8})\\s+"
            + "(?<thediz>\\d{4})\\s+"
            + "(?<thvesgr>\\d{3})\\s+"
            + "(?<thstnaz>\\d{5})\\s+"
            + "(?<thcodgr>\\d{5})\\s+"
            + "(?<thcodpoluch>\\d{4,6})\\s+"
            + "(?<thxz1>\\d \\d \\d \\d)\\s+"
            + "(?<thkont>\\d{2}\\/\\d{2})\\s+"
            + "(?<thxz2>\\d{5})\\s+"
            + "(?<thoxr>\\d{3}\\W\\S{0,6} {0,6})";

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

            str = new String(new String(buffer, "CP1251").getBytes(), "CP866");

            f = TextReplace.getText(str);

        } catch (IOException ex) {
            Logger.getLogger(Spravka93Reader.class.getName()).log(Level.SEVERE, null, ex);
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

        {
            tableHeaderProcessed = false;

            result.addCell("№");
            result.addCell("№ вагона");
            result.addCell("ед.изм");
            result.addCell("вес груза");
            result.addCell("ст. назнач");
            result.addCell("код груза");
            result.addCell("код получателя");
            result.addCell("хз");
            result.addCell("контейнер (гр/пор)");
            result.addCell("хз");
            result.addCell("охраняемый");

            if (!tableHeaderProcessed) {
                tableHeaderProcessed = true;
                result.markCurrentRowAsHeader();
            }

            tHead = true;
            result.advanceToNextRow();
        }

        pattern = Pattern.compile(regexTBody);
        matcher = pattern.matcher(f);

        while (matcher.find()) {
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
        
        System.out.println("docHead902 === " + docHead);
        System.out.println("tHead902 === " + tHead);
        System.out.println("tBody902 === " + tBody);
        if (reading == true && (docHead == true && tHead == true && tBody == true)) {
            System.out.println("can reading SPR902 " + result);
            ReadOnDir.spr = "sprPopup";
            return result;
        } else {
            System.out.println("can not reading SPR902 " + result);
            return null;
        }

    }

    @Override
    public List<HtmlTable> readersResult() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
