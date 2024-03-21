package org.tyf;

import smile.manifold.LaplacianEigenmap;


/**
 *   @desc : Smile LaplacianEigenmap 拉普拉斯特征映射
 *   @auth : tyf
 *   @date : 2022-03-08  16:38:14
*/
public class SmileLaplacianEigenmap {

    // 拉普拉斯特征映射
    // 该算法通过最近邻邻接图的拉普拉斯矩阵，计算数据集的低维表示

    public static void main(String[] args) {

        // 加载数据
        double[][] data = { {1, 2}, {2, 3}, {8, 7}, {8, 9}, {10, 8}, {10, 9} };

        // 设置参数
        int k = 3; // k-nearest neighbor
        int d = 2; // 输出维度
        double t = 1.0; // 高斯核宽度

        // 执行拉普拉斯特征映射
        LaplacianEigenmap eigenmap = LaplacianEigenmap.of(data, k, d, t);

        // 输出结果
        System.out.println("Coordinates:");
        double[][] coordinates = eigenmap.coordinates;
        for (int i = 0; i < coordinates.length; i++) {
            for (int j = 0; j < coordinates[i].length; j++) {
                System.out.print(coordinates[i][j] + " ");
            }
            System.out.println();
        }


    }

}
