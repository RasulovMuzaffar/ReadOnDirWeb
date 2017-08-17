package arm.tableutils.tablereaders;

import arm.tableutils.HtmlTable;
import java.util.List;


public interface TableReaderInterface {

    HtmlTable processFile(String fileName) throws MultipleResultsException;
    List<HtmlTable> readersResult();
}