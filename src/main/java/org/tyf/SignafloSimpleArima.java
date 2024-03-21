package org.tyf;

import com.github.signaflo.timeseries.TimeSeries;
import com.github.signaflo.timeseries.forecast.Forecast;
import com.github.signaflo.timeseries.model.arima.Arima;
import com.github.signaflo.timeseries.model.arima.ArimaOrder;
import com.google.common.collect.Lists;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeriesCollection;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * @auth: Tang YuFan
 * @date: 2022/7/12 9:39
 * @desc: signaflo 自回归积分滑动平均模型  Signaflo库是专门用于时序数据分析的库
 */
public class SignafloSimpleArima {

    public static int p = 1;// 表示季节性自回归（Autoregressive）阶数。这个参数用于捕捉时序数据中的季节性趋势，即前一季度的数据对当前季度的影响。
    public static int d = 1;// 表示进行季节性差分的次数。差分用于减小数据的季节性和趋势。
    public static int q = 1;// 表示季节性移动平均（Moving Average）阶数。这个参数用于捕捉时序数据中的季节性波动。
    public static int P = 1;// 表示非季节性自回归阶数。与 p 类似，但是是针对非季节性部分。
    public static int D = 1;// 表示进行非季节性差分的次数。差分用于减小数据的非季节性趋势。
    public static int Q = 1;// 表示非季节性移动平均阶数。与 q 类似，但是是针对非季节性部

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
        // Arima 用不到时间戳只使用 value 并默认相同时间间隔
        double[] y = new double[learnSet.size()];
        int index = 0;
        for (Point p : learnSet) {
            y[index] = p.value;
            ++index;
        }
        // 默认相同时间间隔
        TimeSeries ts = TimeSeries.from(y);
        ArimaOrder modelOrder = ArimaOrder.order(p,d,q,P,D,Q);
        Arima model = Arima.model(ts,modelOrder);
        // 向后预测 n 条数据
        ArrayList prediction = Lists.newArrayList();
        int forecastSize = (int) ((predictEndTs - predictStartTs) / step);

        Forecast forecast = model.forecast(forecastSize);
        long currTs = predictStartTs + step;
        for (double pred : forecast.pointEstimates().asArray()) {
            prediction.add(new Point(currTs, pred));
            currTs += step;
        }
        return prediction;
    }


    public static void show(List<Point> learnSet, List<Point> predictSet){
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        org.jfree.data.time.TimeSeries learnSeries = new org.jfree.data.time.TimeSeries("LearnSet");
        for (Point point : learnSet) {
            // 毫秒级别的时间戳
            learnSeries.addOrUpdate(new Millisecond(new Date(point.ts)), point.value);
        }
        dataset.addSeries(learnSeries);

        org.jfree.data.time.TimeSeries predictSeries = new org.jfree.data.time.TimeSeries("PredictSet");
        for (Point point : predictSet) {
            // 毫秒级别的时间戳
            predictSeries.addOrUpdate(new Millisecond(new Date(point.ts)), point.value);
        }
        dataset.addSeries(predictSeries);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "LearnSet and PredictSet",  // chart title
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
        // 按照时序生成 300 个样本、每个样本间隔 1 天
        long tsnow = System.currentTimeMillis();
        long step = 1 * 24 * 60 * 60 * 1000l;
        List<Point> learnSet = new ArrayList<>();
        for (int i = 0; i < 300; i++) {
            learnSet.add(new Point(tsnow+step*i,Double.valueOf(r.nextInt(30))));
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
