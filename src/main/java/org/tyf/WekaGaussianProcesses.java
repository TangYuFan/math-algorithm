package org.tyf;

import weka.classifiers.Evaluation;
import weka.classifiers.functions.GaussianProcesses;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.RemovePercentage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

/**
 *   @desc : weka GaussianProcesses高斯过程回归
 *   @auth : tyf
 *   @date : 2022-03-04  17:17:22
*/
public class WekaGaussianProcesses {

    // GaussianProcesses 高斯过程回归算法，用于回归问题。


    // 生成随机训练样本
    public static void generateCsv(String csv){
        Random r = new Random();
        if(new File(csv).exists()){
            return;
        }
        // 写入到本地csv
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csv, true))) {
            for (int i = 0; i < 100; i++) {
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



    public static void main(String[] args) throws Exception{


        // 样本路径
        String csv = "C:\\Users\\唐于凡\\Desktop\\data.csv";
        // 模型保存和加载路径
        String module = "C:\\Users\\唐于凡\\Desktop\\gs.model";

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


        // GR
        GaussianProcesses processes = new GaussianProcesses();

        // GaussianProcesses 分类器在 Weka 中的 setOptions 方法支持多种选项，这些选项用于配置分类器的内部参数。以下是一些常用的选项和它们的含义：
        // -L <double>: 高斯噪声水平相对于转换目标的设置。默认值为1。
        // -N: 是否进行标准化/规范化/不进行任何处理。默认值为0，表示进行标准化。
        // -K <classname and parameters>: 用于指定核函数的选项。默认使用 weka.classifiers.functions.supportVector.PolyKernel 多项式核函数。
        // -S <num>: 随机数种子，用于初始化算法的随机性。默认值为1。
        // -output-debug-info: 如果设置，分类器将以调试模式运行，并可能在控制台上输出额外的信息。
        // -do-not-check-capabilities: 如果设置，将在构建分类器之前不检查分类器的能力（请小心使用）。
        // -num-decimal-places: 模型中数字输出的小数位数。默认值为2。
        //  以下是对多项式核函数的特定选项：weka.classifiers.functions.supportVector.PolyKernel
        //      -E <num>: 多项式核的指数。默认值为1.0。
        //      -L: 是否使用低阶项。默认值为否（no）。
        //      -C <num>: 缓存的大小（质数），0表示完整缓存，-1表示关闭缓存。默认值为250007。
        //      -output-debug-info: 启用调试输出（如果可用）。默认值为关闭。
        //      -no-checks:关闭所有检查 - 谨慎使用。默认值为开启检查。

        processes.setOptions(new String[]{"-output-debug-info"});


        // 训练模型
        processes.buildClassifier(trainingData);


        // 使用验证集进行模型评估
        Evaluation evaluation = new Evaluation(validationData);
        evaluation.evaluateModel(processes, validationData);

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
//        System.out.println("=== Predict ===");
//        SerializationHelper.write(module, processes);
//        GaussianProcesses model = (GaussianProcesses) SerializationHelper.read(module);
//        // 使用模型进行单个样本预测
//        for (int i = 0; i < 30; i++) {
//            Instance in = validationData.get(i);
//            double out = model.classifyInstance(validationData.get(i));
//            System.out.println("样本："+in+" , 标签："+in.toString(data.numAttributes()-1)+" , 预测："+out);
//        }

    }

}

