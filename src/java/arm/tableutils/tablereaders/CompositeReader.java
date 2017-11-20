package arm.tableutils.tablereaders;

import arm.ent.Users;
import arm.tableutils.HtmlTable;
import java.util.List;
import java.util.ArrayList;

public class CompositeReader implements TableReaderInterface {

    public static List<HtmlTable> lht = new ArrayList<>();

    @Override
    public HtmlTable processFile(String fileName, Users u) throws MultipleResultsException {
        List<HtmlTable> results = new ArrayList<>();
        int i = 0;
        System.out.println("---------------)))))))))) "+readersList.size());
        for (TableReaderInterface reader : readersList) {
            HtmlTable table = reader.processFile(fileName, u);
            if (table != null) {
                results.add(table);
//                break;
//                System.out.println(i + " ===table not null=== " + table.generateHtml());
            }
        }
        
        switch (results.size()) {
            case 0:
                return null; // no results, input file not recognized
            case 1:
                return results.get(0); // the only result
            default:
                lht = results;
//                throw new MultipleResultsException(results);
                return null;

        }
    }

    public void registerReader(TableReaderInterface reader) {
        readersList.add(reader);
    }

    @Override
    public List<HtmlTable> readersResult() {
//        if (lht!=null) {
//            for (HtmlTable l : lht) {
//                System.out.println("" + l.generateHtml());
//            }
            return lht;
//        } else {
//            return null;
//        }

    }
    private List<TableReaderInterface> readersList = new ArrayList<>();

}
