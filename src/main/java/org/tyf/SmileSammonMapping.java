package org.tyf;

import smile.manifold.SammonMapping;

import java.util.Arrays;


/**
 *   @desc : Smile SammonMapping Sammon映射
 *   @auth : tyf
 *   @date : 2022-03-08  16:38:14
*/
public class SmileSammonMapping {

    // Sammon映射

    public static void main(String[] args) {

        // 创建一个非负的距离矩阵示例
        double[][] proximity = {
                {0, 0.5, 0.8},
                {0.5, 0, 0.6},
                {0.8, 0.6, 0}
        };

        // 调用 SammonMapping 的 of 方法进行映射
        SammonMapping mapping = SammonMapping.of(proximity);

        // 打印映射结果
        System.out.println("Stress: " + mapping.stress);
        System.out.println("Coordinates:");
        for (double[] coordinate : mapping.coordinates) {
            System.out.println(Arrays.toString(coordinate));
        }


    }

}
