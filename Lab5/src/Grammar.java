import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Grammar {

    private String[] N = {};
    private String[] E = {};
    private HashMap<String, List<String>> P;
    private String S;

    public Grammar(String[] n, String[] e, HashMap<String, List<String>> p, String s) {
        N = n;
        E = e;
        P = p;
        S = s;
    }

    public Grammar() {
    }

    public String printN() {
        String s = "Set of non-terminals: N = {";
        if (this.N.length == 0) {
            s += "}";
        }
        for (int i = 0; i < N.length; i++) {
            if (i == this.N.length - 1) {
                s += this.N[i] + "}\n";
            } else {
                s += this.N[i] + ", ";
            }
        }
        return s;
    }

    public String printE() {
        String s = "Set of terminals: E = {";
        if (this.E.length == 0) {
            s += "}";
        }
        for (int i = 0; i < E.length; i++) {
            if (i == this.E.length - 1) {
                s += this.E[i] + "}\n";
            } else {
                s += this.E[i] + ", ";
            }
        }
        return s;
    }

    public String printP() {
        String s = "Productions: = {";
        if (this.P.size() == 0) {
            s += "}";
        }
        int cnt = P.size();
        for (Map.Entry<String, List<String>> entry : P.entrySet()) {
            String str = entry.getKey() + "-> ";
            cnt -= 1;
            for (int i = 0; i < entry.getValue().size(); i++)
                if (i == entry.getValue().size() - 1 && cnt == 0)
                    s += str + entry.getValue().get(i) + "}\n";
                else
                    s += str + entry.getValue().get(i) + ", ";
        }
        return s;
    }

    public HashMap<String, List<String>> productionsForANonterminal(String nonterminal) {
        HashMap<String, List<String>> hashMap = new HashMap<>();
        for (int i = 0; i < P.size(); i++) {
            if (P.containsKey(nonterminal)) {
                hashMap.put(nonterminal, P.get(nonterminal));
                return hashMap;
            }
        }
        return hashMap;
    }

    public boolean checkCFG() {
        for (String key : P.keySet()) {
            int ok = 0;
            for (int i = 0; i < N.length; i++) {
                if (this.N[i].equals(key)) {
                    ok = 1;
                }
            }
            if (ok == 0) {
                return false;
            }
        }
        return true;
    }
}
