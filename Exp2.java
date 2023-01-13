import java.io.File;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * This class will receive the epsilon value, the amount of steps in between each point
 * a file containing 3D points and a choice of algorithm. It will then read and store the values in an arrayList
 * After that it will use one of two algorithms, the first one using the k-d tree method and the second one using the linear method
 * which will compute the amount of time it takes to find all the neighbouring points of every step point
 * @author Chloe Al-Frenn, 300211508
 */
public class Exp2 {

    // reads a csv file of 3D points (rethrow exceptions!)
    /**
     * read method which reads a csv file of 3d points
     */
    public static List<Point3D> read(String filename) throws Exception {

        List<Point3D> points= new ArrayList<Point3D>();
        double x,y,z;

        Scanner sc = new Scanner(new File(filename));
        // sets the delimiter pattern to be , or \n \r
        sc.useDelimiter(",|\n|\r");

        // skipping the first line x y z
        sc.next(); sc.next(); sc.next();

        // read points
        while (sc.hasNext())
        {
            x= Double.parseDouble(sc.next());
            y= Double.parseDouble(sc.next());
            z= Double.parseDouble(sc.next());
            points.add(new Point3D(x,y,z));
        }

        sc.close();  //closes the scanner

        return points;
    }

    /**
     * main method which can start the program,
     * which will call the methods to
     * read the file, execute the algorithm and
     * compute the time taken to do so. We need to specify the filepath,
     * the values of Epsilon the algorithm we want to use and the
     * amount of steps in between each point we want to find the neighbors of
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        String type = (args[0]);

        double eps= Double.parseDouble(args[1]);

        // reads the csv file
        List<Point3D> points= Exp2.read(args[2]);

        int step = Integer.parseInt(args[3]);

        if(type.equals("kd")){
            kd(eps, points, step);
        }

        if(type.equals("lin")){
            lin(eps, points, step);

        }
    }

    /**
     * one of two algorithms this one is able to find neighbors using the kd tree method
     * it will then compute the time taken to find the neighbours of every point at the step
     * @param eps epsilon value
     * @param points list of all points contained in the file
     * @param step amount of step in between each points we want to calculate
     */
    public static void kd(double eps, List<Point3D> points, int step) {

        NearestNeighborsKD nk = new NearestNeighborsKD(points);
        double average =0;
        Point3D basePoint;

       // long startTime = System.nanoTime();
        for(int i=0; i<points.size(); i=i+step) {
            long startTime = System.nanoTime();
            basePoint = points.get(i);
            nk.rangeQuery(basePoint, eps);
            long endTime = System.nanoTime();
            double duration = (double)(endTime - startTime)/1000000;
            average = average + duration;
        }
        average = average/(points.size()/step);
        System.out.println("the average time taken using the kd method to find the neighbors of every "+step+"th point is "+ average +" ms");

    }

    /**
     * the second algorithm, this one is able to find neighbors using the linear method implemented in part1
     * it will then compute the time taken to find the neighbours of every point at the step
     * @param eps epsilon value
     * @param points list of all points contained in the file
     * @param step amount of step in between each points we want to calculate
     */
    public static void lin(double eps, List<Point3D> points, int step)  {
        NearestNeighbors nn= new NearestNeighbors(points);
        double average =0;
        Point3D basePoint;

        for(int i=0; i<points.size(); i=i+step) {
            long startTime = System.nanoTime();
            basePoint = points.get(i);
            nn.rangeQuery(basePoint, eps);
            long endTime = System.nanoTime();
            double duration = (double)(endTime - startTime)/1000000;
            average = average + duration;
        }
        average = average/(points.size()/step);
        System.out.println("the average time taken using the lin method to find the neighbors of every "+step+"th point is "+ average +" ms");

    }

}
