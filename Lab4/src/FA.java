import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class FA {

    private final List<String> states;
    private final List<String> alphabet;
    private String q0;
    private final List<String> finalstates;
    private final Map<List<String>, String> transitions;

    public FA(Scanner fileReader) {
        this.states = new ArrayList<>();
        this.alphabet = new ArrayList<>();
        this.finalstates = new ArrayList<>();
        this.transitions = new HashMap<>();
        readFAValues(fileReader);
    }

    public List<String> getStates() {
        return states;
    }

    public List<String> getAlphabet() {
        return alphabet;
    }

    public String getQ0() {
        return q0;
    }

    public List<String> getFinalstates() {
        return finalstates;
    }

    public Map<List<String>, String> getTransitions() {
        return transitions;
    }

    private void readFAValues(Scanner reader) {
        String text = null;
        AtomicInteger line = new AtomicInteger();
        while (reader.hasNextLine()) {
            text = reader.nextLine();
            line.getAndIncrement();
            if (line.get() < 6) {
                if (text.contains("final_states= ")) {
                    text = text.replace("final_states= ", "");
                    String[] aux = text.split("\\s+");
                    Collections.addAll(finalstates, aux);
                    continue;
                }
                if (text.contains("states= ")) {
                    text = text.replace("states= ", "");
                    String[] aux = text.split("\\s+");
                    Collections.addAll(states, aux);
                    continue;
                }
                if (text.contains("alphabet= ")) {
                    text = text.replace("alphabet= ", "");
                    String[] aux = text.split("\\s+");
                    Collections.addAll(alphabet, aux);
                    continue;
                }
                if (text.contains("q0= ")) {
                    text = text.replace("q0= ", "");
                    q0 = text;
                }
            }
            //meaning it is a transition
            else {
                String chr1 = String.valueOf(text.charAt(1));
                String chr2 = String.valueOf(text.charAt(4));
                String chr3 = String.valueOf(text.charAt(10));
                transitions.put(List.of(chr1, chr2), chr3);
            }
        }
    }

    public boolean seqIsAccepted(String sequence) {
        if (isDFA()) {
            String currentState = q0;
            for (int i = 0; i < sequence.length(); i++) {
                char constant = sequence.charAt(i);
                List<String> list = new ArrayList<>();
                list.add(currentState);
                list.add(String.valueOf(constant));

                if (transitions.containsKey(list)) {
                    currentState = transitions.get(list);
                } else {
                    return false;
                }
            }
            return isFinalState(currentState);
        }
        return false;
    }

    private boolean isDFA() {
        return transitions.keySet().stream().noneMatch(key -> transitions.get(key) == null);
    }

    private boolean isFinalState(String string) {
        return finalstates.stream().anyMatch(finalState -> finalState.equals(string));
    }

    @Override
    public String toString() {
        return "FA{" +
                "states=" + states +
                ", alphabet=" + alphabet +
                ", q0='" + q0 + '\'' +
                ", finalstates=" + finalstates +
                ", transitions=" + transitions +
                '}';
    }
}
