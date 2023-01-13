/**
 * This class contains instances of a cluster group,
 * it contains the number of the cluster, and it's RGB value
 *
 * @author Chloe Al-Frenn, 300211508
 */
public class Cluster {

    /**
     * Instance variables
     * Contains the identification number of the cluster
     * and the R G and B values which can each vary between 0 and 1
     */
    private int clusterNumber;
    private double R;
    private double G;
    private double B;
    private int counter;

    /**
     * Class constructor to define the identification number of the cluster
     */
    public Cluster(int clusterNumber){
        this.clusterNumber = clusterNumber;
        counter = 0;
    }

    /**
     * Instance methods of getters and setters
     */

    public int getClusterNumber() {
        return clusterNumber;
    }

    public double getR() {
        return R;
    }

    public double getG() {
        return G;
    }

    public double getB() {
        return B;
    }

    public int getCounter() {
        return counter;
    }

    public void setR(double r) {
        R = r;
    }

    public void setG(double g) {
        G = g;
    }

    public void setB(double b) {
        B = b;
    }

    public void incrementCounter() {
        counter++;
    }


}
