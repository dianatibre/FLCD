import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class ST {
    private HashMap<Integer, List<String>> elements;
    private int m;

    public ST(int m) {
        // initializes the ST with size m and for each position, an array (the bucket)
        this.m = m;
        this.elements = new HashMap<>();
        for (int i = 0; i < m; i++) {
            this.elements.put(i, new ArrayList<>());
        }
    }

    public int hashFunction(String s) {
        // computes the sum of the characters' ascii codes
        int sum = IntStream.range(0, s.length()).map(s::charAt).sum();
        return sum % this.m;
    }

    public int lookUp(String s) {
        // returns the position of the element on its bucket
        int pos = hashFunction(s);
        List<String> list = this.elements.get(pos);
        int bound = list.size();
        return IntStream.range(0, bound).filter(i -> list.get(i).equals(s)).findFirst().orElse(-1);
    }

    public int add(String s) {
        // returns the result of the hashFunction (position in ST) if the element already exists, adds it in the bucket if it doesn't
        int pos = hashFunction(s);
        if (lookUp(s) != -1) {
            return pos;
        }
        if (this.elements.containsKey(pos)) {
            this.elements.get(pos).add(s);
        }
        return pos;
    }

    @Override
    public String toString() {
        String str = "\tSymTable \n\t--------\n";
        for (Map.Entry<Integer, List<String>> entry : this.elements.entrySet()) {
            if (entry.getKey() < 10) {
                str += entry.getKey() + "      |      " + entry.getValue() + "\n";
            } else {
                str += entry.getKey() + "     |      " + entry.getValue() + "\n";
            }
        }
        str += "\t--------\n";
        return str;
    }
}