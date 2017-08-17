package arm.tableutils.tablereaders;

import arm.tableutils.HtmlTable;
import java.util.List;
import java.util.ArrayList;

public class CompositeReader implements TableReaderInterface {

//    public static List<HtmlTable> lht = new ArrayList<>();

    @Override
    public HtmlTable processFile(String fileName) throws MultipleResultsException {
        List<HtmlTable> results = new ArrayList<>();
        for (TableReaderInterface reader : readersList) {
            HtmlTable table = reader.processFile(fileName);
            if (table != null) {
                results.add(table);
//                break;
            }
        }

        switch (results.size()) {
            case 0:
                return null; // no results, input file not recognized
            case 1:
                return results.get(0); // the only result
            default:
//                for (int i = 0; i < results.size(); i++) {
//                    lht.add(results.get(i));
//                }
                throw new MultipleResultsException(results);
//                return null;

        }
    }

    public void registerReader(TableReaderInterface reader) {
        readersList.add(reader);
    }

    private List<TableReaderInterface> readersList = new ArrayList<>();
}
