package org.tyf;


import com.google.common.collect.Lists;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * @auth: Tang YuFan
 * @date: 2022/7/12 10:28
 * @desc: apache-math3 简单线性回归
*/
public class Math3SimpleRegression {

    //数据样本对象
    static class Point{
        private Long ts;//遥测时间
        private Double value;//值
        public Point(Long ts, Double value) {
            this.ts = ts;
            this.value = value;
        }
    }

    public static List<Point> predict(List<Point> learnSet, long predictStartTs, long predictEndTs, long step) {

        // 简单线性回归
        org.apache.commons.math3.stat.regression.SimpleRegression sr = new org.apache.commons.math3.stat.regression.SimpleRegression();
        learnSet.forEach(p -> sr.addData((double) p.ts.longValue(), p.value.doubleValue()));

        ArrayList prediction = Lists.newArrayList();
        for (Point point : learnSet) {
            prediction.add(new Point(point.ts, sr.predict((double) point.ts.longValue())));
        }
        for (long currTs = predictStartTs + step; currTs <= predictEndTs; currTs += step) {
            prediction.add(new Point(currTs, sr.predict((double) currTs)));
        }
        return prediction;
    }


    // 图表显示
    public static void show(List<Point> learnSet,List<Point> predictSet){

        TimeSeriesCollection dataset = new TimeSeriesCollection();

        TimeSeries learnSeries = new TimeSeries("LearnSet");
        for (Point point : learnSet) {
            // 毫秒级别的时间戳
            learnSeries.addOrUpdate(new Millisecond(new Date(point.ts)), point.value);
        }
        dataset.addSeries(learnSeries);

        TimeSeries predictSeries = new TimeSeries("PredictSet");
        for (Point point : predictSet) {
            // 毫秒级别的时间戳
            predictSeries.addOrUpdate(new Millisecond(new Date(point.ts)), point.value);
        }
        dataset.addSeries(predictSeries);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "SimpleRegression",  // chart title
                "Time",                     // x-axis label
                "Value",                    // y-axis label
                dataset,                    // data
                true,                       // include legend
                true,                       // generate tooltips
                false                       // generate URLs
        );

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.white);

        // 使用毫秒级别的X轴
        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("HH:mm:ss.SSS"));

        ChartFrame frame = new ChartFrame("SimpleRegression", chart);
        frame.setSize(800, 400);
        frame.setVisible(true);
    }


    public static void main(String[] args) {

        Random r = new Random();

        // 按照时序生成 100 个样本、每个样本间隔 10ms
        long step = 10;
        List<Point> learnSet = new ArrayList<>();
        for (int i = 0; i < 300; i++) {
            learnSet.add(new Point(System.currentTimeMillis(),Double.valueOf(r.nextInt(100))));
            try {
                Thread.sleep(step);
            }
            catch (Exception e){
                e.getMessage();
            }
        }

        //从最后的样本再往后预测20个样本
        long predictStartTs = learnSet.get((learnSet.size()-1)).ts;
        long predictEndTs = predictStartTs + step * 20;

        //预测
        List<Point> predictSet = predict(learnSet,predictStartTs,predictEndTs,step);

        //输出整个曲线
        show(learnSet,predictSet);

    }

}
