package org.tyf;

import org.apache.commons.math3.filter.DefaultMeasurementModel;
import org.apache.commons.math3.filter.DefaultProcessModel;
import org.apache.commons.math3.filter.KalmanFilter;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


/**
 *   @desc : 实现二阶卡尔曼滤波
 *   @auth : tyf
 *   @date : 2022-02-21  14:49:03
 */
public class KalmanFilterLevel2 {

    // 系统矩阵 2*2 矩阵表示状态空间维数为2
    private static RealMatrix A = MatrixUtils.createRealMatrix(new double[][]{{1, 0}, {0, 1}});
    // 控制输入矩阵
    private static RealMatrix B = MatrixUtils.createRealMatrix(new double[][]{{0, 0}, {0, 0}});
    // 过程噪声协方差矩阵
    private static RealMatrix Q = MatrixUtils.createRealMatrix(new double[][]{{0.01, 0}, {0, 0.01}});
    // 测量矩阵
    private static RealMatrix H = MatrixUtils.createRealMatrix(new double[][]{{1, 0}, {0, 1}});
    // 测量噪声协方差矩阵
    private static RealMatrix R = MatrixUtils.createRealMatrix(new double[][]{{0.1, 0}, {0, 0.1}});


    // 展示原始位置和预测的位置
    public static void show(List<double[]> data,List<double[]> data2){

        XYSeries series1 = new XYSeries("Original");
        XYSeries series2 = new XYSeries("Predicted");

        for (double[] point : data) {
            series1.add(point[0], point[1]);
        }

        for (double[] point : data2) {
            series2.add(point[0], point[1]);
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
        dataset.addSeries(series2);

        JFreeChart chart = ChartFactory.createScatterPlot(
                "Kalman Filter",
                "X",
                "Y",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        ChartFrame frame = new ChartFrame("Kalman Filter", chart);
        frame.setSize(600, 400);
        frame.setVisible(true);

    }

    public static void main(String[] args) {

        // 初始化 KalmanFilter
        KalmanFilter kalmanFilter = new KalmanFilter(
                new DefaultProcessModel(A, B, Q, null, null),
                new DefaultMeasurementModel(H, R)
        );

        Random r = new Random();

        // 随机产生观测值以 xy 坐标表示位置、每个样本随机向 xy四个方向移动
        double lastx = 0;
        double lasty = 0;
        List<double[]> data = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            double x = r.nextInt(100)>50?lastx+1:lastx-1;
            double y = r.nextInt(100)>50?lasty+1:lasty-1;
            // 更新上一次位置
            lastx = x;
            lasty = y;
            data.add(new double[]{x,y});
        }

        // 遍历每个新的观测值,进行修正
        List<double[]> data2 = new ArrayList<>();
        data.stream().forEach(n->{
            // 预测下一个状态
            kalmanFilter.predict();
            // 更新状态
            kalmanFilter.correct(n);
            // 输出估计的状态
            double[] estimatiom = kalmanFilter.getStateEstimation();
            System.out.println("观测值:"+Arrays.toString(n)+",预测值:"+Arrays.toString(estimatiom));
            data2.add(estimatiom);
        });

        // 显示
        show(data,data2);

    }

}
