package org.tyf;
 
 
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 *   @desc : 实现一阶卡尔曼滤波
 *   @auth : tyf
 *   @date : 2022-05-16  18:21:26
 */
public class KalmanFilterLevel1 {
 
    /**Kalman Filter*/
    private Integer predict; //观察数据值
    private Integer current; //观察数据值的下一条数据
    private Integer estimate;//每一次计算出的-最终估计值
    private double pdelt; //系统测量误差-为计算高斯噪声方差
    private double mdelt; //系统测量误差-为计算高斯噪声方差
    private double Gauss; //高斯噪声方差
    private double kalmanGain;//估计方差

    //信任程度 因为实际中不同传感器精度不同昂贵的高精度传感器就可以更信任一些Ｒ可以小一些。  或者我建立的模型很优秀误差极小就可以更信任模型Ｑ可以小一些
    /*
    QR：
        Q模型误差与R测量误差的大小，是模型预测值与测量值的加权
            R固定，Q越大，代表越信任侧量值，
            Q无穷代表只用测量值；反之，
            Q越小代表越信任模型预测值，Q为零则是只用模型预测
        Q是系统过程噪声的协方差矩阵，而R则是观测噪声的协方差矩阵。后者和你选择的传感器息息相关,R是看传感器精度，Q是过程误差看环境的影响大不大，我一般Q取0.01
        R为大于0常数都可以 比如1. P初始值设为足够大的对角矩阵。Q大小影响收敛速度。可以试验几个数值。
        Q和R分别代表对预测值和测量值的置信度（反比），通过影响卡尔曼增益K的值，影响预测值和测量值的权重。越大的R代表越不相信测量值。
        q越小，越依赖系统模型，r越小，越依赖观测值
     */
    public static double Q = 0.00001; //（自定义-调参用）
    public static double R = 0.1; //（自定义-调参用
 
    public void initial(){
//        pdelt = 4;    //系统测量误差
//        mdelt = 3;
        pdelt = 4;   //系统测量误差
        mdelt = 3;  //估计方差
    }
    public Integer KalmanFilter(Integer oldValue,Integer value){
        //(1)第一个估计值
        predict = oldValue;
        //第二个估计值
        current = value;
        //(2)高斯噪声方差
        Gauss = Math.sqrt(pdelt * pdelt + mdelt * mdelt) + Q;
        //(3)估计方差
        kalmanGain = Math.sqrt((Gauss * Gauss)/(Gauss * Gauss + pdelt * pdelt)) + R;
        //(4)最终估计值
        estimate = (int) (kalmanGain * (current - predict) + predict);
        //(5)新的估计方差，下一次不确定性的度量
        mdelt = Math.sqrt((1-kalmanGain) * Gauss * Gauss);
 
        return estimate;
    }


    // 图标显示
    public static void show(ArrayList<Integer> list,ArrayList<Integer> alist){

        XYSeries originalSeries = new XYSeries("Original");
        XYSeries filteredSeries = new XYSeries("Filtered");

        for (int i = 0; i < list.size(); i++) {
            originalSeries.add(i, list.get(i));
            filteredSeries.add(i, alist.get(i));
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(originalSeries);
        dataset.addSeries(filteredSeries);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "KalmanFilter",    // 图表标题
                "X-Axis",                  // 横轴（X轴）标签
                "Y-Axis",                  // 纵轴（Y轴）标签
                dataset,                   // 数据集
                PlotOrientation.VERTICAL,  // 图表方向
                true,                      // 是否显示图例
                true,                      // 是否显示工具提示
                false                      // 是否显示URL链接
        );

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setDomainPannable(true);
        plot.setRangePannable(true);
        plot.setDomainZeroBaselineVisible(true);
        plot.setRangeZeroBaselineVisible(true);
        plot.setDomainGridlinePaint(Color.BLACK);
        plot.setRangeGridlinePaint(Color.BLACK);

        ChartFrame frame = new ChartFrame("Kalman Filter Chart", chart);
        frame.setSize(600, 400);
        frame.setVisible(true);

    }
 
    public static void main(String[] args) {

        // 卡尔曼滤波通常用于平滑或修正带有噪声的时间序列数据。
        KalmanFilterLevel1 kalmanfilter = new KalmanFilterLevel1();
        kalmanfilter.initial();
        // QR参数自己调整,影响滤波对噪声的平滑度
        kalmanfilter.Q = 0.001;
        kalmanfilter.R = 0.001;


        // 模拟带有噪声的原始数据
        Random r = new Random();
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < 100; i++) {
            list.add(r.nextInt(100));
        }

        // 保存滤波后的数据
        ArrayList<Integer> alist = new ArrayList<Integer>();

        // 上一个观测值
        int oldvalue = list.get(0);
        for(int i = 0; i < list.size(); i++){
            // 当前观测值
            int value = list.get(i);
            // 将上一个观测值和当前观测值传给滤波器估计下一个观测值
            oldvalue = kalmanfilter.KalmanFilter(oldvalue,value);
            alist.add(oldvalue);
        }
 

        // 图表显示
        show(list,alist);
 
    }
 
}