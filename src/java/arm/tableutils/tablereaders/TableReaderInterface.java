package arm.tableutils.tablereaders;

import arm.ent.Users;
import arm.tableutils.HtmlTable;
import java.util.List;


public interface TableReaderInterface {

    HtmlTable processFile(String fileName, Users u) throws MultipleResultsException;
    List<HtmlTable> readersResult();
}