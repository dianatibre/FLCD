import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class LR0 {

    private Grammar grammar;
    private String starting;

    public LR0(Grammar grammar) {
        this.grammar = grammar;
        List<String> listStrings = new ArrayList<>(Collections.singletonList(this.grammar.getS()));
        List<List<String>> toBeAdded = new ArrayList<>();
        toBeAdded.add(listStrings);
        this.starting = this.grammar.getS();
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
        //rightHandSide.add("S");
        rightHandSide.add("program");
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
        Map<List<String>, List<List<String>>> lr0Table = new LinkedHashMap<>();

        if (!checkReduceReduceConflict(canonicalCollection) && !checkShiftReduceConflict(canonicalCollection)) {
            for (int i = 0; i < canonicalCollection.size(); i++) {
                List<Item> items = canonicalCollection.get(i);
                if (items.size() == 1 &&
                        Objects.equals(items.get(0).getLhs(), "S'") &&
                        items.get(0).getDot() == 1) {
                    lr0Table.put(new ArrayList<>(Arrays.asList(String.valueOf(i), "Accept")), new ArrayList<>());
                } else {
                    if (items.size() == 1) {
                        Item item = items.get(0);
                        if (item.getRhs().size() == item.getDot()) {
                            HashMap<String, List<String>> production = new HashMap<>();
                            production.put(item.getLhs(), item.getRhs());
                            lr0Table.put(
                                    new ArrayList<>(Arrays.asList(String.valueOf(i), "Reduce " + this.grammar.numberProduction().get(production))),
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

    public Stack<String> outputStack(List<String> w, Map<List<String>, List<List<String>>> table) {
        Stack<String> workingStack = new Stack<>();
        Stack<String> inputStack = new Stack<>();
        Stack<String> outputStack = new Stack<>();
        workingStack.add("$");
        workingStack.add("0");
        inputStack.add("$");
        for (int i = w.size() - 1; i >= 0; i--) {
            inputStack.add(w.get(i));
        }
        //System.out.println(workingStack);
        //System.out.println(inputStack);
        while (true) {
            String currentState = workingStack.get(workingStack.size() - 1);
            //System.out.println(currentState);
            if (getKey(table, currentState).equals("Accept")) {
                break;
            } else {
                if (getKey(table, currentState).equals("Shift")) {
                    String currentHead = inputStack.pop();
                    String nextState = getValue(table, currentHead, currentState);
                    //System.out.println(nextState);
                    workingStack.add(currentHead);
                    workingStack.add(nextState);
                    System.out.println(workingStack);
                } else {
                    if (getKey(table, currentState).contains("Reduce")) {
                        String action = getKey(table, currentState);
                        String[] actionSplit = action.split(" ");
                        String productionNumber = actionSplit[1];
                        outputStack.add(productionNumber);
                        HashMap<String, List<String>> production = this.grammar.numberProduction2().get(Integer.valueOf(productionNumber));
                        //System.out.println(production);
                        String lhs = "";
                        List<String> rhs = new ArrayList<>();
                        for (Map.Entry<String, List<String>> entry : production.entrySet()) {
                            lhs = entry.getKey();
                            rhs = entry.getValue();
                            break;
                        }

                        List<String> rhsCopy = new ArrayList<>(List.copyOf(rhs));
                        while (!rhsCopy.isEmpty()) {
                            workingStack.pop();
                            workingStack.pop();
                            rhsCopy.remove(rhsCopy.size() - 1);
                        }
                        String previousElement = workingStack.get(workingStack.size() - 1);
                        workingStack.add(lhs);
                        String nextState = getValue(table, lhs, previousElement);
                        //System.out.println(nextState);
                        workingStack.add(nextState);
//                        System.out.println(inputStack);
//                        System.out.println(workingStack);
                    }
                }
            }
        }
        Stack<String> outputStackReversed = new Stack<>();
        while (!outputStack.isEmpty()) {
            String elem = outputStack.pop();
            outputStackReversed.push(elem);
        }
        return outputStackReversed;
    }

    public String getKey(Map<List<String>, List<List<String>>> table, String state) {
        for (Map.Entry<List<String>, List<List<String>>> entry : table.entrySet()) {
            List<String> key = entry.getKey();
            if (key.get(0).equals(state))
                return key.get(1);
        }
        return "";
    }

    public String getValue(Map<List<String>, List<List<String>>> table, String currentHead, String state) {
        for (Map.Entry<List<String>, List<List<String>>> entry : table.entrySet()) {
            List<String> key = entry.getKey();
            //List<List<String>> values=entry.getValue();
            if (key.get(0).equals(state)) {
                List<List<String>> values = table.get(key);
                for (List<String> value : values)
                    if (value.get(0).equals(currentHead))
                        return value.get(1);
            }
        }
        return "";
    }

    public boolean checkShiftReduceConflict(List<List<Item>> canonicalCollection) {
        int cnt1 = 0;
        for (int i = 0; i < canonicalCollection.size(); i++) {
            List<Item> items = canonicalCollection.get(i);
            int cnt = 0;
            int cnt2 = 0;
            for (Item it : items) {
                if (it.getRhs().size() == it.getDot()) {
                    cnt++;
                }
                if (it.getRhs().size() > it.getDot()) {
                    cnt2++;
                }
            }
            //System.out.println(cnt+" "+cnt2);
            if (cnt >= 1 && cnt2 >= 1) {
                System.out.println("Shift-reduce conflict! At state: " + i + items);
                cnt1 += 1;
            }
        }
        return cnt1 != 0;
    }

    public boolean checkReduceReduceConflict(List<List<Item>> canonicalCollection) {
        int cnt1 = 0;
        for (int i = 0; i < canonicalCollection.size(); i++) {
            List<Item> items = canonicalCollection.get(i);
            int cnt = 0;
            for (Item it : items) {
                if (it.getRhs().size() == it.getDot()) {
                    cnt++;
                }
            }
            if (cnt > 1) {
                System.out.println("Reduce-reduce conflict! At state: " + i + items);
                cnt1 += 1;
            }
        }
        return cnt1 != 0;
    }

    public List<HashMap<String, List<String>>> productionsInOutput(Stack<String> outputStack) {
        List<HashMap<String, List<String>>> productions = new ArrayList<>();
        for (String i : outputStack) {
            productions.add(this.grammar.numberProduction2().get(Integer.valueOf(i)));
        }
        return productions;
    }

    public void printParsingTable(List<HashMap<String, List<String>>> productions) {
        List<ParserOutput> tree = new ArrayList<>();
        tree.add(new ParserOutput(0, -1, -1, this.starting));
        getRecursive(tree, productions, 0, 1, 0);
        try {
            FileWriter myWriter = new FileWriter("tree.txt");
            tree.forEach(line -> {
                try {
                    myWriter.write(String.valueOf(line));
                    myWriter.write("\n");
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            });
            myWriter.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        tree.forEach(System.out::println);
    }

    public void getRecursive(List<ParserOutput> tree, List<HashMap<String, List<String>>> productions, int parent, int rowIndex, int currentProductionIndex) {

        HashMap<String, List<String>> production = productions.get(currentProductionIndex);
        //System.out.println(productions);
        List<String> rhs = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : production.entrySet())
            rhs = entry.getValue();
        List<ParserOutput> auxRows = new ArrayList<>();
        for (int i = 0; i < rhs.size(); i++) {
            ParserOutput row = new ParserOutput();
            row.setIndex(rowIndex);
            rowIndex++;
            row.setSymbol(rhs.get(i));
            //System.out.println(rhs);
            row.setParent(parent);

            if (i < rhs.size() - 1) {
                row.setRightSibling(rowIndex);
            } else {
                row.setRightSibling(-1);
            }
            //System.out.println(row);
            auxRows.add(row);
        }

        tree.addAll(auxRows);

        for (ParserOutput row : auxRows) {
            if (contains(grammar.getN(), row.getSymbol())) {
                currentProductionIndex = currentProductionIndex + 1;
                getRecursive(tree, productions, row.getIndex(), rowIndex, currentProductionIndex);
            }
        }
    }

    public boolean contains(String[] N, String symbol) {
        for (int i = 0; i < N.length; i++) {
            if (N[i].equals(symbol)) {
                return true;
            }
        }
        return false;
    }
}
