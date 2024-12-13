
import java.util.Arrays;
import java.util.List;

class Clause {
    String predicate;
    List<String> arguments;

    public Clause(String predicate, String... args) {
        this.predicate = predicate;
        this.arguments = Arrays.asList(args);
    }

    @Override
    public String toString() {
        return predicate + "(" + String.join(", ", arguments) + ")";
    }
}
