package org.tyf;


import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.evaluation.RegressionEvaluator;
import org.apache.spark.ml.recommendation.ALS;
import org.apache.spark.ml.recommendation.ALSModel;
import org.apache.spark.mllib.recommendation.Rating;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 *   @desc : spark 交替最小二乘 协同过滤推荐算法 ALS
 *   @auth : tyf
 *   @date : 2022-02-22  15:10:34
 */
public class SparkALS {


    // 交替最小二乘 协同过滤推荐算法 ALS

    // 传入jar
    // docker cp /root/tyf/algorithm-v1-jar-with-dependencies.jar tyf-spark-1:/usr/algorithm-v1-jar-with-dependencies.jar

    // 进入主节点容器提交任务
    // docker exec -it tyf-spark-1 /bin/bash
    // cd /opt/bitnami/spark/bin
    // spark-submit --master spark://172.16.5.248:7077 --class org.tyf.SparkALS /usr/algorithm-v1-jar-with-dependencies.jar



    public static void main(String[] args) {

        SparkConf conf = new SparkConf().setAppName("SparkALS").setMaster("local[*]");
        JavaSparkContext jsc = new JavaSparkContext(conf);
        SparkSession ss = SparkSession.builder().sparkContext(jsc.sc()).getOrCreate();

        Random r = new Random();

        // 直接模拟数据
        Row[] data = new Row[10000];
        for (int i = 0; i < data.length; i++) {
            // userId itemId score
            Row row = RowFactory.create(r.nextInt(100),r.nextInt(100),r.nextDouble()*100);
            data[i] = row;
        }

        StructType schema = new StructType(
                new StructField[]{
                        new StructField("userId", DataTypes.IntegerType, false, Metadata.empty()),
                        new StructField("itemId", DataTypes.IntegerType, false, Metadata.empty()),
                        new StructField("score", DataTypes.DoubleType, false, Metadata.empty())
                });


        // 转换为 DataFrame
        Dataset<Row> df = ss.createDataFrame(Arrays.asList(data), schema);

        // 也可以通过自带的用户选项记录对象创建数据集
//        JavaRDD<Rating> data = jsc.parallelize(Arrays.asList(
//                new Rating(1,1,10),
//                new Rating(1,1,10),
//                new Rating(1,1,10)
//        ));
//        Dataset<Row> df = ss.createDataFrame(data2, Rating.class);

        // 训练验证集划分
        Dataset<Row>[] splits = df.randomSplit(new double[]{0.8, 0.2});
        Dataset<Row> trainSet = splits[0];
        Dataset<Row> testSet = splits[1];

        // 构建ALS模型
        ALS als = new ALS()
                .setMaxIter(5)
                .setRegParam(0.01)
                .setUserCol("userId")
                .setItemCol("itemId")
                .setRatingCol("score");

        // 使用数据拟合模型
        ALSModel model = als.fit(trainSet);


        Dataset<Row> itemFactors =  model.itemFactors();
        Dataset<Row> userFactors = model.userFactors();

        // 指定n个用户生成m个推荐项目,als只能对已有的项目和用户进行推荐需要确保数据集中存在这个用户和项目
        Dataset<Row> userRecs = model.recommendForUserSubset(df.select(als.getUserCol()).distinct().limit(3), 5);
        // 指定n个项目生成m个推向用户,als只能对已有的项目和用户进行推荐需要确保存在这个用户和项目
        Dataset<Row> itemRecs = model.recommendForItemSubset(df.select(als.getItemCol()).distinct().limit(3), 5);

        // 为每个用户生成n个推荐项目
        Dataset<Row> userRecsAll = model.recommendForAllUsers(5);
        // 为每个产品生成n个推荐用户
        Dataset<Row> itemRecsAll = model.recommendForAllItems(5);

        System.out.println("userRecs:");
        userRecs.show();

        System.out.println("itemRecs:");
        itemRecs.show();

        System.out.println("userRecsAll:");
        userRecsAll.show();

        System.out.println("itemRecsAll:");
        itemRecsAll.show();

        // 添加打印语句，查看模型输出的列
        System.out.println("Columns in userRecs: " + Arrays.toString(userRecsAll.columns()));
        System.out.println("Columns in itemRecs: " + Arrays.toString(userRecsAll.columns()));
        System.out.println("Columns in userRecsAll: " + Arrays.toString(userRecsAll.columns()));
        System.out.println("Columns in itemRecsAll: " + Arrays.toString(itemRecsAll.columns()));

        // 将模型在测试集上的预测结果与实际评分进行比较  [userId, itemId, score, prediction]
        Dataset<Row> predictions = model.transform(testSet);
        System.out.println("Columns in predictions: " + Arrays.toString(predictions.columns()));

        // 评估器
        RegressionEvaluator evaluator = new RegressionEvaluator()
                .setLabelCol("score")  // 设置实际评分列
                .setPredictionCol("prediction")  // 设置模型预测列
                .setMetricName("rmse");  // 选择评估指标，可以选择 "rmse", "mse", "mae", "r2"

        // 计算并打印评估指标
        double rmse = evaluator.evaluate(predictions);
        System.out.println("Root Mean Squared Error (RMSE) on test data = " + rmse);

        // 关闭Spark上下文
        jsc.stop();



    }


}
