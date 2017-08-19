
package arm.tableutils.tablereaders;

import arm.tableutils.HtmlTable;
import java.util.List;

public class MultipleResultsException extends Exception {
    MultipleResultsException(List<HtmlTable> multipleResults) {
	this.multipleResults = multipleResults;
        System.out.println("PROBLEMAAAA!!!!!!!!");
    }

    public final List<HtmlTable> multipleResults;
}