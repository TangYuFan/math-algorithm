package org.tyf;

import org.apache.commons.csv.CSVFormat;
import smile.classification.Classifier;
import smile.classification.SVM;
import smile.data.DataFrame;
import smile.data.type.DataTypes;
import smile.data.type.StructField;
import smile.data.type.StructType;
import smile.io.Read;
import smile.io.Write;
import smile.validation.metric.Accuracy;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;


/**
 *   @desc : Smile SVM 支持向量机
 *   @auth : tyf
 *   @date : 2022-03-08  16:38:14
*/
public class SmileSVM {

    // 支持向量机

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
                int label = -1;
                double score = f1*10 + f2*5 + f3*5 - f4*5;
                if(score>=1000){
                    label = 1;
                }
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

        // 模型路径
        String module = "C:\\Users\\tyf\\Desktop\\SmileSVM";

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
                        new StructField("label", DataTypes.IntegerType)
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


        // svm 目前仅支持二分类
        Classifier<double[]> svm = SVM.fit(
                // 特征 x – training samples.
                trainData.select("f1","f2","f3","f4").toArray(),
                // 类别 y – training labels of {-1, +1}.
                trainData.intVector("label").array(),
                // 软间隔惩罚参数,该参数用于调整模型对训练误差和间隔（margin）之间的平衡。较大的C值将导致更严格的分类，可能会使模型对训练数据过拟合。
                1,
                // 敛测试的容忍度。在训练过程中，如果模型的变化小于该值，则认为模型已经收敛。这是一个用于控制训练过程何时停止的参数。
                1
        );


        // 打印模型信息
        System.out.println("\nSvm："+svm);

        // 准确率
        int[] truths = valiaData.intVector("label").array();
        int[] preditcs =svm.predict(valiaData.select("f1","f2","f3","f4").toArray());

        System.out.println("\n模型评估：");
        System.out.println("Accuracy："+ Accuracy.of(truths,preditcs));

        System.out.println("\n模型序列化：");
        Write.object(svm,new File(module).toPath());
        System.out.println("Write module："+module);
        Classifier<double[]> obj = (Classifier<double[]>)Read.object(new File(module).toPath());
        System.out.println("Read module："+obj);

        // 单个样本预测
        for (int i = 0; i < 30; i++) {
            double[] in = valiaData.select("f1","f2","f3","f4").toArray()[i];
            int label = valiaData.intVector("label").array()[i];
            int out = obj.predict(in);
            System.out.println("In："+Arrays.toString(in)+"，Label："+label+"，Out："+out);
        }

    }

}
