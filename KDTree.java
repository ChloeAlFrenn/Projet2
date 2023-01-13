/**
 * This class contains represents a k-d tree which contains instances of k-d nodes
 * it also provides methods to insert new nodes to the existing k-d tree
 *
 * 4
 */
public class KDTree {

    final int DIM = 3;

    /**
     * Internal class of k-d node composed of Point3D, an axis, values to compare to each other and the other nodes
     * to the left and right.
     */
    public class KDnode {
        public Point3D pt;
        public int axis;
        public double value;
        public KDnode left;
        public KDnode right;

        /**
         * Class constructor to define the values of point contained in the node, the axis, the value that is going to be compared
         * and the left and right nodes which are left null at this stage.
         */
        public KDnode(Point3D pt, int axis) {
            this.pt= pt;
            this.axis= axis;
            this.value= pt.get(axis);
            left= right= null;
        }

    }
    private KDnode root;

    /**
     * Class constructor to construct an empty tree
     * containing a root.
     */
    public KDTree() {
        root= null;
    }

    /**
     * method to add a point to the k-d tree
     * it will call the insert method to be able to insert it accordingly to the tree's algorithm
     * @param p the point to be added
     */
    public void add(Point3D p){
        if (root == null)
          root = insert(p, null, 0);
        insert(p, root, root.axis);
    }

    /**
     * method which returns the value of the root
     */
    public KDnode root(){
        return root;
    }

    /**
     * recursive method to insert a point inside the kd tree.
     * if the previous node is null it will create a new node containing the point and set it to the node value
     * if not it will compare the point to the previous node and following the k-d tree algorithm
     * will determine if left the point should be right child depending on the axis and the value of the node
     * @param P the point to be inserted
     * @param node the previous node
     * @param axis the axis to compare the node
     * @return node the k-d node which has been added to the tree.
     */
    private KDnode insert(Point3D P, KDnode node, int axis) {
        if (node == null)
            node = new KDnode(P, axis);
        else if (P.get(axis) <= node.value)
            node.left = insert(P, node.left, (axis+1) % DIM);
        else
            node.right = insert(P, node.right, (axis+1) % DIM);
        return node;
    }

}
