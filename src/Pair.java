/**
 * This class creates an object Pair which holds an uppercase character
 * and its probability value.
 * This is Step 2 of the assignment.
 */

public class Pair implements Comparable<Pair> {
    // instance variables
    private char value;
    private double prob;

    // constructors
    public Pair(char value, double prob) {
        this.value = value;
        this.prob = prob;
    }

    // getters and setters
    public char getValue() {
        return value;
    }

    public void setValue(char value) {
        this.value = value;
    }

    public double getProb() {
        return prob;
    }

    public void setProb(double prob) {
        this.prob = prob;
    }

    /**
     * Overridden toString method
     * @return    basic String describing pair
     */
    @Override
    public String toString() {
        return "Pair{" +
                "value=" + value +
                ", prob=" + prob +
                '}';
    }

    /**
     * Overriden compareTo method, specifically for probabilities
     * @param p the object to be compared.
     * @return  the different between the two
     */
    @Override
    public int compareTo(Pair p) {
        return Double.compare(this.getProb(), p.getProb());
    }
}
