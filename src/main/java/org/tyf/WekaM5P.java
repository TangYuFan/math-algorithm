package org.tyf;

import weka.classifiers.Evaluation;
import weka.classifiers.trees.M5P;
import weka.classifiers.trees.RandomForest;
import weka.classifiers.trees.RandomTree;
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
import java.util.Random;

/**
 *   @desc : weka M5PM5P树回归
 *   @auth : tyf
 *   @date : 2022-03-04  17:17:22
*/
public class WekaM5P {

    // M5P 是Model Trees的一种变体，它在每个叶子节点上使用了一个线性回归模型。用于回归任务



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
                double score = f1*10 + f2*5 + f3*5 - f4*5 + r.nextInt(20);
                String line = f1 +","+ f2 + ","+f3 + ","+f4 + ","+score;
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
        String module = "C:\\Users\\tyf\\Desktop\\WekaM5P.model";

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


        M5P m5P = new M5P();


        // 训练
        m5P.buildClassifier(trainingData);


        Evaluation evaluation = new Evaluation(validationData);
        evaluation.evaluateModel(m5P, validationData);

        // 打印概要信息
        System.out.println("=== Evaluation Summary ===");
        System.out.println(evaluation.toSummaryString());

        // 模型保存
        SerializationHelper.write(module, m5P);
        M5P model = (M5P) SerializationHelper.read(module);

        // 使用模型进行单个样本推理
        System.out.println("=== Predict ===");
        // 使用模型进行单个样本预测
        for (int i = 0; i < 30; i++) {
            Instance in = validationData.get(i);
            double out = model.classifyInstance(validationData.get(i));
            System.out.println("样本："+in+" , 标签："+in.toString(data.numAttributes()-1)+" , 预测："+out);
        }

    }

}

