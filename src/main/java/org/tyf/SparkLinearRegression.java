package org.tyf;


import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.evaluation.RegressionEvaluator;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.ml.linalg.Vectors;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.ml.regression.LinearRegression;
import org.apache.spark.ml.regression.LinearRegressionModel;
import org.apache.spark.ml.regression.LinearRegressionTrainingSummary;

import java.util.Arrays;
import java.util.Random;

/**
 *   @desc : spark 线性回归
 *   @auth : tyf
 *   @date : 2022-02-22  15:10:34
 */
public class SparkLinearRegression {



    // 线性回归


    // 传入jar
    // docker cp /root/tyf/algorithm-v1-jar-with-dependencies.jar tyf-spark-1:/usr/algorithm-v1-jar-with-dependencies.jar

    // 进入主节点容器提交任务
    // docker exec -it tyf-spark-1 /bin/bash
    // cd /opt/bitnami/spark/bin
    // spark-submit --master spark://172.16.5.248:7077 --class org.tyf.SparkLinearRegression /usr/algorithm-v1-jar-with-dependencies.jar


    public static Random r = new Random();

    // 生成一个随机的样本
    public static Row randomRow(){
        double f1 = r.nextDouble()*100;
        double f2 = r.nextDouble()*100;
        double f3 = r.nextDouble()*100;
        double f4 = r.nextDouble()*100;
        double price = (f1*2)+f2+3*f3-(5*f4) + r.nextInt(100);
        // 返回样本
        return RowFactory.create(f1,f2,f3,f4,price);
    }

    public static void main(String[] args) {

        SparkConf conf = new SparkConf().setAppName("SparkLinearRegression").setMaster("local[*]");
        JavaSparkContext jsc = new JavaSparkContext(conf);
        SparkSession ss = SparkSession.builder().sparkContext(jsc.sc()).getOrCreate();

        // 随机生成样本数据
        // f1 f2 f3 f4 price
        Row[] data = new Row[10000];
        for (int i = 0; i < data.length; i++) {
            Row row = randomRow();
            data[i] = row;
        }

        StructType schema = new StructType(
                new StructField[]{
                        new StructField("f1", DataTypes.DoubleType, false, Metadata.empty()),
                        new StructField("f2", DataTypes.DoubleType, false, Metadata.empty()),
                        new StructField("f3", DataTypes.DoubleType, false, Metadata.empty()),
                        new StructField("f4", DataTypes.DoubleType, false, Metadata.empty()),
                        new StructField("price", DataTypes.DoubleType, false, Metadata.empty())
                });

        Dataset<Row> df = ss.createDataFrame(Arrays.asList(data), schema);

        // 特征合并
        VectorAssembler assembler = new VectorAssembler()
                .setInputCols(new String[]{"f1", "f2", "f3", "f4"})
                .setOutputCol("features");
        Dataset<Row> assembledData = assembler.transform(df);


        // 训练验证集划分
        Dataset<Row>[] splits = assembledData.randomSplit(new double[]{0.8, 0.2});
        Dataset<Row> trainSet = splits[0];
        Dataset<Row> testSet = splits[1];


        LinearRegression lr = new LinearRegression()
                .setFeaturesCol("features")
                .setLabelCol("price")
                .setMaxIter(10) //设置最大迭代次数
                .setRegParam(0.3) //正则化参数
                .setElasticNetParam(1);//L1，L2混合正则化(aL1+(1-a)L2)

        // 开始训练
        LinearRegressionModel lrModel = lr.fit(trainSet);

        // 获取训练摘要
        LinearRegressionTrainingSummary summary = lrModel.summary();

        // 测试集
        Dataset<Row> testPr = lrModel.transform(testSet);
        System.out.println("Columns in testPr: " + Arrays.toString(testPr.columns()));
        testPr.show(200);

        // 评估模型
        RegressionEvaluator evaluator = new RegressionEvaluator()
                .setLabelCol("price")
                .setPredictionCol("prediction")
                .setMetricName("rmse"); // 使用RMSE损失

        double rmse = evaluator.evaluate(testPr);
        System.out.println("Root Mean Squared Error (RMSE) on test data = " + rmse);

        // 打印系数和统计信息
        System.out.println("Root Mean Squared Error (RMSE): " + summary.rootMeanSquaredError());
        System.out.println("R-squared: " + summary.r2());
        System.out.println("Mean Absolute Error (MAE): " + summary.meanAbsoluteError());
        System.out.println("Coefficients: " + lrModel.coefficients() + " Intercept: " + lrModel.intercept());

        // 保存到hdfs
//        testPr.javaRDD().saveAsTextFile("/data.txt");

        // 保存到本地 逗号合并成一行数据
//        testPr.withColumn("line", functions.concat_ws(",",
//                testPr.col("f1"),
//                testPr.col("f2"),
//                testPr.col("f3"),
//                testPr.col("f4"),
//                testPr.col("price"),
//                testPr.col("prediction"))
//        ).select("line").write().text("/opt/bitnami/spark/bin/prediction");

        // 关闭 Spark 上下文
        jsc.stop();

    }





}
