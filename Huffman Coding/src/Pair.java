/**
 * The Pair class represents a character-probability pair used in Huffman coding. Each Pair has a character (value) and
 * a probability. The class implements Comparable to enable comparison based on probabilities. It also provides
 * toString for easy printing.
 *
 */
public class Pair implements Comparable<Pair> {

    // instance variables
    private char value;
    private double prob;

    // Constructor
    public Pair(char value, double prob) {
        this.value = value;
        this.prob = prob;
    }

    // Getters
    public char getValue() {
        return value;
    }

    public double getProb() {
        return prob;
    }

    // Setters
    public void setValue(char value) {
        this.value = value;
    }

    public void setProb(double prob) {
        this.prob = prob;
    }

    // toString method for easy printing
    @Override
    public String toString() {
        return "Pair{" +
                "value=" + value +
                ", prob=" + prob +
                '}';
    }

    /*
    The compareTo method overrides the compareTo method
    of the Comparable interface.
    It compares Pair objects based on their probabilities.
    */
    @Override
    public int compareTo(Pair p) {
        return Double.compare(this.getProb(), p.getProb());
    }
}
