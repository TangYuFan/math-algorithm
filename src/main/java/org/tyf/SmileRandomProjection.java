package org.tyf;

import org.apache.commons.csv.CSVFormat;
import smile.data.DataFrame;
import smile.data.type.DataTypes;
import smile.data.type.StructField;
import smile.data.type.StructType;
import smile.feature.extraction.RandomProjection;
import smile.io.Read;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;


/**
 *   @desc : Smile RandomProjection 随机投影
 *   @auth : tyf
 *   @date : 2022-03-08  16:38:14
*/
public class SmileRandomProjection {

    // 随机投影，用于降纬


    // 测试非稀疏随机投影
    private static void testDenseRandomProjection() throws Exception{

        // 样本路径
        String csv = "C:\\Users\\tyf\\Desktop\\data.csv";

        // 生成随机样本
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

        // 读取样本
        DataFrame data = Read.csv(
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


        System.out.println("\nTesting Dense Random Projection:");

        // 打印样本行列数
        System.out.println("\n"+data.size()+" => 行："+data.nrow()+" ， 列："+data.ncol()+" ，属性： "+ Arrays.toString(data.names()));

        int n = 5; // 原始纬度
        int p = 3; // 目标纬度

        // 使用非稀疏随机投影生成模型
        RandomProjection model = RandomProjection.of(n, p, data.names());

        // 降维
        double[][] rt = model.apply(data.toArray());

        for (int i = 0; i < rt.length; i++) {
            System.out.println("结果："+Arrays.toString(rt[i]));
        }

    }

    // 测试稀疏随机投影
    private static void testSparseRandomProjection() throws Exception{

        // 样本路径
        String csv = "C:\\Users\\tyf\\Desktop\\data.csv";

        // 生成随机样本
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

        // 读取样本
        DataFrame data = Read.csv(
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


        System.out.println("\nTesting Dense Random Projection:");

        // 打印样本行列数
        System.out.println("\n"+data.size()+" => 行："+data.nrow()+" ， 列："+data.ncol()+" ，属性： "+ Arrays.toString(data.names()));

        int n = 5; // 原始纬度
        int p = 3; // 目标纬度

        // 使用非稀疏随机投影生成模型
        RandomProjection model = RandomProjection.sparse(n, p, data.names());

        // 降维
        double[][] rt = model.apply(data.toArray());


        for (int i = 0; i < rt.length; i++) {
            System.out.println("结果："+Arrays.toString(rt[i]));
        }


    }


    public static void main(String[] args) throws Exception{

        // 测试非稀疏随机投影
        // 使用高斯分布生成矩阵元素。这样生成的投影矩阵是密集的，即大多数元素都非零。
        // 高斯分布通常会生成接近于零的值，因此投影矩阵的元素可能包含较小的值。
        testDenseRandomProjection();

        // 测试稀疏随机投影,投影结果为稀疏矩阵
        // 通过均匀分布从一组预定义的概率中选择元素值。这会导致生成的投影矩阵是稀疏的，
        // 即其中大多数元素为零。由于是均匀分布，元素值可能较大。
//        testSparseRandomProjection();

    }

}
