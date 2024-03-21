package org.tyf;


import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.feature.ChiSqSelector;
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

import java.util.Arrays;
import java.util.List;

/**
 *   @desc : spark 卡方特征选择
 *   @auth : tyf
 *   @date : 2022-02-22  15:10:34
 */
public class SparkChiSqSelector {


    // 卡方特征选择

    // 传入jar
    // docker cp /root/tyf/algorithm-v1-jar-with-dependencies.jar tyf-spark-1:/usr/algorithm-v1-jar-with-dependencies.jar

    // 进入主节点容器提交任务
    // docker exec -it tyf-spark-1 /bin/bash
    // cd /opt/bitnami/spark/bin
    // spark-submit --master spark://172.16.5.248:7077 --class org.tyf.SparkChiSqSelector /usr/algorithm-v1-jar-with-dependencies.jar


    public static void main(String[] args) {


        SparkConf conf = new SparkConf().setAppName("SparkChiSqSelector").setMaster("local[*]");
        JavaSparkContext jsc = new JavaSparkContext(conf);
        SparkSession ss = SparkSession.builder().sparkContext(jsc.sc()).getOrCreate();

        List<Row> data = Arrays.asList(
                RowFactory.create(7, Vectors.dense(0.0, 0.0, 18.0, 1.0), 1.0),
                RowFactory.create(8, Vectors.dense(0.0, 1.0, 12.0, 0.0), 0.0),
                RowFactory.create(9, Vectors.dense(1.0, 0.0, 15.0, 0.1), 0.0)
        );
        StructType schema = new StructType(new StructField[]{
                new StructField("id", DataTypes.IntegerType, false, Metadata.empty()),
                new StructField("features", new VectorUDT(), false, Metadata.empty()),
                new StructField("clicked", DataTypes.DoubleType, false, Metadata.empty())
        });

        Dataset<Row> df = ss.createDataFrame(data, schema);

        ChiSqSelector selector = new ChiSqSelector()
                .setNumTopFeatures(1)
                .setFeaturesCol("features")
                .setLabelCol("clicked")
                .setOutputCol("selectedFeatures");

        Dataset<Row> result = selector.fit(df).transform(df);

        System.out.println("ChiSqSelector output with top " + selector.getNumTopFeatures()+" features selected");
        result.show();


        ss.stop();

    }


}
