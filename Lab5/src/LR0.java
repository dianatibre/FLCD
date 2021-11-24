import java.lang.reflect.Array;
import java.util.*;

public class LR0 {

    private Grammar grammar;
    private ArrayList<String> states;

    public LR0(Grammar grammar) {
        this.grammar = grammar;
        this.grammar.getP().put("S'", Arrays.asList(this.grammar.getS()));
        this.grammar.setS("S'");
        ///!!!!!!! N


        this.states = new ArrayList<>();
    }

    public Set<Production> closure(List<Production> productions) {
        Set<Production> closure = new HashSet<>(productions);
        Queue<Production> queue = new LinkedList<>(productions);
        while (!queue.isEmpty()) {
            Production current = queue.remove();
            if (current.getDot() < current.getRhs().size()) {
                String lhs = current.getRhs().get(current.getDot());
                if (this.grammar.checkNonTerminal(lhs)) {
                    for (String rhs : this.grammar.getP().get(lhs)) {
                        Production production = new Production(lhs, Arrays.asList(rhs));
                        if (!closure.contains(production)) {
                            closure.add(production);
                            queue.add(production);
                        }
                    }
                }
            }
        }
        return closure;
    }

    public Set<Production> goTo(List<Production> state, String element) {
        List<Production> newState = new ArrayList<>();
        for (Production p : state) {
            if (p.getDot() < p.getRhs().size() && p.getRhs().get(p.getDot()).equals(element)) {
                p.setDot(p.getDot() + 1);
                String s = String.join("", p.getRhs());
                p.setRhs(Collections.singletonList(s));
                newState.add(p);
            }
        }
        System.out.println(newState);
        return closure(newState);
    }

    public Set<Production> colCan() {
        Production p = new Production("S'", Collections.singletonList("S"));
        Set<Production> closure = closure(Collections.singletonList(p));
        Set<Production> c = new HashSet<>(closure);
        Queue<Production> queue = new LinkedList<>(Collections.singletonList(p));
        while (!queue.isEmpty()) {
            Production current = queue.remove();
            for (String X : grammar.getN()) {
                if (!goTo(Collections.singletonList(current), X).isEmpty() && !c.contains(goTo(Collections.singletonList(current), X))) {
                    closure = goTo(Collections.singletonList(current), X);
                    c.addAll(closure);
                    for (Production pr : closure) {
                        queue.add(pr);
                    }
                }
            }
            for (String X : grammar.getE()) {
                if (!goTo(Collections.singletonList(current), X).isEmpty() && !c.contains(goTo(Collections.singletonList(current), X))) {
                    closure = goTo(Collections.singletonList(current), X);
                    c.addAll(closure);
                    for (Production pr : closure) {
                        queue.add(pr);
                    }
                }
            }
        }
        return c;
    }
}
