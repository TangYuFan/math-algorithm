package org.tyf;

import smile.clustering.MEC;


/**
 *   @desc : Smile MEC 最小熵聚类
 *   @auth : tyf
 *   @date : 2022-03-08  16:38:14
*/
public class SmileMEC {

    // 最小熵聚类
    // 该算法在不知道确切聚类数的情况下表现良好，能够正确揭示数据结构并有效识别异常值。

    public static void main(String[] args) {

        // 加载数据
        double[][] data = { {1, 2}, {2, 3}, {8, 7}, {8, 9}, {10, 8}, {10, 9} };

        // 设置参数
        int k = 2; // 聚类数
        double radius = 2.0; // 邻域半径

        // 执行最小熵聚类
        MEC<double[]> mec = MEC.fit(data, new smile.math.distance.EuclideanDistance(), k, radius);

        // 打印结果
        System.out.println(mec);



    }

}
