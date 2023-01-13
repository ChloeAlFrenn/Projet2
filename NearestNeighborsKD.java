
import java.util.ArrayList;
import java.util.List;

/**
 * This class stores a List of all the points that were contained in the file.
 * It also provides a method which is able to determine the neighbors of each points in the ArrayList.
 *
 * @author Chloe Al-Frenn, 300211508
 */

public class NearestNeighborsKD {

    protected KDTree kdTree;

    /**
     * Class constructor to store all the points contained in the list into a k-d tree structure
     */
    NearestNeighborsKD(List<Point3D> list) {
        kdTree = new KDTree();
        List<Point3D> neighbors;
        for (Point3D p : list) {
            kdTree.add(p);
        }
    }
    /**
     * creates an arrayList and calls the other rangeQuery method to look
     * for neighbors inside the k-d tree
     * @param p the point for whom we are looking for neighbors.
     * @param eps epsilon which is the biggest distance separating two neighboring points.
     * @return neighbors, an ArrayList which stores neighboring points.
     */
    public List<Point3D> rangeQuery(Point3D p, double eps) {
        List<Point3D> neighbors = new ArrayList<Point3D>();
        rangeQuery(p, eps, neighbors, kdTree.root());
        return neighbors;
    }

    /**
     * a recursive method which searches for neighbouring points contained inside the
     * k-d tree
     * @param P  the point for whom we are looking for neighbors.
     * @param eps epsilon which is the biggest distance separating two neighboring points.
     * @param N an ArrayList which stores neighboring points.
     * @param node a node contained in the k-d tree which stores points
     * @return N, an ArrayList which stores neighboring points.
     */

    private List<Point3D> rangeQuery(Point3D P, double eps, List<Point3D> N, KDTree.KDnode node) {
        if (node == null)
            return N;
        if (P.distance(node.pt) < eps)
            N.add(node.pt);
        if (P.get(node.axis) - eps <= node.value)
        rangeQuery(P, eps, N, node.left);
        if (P.get(node.axis) + eps > node.value)
            rangeQuery(P, eps, N, node.right);
        return N;
    }
}









