package org.tyf;

import smile.clustering.HierarchicalClustering;
import smile.clustering.linkage.Linkage;
import smile.clustering.linkage.SingleLinkage;

import java.util.Arrays;


/**
 *   @desc : Smile HierarchicalClustering 层次聚类
 *   @auth : tyf
 *   @date : 2022-03-08  16:38:14
*/
public class SmileHierarchicalClustering {

    // 层次聚类

    public static void main(String[] args) {

        // 生成示例数据
        double[][] data = {
                {1, 2},
                {2, 3},
                {3, 4},
                {10, 12},
                {11, 13},
                {12, 14}
        };

        // 构建距离矩阵
        double[][] proximityMatrix = new double[data.length][data.length];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data.length; j++) {
                double distance = Math.sqrt(Math.pow(data[i][0] - data[j][0], 2) + Math.pow(data[i][1] - data[j][1], 2));
                proximityMatrix[i][j] = distance;
            }
        }

        // 使用 Ward Linkage 方法进行层次聚类
        HierarchicalClustering clustering = HierarchicalClustering.fit(new SingleLinkage(proximityMatrix));



        // 将聚类结果划分成两个簇
        int[] labels = clustering.partition(2);

        // 打印聚类结果
        System.out.println("Cluster Labels: " + Arrays.toString(labels));


    }

}
