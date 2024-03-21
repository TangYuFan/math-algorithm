package org.tyf;


import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.clustering.BisectingKMeans;
import org.apache.spark.ml.clustering.BisectingKMeansModel;
import org.apache.spark.ml.linalg.VectorUDT;
import org.apache.spark.ml.linalg.Vectors;
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
 *   @desc : spark 二分K均值算法
 *   @auth : tyf
 *   @date : 2022-02-22  15:10:34
 */
public class SparkBisectingKMeans {


    // 二分K均值算法 能够克服K均值收敛于局部最小的局限,在聚类效果上展示出比较稳定的性能


    // 传入jar
    // docker cp /root/tyf/algorithm-v1-jar-with-dependencies.jar tyf-spark-1:/usr/algorithm-v1-jar-with-dependencies.jar

    // 进入主节点容器提交任务
    // docker exec -it tyf-spark-1 /bin/bash
    // cd /opt/bitnami/spark/bin
    // spark-submit --master spark://172.16.5.248:7077 --class org.tyf.SparkBisectingKMeans /usr/algorithm-v1-jar-with-dependencies.jar


    public static void main(String[] args) {


        SparkConf conf = new SparkConf().setAppName("SparkBisectingKMeansExample").setMaster("local[*]");
        JavaSparkContext jsc = new JavaSparkContext(conf);
        SparkSession ss = SparkSession.builder().sparkContext(jsc.sc()).getOrCreate();

        Random r = new Random();

        // 创建模拟数据
        List<Row> data = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            data.add(RowFactory.create(Vectors.dense(new double[]{
                    r.nextDouble(),
                    r.nextDouble(),
                    r.nextDouble(),
                    r.nextDouble(),
                    r.nextDouble()
            })));
        }

        // 构建数据集的Schema
        StructType schema = new StructType(new StructField[]{
                new StructField("features", new VectorUDT(), false, Metadata.empty()),
        });

        // 创建 DataFrame
        Dataset<Row> df = ss.createDataFrame(data, schema);

        // 构建 BisectingKMeans 模型
        BisectingKMeans kmeans = new BisectingKMeans()
                .setK(3)  // 设置聚类的簇数
                .setSeed(1L);  // 设置随机种子

        // 拟合模型
        BisectingKMeansModel model = kmeans.fit(df);

        // 获取聚类结果
        Dataset<Row> predictions = model.transform(df);
        System.out.println("Columns in predictions: " + Arrays.toString(predictions.columns()));
        predictions.show();

        // 打印聚类结果
        System.out.println("Cluster Centers: ");
        Arrays.stream(model.clusterCenters()).forEach(center->{
            System.out.println("center: "+center);
        });

        // 关闭 Spark 上下文
        jsc.stop();

    }



}
