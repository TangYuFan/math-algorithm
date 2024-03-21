package org.tyf;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayesMultinomialUpdateable;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 *   @desc : weka NaiveBayesMultinomialUpdateable可更新的多项式朴素贝叶斯分类器
 *   @auth : tyf
 *   @date : 2022-03-04  17:17:22
*/
public class WekaNaiveBayesMultinomialUpdateable {

    // NaiveBayesMultinomialUpdateable 可更新的多项式朴素贝叶斯分类器，适用于流式数据。

    // 模型评估
    public static void Eval(NaiveBayesMultinomialUpdateable nbClassifier, Instances dataValid) throws Exception{
        Evaluation evaluation = new Evaluation(dataValid);
        evaluation.evaluateModel(nbClassifier, dataValid);
        // 打印概要信息
//        System.out.println("=== Evaluation Summary ===");
//        System.out.println(evaluation.toSummaryString());
        // 仅仅打印准确率
        System.out.println("验证集准确率："+evaluation.pctCorrect());
    }

    // 随机生成一条样本
    public static Instance getOne(Instances dataSet){
        Random r = new Random();
        double f1 = r.nextDouble()*100;
        double f2 = r.nextDouble()*100;
        double f3 = r.nextDouble()*100;
        double f4 = r.nextDouble()*100;
        String label = "";
        double score = f1*10 + f2*5 + f3*5 - f4*5 + r.nextInt(10);
        if(score>=-500&&score<0){ label = "E";}
        else if(score>=0&&score<500){ label = "D";}
        else if(score>=500&&score<1000){ label = "C";}
        else if(score>=1000&&score<1500){ label = "B";}
        else if(score>=1500&&score<2000){ label = "A";}
        else { label = "NO";}
        Instance data = new DenseInstance(5);
        data.setDataset(dataSet);
        data.setValue(0,f1);
        data.setValue(1,f2);
        data.setValue(2,f3);
        data.setValue(3,f4);
        data.setValue(4,label);
        dataSet.add(data);
        return data;
    }

    public static void main(String[] args) throws Exception{

        // 属性
        ArrayList<Attribute> attrs = new ArrayList<>();
        attrs.add(new Attribute("f1", 0));
        attrs.add(new Attribute("f2", 1));
        attrs.add(new Attribute("f3", 2));
        attrs.add(new Attribute("f4", 3));
        attrs.add(new Attribute("label", Arrays.asList("A","B","C","D","E","NO")));


        // 训练样本和验证样本
        Instances dataTrain = new Instances("dataTrain", attrs,10);
        Instances dataValid = new Instances("dataValid", attrs,10);
        for (int i = 0; i < 100000; i++) {
            Instance data = getOne(dataTrain);
        }
        for (int i = 0; i < 100000; i++) {
            Instance data = getOne(dataValid);
        }


        // 最后一列是类别
        dataTrain.setClassIndex(4);
        dataValid.setClassIndex(4);

        // 训练
        NaiveBayesMultinomialUpdateable nbClassifier = new NaiveBayesMultinomialUpdateable();
        nbClassifier.buildClassifier(dataTrain);

        // 打印模型
        System.out.println("=========");
        System.out.println(nbClassifier);
        System.out.println("=========");

        // 使用单个样本进行预测
        Instances dataTest = new Instances("dataTest", attrs,10);
        dataTest.setClassIndex(4);
        for (int i = 0; i < 100; i++) {
            // 预测 label 下标
            Instance data = getOne(dataTest);
            // 预测
            double prediction = nbClassifier.classifyInstance(data);
            // 预测 label
            String predictedClass = data.classAttribute().value((int) prediction);
            // 真实的 label
            String trueClass = data.stringValue(4);
            System.out.println("Sample: " + data + " , True Class: " + trueClass + " , Predicted Class: " + predictedClass);
        }


        // 使用新的数据更新模型
        for (int i = 0; i < 10000000; i++) {
            // 生成一个新的样本来更新模型
            nbClassifier.updateClassifier(getOne(dataTest));
            // 每更新n次后用验证集评估分类是否更加准确
            if((i%100000)==0){
                Eval(nbClassifier,dataValid);
            }
        }

        // 可以发现模型的准确度会上升

    }
}

