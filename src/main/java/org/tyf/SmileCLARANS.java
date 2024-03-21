package org.tyf;

import smile.clustering.CLARANS;
import smile.math.distance.EuclideanDistance;

import java.util.Arrays;


/**
 *   @desc : Smile CLARANS 聚类
 *   @auth : tyf
 *   @date : 2022-03-08  16:38:14
*/
public class SmileCLARANS {

    // 聚类
    // CLARANS算法是对k-means算法的一种改进，而不是计算每个聚类中项的平均值

    public static void main(String[] args) {

        // 加载数据
        double[][] data = { {1, 2}, {2, 3}, {8, 7}, {8, 9}, {10, 8}, {10, 9} };

        // 设置参数
        int k = 2; // 聚类数
        int maxNeighbor = 3; // 最大邻居数

        // 执行聚类
        CLARANS<double[]> clarans = CLARANS.fit(data, new EuclideanDistance(), k, maxNeighbor);

        // 输出结果
        System.out.println("Distortion: " + clarans.distortion);


        System.out.println("聚类：");
        for (int i = 0; i < data.length; i++) {
            System.out.println("样本："+ Arrays.toString(data[i])+"，聚类："+clarans.y[i]);
        }

        System.out.println("预测：");
        for (int i = 0; i < data.length; i++) {
            System.out.println("样本："+ Arrays.toString(data[i])+"，聚类："+ clarans.predict(data[i]));
        }

    }

}
