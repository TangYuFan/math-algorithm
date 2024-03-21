package org.tyf;

import java.awt.*;
import java.io.*;
import java.util.Arrays;
import java.util.Random;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.LinearRegression;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.RemovePercentage;

/**
 *   @desc : weka 线性回归
 *   @auth : tyf
 *   @date : 2022-03-04  15:16:25
*/
public class WekaLinearRegression {


	// 生成随机训练样本
	public static void generateCsv(String csv){
		Random r = new Random();
		if(new File(csv).exists()){
			return;
		}
		// 写入到本地csv
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(csv, true))) {
			for (int i = 0; i < 10000; i++) {
				double f1 = r.nextDouble()*100;
				double f2 = r.nextDouble()*100;
				double f3 = r.nextDouble()*100;
				double f4 = r.nextDouble()*100;
				double label = f1*10 + f2*5 + f3*5 - f4*5 + r.nextInt(40);
				String line = f1 +","+ f2 + ","+f3 + ","+f4 + ","+label;
				writer.write(line);
				writer.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	private static void visualizeRegressionResult(LinearRegression reg, Instances data) throws Exception {

		XYSeries originalSeries = new XYSeries("Original");
		XYSeries filteredSeries = new XYSeries("Filtered");

		// 预测值和实际值
		for (int i = 0; i < data.numInstances(); i++) {
			double actualValue = data.instance(i).classValue();
			double predictedValue = reg.classifyInstance(data.instance(i));
			originalSeries.add(i, actualValue);
			filteredSeries.add(i, predictedValue);
		}

		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(originalSeries);
		dataset.addSeries(filteredSeries);

		JFreeChart chart = ChartFactory.createXYLineChart(
				"linearRegression",    // 图表标题
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

	public static void main(String[] args) throws Exception{

		// 样本路径
		String csv = "C:\\Users\\tyf\\Desktop\\data.csv";
		// 模型保存和加载路径
		String module = "C:\\Users\\tyf\\Desktop\\linearRegression.model";

		// 生成随机训练样本
		generateCsv(csv);

		// 读取数据
		CSVLoader loader = new CSVLoader();
		loader.setSource(new File(csv));

		// 数据集 Instances
		Instances data = loader.getDataSet();
		System.out.println("行:"+data.size()+",列:"+data.numAttributes());

		// 追后一列是 label
		data.setClassIndex(data.numAttributes()-1);

		// 划分训练集
		RemovePercentage data_08 = new RemovePercentage();
		data_08.setInputFormat(data);
		data_08.setPercentage(80); // 百分比
		data_08.setInvertSelection(true);

		// 划分验证集
		RemovePercentage data_02 = new RemovePercentage();
		data_02.setInputFormat(data);
		data_02.setPercentage(20); // 百分比
		data_02.setInvertSelection(true);

		Instances trainingData = Filter.useFilter(data, data_08);
		Instances validationData  = Filter.useFilter(data, data_02);

		System.out.println("训练集: "+trainingData.size()+" x "+trainingData.numAttributes());
		System.out.println("验证集: "+validationData.size()+" x "+validationData.numAttributes());


		// 创建线性回归模型
		LinearRegression linearRegression = new LinearRegression();

		// 训练模型
		linearRegression.buildClassifier(trainingData);

		// 打印模型参数
		System.out.println("Coefficients: ");
		System.out.println(Arrays.toString(linearRegression.coefficients()));

		// 使用验证集进行模型评估
		Evaluation evaluation = new Evaluation(validationData);
		evaluation.evaluateModel(linearRegression, validationData);

		// 打印概要信息
		System.out.println("=== Evaluation Summary ===");
		System.out.println(evaluation.toSummaryString());

		// 获取单一指标
		System.out.println("=== Evaluation One ===");
		System.out.println("Correlation coefficient : " + evaluation.correlationCoefficient());
		System.out.println("Mean absolute error : " + evaluation.meanAbsoluteError());
		System.out.println("Root mean squared error: " + evaluation.rootMeanSquaredError());
		System.out.println("Relative absolute error  : " + evaluation.relativeAbsoluteError());
		System.out.println("Root relative squared error  : " + evaluation.rootRelativeSquaredError());
		System.out.println("Total Cost  : " + evaluation.totalCost());

		// 模型保存、加载、推理
		System.out.println("=== Predict ===");
		SerializationHelper.write(module, linearRegression);
		LinearRegression model = (LinearRegression) SerializationHelper.read(module);
		// 使用模型进行单个样本预测
		for (int i = 0; i < 30; i++) {
			Instance in = validationData.get(i);
			double out = model.classifyInstance(validationData.get(i));
			System.out.println("样本："+in+" , 标签："+in.toString(data.numAttributes()-1)+" , 预测："+out);
		}


		// 可视化
		visualizeRegressionResult(linearRegression,validationData);

	}


}