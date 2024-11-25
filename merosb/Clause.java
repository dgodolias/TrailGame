import java.util.Arrays;
import java.util.List;

class Clause {
    String predicate;
    List<String> arguments;
    boolean isNegative; // Add this field

    public Clause(String predicate, String... args) {
        this.predicate = predicate;
        this.arguments = Arrays.asList(args);
        this.isNegative = false; // Initialize as positive by default
    }

    public void setNegative(boolean isNegative) {
        this.isNegative = isNegative;
    }

    public boolean isNegative() {
        return isNegative;
    }

    @Override
    public String toString() {
        String negation = isNegative ? "NOT " : "";
        return negation + predicate + "(" + String.join(", ", arguments) + ")";
    }
}

