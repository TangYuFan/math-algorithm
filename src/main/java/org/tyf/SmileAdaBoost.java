package org.tyf;

import org.apache.commons.csv.CSVFormat;
import smile.classification.AdaBoost;
import smile.data.DataFrame;
import smile.data.formula.Formula;
import smile.data.type.DataTypes;
import smile.data.type.StructField;
import smile.data.type.StructType;
import smile.data.vector.IntVector;
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
 *   @desc : Smile AdaBoost 集成学习
 *   @auth : tyf
 *   @date : 2022-03-08  16:38:14
*/
public class SmileAdaBoost {

    // 集成学习


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
        String csv = "C:\\Users\\tyf\\Desktop\\data.csv";

        // 模型路径
        String module = "C:\\Users\\tyf\\Desktop\\AdaBoost";

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

        // 划分训练集和验证集
        double radio = 0.8;
        int c = Double.valueOf("\n"+df.size()*radio).intValue();

        System.out.println(df);

        // 去重后的所有类别
        List<String> labels = df.stringVector("label").stream().distinct().collect(Collectors.toList());

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

        // 将类别转为int并添加新的列 labelIndex 到数据中、得到新的数据集
        DataFrame dfn = df.merge(IntVector.of("labelIndex",df.stringVector("label").stream().mapToInt(label2index).toArray()));

        // 打印新的数据
        System.out.println(dfn);


        // 训练样本和验证样本
        DataFrame trainData = dfn.slice(0,c);
        DataFrame valiaData = dfn.slice(c,dfn.size());

        System.out.println("trainData："+trainData.size());
        System.out.println("valiaData："+valiaData.size());

        // labelIndex 作为预测列
        Formula formula = Formula.of("labelIndex","f1","f2","f3","f4");

        // 模型
        AdaBoost model = AdaBoost.fit(formula, trainData);


        // 模型评估
        int[] truthIndex = valiaData.intVector("labelIndex").stream().toArray(); // 样本 label 索引
        int[] prediIndex = model.predict(valiaData); // 预测 label 索引

        System.out.println("\n模型评估：");
        System.out.println("Accuracy："+ Accuracy.of(truthIndex,prediIndex));

        System.out.println("\n模型序列化：");
        Write.object(model,new File(module).toPath());
        System.out.println("Write module："+module);
        AdaBoost obj = (AdaBoost)Read.object(new File(module).toPath());
        System.out.println("Read module："+obj);


        // 使用单个样本进行预测
        System.out.println("\nInference：");
        for (int i = 0; i < 100; i++) {
            // 一个样本
            DataFrame one = valiaData.of(i);
            // f1 f2 f3 f4
            double[] x = one.select("f1","f2","f3","f4").toArray()[0];
            // 类别
            String y = one.stringVector("label").toArray()[0];
            // 预测类别索引
            int index = obj.predict(one)[0];
            // 预测类别
            String yy = labels.get(index);
            System.out.println("Feature："+Arrays.toString(x)+"，label："+y+"，predict："+yy);
        }




    }

}
