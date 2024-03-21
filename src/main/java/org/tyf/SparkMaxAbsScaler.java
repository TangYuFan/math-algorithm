package org.tyf;


import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.ml.feature.MaxAbsScaler;
import org.apache.spark.ml.feature.MaxAbsScalerModel;
import org.apache.spark.ml.linalg.Vectors;
import org.apache.spark.ml.linalg.VectorUDT;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.util.Arrays;
import java.util.List;

/**
 *   @desc : spark 特征工程 特征缩放
 *   @auth : tyf
 *   @date : 2022-02-22  15:10:34
 */
public class SparkMaxAbsScaler {


    // 特征工程 特征缩放

    // 传入jar
    // docker cp /root/tyf/algorithm-v1-jar-with-dependencies.jar tyf-spark-1:/usr/algorithm-v1-jar-with-dependencies.jar

    // 进入主节点容器提交任务
    // docker exec -it tyf-spark-1 /bin/bash
    // cd /opt/bitnami/spark/bin
    // spark-submit --master spark://172.16.5.248:7077 --class org.tyf.SparkMaxAbsScaler /usr/algorithm-v1-jar-with-dependencies.jar


    public static void main(String[] args) {


        SparkConf conf = new SparkConf().setAppName("SparkMaxAbsScaler").setMaster("local[*]");
        JavaSparkContext jsc = new JavaSparkContext(conf);
        SparkSession ss = SparkSession.builder().sparkContext(jsc.sc()).getOrCreate();

        // $example on$
        List<Row> data = Arrays.asList(
                RowFactory.create(0, Vectors.dense(1.0, 0.1, -8.0)),
                RowFactory.create(1, Vectors.dense(2.0, 1.0, -4.0)),
                RowFactory.create(2, Vectors.dense(4.0, 10.0, 8.0))
        );
        StructType schema = new StructType(new StructField[]{
                new StructField("id", DataTypes.IntegerType, false, Metadata.empty()),
                new StructField("features", new VectorUDT(), false, Metadata.empty())
        });
        Dataset<Row> dataFrame = ss.createDataFrame(data, schema);

        MaxAbsScaler scaler = new MaxAbsScaler()
                .setInputCol("features")
                .setOutputCol("scaledFeatures");

        // Compute summary statistics and generate MaxAbsScalerModel
        MaxAbsScalerModel scalerModel = scaler.fit(dataFrame);

        // rescale each feature to range [-1, 1].
        Dataset<Row> scaledData = scalerModel.transform(dataFrame);
        scaledData.select("features", "scaledFeatures").show();



        ss.stop();


    }


}
