import java.util.ArrayList;
import java.util.List;

public class ParsingTable {

    private List<List<Item>> states;
    private int nrRows, nrColumns;
    private List<List<Integer>> goTo = new ArrayList<>();
    private List<String> action = new ArrayList<>();
    private List<String> symbols = new ArrayList<>();

    public ParsingTable(List<List<Item>> states, String[] n, String[] e) {
        this.states = states;
        this.nrRows = states.size();
        this.nrColumns = n.length + e.length;
        for (int i = 0; i < nrRows; i++) {
            goTo.add(new ArrayList<>());
            for (int j = 0; j < nrColumns; j++) {
                goTo.get(i).add(-1);
            }
        }
        for (int i = 0; i < nrRows; i++) {
            action.add("");
        }
        for (int i = 0; i < n.length; i++) {
            symbols.add(n[i]);
        }
        for (int i = 0; i < e.length; i++) {
            symbols.add(e[i]);
        }
    }
}
