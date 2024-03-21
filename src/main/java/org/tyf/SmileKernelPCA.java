package org.tyf;

import smile.data.DataFrame;
import smile.feature.extraction.KernelPCA;
import smile.math.kernel.GaussianKernel;
import smile.math.matrix.Matrix;

import java.util.Arrays;


/**
 *   @desc : Smile KernelPCA 主成分分析
 *   @auth : tyf
 *   @date : 2022-03-08  16:38:14
*/
public class SmileKernelPCA {

    // 主成分分析

    public static void main(String[] args) {

        // 示例数据
        double[][] data = {
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
                // 添加更多数据...
        };

        // 示例 DataFrame
        DataFrame df = DataFrame.of(data);

        // 创建核函数
        GaussianKernel kernel = new GaussianKernel(0.1);

        // 拟合 Kernel PCA 模型
        KernelPCA kpca = KernelPCA.fit(df, kernel, 2);

        System.out.println("变换矩阵：");
        System.out.println(kpca.projection);

        // 降维 Kernel PCA 转换
        System.out.println("降维：");
        double[][] transformed = kpca.apply(df.toArray());
        for (int i = 0; i < transformed.length; i++) {
            System.out.println(Arrays.toString(transformed[i]));
        }


    }

}
