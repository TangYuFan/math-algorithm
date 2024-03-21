package org.tyf;

import smile.neighbor.MPLSH;
import smile.neighbor.Neighbor;


/**
 *   @desc : Smile MPLSH 多平面局部敏感哈希 临近搜索
 *   @auth : tyf
 *   @date : 2022-03-08  16:38:14
*/
public class SmileMPLSH {

    // 多平面局部敏感哈希 临近搜索


    // 数据生成
    public static double[][] genData(){
        double[][] d = new double[10000][];
        return d;
    }

    public static void main(String[] args) {

        int d = 256;
        int L = 100;
        int k = 3;
        double w = 4.0;
        MPLSH<double[]> lsh = new MPLSH<>(d,L,k,w);

        // 示例数据
        double[][] trainingData = genData();  // 训练数据集，示例中包含1000个数据点

        // 训练MPLSH模型
        lsh.fit(lsh, trainingData, 8.0, 2500);

        // 查询条件
        double[] query = new double[5];

        // 查询最近邻
        Neighbor<double[], ?> nearestNeighbor1 = lsh.nearest(query);
        System.out.println("Nearest Neighbor1: " + nearestNeighbor1);

        // 查询多个最近邻
        Neighbor<double[], ?>[] kNearestNeighbors = lsh.search(query, 5);
        System.out.println("K Nearest Neighbors: ");
        for (Neighbor<double[], ?> neighbor : kNearestNeighbors) {
            System.out.println(neighbor);
        }

        // 在指定半径范围内查找邻居
        double radius = 10.0;
        java.util.List<Neighbor<double[], double[]>> neighborsInRange = new java.util.ArrayList<>();
        lsh.search(query, radius, neighborsInRange);
        System.out.println("Neighbors in Range: ");
        for (Neighbor<double[], ?> neighbor : neighborsInRange) {
            System.out.println(neighbor);
        }

    }

}
