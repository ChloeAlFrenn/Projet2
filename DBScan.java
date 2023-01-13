import java.io.*;
import java.util.*;
/**
 * This class contains the main method it also
 * reads a csv file containing points and stores it in an arrayList.
 * It will perform the DBSCAN algorithm on the points to find the amount of clusters,
 * then it will write into a new csv file the points, clusters and RGB values found.
 *
 * @author Chloe Al-Frenn, 300211508
 */
public class DBScan {

    /**
     * Instance variables
     * Contains an ArrayList of the 3DPoints contained in the file
     * Contains a value of epsilon which is the biggest distance separating two neighboring points.
     * Contains minPts which is the minimum amount of points to be considered a significant cluster
     * Contains cluster which is the counter for the amount clusters.
     */
    private List<Point3D> points = new ArrayList<Point3D>();
    private double eps = 1.4;
    private int minPts = 30;
    private int currentCluster = 0; //cluster counter
    private int noiseCounter = 0; // noise counter
    private static final int UNDEFINED = 0; //means it is undefined since the default value for an int is 0.
    private static final int NOISE = -1;
    List<Cluster> clusterList = new ArrayList<Cluster>(); //list of all the clusters created



    /**
     * Class constructor that accepts a list of Point3D
     */
    public DBScan(List<Point3D> points){
        this.points = points;
    }

    /**
     * Instance methods of setters do they return a value?
     */

    public void setEps(double eps) { //returns a value or not?
        this.eps = eps;
    }

    public void setMinPts(int minPts) { //returns a value or not?
        this.minPts = minPts;
    }

    /**
     * Executes the DBScan algorithm seen in the assignment.
     * Will iterate through all the Point3D contained in the points ArrayList.
     * It will check if the points are already in a cluster, or not. If they are not or are noise
     * then depending on the result of the rangeQuery method,
     * which checks if they have neighbors and how many,
     * they will be assigned a cluster.
     */
    public void findClusters(){
        NearestNeighbors neighborsFinder = new NearestNeighbors(points); //helps find the neighbors of the points
        for(int i = 0; i < points.size(); i++){
            if(points.get(i).getCluster() == UNDEFINED){
                List<Point3D> neighborsList = neighborsFinder.rangeQuery(points.get(i), eps); //Store in an array list the list of neighbors of the current point.
                if(neighborsList.size() < minPts){
                    points.get(i).setCluster(NOISE); //if the point doesn't have enough neighbors it becomes a cluster.
                } else {
                    currentCluster++; //gets the next cluster label
                    points.get(i).setCluster(currentCluster); //sets the label of the current point to the current cluster
                    Stack<Point3D> neighborsStack = new Stack<Point3D>();
                    neighborsStack.addAll(neighborsList); //adds all of the neighbors in a stack
                    while (!neighborsStack.isEmpty()) {
                        Point3D neighboringPoint = neighborsStack.pop();
                        if (neighboringPoint.getCluster() == NOISE) {
                            neighboringPoint.setCluster(currentCluster);
                            List<Point3D> neighborsNewList = neighborsFinder.rangeQuery(neighboringPoint, eps); //search if the neighbors have neighbors, if yes they will be added to the stack.
                            if (neighborsNewList.size() >= minPts) {
                                neighborsStack.addAll(neighborsNewList); //add the new list of neighbors
                            }
                        }
                        if (neighboringPoint.getCluster() == UNDEFINED) {
                            neighboringPoint.setCluster(currentCluster);
                            List<Point3D> neighborsNewList = neighborsFinder.rangeQuery(neighboringPoint, eps); //search if the neighbors have neighbors, if yes they will be added to the stack.
                            if (neighborsNewList.size() >= minPts) {
                                neighborsStack.addAll(neighborsNewList); //add the new list of neighbors
                            }
                        }
                    }
                }
            }

        }
    }

    public int getNumberOfClusters(){
        return currentCluster;
    }

    public List<Point3D> getPoints() {
        return points;
    }

    public int getNoiseCounter() {
        return noiseCounter;
    }

    /**
     * reads a CSV file using a BufferedReader and returns a list of Point3D
     * @param filename input file given by the main method
     * @return a list containing all the points stored in the csv file
     */
    public static List<Point3D> read(String filename)  {
        List<Point3D> list = new ArrayList<Point3D>();
        Point3D point;
        String line;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            line = reader.readLine();//skip the first two values.
            line = reader.readLine();
            while((line = reader.readLine()) != null){
                String[] values = line.split(",");
                double[] doubleValues = Arrays.stream(values).mapToDouble(Double::parseDouble).toArray();
                point = new Point3D(doubleValues[0], doubleValues[1], doubleValues[2]);
                list.add(point);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return list;

    }

    /**
     * writes in a file:
     * the points, their cluster label and their associated RGB color.
     * uses the Cluster class to get the RGB values of each cluster.
     * The name of the file follows this format: filename_clusters_eps_minPts_nclusters.
     * @param filename same file that has been read by the read method previously
     */
    public void save(String filename){
        filename = filename.substring(0,(filename.length())-4); //removes the.csv
        File file = new File(filename +"_clusters_"+ eps +"_" +minPts+ "_" +getNumberOfClusters() + "clusters.csv");
        try {
            FileWriter output = new FileWriter(file, true);
            BufferedWriter writer = new BufferedWriter(output);
            PrintWriter printWriter = new PrintWriter(writer);
            List<Point3D> savedList = getPoints();
            //List<Cluster> clusterList = new ArrayList<Cluster>();
            Cluster cluster;
            clusterList.add(null); //so we can start the list of cluster at index one which will be the same as the first cluster.
            Random r = new Random();
            double x,y,z,R,G,B;
            int C;

            printWriter.println("x,y,z,C,R,G,B"); //header
            printWriter.println("0,0,0,0,0,0,0"); //second line which was included in the original file
            for(int j=1; j<=getNumberOfClusters(); j++){ //creates a clusterVariable and gives it an RGB LABEL
                cluster = new Cluster(j);
                cluster.setR(r.nextDouble()); //Returns the next pseudorandom, uniformly distributed double value between 0.0 and 1.0 from this random number generator's sequence.
                cluster.setG(r.nextDouble());
                cluster.setB(r.nextDouble());
                clusterList.add(cluster);
            }
            for(int i=0; i< savedList.size(); i++){
                x = savedList.get(i).getX();
                y = savedList.get(i).getY();
                z = savedList.get(i).getZ();
                C = savedList.get(i).getCluster();
                if(savedList.get(i).getCluster() == -1){
                    noiseCounter++;
                    R = 0;
                    G = 0;
                    B = 0;
                } else {
                    R = clusterList.get(C).getR();
                    G = clusterList.get(C).getG();
                    B = clusterList.get(C).getB();
                    clusterList.get(C).incrementCounter();

                }
                printWriter.println(x+","+y+","+z+","+C+","+R+","+G+","+B);
            }
            printWriter.flush();
            printWriter.close();

            System.out.println("File saved under the name: "+ file.getName() + "\n");

        } catch (IOException e){
            e.printStackTrace();
            System.out.println("File not saved");
        }


    }

    /**
     * main method which can start the program,
     * which will call the methods who
     * read the file, execute the algorithm and
     * writes in a new file. We need to specify the filepath,
     * the values of Epsilon and the minimum amount of point in each cluster.
     * Will print the size of each cluster group from biggest to smallest.
     * Will also print the number of noise clusters.
     * they should each have 36, 33, 46 clusters
     * Computes the time taken to run the program
     * Write in the terminal: java DBScan filename.csv eps minPts
     * @param args will read the values entered in terminal
     */

    public static void main(String[] args) {
        long startTime = System.nanoTime();
        String filename = "./" + args[0];
        DBScan scan = new DBScan(DBScan.read(filename));
        scan.setEps(Double.parseDouble(args[1]));
        scan.setMinPts(Integer.parseInt(args[2]));
        scan.findClusters();
        scan.save(filename);
        scan.printClusterSize();

        System.out.println("Amount of noise clusters:" +scan.getNoiseCounter()+"\n");
        long endTime = System.nanoTime();
        long duration = (endTime - startTime)/1000000;// in milliseconds.
        System.out.println("the time taken using the first DBScan method is:"+ duration + "ms");

    }

    /**
     * will order the list of clusters
     * from the one who has the biggest number of repetition to the smallest,
     * will then print the list of each cluster and their amount
     * respecting the order
     */
    public void printClusterSize() {
        //orders the cluster from biggest to smallest
        List<Cluster> sortingMyClusters = new ArrayList<Cluster>();
        sortingMyClusters.addAll(clusterList);

        Collections.sort(sortingMyClusters, new Comparator<Cluster>() {
            public int compare(Cluster c1, Cluster c2)  {
                if(c2 == null || c1 == null)
                    return 0;
                return c2.getCounter() - c1.getCounter(); // c2-c1 to get biggest to smallest
            }
        });

        //prints the clusters
        System.out.println("After Sorting from biggest to smallest cluster:");
        for (int i = 1; i < sortingMyClusters.size(); i++) {
            System.out.println("Cluster number: " + sortingMyClusters.get(i).getClusterNumber()+" Amount: " + sortingMyClusters.get(i).getCounter());
        }
    }
}

