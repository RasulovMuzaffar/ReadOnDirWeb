package arm.tableutils.tablereaders.utils;

public class TextReplace {

    public static String getText(String str) {
        String text = str.replace("A", "А").replace("B", "В").replace("C", "С").replace("E", "Е").
                replace("H", "Н").replace("K", "К").replace("M", "М").replace("O", "О").
                replace("P", "Р").replace("T", "Т").replace("X", "Х").replace("Y", "У");
        return text;
    }
    public static String getSha(String str){
        return str.replace("?", "Ш");
    }
}
