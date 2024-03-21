package org.tyf;

import smile.neighbor.LSH;
import smile.neighbor.Neighbor;

import java.util.List;


/**
 *   @desc : Smile LSH 局部敏感哈希 临近搜索
 *   @auth : tyf
 *   @date : 2022-03-08  16:38:14
*/
public class SmileLSH {

    // 局部敏感哈希 临近搜索

    public static void main(String[] args) {

        // 示例数据
        double[][] keys = {
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
                // 添加更多数据...
        };

        String[] data = {
                "data1",
                "data2",
                "data3"
                // 添加更多数据...
        };

        // 创建 LSH
        LSH<String> lsh = new LSH<>(keys, data, 0.1);

        // 查询最近邻
        double[] query = {1.0, 2.1, 3.0};
        Neighbor<double[], String> nearestNeighbor = lsh.nearest(query);
        System.out.println("最近邻：");
        System.out.println(nearestNeighbor);

        // 搜索最近的 k 个邻居
        int k = 2;
        Neighbor<double[], String>[] neighbors = lsh.search(query, k);
        System.out.println("最近的 " + k + " 个邻居：");
        for (Neighbor<double[], String> neighbor : neighbors) {
            System.out.println(neighbor);
        }


        // 搜索半径内的邻居
        double radius = 0.5;
        java.util.List<Neighbor<double[], String>> radiusNeighbors = new java.util.ArrayList<>();
        lsh.search(query, radius,radiusNeighbors);
        System.out.println("半径 " + radius + " 内的邻居：");
        for (Neighbor<double[], String> neighbor : radiusNeighbors) {
            System.out.println(neighbor);
        }


    }

}
