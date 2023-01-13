/*
 * Incomplete Experiment 1
 *
 * CSI2510 Algorithmes et Structures de Donnees
 * www.uottawa.ca
 *
 * Robert Laganiere, 2022
 *
 */

/**
 * Completed this class by adding the kd, lin and save method.
 *
 * This class will receive the epsilon value, a point for which we are looking for neighbors,
 * a file containing 3D points and a choice of algorithm. It will then read and store the values in an arrayList
 * After that it will use one of two algorithms, the first one using the k-d tree method and the second one using the linear method and
 * will create a new file containing the neighbouring points of the given point at the begging.
 * @author Chloe Al-Frenn, 300211508
 */

import java.util.List;
import java.util.ArrayList;

import java.io.*;
import java.util.Scanner;

public class Exp1 {

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
     * writes in a new file. We need to specify the filepath,
     * the values of Epsilon the algorithm we want to use and the
     * point for which we are looking for neighbors
     * @param args will read the values entered in terminal
     *
     * @throws Exception for the read method
     */
    public static void main(String[] args) throws Exception {

        String type = (args[0]);

        double eps= Double.parseDouble(args[1]);

        // reads the csv file
        List<Point3D> points= Exp1.read(args[2]);

        Point3D query= new Point3D(Double.parseDouble(args[3]),
                Double.parseDouble(args[4]),
                Double.parseDouble(args[5]));

        if(type.equals("kd")){
            kd(eps, points, query);
        }

        if(type.equals("lin")){
            lin(eps, points, query);

        }
    }

    /**
     * one of two algorithms this one is able to find neighbors using the kd tree method
     * @param eps epsilon value
     * @param points list of all points contained in the file
     * @param query the point for whom we are looking for neighbors
     */

    public static void kd(double eps, List<Point3D> points, Point3D query) {

        NearestNeighborsKD nk = new NearestNeighborsKD(points);
        List<Point3D> neighborskd= nk.rangeQuery(query,eps);
        save("kd", query, neighborskd);
        System.out.println("number of kd neighbors= " +neighborskd.size());
        for (Point3D point: neighborskd){
            System.out.println(point);
        }
    }

    /**
     * the second algorithm, this one is able to find neighbors using the linear method implemented in part1
     * @param eps the epsilon value
     * @param points list of all points contained in the file
     * @param query the poinf for whom we are looking for neighbors
     */
    public static void lin(double eps, List<Point3D> points, Point3D query)  {

        NearestNeighbors nn= new NearestNeighbors(points);
        List<Point3D> neighbors= nn.rangeQuery(query,eps);
        save("lin", query, neighbors);
        System.out.println("number of neighbors= "+neighbors.size());
        for (Point3D point: neighbors){
            System.out.println(point);
        }

    }
    /**
     * save method which writes a txt file of 3d points
     * will name the file accordingly to the point it is refering to and
     * to the method it is using
     */
    public static void save(String fileType, Point3D basePoint, List<Point3D> list){
        PrintWriter writer = null;
        String name = null;
        //TODO: create the 6 base points to compare to query
        Point3D point1 = new Point3D(-5.429850155, 0.807567048, -0.398216823);
        Point3D point2 = new Point3D(-12.97637373, 5.09061138, 0.762238889);
        Point3D point3 = new Point3D(-36.10818686, 14.2416184, 4.293473762);
        Point3D point4 = new Point3D(3.107437007, 0.032869335, 0.428397562);
        Point3D point5 = new Point3D(11.58047393, 2.990601868, 1.865463342);
        Point3D point6 = new Point3D(14.15982089, 4.680702457, -0.133791584);

        if (basePoint.getX() == point1.getX() && basePoint.getY() == point1.getY() && basePoint.getZ() == point1.getZ()){
            name = "pt1";
        } else if (basePoint.getX() == point2.getX() && basePoint.getY() == point2.getY() && basePoint.getZ() == point2.getZ()) {
            name = "pt2";
        } else if (basePoint.getX() == point3.getX() && basePoint.getY() == point3.getY() && basePoint.getZ() == point3.getZ()) {
            name = "pt3";
        } else if (basePoint.getX() == point4.getX() && basePoint.getY() == point4.getY() && basePoint.getZ() == point4.getZ()) {
            name = "pt4";
        }else if (basePoint.getX() == point5.getX() && basePoint.getY() == point5.getY() && basePoint.getZ() == point5.getZ()) {
            name = "pt5";
        } else if (basePoint.getX() == point6.getX() && basePoint.getY() == point6.getY() && basePoint.getZ() == point6.getZ()) {
            name = "pt6";
        }

        try {
            writer = new PrintWriter(name + "_"+ fileType + ".txt", "UTF-8");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        for (Point3D point: list){
            writer.println(point);
        }
        writer.close();
    }
}
