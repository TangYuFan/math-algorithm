package org.tyf;

import org.apache.commons.csv.CSVFormat;
import smile.data.DataFrame;
import smile.data.type.DataTypes;
import smile.data.type.StructField;
import smile.data.type.StructType;
import smile.feature.extraction.ProbabilisticPCA;
import smile.io.Read;
import smile.math.matrix.Matrix;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;


/**
 *   @desc : Smile ProbabilisticPCA 主成分分析
 *   @auth : tyf
 *   @date : 2022-03-08  16:38:14
*/
public class SmileProbabilisticPCA {

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

        int k = 4;
        ProbabilisticPCA pca = ProbabilisticPCA.fit(df,k,"f1","f2","f3","f4","f5");

        System.out.println("projection：");
        System.out.println(pca.projection);
        System.out.println("Loadings：");
        System.out.println(pca.loadings());
        System.out.println("Center：");
        System.out.println(Arrays.toString(pca.center()));
        System.out.println("Variance：");
        System.out.println(pca.variance());

        // 降为后的数据
        Matrix AA = Matrix.of(df.toArray("f1","f2","f3","f4","f5"));
        Matrix BB = pca.projection.transpose();
        Matrix data = AA.mm(BB);

        System.out.println("Data：");
        System.out.println(data);

    }

}