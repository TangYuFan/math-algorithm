package org.tyf;


import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.feature.DCT;
import org.apache.spark.ml.linalg.VectorUDT;
import org.apache.spark.ml.linalg.Vectors;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.util.Arrays;
import java.util.List;

/**
 *   @desc : spark DCT 离散余弦变换
 *   @auth : tyf
 *   @date : 2022-02-22  15:10:34
 */
public class SparkDCT {


    // 离散余弦变换 DCT

    // 传入jar
    // docker cp /root/tyf/algorithm-v1-jar-with-dependencies.jar tyf-spark-1:/usr/algorithm-v1-jar-with-dependencies.jar

    // 进入主节点容器提交任务
    // docker exec -it tyf-spark-1 /bin/bash
    // cd /opt/bitnami/spark/bin
    // spark-submit --master spark://172.16.5.248:7077 --class org.tyf.SparkDCT /usr/algorithm-v1-jar-with-dependencies.jar


    public static void main(String[] args) {


        SparkConf conf = new SparkConf().setAppName("SparkDCT").setMaster("local[*]");
        JavaSparkContext jsc = new JavaSparkContext(conf);
        SparkSession ss = SparkSession.builder().sparkContext(jsc.sc()).getOrCreate();

        List<Row> data = Arrays.asList(
                RowFactory.create(Vectors.dense(0.0, 1.0, -2.0, 3.0)),
                RowFactory.create(Vectors.dense(-1.0, 2.0, 4.0, -7.0)),
                RowFactory.create(Vectors.dense(14.0, -2.0, -5.0, 1.0))
        );
        StructType schema = new StructType(new StructField[]{
                new StructField("features", new VectorUDT(), false, Metadata.empty()),
        });
        Dataset<Row> df = ss.createDataFrame(data, schema);

        DCT dct = new DCT()
                .setInputCol("features")
                .setOutputCol("featuresDCT")
                .setInverse(false);

        Dataset<Row> dctDf = dct.transform(df);

        dctDf.select("featuresDCT").show(false);

        ss.stop();
    }

}
