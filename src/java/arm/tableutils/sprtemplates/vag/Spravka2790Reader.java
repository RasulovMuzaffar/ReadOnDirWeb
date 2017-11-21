package arm.tableutils.sprtemplates.vag;

import arm.ent.History;
import arm.ent.Station;
import arm.ent.Users;
import arm.tableutils.HtmlTable;
import arm.tableutils.tablereaders.TableReaderInterface;
import arm.tableutils.tablereaders.utils.TextReplace;
import arm.wr.HistoryInterface;
import arm.wr.ReadOnDir;
import static arm.wr.Write.fromDB;
import arm.wr.WriteToHist;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Spravka2790Reader implements TableReaderInterface {

//regexDocHead
//СТРОКА  1 : 21298948  DT-010117-279017
    final static String RDH = "\\d{1,2}\\s+\\:\\s+(?<nv>\\d{8})\\s+[A-ZА-Я]{2}\\-(?<s>\\d{6})\\-(?<po>\\d{6})";

//    RTH
    final static String RTH = "";

    final static String RTB = "((?<a>\\d{8})\\s+(?<b>\\d{2})\\s+"
            + "(?<c>[A-ZА-Я]?\\d{0,3})\\s+(?<d>\\d{4}\\s+)?"
            + "(?<e>\\d{5}\\s+)?(?<f>\\d{4}\\s+)?)?"
            + "(?<g>\\+?\\-?[A-ZА-Я]?\\s?[A-ZА-Я]{2,5})\\s+"
            + "(?<h>\\d{2}\\/\\d{2}\\/\\d{2})\\s+"
            + "(?<i>\\d{2}\\:\\d{2})(?<j>\\s{1}\\d{5}\\s)?"
            + "(?<k>\\s{0,7}\\d{2}\\/\\d{2}\\s)?"
            + "(?<l>\\s{0,13}\\d{4}\\s)?"
            + "(?<m>\\s{0,18}\\d{4}\\-\\d{3}\\-\\d{4}\\s)?"
            + "(?<n>\\s{0,32}\\d{1,2}\\/\\d{1,2}\\s)?"
            + "(?<o>\\s{0,38}\\d{8}\\-\\d{8})?";

    final HistoryInterface hi = new WriteToHist();

    @Override
    public HtmlTable processFile(String fileName, Users u) {
        String f = TextReplace.getSha(TextReplace.getText(fileName));
//        HtmlTable htResult = null;
        if (u.getTypeOgr().equalsIgnoreCase("singleArchive")) {
            return getResultToArch(f);
        } else {
            return getResult(f);
        }

//        return htResult;
    }

    public HtmlTable getResult(String f) {
        Pattern pattern;
        Matcher matcher;
        boolean reading = false;
        boolean docHead = false;
        boolean tHead = false;
        boolean tBody = false;

//        String f = TextReplace.getSha(TextReplace.getText(fileName));
        HtmlTable result = new HtmlTable();

        pattern = Pattern.compile(RDH);
        matcher = pattern.matcher(f);

        boolean tableHeaderProcessed = false;

        String sost = "";
        String obj = "";
        while (matcher.find()) {
            result.addCell("Вагон №:<b>" + matcher.group("nv") + "</b>,");
            result.addCell("период (с <b>" + parseToDate(matcher.group("s")) + "</b>");
            result.addCell("по <b>" + parseToDate(matcher.group("po")) + "</b>)");

//            sost = matcher.group("sost");
            obj = matcher.group("nv");
            if (!tableHeaderProcessed) {
                tableHeaderProcessed = true;
                result.markCurrentRowAsDocHeader();
            }

            docHead = true;
            result.advanceToNextRow();
            break;
        }
        if (docHead == false) {
            System.out.println("docHead == false");
            return null;
        } else if (fromDB != true) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM HH:mm");
            Date currDate = new Date();
            History h = new History();
            h.setSprN("2790 : " + obj);
            h.setDate("" + dateFormat.format(currDate));
            h.setTime("");
            h.setObj("");
            hi.infoFromSpr(h);
        }

//        pattern = Pattern.compile(RTH);
//        matcher = pattern.matcher(f);
        tableHeaderProcessed = false;

//        while (matcher.find()) {
        result.addCell("№ вагона");
        result.addCell("Сбст.");
        result.addCell("Тон.");
        result.addCell("Ст.н.");
        result.addCell("К.гр.");
        result.addCell("Получ.");
        result.addCell("Опер.");
        result.addCell("Дата");
        result.addCell("Время");
        result.addCell("Ст.опер.");
        result.addCell("П/П");
        result.addCell("НП");
        result.addCell("ИП");
        result.addCell("Р/кВ");
        result.addCell("Г/Х");

//            for (int i = 1; i <= matcher.groupCount(); i++) {
////                if (matcher.group(i) != null) {
//                result.addCell(matcher.group(i));
////                }
//            }
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
//            System.out.println(matcher.group("a") + " " + matcher.group("b") + " " + matcher.group("c") + " "
//                    + " " + matcher.group("d") + " " + " " + matcher.group("e") + " " + " " + matcher.group("f") + " "
//                    + " " + matcher.group("g") + " " + " " + matcher.group("h") + " " + " " + matcher.group("i") + " "
//                    + " " + matcher.group("j") + " " + " " + matcher.group("k") + " " + " " + matcher.group("l") + " "
//                    + " " + matcher.group("m") + " " + " " + matcher.group("n") + " " + " " + matcher.group("o") + " ");
//            if (matcher.group("nvag") != null) {
//                result.addCell("" + n++);
//            }else{
//                result.addCell("");
//            }
//            for (int i = 1; i <= matcher.groupCount(); i++) {
            if (matcher.group("a") != null) {
                result.addCell(delNull(matcher.group("a")));
                result.addCell(delNull(matcher.group("b")));
                result.addCell(delNull(matcher.group("c")));
                result.addCell(delNull(matcher.group("d")));
                result.addCell(delNull(matcher.group("e")));
                result.addCell(delNull(matcher.group("f")));
                result.addCell(delNull(matcher.group("g")));
                result.addCell(delNull(matcher.group("h")));
                result.addCell(delNull(matcher.group("i")));
                result.addCell(delNull(matcher.group("j")));
                result.addCell(delNull(matcher.group("k")));
                result.addCell(delNull(matcher.group("l")));
                result.addCell(delNull(matcher.group("m")));
                result.addCell(delNull(matcher.group("n")));
                result.addCell(delNull(matcher.group("o")));
                result.markCurrentRowAsRegularUnderscore();
            } else {
                result.addCell(delNull(matcher.group("a")));
                result.addCell(delNull(matcher.group("b")));
                result.addCell(delNull(matcher.group("c")));
                result.addCell(delNull(matcher.group("d")));
                result.addCell(delNull(matcher.group("e")));
                result.addCell(delNull(matcher.group("f")));
                result.addCell(delNull(matcher.group("g")));
                result.addCell(delNull(matcher.group("h")));
                result.addCell(delNull(matcher.group("i")));
                result.addCell(delNull(matcher.group("j")));
                result.addCell(delNull(matcher.group("k")));
                result.addCell(delNull(matcher.group("l")));
                result.addCell(delNull(matcher.group("m")));
                result.addCell(delNull(matcher.group("n")));
                result.addCell(delNull(matcher.group("o")));
            }
//            }

            if (!tableHeaderProcessed) {
                tableHeaderProcessed = true;
                result.markCurrentRowAsHeader();
            }
            result.advanceToNextRow();
        }
        reading = true;
        tBody = true;

        System.out.println("docHead2790 === " + docHead);
        System.out.println("tHead2790 === " + tHead);
        System.out.println("tBody2790 === " + tBody);
        if (reading == true && (docHead == true && tHead == true && tBody == true)) {
            System.out.println("can reading SPR2790 ");
            ReadOnDir.spr = "sprDefault";
            return result;
        } else {
            System.out.println("can not reading SPR2790 ");
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

    private String parseToDate(String s) {
        String outputText = "";
        try {
            SimpleDateFormat inputDateformat = new SimpleDateFormat("ddMMyy");
            SimpleDateFormat outputDateformat = new SimpleDateFormat("dd.MM.yyyy");
            Date date = inputDateformat.parse(s);
            outputText = outputDateformat.format(date);
        } catch (ParseException ex) {
            Logger.getLogger(Spravka2790Reader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return outputText;
    }

    @Override
    public List<HtmlTable> readersResult() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private HtmlTable getResultToArch(String f) {
        Map<String, String> ms = getStationsMap();
        Map<String, String> mg = getGruzsMap();
        System.out.println("STATIONS COUNT" + ms.size());
        System.out.println("GRUZS COUNT" + mg.size());
        Pattern pattern;
        Matcher matcher;
        boolean reading = false;
        boolean docHead = false;
        boolean tHead = false;
        boolean tBody = false;

//        String f = TextReplace.getSha(TextReplace.getText(fileName));
        HtmlTable result = new HtmlTable();

        pattern = Pattern.compile(RDH);
        matcher = pattern.matcher(f);

        boolean tableHeaderProcessed = false;

        String sost = "";
        String obj = "";
        while (matcher.find()) {
            result.addCell("Вагон №:<b>" + matcher.group("nv") + "</b>,");
            result.addCell("период (с <b>" + parseToDate(matcher.group("s")) + "</b>");
            result.addCell("по <b>" + parseToDate(matcher.group("po")) + "</b>)");

//            sost = matcher.group("sost");
            obj = matcher.group("nv");
            if (!tableHeaderProcessed) {
                tableHeaderProcessed = true;
                result.markCurrentRowAsDocHeader();
            }

            docHead = true;
            result.advanceToNextRow();
            break;
        }
        if (docHead == false) {
            System.out.println("docHead == false");
            return null;
        } else if (fromDB != true) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM HH:mm");
            Date currDate = new Date();
            History h = new History();
            h.setSprN("2790 : " + obj);
            h.setDate("" + dateFormat.format(currDate));
            h.setTime("");
            h.setObj("");
            hi.infoFromSpr(h);
        }

//        pattern = Pattern.compile(RTH);
//        matcher = pattern.matcher(f);
        tableHeaderProcessed = false;

//        while (matcher.find()) {
        result.addCell("№ вагона");
        result.addCell("Сбст.");
        result.addCell("Тон.");
        result.addCell("Ст.н.");
        result.addCell("Наим.Ст.");
        result.addCell("К.гр.");
        result.addCell("Наим.гр.");
        result.addCell("Получ.");
        result.addCell("Опер.");
        result.addCell("Дата");
        result.addCell("Время");
        result.addCell("Ст.опер.");
        result.addCell("П/П");
        result.addCell("НП");
        result.addCell("ИП");
        result.addCell("Р/кВ");
        result.addCell("Г/Х");

//            for (int i = 1; i <= matcher.groupCount(); i++) {
////                if (matcher.group(i) != null) {
//                result.addCell(matcher.group(i));
////                }
//            }
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
//            System.out.println(matcher.group("a") + " " + matcher.group("b") + " " + matcher.group("c") + " "
//                    + " " + matcher.group("d") + " " + " " + matcher.group("e") + " " + " " + matcher.group("f") + " "
//                    + " " + matcher.group("g") + " " + " " + matcher.group("h") + " " + " " + matcher.group("i") + " "
//                    + " " + matcher.group("j") + " " + " " + matcher.group("k") + " " + " " + matcher.group("l") + " "
//                    + " " + matcher.group("m") + " " + " " + matcher.group("n") + " " + " " + matcher.group("o") + " ");
//            if (matcher.group("nvag") != null) {
//                result.addCell("" + n++);
//            }else{
//                result.addCell("");
//            }
//            for (int i = 1; i <= matcher.groupCount(); i++) {
            if (matcher.group("a") != null) {
                result.addCell(delNull(matcher.group("a")));
                result.addCell(delNull(matcher.group("b")));
                result.addCell(delNull(matcher.group("c")));

                String kodSt = delNull(matcher.group("d"));
                result.addCell(kodSt);
                String nSt = "";
                for (Map.Entry<String, String> o : ms.entrySet()) {
//                    System.out.println("8888888888888888888888888>>>>>> " + o.getKey().substring(0, 4));
                    if (o.getKey().equals(kodSt)) {
                        nSt = o.getValue();
                        break;
                    } else if ((o.getKey().substring(0, 4)).equalsIgnoreCase(kodSt)) {
                        nSt = o.getValue();
                        break;
                    }
                }
                if (nSt.equalsIgnoreCase(null) || nSt.equalsIgnoreCase("")) {
                    result.addCell("");
                } else {
                    result.addCell(nSt);
                }
                

                String kodGr = delNull(matcher.group("e"));
                result.addCell(kodGr);
                String nGr = "";
                for (Map.Entry<String, String> o : mg.entrySet()) {
//                    System.out.println("8888888888888888888888888>>>>>> " + o.getKey().substring(0, 4));
                    if (o.getKey().equals(kodGr)) {
                        nGr = o.getValue();
                        break;
                    } else if ((o.getKey().substring(0, 4)).equalsIgnoreCase(kodGr)) {
                        nGr = o.getValue();
                        break;
                    }
                }
                if (nGr.equalsIgnoreCase(null) || nGr.equalsIgnoreCase("")) {
                    result.addCell("");
                } else {
                    result.addCell(nGr);
                }
                

                result.addCell(delNull(matcher.group("f")));
                result.addCell(delNull(matcher.group("g")));
                result.addCell(delNull(matcher.group("h")));
                result.addCell(delNull(matcher.group("i")));
                result.addCell(delNull(matcher.group("j")));
                result.addCell(delNull(matcher.group("k")));
                result.addCell(delNull(matcher.group("l")));
                result.addCell(delNull(matcher.group("m")));
                result.addCell(delNull(matcher.group("n")));
                result.addCell(delNull(matcher.group("o")));
                result.markCurrentRowAsRegularUnderscore();
            } else {
                result.addCell(delNull(matcher.group("a")));
                result.addCell(delNull(matcher.group("b")));
                result.addCell(delNull(matcher.group("c")));

                String kodSt = delNull(matcher.group("d"));
                result.addCell(kodSt);
                String nSt = "";
                for (Map.Entry<String, String> o : ms.entrySet()) {
//                    System.out.println("8888888888888888888888888>>>>>> " + o.getKey().substring(0, 4));
                    if (o.getKey().equals(kodSt)) {
                        nSt = o.getValue();
                        break;
                    } else if ((o.getKey().substring(0, 4)).equalsIgnoreCase(kodSt)) {
                        nSt = o.getValue();
                        break;
                    }
                }
                if (nSt.equalsIgnoreCase(null) || nSt.equalsIgnoreCase("")) {
                    result.addCell("");
                } else {
                    result.addCell(nSt);
                }
                
                String kodGr = delNull(matcher.group("e"));
                result.addCell(kodGr);
                String nGr = "";
                for (Map.Entry<String, String> o : mg.entrySet()) {
//                    System.out.println("8888888888888888888888888>>>>>> " + o.getKey().substring(0, 4));
                    if (o.getKey().equals(kodGr)) {
                        nGr = o.getValue();
                        break;
                    } else if ((o.getKey().substring(0, 4)).equalsIgnoreCase(kodGr)) {
                        nGr = o.getValue();
                        break;
                    }
                }
                if (nGr.equalsIgnoreCase(null) || nGr.equalsIgnoreCase("")) {
                    result.addCell("");
                } else {
                    result.addCell(nGr);
                }

                result.addCell(delNull(matcher.group("f")));
                result.addCell(delNull(matcher.group("g")));
                result.addCell(delNull(matcher.group("h")));
                result.addCell(delNull(matcher.group("i")));
                result.addCell(delNull(matcher.group("j")));
                result.addCell(delNull(matcher.group("k")));
                result.addCell(delNull(matcher.group("l")));
                result.addCell(delNull(matcher.group("m")));
                result.addCell(delNull(matcher.group("n")));
                result.addCell(delNull(matcher.group("o")));
            }
//            }

            if (!tableHeaderProcessed) {
                tableHeaderProcessed = true;
                result.markCurrentRowAsHeader();
            }
            result.advanceToNextRow();
        }
        reading = true;
        tBody = true;

        System.out.println("docHead2790 === " + docHead);
        System.out.println("tHead2790 === " + tHead);
        System.out.println("tBody2790 === " + tBody);
        if (reading == true && (docHead == true && tHead == true && tBody == true)) {
            System.out.println("can reading SPR2790 ");
            ReadOnDir.spr = "sprDefault";
            return result;
        } else {
            System.out.println("can not reading SPR2790 ");
            return null;
        }
    }

    private static final String URL = "jdbc:mysql://localhost:3306/arm";
    private static final String USER = "test";
    private static final String PASS = "test";

    private Map<String, String> getStationsMap() {
        System.out.println("============================== getStationsMap");

        Station s = null;
        Map<String, String> m = new HashMap<>();
        try {
            String sql = "select * from spr_stations";
            try (Connection con = (Connection) DriverManager.getConnection(URL, USER, PASS);
                    PreparedStatement pstmt = con.prepareStatement(sql);
                    ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
//                    s = new Station(rs.getLong("id"), rs.getString("code_station"), rs.getString("name_station"));
                    m.put(rs.getString("code_station"), rs.getString("name_station"));
                }
            }

        } catch (SQLException ex) {
            System.out.println("exexexexex " + ex);
            Logger.getLogger(Spravka2790Reader.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (Map.Entry<String, String> entry : m.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (key.equals("73390")) {
                System.out.println(key + " : " + value);
            } else if (key.equals("7339")) {
                System.out.println(key + " : " + value);
            }
        }
        return m;
    }

    private Map<String, String> getGruzsMap() {
        System.out.println("============================== getGruzsMap");
        Map<String, String> m = new HashMap<>();
        try {
            String sql = "select * from spr_gruz";
            try (Connection con = (Connection) DriverManager.getConnection(URL, USER, PASS);
                    PreparedStatement pstmt = con.prepareStatement(sql);
                    ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
//                    s = new Station(rs.getLong("id"), rs.getString("code_station"), rs.getString("name_station"));
                    m.put(rs.getString("code_gruz"), rs.getString("name_gruz"));
                }
            }

        } catch (SQLException ex) {
            System.out.println("exexexexex " + ex);
            Logger.getLogger(Spravka2790Reader.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (Map.Entry<String, String> entry : m.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (key.equals("28114")) {
                System.out.println(key + " : " + value);
            } else if (key.equals("28114")) {
                System.out.println(key + " : " + value);
            }
        }
        return m;
    }
}
