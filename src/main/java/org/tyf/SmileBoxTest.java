package org.tyf;

import smile.timeseries.BoxTest;


/**
 *   @desc : Smile BoxTest 盒式检验
 *   @auth : tyf
 *   @date : 2022-03-08  16:38:14
*/
public class SmileBoxTest {

    // 盒式检验
    // 用于执行盒式检验（Box Test）以判断时间序列的自相关是否存在。盒式检验是一种用于测试多个自相关系数是否为零的统计检验方法，通常用于检验时间序列的随机性和白噪声特性。

    public static void main(String[] args) {

        // 创建示例时间序列
        double[] series = {0.1, 0.2, 0.3, 0.4, 0.5};

        // 设置要测试的滞后阶数
        int lag = 1;

        // 执行Box-Pierce测试
        BoxTest boxPierceTest = BoxTest.pierce(series, lag);

        // 打印Box-Pierce测试结果
        System.out.println("Box-Pierce Test:");
        System.out.println(boxPierceTest);

        // 执行Ljung-Box测试
        BoxTest ljungBoxTest = BoxTest.ljung(series, lag);

        // 打印Ljung-Box测试结果
        System.out.println("Ljung-Box Test:");
        System.out.println(ljungBoxTest);


    }

}
