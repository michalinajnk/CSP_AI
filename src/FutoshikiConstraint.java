import java.util.ArrayList;
import java.util.List;

public class FutoshikiConstraint implements Constraint {

    private Indices smaller;
    private Indices bigger;

    FutoshikiConstraint(Indices smaller, Indices bigger) {
        this.smaller = smaller;
        this.bigger = bigger;
    }

    public Indices getBigger() {
        return bigger;
    }

    public Indices getSmaller() {
        return smaller;
    }

    @Override
    public String toString() {
        return " { " + smaller + " < " + bigger + "} ";
    }

}
