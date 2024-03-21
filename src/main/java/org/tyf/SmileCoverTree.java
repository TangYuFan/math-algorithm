package org.tyf;

import smile.math.distance.EuclideanDistance;
import smile.neighbor.CoverTree;


/**
 *   @desc : Smile CoverTree 覆盖树 临近搜索
 *   @auth : tyf
 *   @date : 2022-03-08  16:38:14
*/
public class SmileCoverTree {

    // 覆盖树 临近搜索

    public static void main(String[] args) {

        // 创建一些示例数据
        double[][] data = {
                {1.0, 2.0},
                {3.0, 4.0},
                {5.0, 6.0},
                {7.0, 8.0},
                {9.0, 10.0}
        };

        // 创建一个 CoverTree 对象
        CoverTree<double[], double[]> coverTree = new CoverTree<>(
                data,
                data,
                new EuclideanDistance()
        );

        // 执行临近搜索
        double[] query = {2.0, 3.0};
        int k = 3; // 选择最近的 3 个邻居
        smile.neighbor.Neighbor<double[], double[]>[] neighbors = coverTree.search(query, k);

        // 输出结果
        for (smile.neighbor.Neighbor<double[], double[]> neighbor : neighbors) {
            System.out.println("Nearest neighbor: " + neighbor.key.toString());
            System.out.println("Distance: " + neighbor.distance);
        }

    }

}
