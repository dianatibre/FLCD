import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PIF{

    private List<List<Object>> tokens;
    //private Map<String,Integer> tokens = new HashMap<String, Integer>();


    public List<List<Object>> getTokens() {
        return tokens;
    }

    public PIF() {
        this.tokens = new ArrayList<>();
    }

    public void addToken(int code, int ST_pos) {
        List<Object> token = new ArrayList<>(2);
        token.add(code);
        token.add(ST_pos);
        this.tokens.add(token);
    }

    @Override
    public String toString() {
        String str = "\tPIF \n\t--------\n";
        for (List<Object> token : tokens)
            if (token.get(0).toString().length() < 2) {
                str += token.get(0) + "     |     " + token.get(1) + "\n";
            } else {
                str += token.get(0) + "    |     " + token.get(1) + "\n";
            }
        str += "\t--------\n";
        return str;
    }
}