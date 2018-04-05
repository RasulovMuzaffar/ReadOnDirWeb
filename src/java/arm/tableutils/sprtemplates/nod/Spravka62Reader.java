package arm.tableutils.sprtemplates.nod;

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

public class Spravka62Reader implements TableReaderInterface {

//    regexDocHead
	final static String RDH = "(?<dhdor>\\S{2,3}\\s+\\S{2,4})\\s+(?<spr>62)\\s+(?<date>\\d{2}.\\d{2})\\s+(?<time>\\d{2}-\\d{2})\\s+(?<brp>\\S+\\s+\\S+)";

//    regexTHead
	final static String RTH = "";

//    regexTBody
	final static String RTB = "((?<nod>\\S{3}-\\d{2})\\s+)?(?<pzd>\\d{4})\\s?\\((?<idx>\\d{4}\\+\\s?\\d{2,3}\\+\\d{4})\\)\\s+(?<st>\\S{3,7})\\s+(?<data>\\d{2}.\\d{2})\\s+(?<time>\\d{2}-\\d{2})\\s+(?<xz1>\\d{2})\\s+(?<xz2>\\d{4})\\s+(?<xz3>\\d{3}\\/\\d{3})\\s{0,5}(?<xz4>\\S?)";

	final HistoryInterface hi = new WriteToHist();

	@Override
	public HtmlTable processFile(String fileName, Users u) {

		String f = TextReplace.getSha(TextReplace.getText(fileName));
		return getResult(f);
	}

	private HtmlTable getResult(String str) {
		Pattern pattern;
		Matcher matcher;
		boolean reading = false;
		boolean docHead = false;
		boolean tHead = false;
		boolean tBody = false;
		String doroga = "";

		HtmlTable result = new HtmlTable();

		pattern = Pattern.compile(RDH);
		matcher = pattern.matcher(str);

		boolean tableHeaderProcessed = false;

		String obj = "";
		while (matcher.find()) {
			for (int i = 1; i <= matcher.groupCount(); i++) {
				result.addCell(matcher.group(i));

			}

			doroga = matcher.group("dhdor");

			obj = "NOD";

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
			h.setSprN("62");
			h.setDate("" + dateFormat.format(currDate));
			h.setTime("");
			h.setObj(obj);
			hi.infoFromSpr(h);
		}

//        tableHeaderProcessed = false;
		{
			result.addCell("НОД");
			result.addCell("№ поезда");
			result.addCell("Индекс поезда");
			result.addCell("Станция");
			result.addCell("Дата");
			result.addCell("Время");
			result.addCell("усд");
			result.addCell("брт");
			result.addCell("осей");
			result.addCell("прим.");

//            if (!tableHeaderProcessed) {
//                tableHeaderProcessed = true;
			result.markCurrentRowAsHeader();
//            }

			tHead = true;
			result.advanceToNextRow();
		}

		pattern = Pattern.compile(RTB);
		matcher = pattern.matcher(str);

		while (matcher.find()) {
			 String column1 = delNull(htmlParse(matcher.group("nod")));
			// String column2 = delNull(htmlParse(matcher.group("tbst")));
			// result.addCell(column2);
			result.addCell(column1);
			result.addCell(matcher.group("pzd"));
			result.addCell(matcher.group("idx"));
			result.addCell(matcher.group("st"));
			result.addCell(matcher.group("data"));
			result.addCell(matcher.group("time"));
			result.addCell(matcher.group("xz1"));
			result.addCell(matcher.group("xz2"));
			result.addCell(matcher.group("xz3"));
			result.addCell(matcher.group("xz4"));

			if (!tableHeaderProcessed) {
				tableHeaderProcessed = true;
				result.markCurrentRowAsHeader();
			}

			if (!column1.equals("")) {
				result.markCurrentRowAsRegularUnderscore();
			}
			reading = true;
			tBody = true;
			result.advanceToNextRow();
		}

		if (tBody == false) {
			System.out.println("fignya v 62 tBody!!!");
			return null;
		}
		System.out.println("docHead62 === " + docHead);
		System.out.println("tHead62 === " + tHead);
		System.out.println("tBody62 === " + tBody);
		if (reading == true && (docHead == true && tHead == true && tBody == true)) {
			System.out.println("can reading SPR62 ");
			ReadOnDir.spr = "sprDefault";
			return result;
		} else {
			System.out.println("can not reading SPR62 ");
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
		if (s != null) {
			return "<b>" + s + "</b>";
		} else {
			return null;
		}
	}

	@Override
	public List<HtmlTable> readersResult() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
