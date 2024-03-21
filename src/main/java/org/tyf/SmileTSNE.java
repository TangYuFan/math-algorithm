package org.tyf;

import smile.manifold.TSNE;


/**
 *   @desc : Smile TSNE t分布随机邻域嵌入
 *   @auth : tyf
 *   @date : 2022-03-08  16:38:14
*/
public class SmileTSNE {

    // t分布随机邻域嵌入

    public static void main(String[] args) {

        // 示例数据
        double[][] X = {
                {0.1, 0.2, 0.3},
                {0.4, 0.5, 0.6},
                {0.7, 0.8, 0.9}
                // 添加更多数据...
        };

        // 嵌入空间的维度
        int d = 2;

        // 创建 t-SNE 模型并拟合数据
        TSNE tsne = new TSNE(X, d);

        // 执行额外的迭代
        tsne.update(1000);

        // 获取嵌入空间中的坐标
        double[][] coordinates = tsne.coordinates;

        // 打印嵌入空间中的坐标
        for (int i = 0; i < coordinates.length; i++) {
            System.out.println("Data point " + i + ": (" + coordinates[i][0] + ", " + coordinates[i][1] + ")");
        }


    }

}
