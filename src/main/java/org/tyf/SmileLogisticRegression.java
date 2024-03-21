package org.tyf;

import org.apache.commons.csv.CSVFormat;
import smile.classification.LogisticRegression;
import smile.data.DataFrame;
import smile.data.type.*;
import smile.io.*;
import smile.validation.metric.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

import static smile.validation.metric.FScore.F1;


/**
 *   @desc : Smile LogisticRegression 逻辑回归
 *   @auth : tyf
 *   @date : 2022-03-08  16:38:14
*/
public class SmileLogisticRegression {

    // 逻辑回归


    // 生成随机训练样本
    public static void generateCsv(String csv){
        Random r = new Random();
        if(new File(csv).exists()){
            new File(csv).delete();
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
                else{label = "NO";}
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

        // 模型路径
        String module = "C:\\Users\\唐于凡\\Desktop\\SmileLogisticRegression";

        // 生成随机训练样本
        generateCsv(csv);

        // 读取样本
        DataFrame df = Read.csv(
                csv,    // 文件地址
                CSVFormat.newFormat(','),   // 分隔符
                new StructType(Arrays.asList(   // 列名称
                        new StructField("f1", DataTypes.DoubleType),
                        new StructField("f2", DataTypes.DoubleType),
                        new StructField("f3", DataTypes.DoubleType),
                        new StructField("f4", DataTypes.DoubleType),
                        new StructField("label", DataTypes.StringType)
                ))
        );

        // 打印样本行列数
        System.out.println("\n"+df.size()+" => 行："+df.nrow()+" ， 列："+df.ncol()+" ，属性： "+ Arrays.toString(df.names()));
        // 打印样本前十
        System.out.println("\n"+df);
        // 打印样本列统计信息包括总数、最小值最大值平均值
//        System.out.println(df.summary());

        // 划分训练集和验证集
        double radio = 0.8;
        int c = Double.valueOf(df.size()*radio).intValue();

        // 训练样本和验证样本
        DataFrame trainData = df.slice(0,c);
        DataFrame valiaData = df.slice(c,df.size());

        System.out.println("trainData："+trainData.size());
        System.out.println("valiaData："+valiaData.size());

        // 类别和整数映射
        List<String> labels = trainData.stringVector("label").stream().distinct().collect(Collectors.toList());

        // 类别映射为整数
        ToIntFunction label2index = value -> {
            int rt = 0;
            for (int i = 0; i < labels.size() ; i++) {
                if(labels.get(i).equals(value)){
                    rt = i;
                    break;
                }
            }
            return rt;
        };


        // 模型
        LogisticRegression lr = LogisticRegression.fit(
                // 特征
                trainData.select("f1","f2","f3","f4").toArray(),
                // 类别
                trainData.stringVector("label").stream().mapToInt(label2index).toArray()
        );

        // 打印系数矩阵, 特征数*类别数
        // 多分类 LogisticRegression.Multinomial  => double[][] dd = ((LogisticRegression.Multinomial) lr).coefficients()
        // 二分类 LogisticRegression.Binomial     => double[] dd = ((LogisticRegression.Binomial) lr).coefficients()
        double[][] dd = ((LogisticRegression.Multinomial) lr).coefficients();
        System.out.println("\nCoefficients：");
        for (int i = 0; i < dd.length; i++) {
            System.out.println(Arrays.toString(dd[i]));
        }

        // 模型评估, 分类模型的评估是 ClassificationMetric 子类
        int[] truth = valiaData.stringVector("label").stream().mapToInt(label2index).toArray(); // 真实值
        int[] pred = lr.predict(valiaData.select("f1","f2","f3","f4").toArray()); // 预测值

        // 准确率 (Accuracy):正确分类的样本数除以总样本数
        System.out.println("\n模型评估：");
        System.out.println("Accuracy："+Accuracy.of(truth,pred));

        // Recall (召回率):正类别的真正例数除以正类别的真正例数与假负例数之和。
//        System.out.println("Recall："+ Recall.of(truth,pred));

        // Sensitivity (灵敏度):同Recall，衡量模型对正类别的敏感性。
//        System.out.println("Sensitivity："+ Sensitivity.of(truth,pred));

        // Specificity (特异性):负类别的真负例数除以负类别的真负例数与假正例数之和。
//        System.out.println("Specificity："+ Specificity.of(truth,pred));

        // Fallout (误报率): 假正例数除以负类别的真负例数与假正例数之和。
//        System.out.println("Fallout："+ Fallout.of(truth,pred));

        // FDR (假发现率): 假正例数除以真正例数与假正例数之和。
//        System.out.println("FDR："+ FDR.of(truth,pred));

        // FScore (F1分数): 精确率和召回率的调和平均值，2 * (Precision * Recall) / (Precision + Recall)。
//        System.out.println("FScore："+ FScore.of(0,truth,pred));

        System.out.println("\n模型序列化：");
        Write.object(lr,new File(module).toPath());
        System.out.println("Write module："+module);
        LogisticRegression obj = (LogisticRegression)Read.object(new File(module).toPath());
        System.out.println("Read module："+obj);

        // 使用单个样本进行预测
        System.out.println("\nInference：");
        for (int i = 0; i < 20; i++) {
            DataFrame one = valiaData.of(i);
            double[] x = one.select("f1","f2","f3","f4").toArray()[0];
            String y = one.stringVector("label").toArray()[0];
            int index = obj.predict(x);
            String yy = labels.get(index);
            System.out.println("Feature："+Arrays.toString(x)+"，label："+y+"，predict："+yy);
        }


    }

}