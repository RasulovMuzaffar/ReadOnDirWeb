package arm.tableutils;

import java.util.LinkedList;
import java.util.List;

public class HtmlTable {

    public void advanceToNextRow() {
        currentRow = new TableRow();
        tableData.add(currentRow);
    }

    public void markCurrentRowAsHeader() {
        currentRow.type = RowType.Header;
    }

    public void markCurrentRowAsDocHeader() {
        currentRow.type = RowType.DocHeader;
    }

    public void addCell(String value) {
        if (currentRow == null) {
            advanceToNextRow();
        }

        currentRow.cells.add(value);
    }

    public String generateHtmlTable() {
        String result = "";//HTML_OPEN + BODY_OPEN;
        result += TABLE_OPEN;
        for (TableRow row : tableData) {
            if (!row.cells.isEmpty() && !row.type.name().equals("DocHeader")) {
                //////////////////////////////////
//                result += ROW_OPEN;
//                for (String value : row.cells) {
//                    if (row.type == RowType.Regular) {
//                        result += CELL_OPEN;
//                        result += value;
//                        result += CELL_CLOSE;
//                    } else if (row.type == RowType.Header) {
//                        result += HEADER_CELL_OPEN;
//                        result += value;
//                        result += HEADER_CELL_CLOSE;
//                    }
//                }
//                result += ROW_CLOSE;
                //////////////////////////////////
                if (row.type.name().equals("Header")) {
                    result += (THEAD_OPEN + ROW_OPEN);
                    for (String value : row.cells) {
                        if (row.type == RowType.Header) {
                            result += HEADER_CELL_OPEN;
                            result += value;
                            result += HEADER_CELL_CLOSE;
                        }
                    }
                    result += (ROW_CLOSE + THEAD_CLOSE);
                }
                if (row.type.name().equals("Regular")) {
                    result += ROW_OPEN;
                    for (String value : row.cells) {
                        if (row.type == RowType.Regular) {
                            result += CELL_OPEN;
                            result += value;
                            result += CELL_CLOSE;
                        }
                    }
                    result += ROW_CLOSE;
                }
            }
        }
        result += TABLE_CLOSE;
//        result += (TABLE_CLOSE + BODY_CLOSE + HTML_CLOSE);
        return result;
    }

    public String generateHtml() {
//        String result = HTML_OPEN + BODY_OPEN;
        String result = LABELDOC_OPEN;
        for (TableRow row : tableData) {
            for (String value : row.cells) {
                if (row.type == RowType.DocHeader) {
                    result += value + " ";
                }
            }
        }
        result += LABELDOC_CLOSE;
        result += DIV_TABL_OPEN;
        result += "\n";
        result += generateHtmlTable();
        result += DIV_TABL_CLOSE;
//        result += (BODY_CLOSE + HTML_CLOSE);
        return result;
    }

    private List<TableRow> tableData = new LinkedList<>();
    private TableRow currentRow = null;
    private static final String HTML_OPEN = "<html>\n";
    private static final String HTML_CLOSE = "</html>";
    private static final String BODY_OPEN = "<body>\n";
    private static final String BODY_CLOSE = "</body>\n";
    private static final String TABLE_OPEN = "<table class=\"mytable\" cellspacing=\"0\">\n";
    private static final String TABLE_CLOSE = "</table>\n";
    private static final String HEADER_CELL_OPEN = "<th>";
    private static final String HEADER_CELL_CLOSE = "</th>";
    private static final String ROW_OPEN = "<tr>";
    private static final String ROW_CLOSE = "</tr>\n";
    private static final String CELL_OPEN = "<td>";
    private static final String CELL_CLOSE = "</td>";
    private static final String LABELDOC_OPEN = "<label><h3>";
    private static final String LABELDOC_CLOSE = "</h3></label>";
    private static final String DIV_TABL_OPEN = "<div class=\"col-md-12 tabl\">";
    private static final String DIV_TABL_CLOSE = "</div>";
    private static final String THEAD_OPEN = "<thead>";
    private static final String THEAD_CLOSE = "</thead>";
    private static final String TBODY_OPEN = "<tbody>";
    private static final String TBODY_CLOSE = "</tbody>";
}

enum RowType {
    DocHeader,
    Header,
    Regular
}

class TableRow {

    public List<String> cells = new LinkedList<>();
    public RowType type = RowType.Regular;
}
