package arm.tableutils.tablereaders;

import arm.tableutils.HtmlTable;


public interface TableReaderInterface {

    HtmlTable processFile(String fileName) throws MultipleResultsException;
}
