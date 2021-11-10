import java.io.IOException;
import java.util.Scanner;

public class Main {

    private static String menu = "\t MENU \n" +
            "1. print the set of states \n" +
            "2. print the alphabet \n" +
            "3. print all the transitions \n" +
            "4. print the set of final states \n" +
            "5. For a DFA, verify if a sequence is accepted by the FA. \n" +
            "0. exit\n";


    public Main() {
    }

    public static void main(String[] args) throws IOException {
        try (Scanner reader = FileOperations.readFile("fa2.in")) {
            FA fa = new FA(reader);
            //System.out.println(fa);
            System.out.println(menu);
            Scanner in = new Scanner(System.in);
            while (true) {
                int command = in.nextInt();
                switch (command) {
                    case 0:
                        return;
                    case 1:
                        System.out.println("States: ");
                        System.out.println(fa.getStates());
                        break;
                    case 2:
                        System.out.println("Alphabet: ");
                        System.out.println(fa.getAlphabet());
                        break;
                    case 3:
                        System.out.println("Transitions: ");
                        System.out.println(fa.getTransitions());
                        break;
                    case 4:
                        System.out.println("Final States: ");
                        System.out.println(fa.getFinalstates());
                        break;
                    case 5:
                        System.out.println("Give the sequence:");
                        String str = in.next();
                        while (true) {
                            if (str.equals("stop"))
                                return;
                            else {
                                if (!fa.seqIsAccepted(str)) {
                                    System.out.println("The FA is not accepted!");
                                } else {
                                    System.out.println("The FA is accepted!");
                                }
                                str = in.next();
                            }
                        }
                }
            }
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }



    }

}