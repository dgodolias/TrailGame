
import java.util.List;

class Rule {
    List<Clause> premises;
    Clause conclusion;

    public Rule(List<Clause> premises, Clause conclusion) {
        this.premises = premises;
        this.conclusion = conclusion;
    }

    public List<Clause> getPremises() {
        return premises;
    }

    public Clause getConclusion() {
        return conclusion;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("IF ");
        for (int i = 0; i < premises.size(); i++) {
            sb.append(premises.get(i));
            if (i < premises.size() - 1) {
                sb.append(" AND ");
            }
        }
        sb.append(" THEN ").append(conclusion);
        return sb.toString();
    }
}
