package org.tyf;

import org.apache.commons.csv.CSVFormat;
import smile.clustering.KMeans;
import smile.data.DataFrame;
import smile.data.type.DataTypes;
import smile.data.type.StructField;
import smile.data.type.StructType;
import smile.io.Read;
import smile.io.Write;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;


/**
 *   @desc : Smile KMeans 聚类
 *   @auth : tyf
 *   @date : 2022-03-08  16:38:14
*/
public class SmileKMeans {

    // 聚类

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
                double f5 = r.nextDouble()*100;
                String line = f1 +","+ f2 + ","+f3 + ","+f4 + ","+f5;
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
        String module = "C:\\Users\\唐于凡\\Desktop\\SmileKMeans";

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
                        new StructField("f5", DataTypes.DoubleType)
                ))
        );

        // 打印样本行列数
        System.out.println("\n"+df.size()+" => 行："+df.nrow()+" ， 列："+df.ncol()+" ，属性： "+ Arrays.toString(df.names()));

        // 划分训练集和验证集
        double radio = 0.8;
        int c = Double.valueOf("\n"+df.size()*radio).intValue();

        // 训练样本和测试样本
        DataFrame trainData = df.slice(0,c);
        DataFrame testData = df.slice(c,df.size());

        System.out.println("trainData："+trainData.size());
        System.out.println("testData："+testData.size());


        int k = 3;
        KMeans kMeans = KMeans.fit(trainData.select("f1","f2","f3","f4","f5").toArray(),k);

        System.out.println("kMeans:"+kMeans);

        System.out.println("\n模型序列化：");
        Write.object(kMeans,new File(module).toPath());
        System.out.println("Write module："+module);
        KMeans obj = (KMeans)Read.object(new File(module).toPath());
        System.out.println("Read module："+obj);

        // 预测
        for (int i = 0; i < 30; i++) {
            double[] in = testData.select("f1","f2","f3","f4","f5").of(i).toArray()[0];
            int out = obj.predict(in);
            System.out.println("In："+Arrays.toString(in)+"，Out："+out);
        }

    }

}
