package org.tyf;


import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import java.util.Arrays;
import java.util.List;

import org.apache.spark.ml.feature.BucketedRandomProjectionLSH;
import org.apache.spark.ml.feature.BucketedRandomProjectionLSHModel;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.ml.linalg.Vectors;
import org.apache.spark.ml.linalg.VectorUDT;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import static org.apache.spark.sql.functions.col;


/**
 *   @desc : spark 特征工程局部敏感哈希LSH
 *   @auth : tyf
 *   @date : 2022-02-22  15:10:34
 */
public class SprakBucketedRandomProjectionLSH {


    // 特征工程局部敏感哈希LSH 算法

    // 传入jar
    // docker cp /root/tyf/algorithm-v1-jar-with-dependencies.jar tyf-spark-1:/usr/algorithm-v1-jar-with-dependencies.jar

    // 进入主节点容器提交任务
    // docker exec -it tyf-spark-1 /bin/bash
    // cd /opt/bitnami/spark/bin
    // spark-submit --master spark://172.16.5.248:7077 --class org.tyf.SprakBucketedRandomProjectionLSH /usr/algorithm-v1-jar-with-dependencies.jar

    public static void main(String[] args) {

        SparkConf conf = new SparkConf().setAppName("SprakBucketedRandomProjectionLSH").setMaster("local[*]");
        JavaSparkContext jsc = new JavaSparkContext(conf);
        SparkSession ss = SparkSession.builder().sparkContext(jsc.sc()).getOrCreate();

        List<Row> dataA = Arrays.asList(
                RowFactory.create(0, Vectors.dense(1.0, 1.0)),
                RowFactory.create(1, Vectors.dense(1.0, -1.0)),
                RowFactory.create(2, Vectors.dense(-1.0, -1.0)),
                RowFactory.create(3, Vectors.dense(-1.0, 1.0))
        );

        List<Row> dataB = Arrays.asList(
                RowFactory.create(4, Vectors.dense(1.0, 0.0)),
                RowFactory.create(5, Vectors.dense(-1.0, 0.0)),
                RowFactory.create(6, Vectors.dense(0.0, 1.0)),
                RowFactory.create(7, Vectors.dense(0.0, -1.0))
        );

        StructType schema = new StructType(new StructField[]{
                new StructField("id", DataTypes.IntegerType, false, Metadata.empty()),
                new StructField("features", new VectorUDT(), false, Metadata.empty())
        });
        Dataset<Row> dfA = ss.createDataFrame(dataA, schema);
        Dataset<Row> dfB = ss.createDataFrame(dataB, schema);

        Vector key = Vectors.dense(1.0, 0.0);

        BucketedRandomProjectionLSH mh = new BucketedRandomProjectionLSH()
                .setBucketLength(2.0)
                .setNumHashTables(3)
                .setInputCol("features")
                .setOutputCol("hashes");

        BucketedRandomProjectionLSHModel model = mh.fit(dfA);

        // Feature Transformation
        System.out.println("The hashed dataset where hashed values are stored in the column 'hashes':");
        model.transform(dfA).show();

        // Compute the locality sensitive hashes for the input rows, then perform approximate
        // similarity join.
        // We could avoid computing hashes by passing in the already-transformed dataset, e.g.
        // `model.approxSimilarityJoin(transformedA, transformedB, 1.5)`
        System.out.println("Approximately joining dfA and dfB on distance smaller than 1.5:");
        model.approxSimilarityJoin(dfA, dfB, 1.5, "EuclideanDistance")
                .select(col("datasetA.id").alias("idA"),
                        col("datasetB.id").alias("idB"),
                        col("EuclideanDistance")).show();

        // Compute the locality sensitive hashes for the input rows, then perform approximate nearest
        // neighbor search.
        // We could avoid computing hashes by passing in the already-transformed dataset, e.g.
        // `model.approxNearestNeighbors(transformedA, key, 2)`
        System.out.println("Approximately searching dfA for 2 nearest neighbors of the key:");
        model.approxNearestNeighbors(dfA, key, 2).show();

        ss.stop();
    }



}
