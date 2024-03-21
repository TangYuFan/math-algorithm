package org.tyf;

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

/**
 *   @desc : apache-math3 多元最小二乘线性回归
 *   @auth : tyf
 *   @date : 2022-02-22  10:54:04
*/
public class Math3OLSRegressionLevel3 {


    //y = b0* + b1*(x1^1) + b2*(x2^2) + b3*(x3^3)
    private static double calculateEstimation(double[] x, double[] coe) {
        double result = 0.0;
        for (int i = 0; i < coe.length; ++i) {
            if(i==0){
                //bo
                result += coe[i];
            }else{
                //b(n)*(x(n-1)^n)
                result += coe[i] * Math.pow(x[i-1], i);
            }
        }
        return result;
    }


    public static void main(String[] args) {


        // 三个自变量一个因变量
        // x1 x2 x3 y
        double[][] data = {
                {1, 2, 3, 10},
                {2, 3, 4, 12},
                {3, 4, 5, 15},
                {2, 4, 5, 15},
                {7, 4, 5, 15},
                {3, 8, 5, 15},
                {9, 4, 5, 16},
        };

        // 前三列是自变量
        double[][] x = new double[data.length][3];
        // 最后一列是因变量
        double[] y = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            x[i] = new double[]{data[i][0], data[i][1], data[i][2]};
            y[i] = data[i][3];
        }

        // 多元线性回归
        OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();
        // 拟合
        regression.newSampleData(y, x);

        // 回归系数
        double[] beta = regression.estimateRegressionParameters();

        // 输出回归系数
        System.out.println("Intercept:"+beta[0]+",Coefficient1:"+beta[1]+",Coefficient2:"+beta[2]+",Coefficient3:"+beta[3]);

        // 手动使用回归系数进行预测
        double[] xx = new double[]{9,8,7};
        double yy = beta[0] + beta[1] * xx[0] + beta[2] * xx[1] + beta[3] * xx[2];

        // math3中没有针对多项式回归专用工具,只能在 OLSMultipleLinearRegression
        // 如果拟合时 newSampleData 传入的 x1、x2、x3 是平方之后的则使用下面的公式实现多项式回归
        // y = b0* + b1*(x1^1) + b2*(x2^2) + b3*(x3^3)
        // 如果拟合时 newSampleData 传入的 x1、x2、x3 是平方之前的则使用下面的公式实现多元线性回归
        // y = b0* + b1*(x1) + b2*(x2) + b3*(x3)

    }


}
