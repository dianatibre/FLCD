import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {

    public static Grammar grammar = new Grammar();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            menu();
            System.out.print("Option = ");
            int option = scanner.nextInt();
            if (option == 0) {
                System.exit(0);
            } else if (option == 1) {
                grammar = readGrammarFromFile();
            } else if (option == 2) {
                grammar = readMiniLanguage();
            } else if (option == 3) {
                System.out.println(grammar.printN());
            } else if (option == 4) {
                System.out.println(grammar.printE());
            } else if (option == 5) {
                System.out.println(grammar.printP());
            } else if (option == 6) {
                System.out.print("Non-terminal = ");
                String nonterminal = scanner.next();
                HashMap<String, List<String>> hashMap = grammar.productionsForANonterminal(nonterminal);
                String s = "Productions: = {";
                if (hashMap.size() == 0) {
                    s += "}";
                }
                int cnt = hashMap.size();
                for (Map.Entry<String, List<String>> entry : hashMap.entrySet()) {
                    String str = entry.getKey() + "-> ";
                    cnt -= 1;
                    for (int i = 0; i < entry.getValue().size(); i++)
                        if (i == entry.getValue().size() - 1 && cnt == 0)
                            s += str + entry.getValue().get(i) + "}\n";
                        else
                            s += str + entry.getValue().get(i) + ", ";
                }
                System.out.println(s);
            } else if (option == 7) {
                System.out.println(grammar.checkCFG());
            } else if (option == 8) {
                LR0 lr0 = new LR0(grammar);
                Production production = new Production("S'", List.of("S"));
                Production production1 = new Production("S", List.of("B"));
                List<Production> productions = new ArrayList<>();
                Production p1 = new Production("S", Arrays.asList("0", "B"));
                Production p2 = new Production("S", Arrays.asList("1", "A"));
                Production p3 = new Production("A", Collections.singletonList("0"));
                Production p4 = new Production("A", Arrays.asList("0", "S"));
                Production p5 = new Production("A", Arrays.asList("1", "A", "A"));
                productions.add(p1);
                productions.add(p2);
                productions.add(p3);
                productions.add(p4);
                productions.add(p5);
                System.out.println("Closure: ");
                System.out.println(lr0.closure(Arrays.asList(production, production1)));
                System.out.println("GoTo: ");
                System.out.println(lr0.goTo(productions, "0"));
//                System.out.println("ColCan: ");
//                System.out.println(lr0.colCan());
            }
        }
    }

    public static void menu() {
        System.out.println();
        System.out.println("0. Exit.");
        System.out.println("1. Read grammar from a file.");
        System.out.println("2. Read miniLanguage.");
        System.out.println("3. Print set of non-terminals.");
        System.out.println("4. Print set of terminals.");
        System.out.println("5. Print set of productions. ");
        System.out.println("6. Print productions for a given non-terminal");
        System.out.println("7. Check CGF");
        System.out.println("8. Test");
    }

    public static Grammar readGrammarFromFile() {
        String[] N = {};
        String[] E = {};
        HashMap<String, List<String>> P = new HashMap<>();
        String S = "";

        try {
            BufferedReader reader = new BufferedReader(new FileReader("grammar.txt"));
            String text = reader.readLine();
            while (text != null) {
                text = text.replace(" ", "");
                String[] elements = text.split("=");
                String terms = elements[1].replace("{", "");
                terms = terms.replace("}", "");
                if (elements[0].equals("N")) {
                    N = terms.split(",");
                } else if (elements[0].equals("E")) {
                    E = terms.split(",");
                } else if (elements[0].equals("P")) {
                    String[] t = terms.split(",");
                    for (int i = 0; i < t.length; i++) {
                        String[] r = t[i].split("->");
                        String[] r1 = r[1].split("\\|");
                        List<String> list = new ArrayList<>();
                        for (int j = 0; j < r1.length; j++) {
                            list.add(r1[j]);
                        }
                        if (P.containsKey(r[0])) {
                            for (int k = 0; k < list.size(); k++) {
                                P.get(r[0]).add(list.get(k));
                            }
                        } else {
                            P.put(r[0], list);
                        }
                    }
                } else if (elements[0].equals("S")) {
                    S = terms;
                } else {
                    System.out.println("Invalid token!");
                }
                text = reader.readLine();
            }

            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return new Grammar(N, E, P, S);
    }

    public static Grammar readMiniLanguage() {
        String[] N = {};
        String[] E = {};
        HashMap<String, List<String>> P = new HashMap<>();
        String S = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader("minilanguage.txt"));
            String text = reader.readLine();
            while (text != null) {
                if (text.charAt(0) == 'N' || text.charAt(0) == 'E' || text.charAt(0) == 'S') {
//                    text = text.replace(" ", "");
                    String[] elements = text.split(" = ");
                    elements[0] = elements[0].replace(" ", "");
                    elements[1] = elements[1].replace(" ", "");
                    String terms = elements[1].replace("{", "").replace("}", "");
                    if (elements[0].equals("N")) {
                        N = terms.split(",");
                    } else if (elements[0].equals("E")) {
                        String[] a = terms.split(",");
                        String[] aux = new String[a.length + 1];
                        aux = a;
                        aux[aux.length - 1] = String.valueOf(',');
                        E = aux;
//                        E = terms.split(",");
                    } else if (elements[0].equals("S")) {
                        S = terms;
                    }
                } else if (text.charAt(0) == 'P') {
                    text = reader.readLine();
                    while (text != null && !text.equals("}")) {
                        String[] r2 = text.split(" -> ");
                        String[] r3 = r2[1].split("\\|");
                        List<String> list = new ArrayList<>();
                        for (int j = 0; j < r3.length; j++) {
                            list.add(r3[j]);
                        }
                        if (P.containsKey(r2[0]))
                            for (int k = 0; k < list.size(); k++) {
                                P.get(r2[0]).add(list.get(k));
                            }
                        else {
                            P.put(r2[0], list);
                        }
                        text = reader.readLine();
                    }
                } else {
                    System.out.println("Invalid token!");
                }
                text = reader.readLine();
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return new Grammar(N, E, P, S);
    }
}
