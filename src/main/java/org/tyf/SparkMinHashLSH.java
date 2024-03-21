package org.tyf;


import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.ml.feature.MinHashLSH;
import org.apache.spark.ml.feature.MinHashLSHModel;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.ml.linalg.VectorUDT;
import org.apache.spark.ml.linalg.Vectors;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import static org.apache.spark.sql.functions.col;

import java.util.Arrays;
import java.util.List;

/**
 *   @desc : spark 局部敏感哈希 临近搜索
 *   @auth : tyf
 *   @date : 2022-02-22  15:10:34
 */
public class SparkMinHashLSH {


    // 局部敏感哈希 临近搜索

    // 传入jar
    // docker cp /root/tyf/algorithm-v1-jar-with-dependencies.jar tyf-spark-1:/usr/algorithm-v1-jar-with-dependencies.jar

    // 进入主节点容器提交任务
    // docker exec -it tyf-spark-1 /bin/bash
    // cd /opt/bitnami/spark/bin
    // spark-submit --master spark://172.16.5.248:7077 --class org.tyf.SparkMinHashLSH /usr/algorithm-v1-jar-with-dependencies.jar


    public static void main(String[] args) {

        SparkConf conf = new SparkConf().setAppName("SparkMinHashLSH").setMaster("local[*]");
        JavaSparkContext jsc = new JavaSparkContext(conf);
        SparkSession ss = SparkSession.builder().sparkContext(jsc.sc()).getOrCreate();

        List<Row> dataA = Arrays.asList(
                RowFactory.create(0, Vectors.sparse(6, new int[]{0, 1, 2}, new double[]{1.0, 1.0, 1.0})),
                RowFactory.create(1, Vectors.sparse(6, new int[]{2, 3, 4}, new double[]{1.0, 1.0, 1.0})),
                RowFactory.create(2, Vectors.sparse(6, new int[]{0, 2, 4}, new double[]{1.0, 1.0, 1.0}))
        );

        List<Row> dataB = Arrays.asList(
                RowFactory.create(0, Vectors.sparse(6, new int[]{1, 3, 5}, new double[]{1.0, 1.0, 1.0})),
                RowFactory.create(1, Vectors.sparse(6, new int[]{2, 3, 5}, new double[]{1.0, 1.0, 1.0})),
                RowFactory.create(2, Vectors.sparse(6, new int[]{1, 2, 4}, new double[]{1.0, 1.0, 1.0}))
        );

        StructType schema = new StructType(new StructField[]{
                new StructField("id", DataTypes.IntegerType, false, Metadata.empty()),
                new StructField("features", new VectorUDT(), false, Metadata.empty())
        });
        Dataset<Row> dfA = ss.createDataFrame(dataA, schema);
        Dataset<Row> dfB = ss.createDataFrame(dataB, schema);

        int[] indices = {1, 3};
        double[] values = {1.0, 1.0};
        Vector key = Vectors.sparse(6, indices, values);

        MinHashLSH mh = new MinHashLSH()
                .setNumHashTables(5)
                .setInputCol("features")
                .setOutputCol("hashes");

        MinHashLSHModel model = mh.fit(dfA);

        // Feature Transformation
        System.out.println("The hashed dataset where hashed values are stored in the column 'hashes':");
        model.transform(dfA).show();

        // Compute the locality sensitive hashes for the input rows, then perform approximate
        // similarity join.
        // We could avoid computing hashes by passing in the already-transformed dataset, e.g.
        // `model.approxSimilarityJoin(transformedA, transformedB, 0.6)`
        System.out.println("Approximately joining dfA and dfB on Jaccard distance smaller than 0.6:");
        model.approxSimilarityJoin(dfA, dfB, 0.6, "JaccardDistance")
                .select(col("datasetA.id").alias("idA"),
                        col("datasetB.id").alias("idB"),
                        col("JaccardDistance")).show();

        // Compute the locality sensitive hashes for the input rows, then perform approximate nearest
        // neighbor search.
        // We could avoid computing hashes by passing in the already-transformed dataset, e.g.
        // `model.approxNearestNeighbors(transformedA, key, 2)`
        // It may return less than 2 rows when not enough approximate near-neighbor candidates are
        // found.
        System.out.println("Approximately searching dfA for 2 nearest neighbors of the key:");
        model.approxNearestNeighbors(dfA, key, 2).show();


        ss.stop();

    }

}
