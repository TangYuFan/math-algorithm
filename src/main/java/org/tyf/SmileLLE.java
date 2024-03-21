package org.tyf;

import smile.manifold.LLE;


/**
 *   @desc : Smile LLE 局部线性嵌入
 *   @auth : tyf
 *   @date : 2022-03-08  16:38:14
*/
public class SmileLLE {

    // 局部线性嵌入

    public static void main(String[] args) {

        // 示例数据
        double[][] data = {
                {1.0, 2.0, 3.0 , 1},
                {4.0, 5.0, 6.0 , 1},
                {7.0, 8.0, 9.0 , 1}
                // 添加更多数据...
        };

        int k = 2; // k-nearest neighbor
        int d = 2; // 目标维度

        // 运行 LLE 算法
        LLE lle = LLE.of(data, k, d);

        // 获取降维后的坐标
        double[][] coordinates = lle.coordinates;
        System.out.println("降维后的坐标：");
        for (double[] coordinate : coordinates) {
            for (double val : coordinate) {
                System.out.print(val + " ");
            }
            System.out.println();
        }


    }

}
