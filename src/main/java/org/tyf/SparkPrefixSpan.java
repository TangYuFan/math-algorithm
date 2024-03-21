package org.tyf;


import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import java.util.Arrays;
import java.util.List;

import org.apache.spark.ml.fpm.PrefixSpan;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.*;

/**
 *   @desc : spark 时序数据频繁挖掘
 *   @auth : tyf
 *   @date : 2022-02-22  15:10:34
 */
public class SparkPrefixSpan {


    // 时序数据频繁挖掘

    // 传入jar
    // docker cp /root/tyf/algorithm-v1-jar-with-dependencies.jar tyf-spark-1:/usr/algorithm-v1-jar-with-dependencies.jar

    // 进入主节点容器提交任务
    // docker exec -it tyf-spark-1 /bin/bash
    // cd /opt/bitnami/spark/bin
    // spark-submit --master spark://172.16.5.248:7077 --class org.tyf.SparkPrefixSpan /usr/algorithm-v1-jar-with-dependencies.jar


    public static void main(String[] args) {

        SparkConf conf = new SparkConf().setAppName("SparkPrefixSpan").setMaster("local[*]");
        JavaSparkContext jsc = new JavaSparkContext(conf);
        SparkSession ss = SparkSession.builder().sparkContext(jsc.sc()).getOrCreate();

        List<Row> data = Arrays.asList(
                RowFactory.create(Arrays.asList(Arrays.asList(1, 2), Arrays.asList(3))),
                RowFactory.create(Arrays.asList(Arrays.asList(1), Arrays.asList(3, 2), Arrays.asList(1,2))),
                RowFactory.create(Arrays.asList(Arrays.asList(1, 2), Arrays.asList(5))),
                RowFactory.create(Arrays.asList(Arrays.asList(6)))
        );
        StructType schema = new StructType(new StructField[]{ new StructField(
                "sequence", new ArrayType(new ArrayType(DataTypes.IntegerType, true), true),
                false, Metadata.empty())
        });
        Dataset<Row> sequenceDF = ss.createDataFrame(data, schema);

        PrefixSpan prefixSpan = new PrefixSpan().setMinSupport(0.5).setMaxPatternLength(5);

        // Finding frequent sequential patterns
        prefixSpan.findFrequentSequentialPatterns(sequenceDF).show();

        ss.stop();

    }


}
