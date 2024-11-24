import java.util.List;

class Rule {
    List<Clause> premises; // Οι προϋποθέσεις του κανόνα (π.χ. isFatherOf(x, y) AND isFatherOf(y, z))
    Clause conclusion;     // Το συμπέρασμα του κανόνα (π.χ. isGrandadOf(x, z))

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
        sb.append("(");
        for (int i = 0; i < premises.size(); i++) {
            sb.append(premises.get(i));
            if (i < premises.size() - 1) {
                sb.append(" AND ");
            }
        }
        sb.append(") => ").append(conclusion);
        return sb.toString();
    }
}
