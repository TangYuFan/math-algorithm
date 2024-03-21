package org.tyf;

import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
import org.apache.commons.math3.fitting.CurveFitter;
import org.apache.commons.math3.fitting.SimpleCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.apache.commons.math3.optim.nonlinear.vector.jacobian.LevenbergMarquardtOptimizer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Random;

/**
 *   @desc : apache-math3 自定义一元多项式目标函数拟合
 *   @auth : tyf
 *   @date : 2022-02-22  12:02:44
*/
public class Math3ParametricUnivariateFunctionDemo {


    //  在 Apache Commons Math 库中，这些接口用于描述不同类型的数学函数，并提供了对这些函数进行求导等操作的能力。下面是对每个接口的解释和它们之间的区别分析：
    // UnivariateFunction：
    // 描述：代表一个单变量函数，即只接受一个参数并返回一个结果的函数。用途：用于描述如 f(x) = x^2 这样的函数。

    // DifferentiableUnivariateFunction：
    // 描述：这是一个可以求导的单变量函数接口。用途：除了可以计算函数值外，还可以计算其一阶、二阶等导数。

    // ParametricUnivariateFunction：
    // 描述：这是一个参数化的单变量函数接口，其中函数值可能依赖于额外的参数。用途：用于描述如 f(x, p1, p2) 这样的函数，其中 p1 和 p2 是额外的参数。

    // MultivariateFunction：
    // 描述：代表一个多变量函数，即接受多个参数并返回一个结果的函数。用途：用于描述如 f(x, y) = x^2 + y^2 这样的函数。

    // DifferentiableMultivariateFunction：
    // 描述：这是一个可以求导的多变量函数接口。用途：除了可以计算函数值外，还可以计算其关于各个变量的偏导数。

    // MultivariateVectorFunction：
    // 描述：代表一个返回向量的多变量函数，即接受多个参数并返回一个向量结果的函数。用途：用于描述如 F(x, y) = [f1(x, y), f2(x, y)] 这样的函数，其中 F 是一个向量。

    // DifferentiableMultivariateVectorFunction：
    // 描述：这是一个可以求导的多变量向量函数接口。用途：除了可以计算向量函数的值外，还可以计算其关于各个变量的偏导数向量。

    // MultivariateMatrixFunction：
    // 描述：代表一个返回矩阵的多变量函数，即接受多个参数并返回一个矩阵结果的函数。用途：用于描述如 M(x, y) = [[m11(x, y), m12(x, y)], [m21(x, y), m22(x, y)]] 这样的函数，其中 M 是一个矩阵。

    // UnivariateMatrixFunction：
    // 描述：代表一个单变量到矩阵的函数，即接受一个参数并返回一个矩阵结果的函数。用途：用于描述如 M(x) = [[m11(x), m12(x)], [m21(x), m22(x)]] 这样的函数。

    // UnivariateVectorFunction：
    // 描述：代表一个单变量到向量的函数，即接受一个参数并返回一个向量结果的函数。用途：用于描述如 V(x) = [v1(x), v2(x)] 这样的函数，其中 V 是一个向量。

    // DifferentiableUnivariateMatrixFunction：
    // 描述：这是一个可以求导的单变量矩阵函数接口。用途：除了可以计算矩阵函数的值外，还可以计算其关于变量的导数矩阵。

    // DifferentiableUnivariateVectorFunction：
    // 描述：这是一个可以求导的单变量向量函数接口。用途：除了可以计算向量函数的值外，还可以计算其关于变量的导数向量。

    // TrivariateFunction：
    // 描述：代表一个三变量函数，即接受三个参数并返回一个结果的函数。用途：用于描述如 f(x, y, z) = x^2 + y^2 + z^2 这样的函数。

    // 区别分析：
    // 变量数量：接口根据它们处理的变量数量（单变量、多变量、三变量）进行区分。
    // 返回值类型：函数可以返回标量（UnivariateFunction）、向量（UnivariateVectorFunction、MultivariateVectorFunction）或矩阵（MultivariateMatrixFunction、UnivariateMatrixFunction）。
    // 可导性：接口通过添加“Differentiable”前缀来指示函数是否可导，即可以计算其导数或偏导数。
    // 参数化：ParametricUnivariateFunction 是一种特殊类型的单变量函数，它接受额外的参数。
    // 这些接口的设计使得 Apache Commons Math 库能够灵活地处理不同类型的数学函数，并为这些函数提供统一的接口以进行计算和求导等操作。


    public static class Function implements ParametricUnivariateFunction {
        // 计算函数在给定的 x 和参数下的函数值,这里函数有 abcd 四个参数
        public double value(double x, double ... parameters) {
            double a = parameters[0];
            double b = parameters[1];
            double c = parameters[2];
            double d = parameters[3];
            // 目标函数
            return d + ((a - d) / (1 + Math.pow(x / c, b)));
        }
        // 计算函数在给定的 x 和参数下的偏导数, 用于在梯度下降等优化算法中计算实现最小误差
        public double[] gradient(double x, double ... parameters) {
            double a = parameters[0];
            double b = parameters[1];
            double c = parameters[2];
            double d = parameters[3];
            double[] gradients = new double[4];
            double den = 1 + Math.pow(x / c, b);
            // 返回目标函数的偏导数
            gradients[0] = 1 / den; // 对 a 求导
            gradients[1] = -((a - d) * Math.pow(x / c, b) * Math.log(x / c)) / (den * den); // 对 b 求导
            gradients[2] = (b * Math.pow(x / c, b - 1) * (x / (c * c)) * (a - d)) / (den * den); // 对 c 求导
            gradients[3] = 1 - (1 / den); // 对 d 求导
            return gradients;
        }
    }


    // points 原始样本点
    // function1 函数以及拟合参数 parameters1
    // function2 函数以及拟合参数 parameters2
    public static void show(double[][] points,ParametricUnivariateFunction function,double[] parameters1,double[] parameters2){


        XYSeries originalSeries = new XYSeries("Original Points");
        XYSeries simpleFitterSeries = new XYSeries("SimpleCurveFitter");
        XYSeries curveFitterSeries = new XYSeries("CurveFitter");

        for (double[] point : points) {
            // 原始线
            originalSeries.add(point[0], point[1]);
            // 拟合线1  // 使用自定义函数以及参数进行预测是调用 value 函数将 x 和参数传进去
            simpleFitterSeries.add(point[0], function.value(point[0],parameters1));
            // 拟合线2  // 使用自定义函数以及参数进行预测是调用 value 函数将 x 和参数传进去
            curveFitterSeries.add(point[0], function.value(point[0],parameters2));
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(originalSeries);
        dataset.addSeries(simpleFitterSeries);
        dataset.addSeries(curveFitterSeries);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Curve Fitter Fit",    // 图表标题
                "X",                  // 横轴（X轴）标签
                "Y",                  // 纵轴（Y轴）标签
                dataset,                   // 数据集
                PlotOrientation.VERTICAL,  // 图表方向
                true,                      // 是否显示图例
                true,                      // 是否显示工具提示
                false                      // 是否显示URL链接
        );

        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(1, true);
        renderer.setSeriesLinesVisible(2, true);
        plot.setRenderer(renderer);

        JFrame frame = new JFrame("Curve Fitter Plot");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new ChartPanel(chart), BorderLayout.CENTER);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    public static void main(String[] args) {

        Random r = new Random();

        // 系数猜测 依次为 a b c d
        double a = 1500;
        double b = 0.95;
        double c = 65;
        double d = 35000;
        double[] guess = {a,b,c,d};

        // 待拟合数据随机生成
        double[][] points = new double[100][2];
        for (int i = 0; i < points.length; i++) {
            double x = r.nextInt(1000);
            double y = d + ((a - d) / (1 + Math.pow(x / c, b))) - i * r.nextInt(200); // 使用公式近似计算Y
            points[i] = new double[]{x,y};
        }

        // 一元多项式函数
        ParametricUnivariateFunction function = new Function();

        //----------------------------------------
        // simple 通过最小化残差平方和来拟合数据
        SimpleCurveFitter fitter1 = SimpleCurveFitter.create(function,guess);
        WeightedObservedPoints observedPoints = new WeightedObservedPoints();
        for (double[] point : points) {
            observedPoints.add(point[0], point[1]);
        }

        // 进行拟合 best 为拟合结果 对应 a b c d, 可能会出现无法拟合的情况,需要合理设置初始值
        double[] best1 = fitter1.fit(observedPoints.toList());
        System.out.println("best1: "+Arrays.toString(best1));

        //----------------------------------------
        // CurveFitter 通过自定义优化器来进行拟合数据
        CurveFitter<ParametricUnivariateFunction> fitter2 = new CurveFitter<>(new LevenbergMarquardtOptimizer());
        for (double[] point : points) {
            fitter2.addObservedPoint(point[0], point[1]);
        }
        double[] best2 = fitter2.fit(function, guess);
        System.out.println("best2: "+Arrays.toString(best2));


        // 绘制原始点
        // 绘制 SimpleCurveFitter 的拟合曲线
        // 绘制 CurveFitter 的拟合曲线
        show(points,function,best1,best2);


    }


}
