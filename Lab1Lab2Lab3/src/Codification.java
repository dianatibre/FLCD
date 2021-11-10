import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Codification {

    private String[] separators = {"[", "]", "(", ")", ",", ":"};
    private String[] operators = {"+", "-", "*", "/", "<-", "<-+", "<--", "<-/", "<-*", "<", ">", "<=",">=", "=", "<>"};
    private String[] reservedWords = {"in", "declare", "int", "float", "start", "stop", "read", "write", "array", "if", "else", "while", "string", "for", "begin", "end"};
    private HashMap<String, Integer> codificationTable = new HashMap<>();
    private static int index;

    public Codification() {
        codificationTable.put("identifier", 0);
        codificationTable.put("constant", 1);
        int current = 2;
        for (String reservedWord : reservedWords) {
            codificationTable.put(reservedWord, current);
            current++;
        }
        for (String operator : operators) {
            codificationTable.put(operator, current);
            current++;
        }
        for (String separator : separators) {
            codificationTable.put(separator, current);
            current++;
        }
    }

    public HashMap<String, Integer> getCodificationTable() {
        return codificationTable;
    }

    public Boolean checkIdentifier(String token) {
        return token.matches("^[a-zA-Z]+[0-9]*$");
    }

    public Boolean checkConstant(String token) {
        // integer
        if (token.matches("^(0|[+-]?[1-9][0-9]*)$")) return true;
        // float
        if (token.matches("^([0-9]*[.][0-9]*)$")) return true;
        // string
        if (token.charAt(0) == '"' && token.charAt(token.length() - 1) == '"') return true;
        // character
        return token.matches("[0-9a-zA-Z]");

    }

    public Boolean checkOperator(String token) {
        return IntStream.range(0, operators.length).anyMatch(i -> token.equals(operators[i]));
    }

    public Boolean checkSeparator(String token) {
        return IntStream.range(0, separators.length).anyMatch(i -> token.equals(separators[i]));
    }

    public Boolean checkReservedWord(String token) {
        return IntStream.range(0, reservedWords.length).anyMatch(i -> token.equals(reservedWords[i]));
    }

    /*public String getStringToken(String line) {
        // for returning the string in quotes
        String token = "";
        Pattern p = Pattern.compile("\"([^\"]*)\"");
        Matcher m = p.matcher(line);
        if (m.find()) {
            token = (m.group(1));
        }
        index += token.length();
        return token;
    }

    public String getToken(String line) {
        String token = "";
        while (index < line.length()) {
            if (checkSeparator("" + line.charAt(index)) || checkOperator("" + line.charAt(index)) || line.charAt(index) == ' ') {
                index--;
                return token;
            } else {
                token += line.charAt(index);
                index++;
            }
        }
        return token;
    }

    public String getOperatorToken(String line) {
        String token = "";
        token += line.charAt(index);
        if (checkOperator(token + line.charAt(index + 1))) {
            index++;
            return token + line.charAt(index);
        }
        return token;
    }

     */

    public List<String> tokenGenerator(String line) {
        List<String> token = Arrays.asList(line.split("\\s+"));
        List<String> myToken = new ArrayList<>();
        int j;
        for (j = 0; j<token.size(); j++) {
            if (token.get(j).length()!=0)
            myToken.add(token.get(j));
        }
        return myToken;
    }

    @Override
    public String toString() {
        String str = "\tCodification table \n\t--------\n";
        HashMap<Integer, String> hashMap = new HashMap<>();
        this.codificationTable.forEach((key, value) -> hashMap.put(value, key));
        for (int i = 0; i < separators.length + operators.length + reservedWords.length + 2; i++)
            if (i < 10)
                str += i + "     |     " + hashMap.get(i) + "\n";
            else
                str += i + "    |     " + hashMap.get(i) + "\n";
        str += "\t--------\n";
        return str;
    }


}
