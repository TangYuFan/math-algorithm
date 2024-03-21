package org.tyf;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
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
import java.util.concurrent.TimeUnit;

/**
 * @auth: Tang YuFan
 * @date: 2022/7/12 9:51
 * @desc: 快速傅里叶变幻
 */
public class FFTLearn {

    // 模拟设备方波的时序数据生成
    public static class SquareWaveGenerator{
        private long period;
        private long currentTime;
        public SquareWaveGenerator(long period) {
            this.period = period;
            this.currentTime = System.currentTimeMillis();
        }
        // 生成下一个 value 值
        public double generateNextValue() {
            double value = (currentTime % (2 * period) < period) ? 1.0 : -1.0;
            currentTime += period; // 此处使用固定步长10ms，你可以根据需要调整
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return value;
        }
    }

    //数据样本对象
    public static class Point{
        public Long ts;//遥测时间
        public Double value;//值
        public Point(Long ts, Double value) {
            this.ts = ts;
            this.value = value;
        }
    }

    private static int[] getSortedIndexes(int n, int harmonics) {
        double[] freq = fftFreq(n);
        ArrayList<Pair> pairs = Lists.newArrayList();
        int idnex = 0;
        for (double f : freq) {
            pairs.add(Pair.of(idnex++, f));
        }
        pairs.sort(Comparator.comparingDouble(p -> Math.abs((Double) p.getRight())));
        int[] result = new int[1 + 2 * harmonics];
        for (int i = 0; i < result.length; ++i) {
            result[i] = (Integer) ((Pair) pairs.get(i)).getLeft();
        }
        return result;
    }

    private static double[] fftFreq(int n) {
        int i;
        double val = 1.0 / (double) (n * 1);
        int N = (n - 1) / 2 + 1;
        double[] results = new double[n];
        int[] p1 = new int[N];
        for (int i2 = 0; i2 < p1.length; ++i2) {
            p1[i2] = i2;
        }
        int[] p2 = new int[n / 2 + 1];
        int value = -(n / 2);
        int index = 0;
        while (value <= 0) {
            p2[index++] = value++;
        }
        for (i = 0; i < N; ++i) {
            results[i] = (double) p1[i] * val;
        }
        for (i = 0; i < p2.length - 1; ++i) {
            results[i + N] = (double) p2[i] * val;
        }
        return results;
    }

    private static double[] trimToPowerOfTwo(double[] original) {
        int currLength = original.length;
        int nextLength = nextPowerOf2(currLength);
        if (currLength != nextLength) {
            --currLength;
            while (currLength != nextPowerOf2(currLength)) {
                --currLength;
            }
            return Arrays.copyOfRange(original, original.length - currLength, original.length);
        }
        return original;
    }

    public static List<Point> predict(List<Point> learnSet, long predictStartTs, long predictEndTs, long step) {
        double[] values = new double[learnSet.size()];
        int index = 0;
        for (Point p : learnSet) {
            values[index] = p.value;
            ++index;
        }
        values = trimToPowerOfTwo(values);
        int indexDelta = learnSet.size() - values.length;
        int padSize = values.length - learnSet.size();
        WeightedObservedPoints qqq = new WeightedObservedPoints();
        index = 0;
        for (double v : values) {
            qqq.add((double) index, v);
            ++index;
        }
        PolynomialCurveFitter fitter = PolynomialCurveFitter.create((int) 1);
        double[] fit = fitter.fit(qqq.toList());
        index = 0;
        for (double v : values) {
            values[index] = values[index] - fit[1] * (double) index;
            ++index;
        }
        FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
        Complex[] complx = fft.transform(values, TransformType.FORWARD);
        ArrayList prediction = Lists.newArrayList();
        int harmonics = (int) ((double) values.length * 0.1);
        int[] indexes = getSortedIndexes(values.length, harmonics);
        double[] freq = fftFreq(values.length);
        int forecastSize = (int) ((predictEndTs - predictStartTs) / step);
        double[] restoredArray = new double[values.length + forecastSize];
        for (int i = 0; i < restoredArray.length; ++i) {
            restoredArray[i] = 0.0;
        }
        for (int i : indexes) {
            double amplitude = Math.abs(complx[i].abs()) / (double) values.length;
            double phase = Math.atan2(complx[i].getImaginary(), complx[i].getReal());
            for (int j = 0; j < restoredArray.length; ++j) {
                restoredArray[j] = restoredArray[j] + amplitude * Math.cos(Math.PI * 2 * freq[i] * (double) j + phase);
            }
        }
        long currTs = 0L;
        for (int i = 0; i < restoredArray.length; ++i) {
            if (i < padSize) continue;
            double v = restoredArray[i] + fit[1] * (double) i;
            if (i + indexDelta < learnSet.size()) {
                Long pointTs = learnSet.get(i + indexDelta).ts;
                prediction.add(new Point(pointTs, v));
                currTs = pointTs;
                continue;
            }
            prediction.add(new Point(currTs += step, v));
        }
        return prediction;
    }

    public static int nextPowerOf2(int a) {
        int b;
        for (b = 1; b < a; b <<= 1) {
        }
        return b;
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

        // 物联网设备遥测一般跟随时间进行变换,所以可以使用fft来进行预测
        // 下面是模拟设备周期变换的数据、下面模拟方波的生成
        SquareWaveGenerator generator = new SquareWaveGenerator(2);

        // 按照时序生成 100 个样本、每个样本间隔 10ms
        long step = 10;
        List<Point> learnSet = new ArrayList<>();
        for (int i = 0; i < 300; i++) {
            // value 使用方波
            learnSet.add(new Point(System.currentTimeMillis(),generator.generateNextValue()));
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
        System.out.println("predictSet:"+predictSet.size());

        //输出整个曲线
        show(learnSet,predictSet);

    }

}
