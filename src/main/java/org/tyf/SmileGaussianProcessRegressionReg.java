package org.tyf;

import org.apache.commons.csv.CSVFormat;
import smile.data.DataFrame;
import smile.data.type.DataTypes;
import smile.data.type.StructField;
import smile.data.type.StructType;
import smile.io.Read;
import smile.io.Write;
import smile.plot.swing.Line;
import smile.plot.swing.LinePlot;
import smile.regression.GaussianProcessRegression;
import smile.validation.metric.*;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.Random;


/**
 *   @desc : Smile GaussianProcessRegressionReg 高斯过程回归
 *   @auth : tyf
 *   @date : 2022-03-08  16:38:14
*/
public class SmileGaussianProcessRegressionReg {

    // 高斯过程回归

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
                double score = f1*10 + f2*5 + f3*5 - f4*5 + r.nextInt(20);
                String line = f1 +","+ f2 + ","+f3 + ","+f4 + ","+score;
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
        String module = "C:\\Users\\tyf\\Desktop\\GaussianProcessRegression";

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
                        new StructField("score", DataTypes.DoubleType)
                ))
        );

        // 打印样本行列数
        System.out.println("\n"+df.size()+" => 行："+df.nrow()+" ， 列："+df.ncol()+" ，属性： "+ Arrays.toString(df.names()));
        System.out.println("\n"+df);

        // 划分训练集和验证集
        double radio = 0.98;
        int c = Double.valueOf(df.size()*radio).intValue();

        // 训练样本和验证样本
        DataFrame trainData = df.slice(0,c);
        DataFrame valiaData = df.slice(c,df.size());

        System.out.println("trainData："+trainData.size());
        System.out.println("valiaData："+valiaData.size());

        Properties params = new Properties();
        params.setProperty("smile.gaussian_process.kernel", "linear");// polynomial gaussian matern laplacian tanh tps pearson hellinger
        params.setProperty("smile.gaussian_process.noise", "1E-5");
        params.setProperty("smile.gaussian_process.normalize", "true");
        params.setProperty("smile.gaussian_process.tolerance", "1E-5");
        params.setProperty("smile.gaussian_process.iterations", "0");

        // 特征
        double[][] in_data = trainData.select("f1","f2","f3","f4").toArray();
        // 预测值
        double[] la_data = trainData.doubleVector("score").toDoubleArray();


        GaussianProcessRegression fit = GaussianProcessRegression.fit(in_data, la_data, params);


        // 模型评估 - 准确率
        double[] truth = valiaData.doubleVector("score").toDoubleArray(); // 真实值
        double[] predit = fit.predict(valiaData.select("f1","f2","f3","f4").toArray());

        // 回归模型评估是 RegressionMetric 子类
        double mes = MSE.of(truth,predit);
        double rmes = RMSE.of(truth,predit);
        double mad = MAD.of(truth,predit);
        double r2 = R2.of(truth,predit);
        double rss = RSS.of(truth,predit);
        System.out.println("mes:"+mes);
        System.out.println("rmes:"+rmes);
        System.out.println("mad:"+mad);
        System.out.println("r2:"+r2);
        System.out.println("rss:"+rss);


        System.out.println("\n模型序列化：");
        Write.object(fit,new File(module).toPath());
        System.out.println("Write module："+module);
        GaussianProcessRegression obj = (GaussianProcessRegression)Read.object(new File(module).toPath());
        System.out.println("Read module："+obj);


        // 单个样本预测
        for (int i = 0; i < 30; i++) {
            double[] in = valiaData.select("f1","f2","f3","f4").toArray()[i]; // 输入
            double score = valiaData.doubleVector("score").toDoubleArray()[i]; // 真实值
            double out = obj.predict(valiaData.of(i).select("f1","f2","f3","f4").toArray())[0]; // 预测值
            System.out.println("In："+Arrays.toString(in)+"，Score："+score+"，out："+out);
        }

        // 可视化折线图 double[][] 就是 n个xy点、double[] 就是 n个y其中x默认从1开始
        Line line1 = Line.of(Line.zipWithIndex(truth), Color.RED);
        Line line2 = Line.of(Line.zipWithIndex(predit),Color.GREEN);

        // 可以画多条线
        LinePlot lines = new LinePlot(line1,line2);
        lines.canvas().window();

    }


}
