import java.util.ArrayList;
import java.util.List;

public class PIF {
    private List<List<String>> tokens;

    public PIF() {
        this.tokens = new ArrayList<>();
    }

    public void addToken(String code, int ST_pos) {
        List<String> token = new ArrayList<>(2);
        token.add(code);
        token.add(String.valueOf(ST_pos));
        this.tokens.add(token);
    }

    @Override
    public String toString() {
        String str = "PIF \n-------------------------\n";
        for (int i = 0; i < this.tokens.size(); i++)
            str += this.tokens.get(i).get(0) + "    |     " + this.tokens.get(i).get(1) + "\n";
        str += "-------------------------\n";
        return str;
    }
}
