package org.tyf;

import smile.clustering.KModes;

import java.util.Arrays;


/**
 *   @desc : Smile KModes 聚类
 *   @auth : tyf
 *   @date : 2022-03-08  16:38:14
*/
public class SmileKModes {

    // 聚类

    public static void main(String[] args) {

        int[][] data = {
                {1, 0, 0, 9, 18, 21},
                {0, 1, 0, 0, 0, 9},
                {1, 1, 0, 9, 18, 20},
                {0, 0, 1, 1, 0, 9,},
                {1, 0, 1, 0, 0, 1,},
                {1, 1, 1, 3, 90, 21},
                {0, 1, 1, 7, 9, 10}
        };
        int k = 3;

        KModes kModes = KModes.fit(data,k);

        // 类中心
        for (int i = 0; i < kModes.centroids.length; i++) {
            System.out.println("kModes.centroids："+ Arrays.toString(kModes.centroids[i]));
        }

        System.out.println("Distortion："+kModes.distortion);

        // 所有输入样本的聚类结果
        System.out.println("Y："+Arrays.toString(kModes.y));

        // 打印类别
        System.out.println("样本聚类:");
        for (int i = 0; i < data.length; i++) {
            int[] x = data[i];
            int y = kModes.y[i];
            System.out.println("样本："+Arrays.toString(x)+"，类："+y);
        }

        // 预测
        System.out.println("预测:");
        for (int i = 0; i < data.length; i++) {
            int[] x = data[i];
            System.out.println("样本："+Arrays.toString(x)+"，类："+kModes.predict(x));
        }
    }

}
