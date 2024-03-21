package org.tyf;

import smile.manifold.MDS;

import java.util.Arrays;


/**
 *   @desc : Smile MDS 多维尺度分析
 *   @auth : tyf
 *   @date : 2022-03-08  16:38:14
*/
public class SmileMDS {

    // 多维尺度分析  降维

    public static void main(String[] args) {

        // 示例数据
        double[][] proximity = {
                {0.0, 0.3, 0.5, 0.7},
                {0.3, 0.0, 0.2, 0.4},
                {0.5, 0.2, 0.0, 0.6},
                {0.7, 0.4, 0.6, 0.0}
                // 添加更多数据...
        };

        // 计算 MDS
        MDS mds = MDS.of(proximity, 2);

        // 打印主坐标
        System.out.println("主坐标：");
        for (int i = 0; i < mds.coordinates.length; i++) {
            System.out.println(Arrays.toString(mds.coordinates[i]));
        }

        // 打印方差比例
        System.out.println("方差比例：");
        System.out.println(Arrays.toString(mds.proportion));

        // 打印特征值
        System.out.println("特征值：");
        System.out.println(Arrays.toString(mds.scores));




    }

}
