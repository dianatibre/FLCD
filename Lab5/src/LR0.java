import java.util.*;
import java.util.stream.Collectors;

public class LR0 {

    private Grammar grammar;

    public LR0(Grammar grammar) {
        this.grammar = grammar;
        List<String> listStrings = new ArrayList<>(Collections.singletonList(this.grammar.getS()));
        List<List<String>> toBeAdded = new ArrayList<>();
        toBeAdded.add(listStrings);

        this.grammar.getP().put("S'", toBeAdded);
        this.grammar.setS("S'");
    }

    public List<Item> closure(List<Item> items) {
        List<Item> closure = new ArrayList<>(items);
        Queue<Item> queue = new LinkedList<>(items);
        while (!queue.isEmpty()) {
            Item current = queue.remove();
            if (current.getDot() < current.getRhs().size()) {
                String lhs = current.getRhs().get(current.getDot());
                if (this.grammar.checkNonTerminal(lhs)) {
                    for (List<String> rhs : this.grammar.getP().get(lhs)) {
                        Item item = new Item(lhs, rhs);
                        if (!checkContains(closure, item)) {
                            closure.add(item);
                            queue.add(item);
                        }
                    }
                }
            }
        }
        return closure;
    }

    private boolean checkContains(List<Item> items, Item item) {
        for (Item p : items) {
            if (p.getLhs().equals(item.getLhs()) && p.getRhs().equals(item.getRhs()) && p.getDot() == item.getDot()) {
                return true;
            }
        }
        return false;
    }

    public List<Item> goTo(List<Item> state, String element) {
        List<Item> newStates = new ArrayList<>();
        for (Item p : state) {
            if (p.getDot() < p.getRhs().size() && p.getRhs().get(p.getDot()).equals(element)) {
                Item a = new Item(p.getLhs(), p.getRhs());
                a.setDot(p.getDot() + 1);
                newStates.add(a);
            }
        }
//        System.out.println(newStates);
        return closure(newStates);
    }

    public List<List<Item>> canonicalCollection() {
        List<List<Item>> canonicalCollection = new ArrayList<>();
        List<String> rightHandSide = new ArrayList<>();
        rightHandSide.add("S");
        //rightHandSide.add("program");
        List<Item> productionsS0 = new ArrayList<>();
        productionsS0.add(new Item("S'", rightHandSide));
        List<Item> s0 = closure(productionsS0);
        canonicalCollection.add(s0);
        List<String> nue = new ArrayList<>();
        nue.addAll(Arrays.asList(this.grammar.getN()));
        nue.addAll(Arrays.asList(this.grammar.getE()));
        List<List<Item>> canonicalCollectionCopy;
        canonicalCollectionCopy = canonicalCollection.stream().collect(Collectors.toList());
        while (true) {
            for (List<Item> p : canonicalCollection) {
                for (String n : nue) {
                    List<Item> elem = goTo(p, n);
                    if (!elem.isEmpty() && !check(canonicalCollectionCopy, elem)) {
                        canonicalCollectionCopy.add(elem);
                    }
                }
            }
            int ok = 1;
            for (int i = 0; i < canonicalCollectionCopy.size(); i++) {
                if (!check(canonicalCollection, canonicalCollectionCopy.get(i))) {
                    ok = 0;
                }
            }
            if (ok == 1) {
                break;
            }
            canonicalCollection = List.copyOf(canonicalCollectionCopy);
        }
        return canonicalCollection;
    }

    public boolean check(List<List<Item>> list, List<Item> elem) {
        int ok, nr;
        for (int i = 0; i < list.size(); i++) {
            ok = 0;
            nr = 0;
            for (int j = 0; j < list.get(i).size(); j++) {
                for (int k = 0; k < elem.size(); k++) {
                    if (list.get(i).get(j).getLhs().equals(elem.get(k).getLhs()) && list.get(i).get(j).getRhs().equals(elem.get(k).getRhs()) && list.get(i).get(j).getDot() == elem.get(k).getDot()) {
                        nr++;
                    }
                }
            }
            if (nr == list.get(i).size()) {
                return true;
            }
        }
        return false;
    }

    public String stateToString(List<List<Item>> states) {
        String s = "";
        for (int i = 0; i < states.size(); i++) {
            s += "state " + i + " " + states.get(i);
            s += "\n";
        }

        return s;
    }


    public Map<List<String>, List<List<String>>> generateLR0Table(List<List<Item>> canonicalCollection) {
        // Map<Pair<State,Action>,List<Pair<NUE,State>>>
        Map<List<String>, List<List<String>>> lr0Table = new LinkedHashMap<>();
        int reduceIndex = 1;

        for (int i = 0; i < canonicalCollection.size(); i++) {
            List<Item> items = canonicalCollection.get(i);
            if (items.size() == 1 &&
                    Objects.equals(items.get(0).getLhs(), "S'") &&
                    Objects.equals(items.get(0).getRhs().get(0), "S") &&
                    items.get(0).getDot() == 1) {
                lr0Table.put(new ArrayList<>(Arrays.asList(String.valueOf(i), "Accept")), new ArrayList<>());
            } else {
                for (Item item : items) {
                    if (item.getRhs().size() == item.getDot()) {
                        lr0Table.put(
                                new ArrayList<>(Arrays.asList(String.valueOf(i), "Reduce " + reduceIndex++)),
                                new ArrayList<>()
                        );
                    }
                }
            }
            if (allShift(items)) {
                for (Item item : items) {
                    String elem = item.getRhs().get(item.getDot());
                    var goToValue = goTo(items, elem);
                    int size = -1;
                    for (int k = 0; k < canonicalCollection.size(); k++) {
                        if (checkContainsAll(canonicalCollection.get(k), goToValue)) {
                            size = k;
                        }
                    }
                    List<String> value = new ArrayList<>();
                    value.add(elem);
                    value.add(String.valueOf(size));
                    List<List<String>> values = new ArrayList<>();
                    values.add(value);
                    List<String> key = new ArrayList<>();
                    key.add(String.valueOf(i));
                    key.add("Shift");
                    if (!lr0Table.containsKey(key))
                        lr0Table.put(key, values);
                    else if (!lr0Table.get(key).contains(value))
                        lr0Table.get(key).add(value);
                }
            }
//            System.out.println(allShift(productions));
        }
        return lr0Table;
    }

    private boolean checkContainsAll(List<Item> state, List<Item> goToResult) {
        for (int i = 0; i < state.size(); i++) {
            if (!state.get(i).equals(goToResult.get(i))) {
                return false;
            }
        }
        return true;
    }

    private boolean allShift(List<Item> items) {
        for (Item item : items) {
            if (item.getDot() == item.getRhs().size()) {
                return false;
            }
        }
        return true;
    }

    public String tableToString(Map<List<String>, List<List<String>>> table) {
        String s = "";
        s += "   " + "Action" + "    ";
        List<String> nue = new ArrayList<>();
        nue.addAll(Arrays.asList(this.grammar.getN()));
        nue.addAll(Arrays.asList(this.grammar.getE()));
        for (String i : nue)
            s += i + "   ";
        s += "\n";
        for (Map.Entry<List<String>, List<List<String>>> entry : table.entrySet()) {

            List<String> key = entry.getKey();
            List<List<String>> value = entry.getValue();
            s += key.get(0) + "  " + key.get(1);
            if (key.get(1).equals("Accept"))
                s += "    ";
            else if (key.get(1).contains("Reduce"))
                s += "  ";
            else if (key.get(1).contains("Shift"))
                s += "     ";
            for (String i : nue) {
                if (checkValueIsContained(value, i)) {
                    String elem = checkValueIsContainedAndReturnIt(value, i);
                    s += elem + "   ";
                } else
                    s += "-" + "   ";
            }
            s += "\n";
        }
        return s;
    }

    private boolean checkValueIsContained(List<List<String>> value, String i) {
        for (List<String> j : value) {
            String elem = j.get(0);
            if (elem.equals(i))
                return true;
        }
        return false;
    }

    private String checkValueIsContainedAndReturnIt(List<List<String>> value, String i) {
        for (List<String> j : value) {
            String elem = j.get(0);
            if (elem.equals(i))
                return j.get(1);
        }
        return " ";
    }
//
//    public List<Production> getStates() {
//        Production production = new Production(this.grammar.getS(), this.grammar.getP().get(this.grammar.getS()).get(0));
//        List<Production> p = new ArrayList<>();
//        p.add(production);
//        List<Production> currentState = closure(p);
//        List<List<Production>> states = new ArrayList<>();
//        states.add(currentState);
//        int currentStateIndex = -1;
//        List<String> nue = new ArrayList<>();
//        nue.addAll(Arrays.asList(this.grammar.getN()));
//        nue.addAll(Arrays.asList(this.grammar.getE()));
////        List<>
//        while (currentStateIndex < states.size() - 1) {
//            currentStateIndex += 1;
//            List<Production> state = states.get(currentStateIndex);
//            for (String symbol : nue) {
//                List<Production> goToProductions = new ArrayList<>();
//                for (Production production1 : state) {
//                    if (production1.getDot() < production1.getRhs().size() && production1.getRhs().get(production1.getDot()).equals(symbol)) {
//                        Production pr = new Production(production1.getLhs(), production1.getRhs());
//                        pr.setDot(production1.getDot());
//                        goToProductions.add(pr);
//                    }
//                    if (goToProductions.size() > 0) {
//                        List<Production> goTo = goTo(goToProductions, symbol);
//                        if (goTo.size() != 0) {
//                            List<List<Production>> s = new ArrayList<>();
//                            s.add(goTo);
//                            if (!states.contains(s)) {
//                                states.add(s);
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    public ParsingTable createParsingTable() {
//
//    }

//
//    public Set<Production> colCan() {
//        Production p = new Production("S'", Collections.singletonList("S"));
//        Set<Production> closure = closure(Collections.singletonList(p));
//        List<String> nue = new ArrayList<>();
//        nue.addAll(Arrays.asList(this.grammar.getN()));
//        nue.addAll(Arrays.asList(this.grammar.getE()));
//        Set<Production> closureCopy = closure.stream().collect(Collectors.toSet());
//        while (true) {
//            for (Production production : closure) {
//                for (String n : nue) {
//                    List<Production> elem = goTo(p, n);
//                    if (!elem.isEmpty() && !check(canonicalCollectionCopy, elem)) {
//                        canonicalCollectionCopy.add(elem);
//                    }
//                }
//            }
//        }
////        Set<Production> c = new HashSet<>(closure);
////        Queue<Set<Production>> queue = new LinkedList<>();
////        queue.add(c);
////        while (!queue.isEmpty()) {
////            Set<Production> current = queue.remove();
////            for (String X : grammar.getN()) {
////                if (!goTo(current, X).isEmpty() && !c.contains(goTo(current, X))) {
////                    closure = goTo(current, X);
////                    c.addAll(closure);
////                    queue.add(closure);
////                }
////            }
////            for (String X : grammar.getE()) {
////                if (!goTo(current, X).isEmpty() && !c.contains(goTo(current, X))) {
////                    closure = goTo(current, X);
////                    c.addAll(closure);
////                    queue.add(closure);
////                }
////            }
////        }
//        return c;
//    }
}
