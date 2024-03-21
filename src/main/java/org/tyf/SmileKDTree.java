package org.tyf;

import smile.neighbor.KDTree;
import smile.neighbor.Neighbor;


/**
 *   @desc : Smile KDTree k-d树 临近搜索
 *   @auth : tyf
 *   @date : 2022-03-08  16:38:14
*/
public class SmileKDTree {

    // k-d树 临近搜索

    public static void main(String[] args) {


// Sample data (replace with your own dataset)
        double[][] dataPoints = {
                {2, 3},
                {5, 7},
                {8, 1},
                {4, 9},
                {1, 6}
        };

        // Create KD-Tree
        KDTree kdTree = KDTree.of(dataPoints);

        // Test 1: 最近邻搜索（kNN）：
        double[] queryPoint = {3, 5};
        Neighbor<double[], String> nearestNeighbor = kdTree.nearest(queryPoint);

        System.out.println("Test 1: Nearest Neighbor Search");
        System.out.println("Query Point: " + java.util.Arrays.toString(queryPoint));
        System.out.println("Nearest Neighbor: " + nearestNeighbor);
        System.out.println();

        // Test 2: 最近邻搜索（kNN）：
        int k = 3;
        Neighbor<double[], String>[] kNearestNeighbors = kdTree.search(queryPoint, k);

        System.out.println("Test 2: K-Nearest Neighbors Search");
        System.out.println("Query Point: " + java.util.Arrays.toString(queryPoint));
        System.out.println("K-Nearest Neighbors: " + java.util.Arrays.toString(kNearestNeighbors));
        System.out.println();

        // Test 3: 半径最近邻搜索（RNN）
        double radius = 3.0;
        java.util.List<Neighbor<double[], String>> radiusNeighbors = new java.util.ArrayList<>();
        kdTree.search(queryPoint, radius, radiusNeighbors);

        System.out.println("Test 3: Radius Neighbors Search");
        System.out.println("Query Point: " + java.util.Arrays.toString(queryPoint));
        System.out.println("Radius Neighbors: " + radiusNeighbors);
    }

}
