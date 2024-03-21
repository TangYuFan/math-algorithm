package org.tyf;


import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import java.util.Arrays;
import java.util.List;

import org.apache.spark.ml.clustering.PowerIterationClustering;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

/**
 *   @desc : spark 幂迭代聚类 (PIC)
 *   @auth : tyf
 *   @date : 2022-02-22  15:10:34
 */
public class SparkPowerIterationClustering {



    // 幂迭代聚类 (PIC) 一种由 Lin 和 Cohen 开发的可扩展图聚类算法。

    // 传入jar
    // docker cp /root/tyf/algorithm-v1-jar-with-dependencies.jar tyf-spark-1:/usr/algorithm-v1-jar-with-dependencies.jar

    // 进入主节点容器提交任务
    // docker exec -it tyf-spark-1 /bin/bash
    // cd /opt/bitnami/spark/bin
    // spark-submit --master spark://172.16.5.248:7077 --class org.tyf.SparkPowerIterationClustering /usr/algorithm-v1-jar-with-dependencies.jar


    public static void main(String[] args) {

        SparkConf conf = new SparkConf().setAppName("SparkPowerIterationClustering").setMaster("local[*]");
        JavaSparkContext jsc = new JavaSparkContext(conf);
        SparkSession ss = SparkSession.builder().sparkContext(jsc.sc()).getOrCreate();

        List<Row> data = Arrays.asList(
                RowFactory.create(0L, 1L, 1.0),
                RowFactory.create(0L, 2L, 1.0),
                RowFactory.create(1L, 2L, 1.0),
                RowFactory.create(3L, 4L, 1.0),
                RowFactory.create(4L, 0L, 0.1)
        );

        StructType schema = new StructType(new StructField[]{
                new StructField("src", DataTypes.LongType, false, Metadata.empty()),
                new StructField("dst", DataTypes.LongType, false, Metadata.empty()),
                new StructField("weight", DataTypes.DoubleType, false, Metadata.empty())
        });

        Dataset<Row> df = ss.createDataFrame(data, schema);

        PowerIterationClustering model = new PowerIterationClustering()
                .setK(2)
                .setMaxIter(10)
                .setInitMode("degree")
                .setWeightCol("weight");

        Dataset<Row> result = model.assignClusters(df);
        result.show(false);
        // $example off$
        ss.stop();

    }



}
