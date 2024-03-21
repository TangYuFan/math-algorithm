package org.tyf;

import weka.classifiers.Evaluation;
import weka.classifiers.rules.JRip;
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
 *   @desc : weka JRip基于规则的归纳分类算法
 *   @auth : tyf
 *   @date : 2022-03-04  17:17:22
*/
public class WekaJRip {

    // JRip 基于规则的归纳算法，生成简化的规则集。

    // 生成随机训练样本
    public static void generateCsv(String csv){
        Random r = new Random();
        if(new File(csv).exists()){
            return;
        }
        // 写入到本地csv
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csv, true))) {
            writer.write("f1" +","+ "f2" + ","+"f3" + ","+"f4" + ","+"label");
            writer.newLine();
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
        String module = "C:\\Users\\tyf\\Desktop\\JRip.model";

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

        // 决策树
        JRip vote = new JRip();

        // 训练
        vote.buildClassifier(data);

        // 模型评估
        Evaluation evaluation = new Evaluation(validationData);
        evaluation.evaluateModel(vote, validationData);

        // 打印概要信息
        System.out.println("=== Evaluation Summary ===");
        System.out.println(evaluation.toSummaryString());

        // 模型保存
        SerializationHelper.write(module, vote);
        JRip model = (JRip) SerializationHelper.read(module);

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

