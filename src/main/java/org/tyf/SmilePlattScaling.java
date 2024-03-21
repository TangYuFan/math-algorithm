package org.tyf;

import smile.classification.PlattScaling;


/**
 *   @desc : Smile PlattScaling Platt缩放
 *   @auth : tyf
 *   @date : 2022-03-08  16:38:14
*/
public class SmilePlattScaling {

    // Platt缩放
    // 用于将分类器的输出转换为类别的后验概率估计。
    // Platt Scaling 的基本思想是通过拟合一个 logistic 回归模型来调整分类器的分数，从而得到类别的概率估计。

    public static void main(String[] args) {

        // 生成一些示例数据
        double[] scores = {1.2, 0.5, -0.8, 2.1, -1.5};
        int[] labels = {1, 0, 1, 1, 0};

        // 使用 Platt Scaling 训练模型
        PlattScaling scaling = PlattScaling.fit(scores, labels);


        // Platt Scaling 模型将分类器输出转换为概率
        for (double score : scores) {
            double probability = scaling.scale(score);
            System.out.println("Score: " + score + ", Probability: " + probability);
        }

    }

}
