package org.tyf;

import org.apache.commons.csv.CSVFormat;
import smile.data.DataFrame;
import smile.data.type.DataTypes;
import smile.data.type.StructField;
import smile.data.type.StructType;
import smile.io.Read;
import smile.manifold.UMAP;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;


/**
 *   @desc : Smile UMAP 均匀流形逼近投影
 *   @auth : tyf
 *   @date : 2022-03-08  16:38:14
*/
public class SmileUMAP {

    // 均匀流形逼近投影
    // UMAP是一种用于降维的算法，可以用于数据可视化和一般的非线性降维。该算法基于三个关于数据的假设：
    // 数据在Riemannian流形上均匀分布。
    // Riemannian度量在局部是恒定的（或可以近似为恒定的）。
    // 流形在局部是连通的。



    // 生成随机训练样本
    public static void generateCsv(String csv){
        Random r = new Random();
        if(new File(csv).exists()){
            new File(csv).delete();
        }
        // 写入到本地csv
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csv, true))) {
            for (int i = 0; i < 3000; i++) {
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


        int k = 3;
        UMAP umap = UMAP.of(df.toArray(),k);

        System.out.println("Coordinates：");
        for (int i = 0; i < umap.coordinates.length; i++) {
            System.out.println("C:"+Arrays.toString(umap.coordinates[i]));
        }




    }

}
