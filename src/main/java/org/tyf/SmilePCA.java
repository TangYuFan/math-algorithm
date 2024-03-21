package org.tyf;

import org.apache.commons.csv.CSVFormat;
import smile.data.DataFrame;
import smile.data.type.DataTypes;
import smile.data.type.StructField;
import smile.data.type.StructType;
import smile.feature.extraction.PCA;
import smile.io.Read;
import smile.math.matrix.Matrix;
import smile.plot.swing.ScatterPlot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;


/**
 *   @desc : Smile PCA 主成分分析
 *   @auth : tyf
 *   @date : 2022-03-08  16:38:14
*/
public class SmilePCA {

    // 主成分分析

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
        String csv = "C:\\Users\\tyf\\Desktop\\data.csv";

        // 生成随机样本
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


        PCA pca = PCA.fit(df,"f1","f2","f3","f4","f5");

        // 获取主成分、可以设置比例或者主成分个数
        int c = 4;
        PCA pcaResult = pca.getProjection(c);

        System.out.println("主成分特征向量:");
        System.out.println(pcaResult.loadings());

        System.out.println("主成分特征向量前k个:");
        System.out.println(pcaResult.projection);

        System.out.println("Center："+Arrays.toString(pcaResult.center()));
        System.out.println("VarianceProportion："+Arrays.toString(pcaResult.varianceProportion()));
        System.out.println("Variance："+Arrays.toString(pcaResult.variance()));


        // 投影
        double[][] A = pcaResult.projection.toArray();
        double[][] B = df.toArray();

        System.out.println("A："+A.length+"x"+A[0].length);
        System.out.println("B："+B.length+"x"+B[0].length);

        // 降维计算 C = 矩阵 B 从乘 A 的转置
        Matrix BB = Matrix.of(B);
        Matrix AA = pcaResult.projection.transpose();

        System.out.println("BB:"+(BB.nrow() +" X "+ BB.ncol()));
        System.out.println("AA:"+(AA.nrow() +" X "+ AA.ncol()));

        // 降纬后的数据
        double[][] data = BB.mm(AA).toArray();
        System.out.println("data："+data.length+"x"+data[0].length);

    }

}
