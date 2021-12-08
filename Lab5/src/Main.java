import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.lang.*;

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
                grammar.productionsForANonterminal(nonterminal);
            } else if (option == 7) {
                System.out.println(grammar.checkCFG());
            } else if (option == 8) {
                LR0 lr0 = new LR0(grammar);
                List<Item> items = new ArrayList<>();
                for (String s : grammar.getP().keySet()) {
                    for (List<String> list : grammar.getP().get(s))
                        items.add(new Item(s, list));

                }

//                System.out.println(items);
//                System.out.println("Closure: ");
//                System.out.println(lr0.closure(items));
//                System.out.println("GoTo: ");
//                System.out.println(lr0.goTo(items, "public"));
////                System.out.println(lr0.goTo(productions, "0"));
//                System.out.println("ColCan: ");
//                System.out.println(lr0.canonicalCollection());

                List<List<Item>> states = lr0.canonicalCollection();
                System.out.println(lr0.stateToString(states));
                Map<List<String>, List<List<String>>> table = lr0.generateLR0Table(states);
                //System.out.println(lr0.generateLR0Table(states));
                System.out.println(lr0.tableToString(table));
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
        HashMap<String, List<List<String>>> P = new HashMap<>();
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
                                List<String> strings = new ArrayList<>();
                                String s = list.get(k);
                                for (int a = 0; a < s.length(); a++) {
                                    strings.add(String.valueOf(s.charAt(a)));
                                }
                                P.get(r[0]).add(strings);
                            }
                        } else {
                            P.put(r[0], new ArrayList<>());
                            for (int k = 0; k < list.size(); k++) {
                                List<String> strings = new ArrayList<>();
                                String s = list.get(k);
                                for (int a = 0; a < s.length(); a++) {
                                    strings.add(String.valueOf(s.charAt(a)));
                                }
                                P.get(r[0]).add(strings);
                            }
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
        HashMap<String, List<List<String>>> P = new HashMap<>();
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
                        String[] aux = new String[a.length + 3];
                        for (int i = 0; i < a.length; i++) {
                            aux[i] = a[i];
                        }
                        aux[a.length] = String.valueOf(',');
                        aux[a.length + 1] = String.valueOf('{');
                        aux[a.length + 2] = String.valueOf('}');
                        int index = 0;
                        String[] aux1 = new String[aux.length-2];
                        for (int i = 0; i < aux.length; i++) {
                            if (aux[i].length()!=0) {
                                aux1[index] = aux[i];
                                index++;
                            }
                        }
                        E = aux1;
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
                        if (P.containsKey(r2[0])) {
                            for (int k = 0; k < list.size(); k++) {
                                List<String> strings = new ArrayList<>();
                                String s = list.get(k);
                                String[] res = s.split(" ");
                                for (int q = 0; q < res.length; q++) {
                                    strings.add(res[q]);
                                }
                                P.get(r2[0]).add(strings);
//                                    for (int a = 0; a < s.length(); a++) {
//                                        strings.add(String.valueOf(s.charAt(a)));
//                                    }
//                                P.get(r2[0]).add(s);
                            }
                        } else {
                            P.put(r2[0], new ArrayList<>());
                            for (int k = 0; k < list.size(); k++) {
                                List<String> strings = new ArrayList<>();
                                String s = list.get(k);
                                String[] res = s.split(" ");
                                for (int q = 0; q < res.length; q++) {
                                    strings.add(res[q]);
                                }
                                P.get(r2[0]).add(strings);
                            }
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
