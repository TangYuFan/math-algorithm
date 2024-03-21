package org.tyf;

import smile.plot.swing.Point;
import smile.plot.swing.ScatterPlot;
import smile.timeseries.AR;

import java.awt.*;
import java.util.Arrays;
import java.util.Random;


/**
 *   @desc : Smile AR 自回归模型
 *   @auth : tyf
 *   @date : 2022-03-08  16:38:14
*/
public class SmileAR {

    // 自回归模型


    // 生成随机样本
    public static double[] genData(){
        int numPoints = 1000;
        double period = 100.0; // 周期
        double amplitude = 10.0; // 振幅
        double[] data = new double[numPoints];
        Random random = new Random();
        for (int i = 0; i < numPoints; i++) {
            // 生成带有周期性的随机数据，可以根据需要调整生成方式
            double angle = (2.0 * Math.PI * i) / period;
            data[i] = amplitude * Math.sin(angle) + random.nextGaussian(); // 正弦函数 + 随机噪声
        }
        return data;
    }

    public static void main(String[] args) throws Exception{


        // AR.fit();
        // AR.ols();

        double[] data = genData();

        int p = 10;
        AR ar = AR.ols(data,p);

        // 打印模型信息
        System.out.println(ar);

        // 进行一步预测
        double[] forecast = ar.forecast(300);
        System.out.println("1-Step Ahead Forecast:");
        System.out.println(Arrays.toString(forecast));

        // 可视化、使用两种颜色的散点图进行可视化

        // 原始数据
        double[][] points1 = new double[data.length][];
        for (int i = 0; i < data.length; i++) {
            points1[i] = new double[]{i,data[i]};
        }

        // 预测数据
        double[][] points2 = new double[forecast.length][];
        for (int i = 0; i < forecast.length; i++) {
            // x从原始数据最后一个点进行延申
            points2[i] = new double[]{i+data.length,forecast[i]};
        }

        Point p1 = new Point(points1, 'o', Color.RED);
        Point p2 = new Point(points2, 'o', Color.BLUE);
        ScatterPlot plot = new ScatterPlot(p1,p2);
        plot.canvas().window();

    }

}
