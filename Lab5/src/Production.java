import java.util.List;

public class Production {

    private String lhs;
    private List<String> rhs;
    private Integer dot;

    public Production(String lhs, List<String> rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.dot = 0;
    }

    public Integer getDot() {
        return dot;
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
}
