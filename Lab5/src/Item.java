import java.util.List;
import java.util.Objects;

public class Item {

    private String lhs;
    private List<String> rhs;
    private int dot;

    public Item(String lhs, List<String> rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.dot = 0;
    }

    public int getDot() {
        return dot;
    }

    public String getLhs() {
        return lhs;
    }

    public List<String> getRhs() {
        return rhs;
    }

    public void setDot(Integer dot) {
        this.dot = dot;
    }

    public void setRhs(List<String> rhs) {
        this.rhs = rhs;
    }

    @Override
    public String toString() {
        return "Production{" +
                "lhs='" + lhs + '\'' +
                ", rhs='" + rhs + '\'' +
                ", dot=" + dot +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return dot == item.dot && Objects.equals(lhs, item.lhs) && Objects.equals(rhs, item.rhs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lhs, rhs, dot);
    }
}
