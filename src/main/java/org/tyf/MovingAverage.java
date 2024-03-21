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
 * @date: 2022/7/12 9:45
 * @desc: 滑动平均
 */
public class MovingAverage {

    //数据样本对象
    static class Point{
        private Long ts;//遥测时间
        private Double value;//值
        public Point(Long ts, Double value) {
            this.ts = ts;
            this.value = value;
        }
    }

    public static List<Point> predict(List<Point> learnSet,int range) {
        //平均值窗口,每n条数据进行一次平均值计算 range
        ArrayList prediction = Lists.newArrayList();
        LinkedList<Double> window = new LinkedList<Double>();
        double sum = 0.0;
        for (Point p : learnSet) {
            sum += p.value.doubleValue();
            window.add(p.value);
            if (window.size() > range) {
                sum -= ((Double) window.remove()).doubleValue();
            }
            prediction.add(new Point(p.ts, sum / (double) window.size()));
        }
        return prediction;
    }

    public static void show(List<Point> learnSet, List<Point> predictSet){

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
                "MovingAverage",  // chart title
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

        ChartFrame frame = new ChartFrame("Time Series Chart", chart);
        frame.setSize(800, 400);
        frame.setVisible(true);
    }

    public static void main(String[] args) {

        Random r = new Random();

        //样本
        List<Point> learnSet = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            learnSet.add(new Point(System.currentTimeMillis(),Double.valueOf(r.nextInt(100))));
            try {
                Thread.sleep(2);
            }
            catch (Exception e){
                e.getMessage();
            }
        }

        //预测
        List<Point> predictSet = predict(learnSet,3);

        //显示
        show(learnSet,predictSet);

    }


}
