package org.tyf;

import smile.manifold.IsoMap;


/**
 *   @desc : Smile IsoMap 等距映射
 *   @auth : tyf
 *   @date : 2022-03-08  16:38:14
*/
public class SmileIsoMap {

    // 等距映射
    // 该算法是一种用于降维的流形学习方法。

    public static void main(String[] args) {

        // 创建示例数据
        double[][] data = {
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        };

        // 设置参数
        int k = 2;
        int d = 2;
        boolean conformal = true;

        // 执行等距映射算法
        IsoMap isomap = IsoMap.of(data, k, d, conformal);

        // 打印结果
        System.out.println("Coordinates:");
        double[][] coordinates = isomap.coordinates;
        for (int i = 0; i < coordinates.length; i++) {
            System.out.println("Point " + isomap.index[i] + ": " + coordinates[i][0] + ", " + coordinates[i][1]);
        }


    }

}
