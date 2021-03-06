package arm.tableutils;

import java.util.LinkedList;
import java.util.List;

public class HtmlTable {

    public void advanceToNextTable() {
        currentTable = new Table();
        tables.add(currentTable);
    }

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

    public void markCurrentRowAsRegularUnderlining() {
        currentRow.type = RowType.RegularUnderlining;
    }

    public void markCurrentRowAsRegularUnderscore() {
        currentRow.type = RowType.RegularUnderscore;
    }

    public void markCurrentRowAsRegularHead() {
        currentRow.type = RowType.RegularHead;
    }

    public void markNextTable() {
        currentRow.type = RowType.NextTable;
    }

    public void addCell(String value) {
        if (currentRow == null) {
            advanceToNextRow();
        }
        currentRow.cells.add(value);
    }

    public void addTable(HtmlTable value) {
        if (currentTable == null) {
            advanceToNextTable();
        }
        currentTable.tables.add(value);
    }

    public String generateHtmlTable() {
        StringBuilder result = new StringBuilder();
        result.append(TABLE_OPEN);
        for (TableRow row : tableData) {
            if (!row.cells.isEmpty() && !row.type.name().equals("DocHeader")) {
                switch (row.type.name()) {
                    case "Header":
                        result.append(THEAD_OPEN).append(ROW_OPEN);
                        for (String value : row.cells) {
                            if (row.type == RowType.Header) {
                                result.append(HEADER_CELL_OPEN).append(value).append(HEADER_CELL_CLOSE);
                            }
                        }
                        result.append(ROW_CLOSE).append(THEAD_CLOSE);
                        break;
                    case "Regular":
                        result.append(ROW_OPEN);
                        for (String value : row.cells) {
                            if (row.type == RowType.Regular) {
                                result.append(CELL_OPEN).append(value).append(CELL_CLOSE);
                            }
                        }
                        result.append(ROW_CLOSE);
                        break;
                    case "RegularUnderlining":
                        result.append(ROW_OPEN_UNDERLINE);
                        for (String value : row.cells) {
                            if (row.type == RowType.RegularUnderlining) {
                                result.append(CELL_OPEN).append(value).append(CELL_CLOSE);
                            }
                        }
                        result.append(ROW_CLOSE_UNDERLINE);
                        break;
                    case "RegularUnderscore":
                        result.append(ROW_OPEN_UNDERSCORE);
                        for (String value : row.cells) {
                            if (row.type == RowType.RegularUnderscore) {
                                result.append(CELL_OPEN).append(value).append(CELL_CLOSE);
                            }
                        }
                        result.append(ROW_CLOSE_UNDERSCORE);
                        break;
                    case "RegularHead":
                        result.append(ROW_OPEN);
                        for (String value : row.cells) {
                            if (row.type == RowType.RegularHead) {
                                result.append(CELL_OPEN).append(value).append(CELL_CLOSE);
                            }
                        }
                        result.append(ROW_CLOSE);
                        break;
                    default:
                        break;
                }
            }
        }
        result.append(TABLE_CLOSE).append("<br/>");
        return result.toString();
    }

    public String generateHtml() {
        StringBuilder result = new StringBuilder();

        if (tables.size() > 1) {
            for (Table t : tables) {
                for (HtmlTable t2 : t.tables) {
                    result.append("<br/>").append(LABELDOC_OPEN);
                    for (TableRow row : t2.tableData) {
                        for (String value : row.cells) {
                            if (row.type == RowType.DocHeader) {
                                result.append(value).append(" ");
                            }
                        }
                    }
                    result.append(LABELDOC_CLOSE);

                    result.append(DIV_TABL_OPEN).append(t2.generateHtmlTable()).append(DIV_TABL_CLOSE);
                }
            }
        } else {
            result.append(LABELDOC_OPEN);
            for (TableRow row : tableData) {
                for (String value : row.cells) {
                    if (row.type == RowType.DocHeader) {
                        result.append(value).append(" ");
                    }
                }
            }
            result.append(LABELDOC_CLOSE);
            result.append(DIV_TABL_OPEN).append(generateHtmlTable()).append(DIV_TABL_CLOSE);
        }
        return result.toString();
    }

    private List<TableRow> tableData = new LinkedList<>();
    private List<Table> tables = new LinkedList<>();
    private TableRow currentRow = null;
    private Table currentTable = null;
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
    private static final String LABELDOC_OPEN = "<label class='col-md-10 tTitle'><h3>";
    private static final String LABELDOC_CLOSE = "</h3></label>";
    private static final String DIV_TABL_OPEN = "<div class=\"col-md-12 tabl\">";
    private static final String DIV_TABL_CLOSE = "</div>";
    private static final String THEAD_OPEN = "<thead>";
    private static final String THEAD_CLOSE = "</thead>";
    private static final String TBODY_OPEN = "<tbody>";
    private static final String TBODY_CLOSE = "</tbody>";
    private static final String ROW_OPEN_UNDERLINE = "<tr class = 'RegularUnderlining'>";
    private static final String ROW_CLOSE_UNDERLINE = "</tr>";
    private static final String ROW_OPEN_UNDERSCORE = "<tr class = 'RegularUnderscore'>";
    private static final String ROW_CLOSE_UNDERSCORE = "</tr>";

}

enum RowType {
    DocHeader,
    Header,
    Regular,
    RegularUnderlining,
    RegularUnderscore,
    RegularHead,
    NextTable
}

class TableRow {

    public List<String> cells = new LinkedList<>();
    public RowType type = RowType.Regular;
}

class Table {
    public List<HtmlTable> tables = new LinkedList<>();
    public RowType type = RowType.NextTable;
}
