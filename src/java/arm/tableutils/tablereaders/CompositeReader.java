package arm.tableutils.tablereaders;

import arm.tableutils.HtmlTable;
import java.util.List;
import java.util.ArrayList;

public class CompositeReader implements TableReaderInterface {

    public static List<HtmlTable> lht = new ArrayList<>();

    @Override
    public HtmlTable processFile(String fileName) throws MultipleResultsException {
        List<HtmlTable> results = new ArrayList<>();
        System.out.println("RESULTS SIZE =====>>> " + results.size());
        int i = 0;
        for (TableReaderInterface reader : readersList) {
            HtmlTable table = reader.processFile(fileName);
            System.out.println("SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
            System.out.println(++i + " ===== " + table);
            if (table != null) {
                results.add(table);
//                break;
                System.out.println(i + " ===table not null=== " + table.generateHtml());
            }
            System.out.println("SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
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
