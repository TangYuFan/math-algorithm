package org.tyf;

import com.google.common.collect.Lists;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.List;

import com.cloudera.sparkts.models.ARIMA;
import com.cloudera.sparkts.models.ARIMAModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

/**
 * @auth: Tang YuFan
 * @date: 2022/7/12 9:42
 * @desc: spark 时序数据预测模型 自回归移动平均模型
 */
public class SparkArima {


    // 对时间序列数据进行分析和预测比较完善和精确的算法是博克思-詹金斯(Box-Jenkins)方法，其常用模型包括：
    // 自回归模型（AR模型）、滑动平均模型（MA模型）、（自回归-滑动平均混合模型）ARMA模型、（差分整合移动平均自回归模型）ARIMA模型。

    public static int p = 1;
    public static int d = 1;
    public static int q = 3;

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
        double[] y = new double[learnSet.size()];
        int index = 0;
        for (Point p : learnSet) {
            y[index] = p.value;
            ++index;
        }
        Vector tsvector = Vectors.dense(y);
        ARIMAModel model = ARIMA.autoFit(tsvector, p,d,q);
        // 向后预测n个
        int forecastSize = (int) ((predictEndTs - predictStartTs) / step);
        System.out.println("forecastSize:"+forecastSize);
        Vector forecast = model.forecast(tsvector, forecastSize);
        ArrayList prediction = Lists.newArrayList();
        long currTs = predictStartTs + step;
        index = 0;
        for (double pred : forecast.toArray()) {
            if (index >= learnSet.size()) {
                prediction.add(new Point(currTs, pred));
                currTs += step;
            } else {
                prediction.add(new Point(learnSet.get(index).ts, pred));
            }
            ++index;
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
                "SparkArima",  // chart title
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

        ChartFrame frame = new ChartFrame("SparkArima", chart);
        frame.setSize(800, 400);
        frame.setVisible(true);
    }


    public static void main(String[] args) {

        Random r = new Random();
        // 按照时序生成 300 个样本、每个样本间隔 1 天
        long tsnow = System.currentTimeMillis();
        long step = 1 * 24 * 60 * 60 * 1000l;
        List<Point> learnSet = new ArrayList<>();
        for (int i = 0; i < 300; i++) {
            Long ts = tsnow+step*i;
            double value = LocalDateTime.ofInstant(Instant.ofEpochMilli(ts), ZoneId.systemDefault()).getDayOfMonth()+r.nextInt(50);
            learnSet.add(new Point(ts,value));
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
