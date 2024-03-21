package org.tyf;


import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.regression.IsotonicRegression;
import org.apache.spark.ml.regression.IsotonicRegressionModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.util.*;

/**
 *   @desc : spark 保序回归（单调回归）分析
 *   @auth : tyf
 *   @date : 2022-02-22  15:10:34
 */
public class SparkIsotonicRegression {


    // 保序回归（单调回归）分析
    // 主要用于处理一维有序数据的回归问题、时序数据等等
    // 是在线性回归的基础上进行扩展，是比线性回归更加灵活的模型

    // 传入jar
    // docker cp /root/tyf/algorithm-v1-jar-with-dependencies.jar tyf-spark-1:/usr/algorithm-v1-jar-with-dependencies.jar

    // 进入主节点容器提交任务
    // docker exec -it tyf-spark-1 /bin/bash
    // cd /opt/bitnami/spark/bin
    // spark-submit --master spark://172.16.5.248:7077 --class org.tyf.SparkIsotonicRegression /usr/algorithm-v1-jar-with-dependencies.jar


    public static void main(String[] args) {

        SparkConf conf = new SparkConf().setAppName("SparkIsotonicRegression").setMaster("local[*]");
        JavaSparkContext jsc = new JavaSparkContext(conf);
        SparkSession ss = SparkSession.builder().sparkContext(jsc.sc()).getOrCreate();

        List<Row> rows = new ArrayList<>();
        Random r = new Random();
        for (int i = 1; i <= 1000; i++) {
            double x = r.nextDouble() * 10; // 生成0到10之间的随机数作为特征
            double y = x + r.nextGaussian() * 2; // 生成带有噪声的目标值
            rows.add(RowFactory.create(x,y));
        }

        StructType schema = new StructType(
                new StructField[]{
                        new StructField("feature", DataTypes.DoubleType, false, Metadata.empty()),
                        new StructField("label", DataTypes.DoubleType, false, Metadata.empty())
                });

        // 构造示例数据
        Dataset<Row> df = ss.createDataFrame(rows,schema);


        // 创建 Isotonic Regression 模型
        IsotonicRegression ir = new IsotonicRegression()
                .setFeaturesCol("feature")
                .setLabelCol("label")
                .setIsotonic(false); // 设置为 true 表示保序、要求输入数据有序

        // 训练模型
        IsotonicRegressionModel model = ir.fit(df);

        System.out.println("Boundaries in increasing order: " + model.boundaries() + "\n");

        // 打印模型的拟合结果
        System.out.println("Transformed data points:");
        model.transform(df).select("feature", "label", "prediction").show(50);

        ss.stop();

    }

}
