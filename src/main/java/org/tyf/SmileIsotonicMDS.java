package org.tyf;

import smile.manifold.IsotonicMDS;


/**
 *   @desc : Smile IsotonicMDS 保序多维尺度分析
 *   @auth : tyf
 *   @date : 2022-03-08  16:38:14
*/
public class SmileIsotonicMDS {

    // 保序多维尺度分析
    // 实现了 Kruskal 的非度量多维尺度分析（Non-metric Multidimensional Scaling，简称 NMDS）算法。在非度量多维尺度分析中，假定仅考虑近似矩阵（即相似度矩阵中的排名顺序，而非实际的不相似度值）包含了重要信息。因此，最终配置中的距离应尽可能与原始数据的排名顺序相同。通常情况下，不可能完美地将数据重新缩放为距离，而是通过保序回归来找到关系。

    public static void main(String[] args) {

        // 示例数据
        double[][] proximity = {
                {0, 0.3, 0.4, 0.5},
                {0.3, 0, 0.2, 0.7},
                {0.4, 0.2, 0, 0.6},
                {0.5, 0.7, 0.6, 0}
        };

        // 使用 Smile 库中的 IsotonicMDS 进行保序多维尺度分析
        IsotonicMDS mds = IsotonicMDS.of(proximity);

        // 输出结果
        System.out.println("Stress: " + mds.stress);
        System.out.println("Coordinates:");
        for (int i = 0; i < mds.coordinates.length; i++) {
            for (int j = 0; j < mds.coordinates[i].length; j++) {
                System.out.print(mds.coordinates[i][j] + " ");
            }
            System.out.println();
        }


    }

}
