import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Grammar {

    private String[] N = {};
    private String[] E = {};
    private HashMap<String, List<List<String>>> P;
    private String S;

    public Grammar(String[] n, String[] e, HashMap<String, List<List<String>>> p, String s) {
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
        for (Map.Entry<String, List<List<String>>> entry : P.entrySet()) {
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

    public void productionsForANonterminal(String nonterminal) {
        if (this.P.containsKey(nonterminal)) {
            System.out.println(this.P.get(nonterminal));
        }
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

    public HashMap<String, List<List<String>>> getP() {
        return P;
    }

    public String getS() {
        return S;
    }

    public void setS(String s) {
        S = s;
    }

    public boolean checkNonTerminal(String nonTerminal) {
        for (int i = 0; i < N.length; i++) {
            if (this.N[i].equals(nonTerminal)) {
                return true;
            }
        }
        return false;
    }

    public String[] getN() {
        return N;
    }

    public String[] getE() {
        return E;
    }

    public LinkedHashMap<HashMap<String, List<String>>,Integer> numberProduction()
    {
        LinkedHashMap<HashMap<String, List<String>>,Integer> number=new LinkedHashMap<>();
        int index=1;
        HashMap<String, List<List<String>>> productions=this.getP();
        for(Map.Entry<String,List<List<String>>>entry : productions.entrySet())
        {
            String key=entry.getKey();
            if(!key.equals("S'")) {
                List<List<String>> values = productions.get(key);
                for (List<String> value : values) {

                    HashMap<String, List<String>> elem = new HashMap<>();
                    elem.put(key, value);
                    number.put(elem,index);
                    index += 1;
                }
            }
        }
        return number;
    }
    public LinkedHashMap<Integer,HashMap<String, List<String>>> numberProduction2()
    {
        LinkedHashMap<Integer,HashMap<String, List<String>>> number=new LinkedHashMap<>();
        int index=1;
        HashMap<String, List<List<String>>> productions=this.getP();
        for(Map.Entry<String,List<List<String>>>entry : productions.entrySet())
        {
            String key=entry.getKey();
            if(!key.equals("S'")) {
                List<List<String>> values = productions.get(key);
                for (List<String> value : values) {

                    HashMap<String, List<String>> elem = new HashMap<>();
                    elem.put(key, value);
                    number.put(index,elem);
                    index += 1;
                }
            }
        }
        return number;
    }
}
