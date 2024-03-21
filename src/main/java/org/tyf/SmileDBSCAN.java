package org.tyf;

import org.apache.commons.csv.CSVFormat;
import smile.clustering.DBSCAN;
import smile.data.DataFrame;
import smile.data.type.DataTypes;
import smile.data.type.StructField;
import smile.data.type.StructType;
import smile.io.Read;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;


/**
 *   @desc : Smile DBSCAN 聚类
 *   @auth : tyf
 *   @date : 2022-03-08  16:38:14
*/
public class SmileDBSCAN {

    // 聚类
    // 基于密度的空间聚类算法 Density-Based Spatial Clustering of Applications with

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
                double f6 = r.nextDouble()*100;
                double f7 = r.nextDouble()*100;
                double f8 = r.nextDouble()*100;
                double f9 = r.nextDouble()*100;
                double f10 = r.nextDouble()*100;
                String line = f1 +","+ f2 + ","+f3 + ","+f4 + ","+f5 + ","+f6 + ","+f7 + ","+f8 + ","+f9 + ","+f10;
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
        String module = "C:\\Users\\tyf\\Desktop\\SmileDBSCAN";

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
                        new StructField("f5", DataTypes.DoubleType),
                        new StructField("f6", DataTypes.DoubleType),
                        new StructField("f7", DataTypes.DoubleType),
                        new StructField("f8", DataTypes.DoubleType),
                        new StructField("f9", DataTypes.DoubleType),
                        new StructField("f10", DataTypes.DoubleType)
                ))
        );

        // 打印样本行列数
        System.out.println("\n"+df.size()+" => 行："+df.nrow()+" ， 列："+df.ncol()+" ，属性： "+ Arrays.toString(df.names()));



        double[][] data = df.toArray();

        // 邻域半径（radius）和形成一个簇所需的最小点数（minPts）
        int minPts = 5;
        double radius = 0.01;

        DBSCAN dbscan = DBSCAN.fit(data,minPts,radius);

        System.out.println("K："+dbscan.k);



    }

}
