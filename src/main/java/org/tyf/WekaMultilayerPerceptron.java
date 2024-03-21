package org.tyf;

import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Standardize;
import weka.filters.unsupervised.instance.RemovePercentage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 *   @desc : weka MultilayerPerceptron多层感知器神经网络分类器
 *   @auth : tyf
 *   @date : 2022-03-04  17:17:22
*/
public class WekaMultilayerPerceptron {

    // MultilayerPerceptron 多层感知器神经网络分类器。

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
                String label = "";
                double score = f1*10 + f2*5 + f3*5 - f4*5;
                if(score>=-500&&score<0){ label = "E";}
                else if(score>=0&&score<500){ label = "D";}
                else if(score>=500&&score<1000){ label = "C";}
                else if(score>=1000&&score<1500){ label = "B";}
                else if(score>=1500&&score<2000){ label = "A";}
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
        String csv = "C:\\Users\\tyf\\Desktop\\data.csv";
        // 模型保存和加载路径
        String module = "C:\\Users\\tyf\\Desktop\\MultilayerPerceptron.model";

        // 生成随机训练样本
        generateCsv(csv);

        // 读取数据
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File(csv));

        // 数据集 Instances
        Instances data = loader.getDataSet();
        System.out.println("行:"+data.size()+",列:"+data.numAttributes());

        // 最后一列是 label
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

        // 设置多层感知器参数
        // -L <学习率> 反向传播算法的学习率。 取值应在0-1之间，默认值为0.3。
        // -M <动量> 反向传播算法的动量率。 取值应在0-1之间，默认值为0.2。
        // -N <训练轮数> 通过的训练轮数。 默认值为500。
        // -V <验证集百分比大小> 用于终止训练的验证集大小百分比。如果非零，它可以在轮数之前终止。取值应在0-100之间，默认值为0。
        // -S <种子>用于初始化随机数生成器的值。取值应大于等于0且为长整型，默认值为0。
        // -E <连续错误次数阈值>在验证测试中允许的连续错误增加的次数，训练将在此之后终止。取值应大于0，默认值为20。
        // -A 将不会自动创建网络连接。如果未设置-G，则将忽略此选项。
        // -B 将不会自动使用NominalToBinary过滤器。设置此选项以不使用NominalToBinary过滤器。
        // -H <每层节点的逗号分隔数字> 要为网络创建的隐藏层。取值应为逗号分隔的自然数列表，
        // -C 将不会对数值类进行归一化。如果类别是数值类型，则设置此选项将不进行类别归一化。
        // -I 将不会对属性进行归一化。设置此选项将不进行属性归一化。
        // -R 不允许网络重置。设置此选项将不允许网络重置。
        // -D 将发生学习率衰减。设置此选项将导致学习率衰减。
        MultilayerPerceptron classifier = new MultilayerPerceptron();
//        classifier.setOptions();

        // 训练
        classifier.buildClassifier(data);

        // 模型评估
        Evaluation evaluation = new Evaluation(validationData);
        evaluation.evaluateModel(classifier, validationData);

        // 打印概要信息
        System.out.println("=== Evaluation Summary ===");
        System.out.println(evaluation.toSummaryString());

        // 模型保存
        SerializationHelper.write(module, classifier);
        MultilayerPerceptron model = (MultilayerPerceptron) SerializationHelper.read(module);

        // 使用模型进行单个样本推理
        System.out.println("=== Predict ===");
        for (int i = 0; i < 30; i++) {
            // 预测 label 下标
            double prediction = model.classifyInstance(validationData.get(i));
            // 预测 label
            String predictedClass = validationData.classAttribute().value((int) prediction);
            // 真实的 label
            String trueClass = validationData.instance(i).stringValue(validationData.numAttributes() - 1);
            System.out.println("Sample: " + validationData.get(i) + " , True Class: " + trueClass + " , Predicted Class: " + predictedClass);
        }



    }

}

