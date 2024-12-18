# Τίτλος Εργασίας: Υλοποίηση Μεθόδων Εξαγωγής Συμπερασμάτων

(Forward Chaining για Προτασιακή Λογική & Backward Chaining για Πρωτοβάθμια Κατηγορηματική Λογική)

## Περίληψη
Η εργασία υλοποιεί δύο μεθόδους εξαγωγής συμπερασμάτων, ανάλογα με τις απαιτήσεις:

### Μέθοδοι
1. **Εξαγωγή Συμπερασμάτων προς τα εμπρός (Forward Chaining) για οριστικές (Horn) προτάσεις προτασιακής λογικής:**
   - Υλοποίηση του αλγορίθμου Forward Chaining (PL-FC-Entail).
   - Θεωρείται ότι ο προς απόδειξη τύπος είναι ένα απλό προτασιακό σύμβολο χωρίς άρνηση (π.χ. `Q`).

2. **Εξαγωγή Συμπερασμάτων με Backward Chaining για οριστικές (Horn) προτάσεις πρωτοβάθμιας κατηγορηματικής λογικής:**
   - Χρήση κανόνων και γεγονότων με κατηγορήματα, σταθερές και μεταβλητές.
   - Εφαρμογή ενοποίησης (unification) και αναζήτησης συμπερασμάτων με οπισθοβαρή αλυσίδωση (Backward Chaining).
   - Θεωρείται ότι ο προς απόδειξη τύπος είναι ένας ατομικός τύπος χωρίς άρνηση (π.χ. `Loves(John, x)`).

Στο παρόν πακέτο περιλαμβάνονται τα αρχεία κώδικα, καθώς και παραδείγματα αρχείων γνώσης.

---

## Δομή Κώδικα και Αρχεία

### Μέρος B1: Προτασιακή Λογική (Forward Chaining)
- **`BaseKnowledge.java`**: Φορτώνει και αποθηκεύει τη γνώση (Horn προτάσεις) από ένα αρχείο κειμένου.
- **`InputReader.java`**: Ανάγνωση δεδομένων από ένα εξωτερικό αρχείο (π.χ. `example.txt`).
- **`plfcEntails.java`**: Υλοποίηση του αλγορίθμου Forward Chaining (PL-FC-Entail) για προτασιακή λογική.
- **`Main.java` (στο πακέτο `merosB1`)**: Κύρια κλάση εκτέλεσης για την προτασιακή λογική.
  - Προσαρμόζοντας τη μεταβλητή στο `Main.java` μπορείτε να ορίσετε ποιον τύπο θέλετε να αποδείξετε (π.χ. `boolean result = Entails.solve('Q');`).

#### Αρχεία γνώσης:
- `example.txt` (δίνει αποτέλεσμα `true` για `Q`)
- `example2.txt` (δίνει αποτέλεσμα `false` για `Q`)

---

### Μέρος B2: Πρωτοβάθμια Κατηγορηματική Λογική (Backward Chaining)
- **`KnowledgeBase.java`**: Αναπαριστά τη βάση γνώσεων. Περιλαμβάνει λίστες με γεγονότα, κανόνες, σταθερές και σχέσεις.
- **`Clause.java`**: Αναπαράσταση ενός κατηγορήματος με ορίσματα.
- **`Rule.java`**: Αναπαράσταση κανόνα: προϋποθέσεις (premises) και ένα συμπέρασμα (conclusion).
- **`Unifier.java`**: Υλοποίηση βασικής ενοποίησης δύο όρων/προτάσεων.
- **`Conclusion.java`**: Περιλαμβάνει τη μέθοδο `folBcAsk` για εξαγωγή συμπερασμάτων με οπισθοβαρή αλυσίδωση (Backward Chaining). Προσφέρει και εκτύπωση του proof tree.
- **`Main.java` (π.χ. σε πακέτο `merosB2`)**: Κύρια κλάση εκτέλεσης για την πρωτοβάθμια λογική.
  - Ζητάει από τον χρήστη ένα ερώτημα (π.χ. `isGrandadOf(Giannis, Babis)`) και προσπαθεί να το αποδείξει μέσω Backward Chaining.

#### Αρχείο γνώσης:
- `knowledge_base.txt`: Περιέχει την αρχική Βάση Γνώσης (γεγονότα, κανόνες, σχέσεις, σταθερές).

---

## Οδηγίες Εκτέλεσης

### Μέρος B1: Προτασιακή Λογική (Forward Chaining)
* **Εκτέλεση:**
  * **Μετάβαση εντός φακέλου:** ``merosB1`:`cd merosB1``
  * **Μεταγλώττιση:** `javac *.java`
  * **Εκτέλεση:** `java merosB1.Main`
  * **Αλλαγή Ερωτήματος:** Στο αρχείο InputReader διαβάζει το πρόγραμμα την βάση γνώσης. Αν επιλέξετε να την αλλάξετε για σκοπούς testing, ξανατρέξτε τισ παραπάνω εντολές μεταγλώττισης και εκτέλεσης.

* **Παραδείγματα(γίνεται αλλαγή στην ΒΓ):**
  * `example.txt`: Επιστρέφει `false`.
  * `example2.txt`: Επιστρέφει `true`.

### Μέρος B2: Πρωτοβάθμια Κατηγορηματική Λογική
  * **Μετάβαση εντός φακέλου:** ``merosB2`:`cd merosB2``
* **Μεταγλώττιση:** `javac *.java`
* **Εκτέλεση:** `java Main`
* **Εισαγωγή Ερωτήματος:** Το πρόγραμμα θα σας ζητήσει να εισάγετε ένα ερώτημα.

**Παράδειγμα Ερωτήματος:**
* **Βάση Γνώσεων:**
  * `isFatherOf(x,y) AND isFatherOf(y,z)] => isGrandadOf(x,z)`
  * `isFatherOf(Giannis, Xristos)`
  * `isFatherOf(Xristos, Babis)`
* **Ερώτημα:** (πχ, ένα από τα πολλά)`isGrandadOf(Giannis, Babis)` που εισάγει ο χρήστης μέσω της γραμμής εντολών.
* **Απάντηση:** `Apotelesma: Alithes` (αν ισχύει)

### Σημειώσεις και Προσαρμογές
* **Τροποποίηση Αρχείων(merosB1):** Μπορείτε να αλλάξετε τα αρχεία `.txt` για διαφορετικά σενάρια.
* **Προσαρμογή του Main.java(merosB2):** Μπορείτε να τροποποιήσετε το `Main.java` για να αλλάξετε το όνομα του αρχείου της βάσης γνώσεων ή να προσθέσετε νέους κανόνες.

### Δημιουργοί
* Ονόματα: Γκοντόλιας Παναγιώτης Δημοσθένης, Γιαννούτσος Βησσαρίων Σωκράτης
* Αριθμοί Μητρώου: 3220031, 3220028
* Τμήμα: Πληροφορικής