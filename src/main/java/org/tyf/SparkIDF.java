package org.tyf;


import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.ml.feature.HashingTF;
import org.apache.spark.ml.feature.IDF;
import org.apache.spark.ml.feature.IDFModel;
import org.apache.spark.ml.feature.Tokenizer;
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
 *   @desc : spark F-IDF特征提取
 *   @auth : tyf
 *   @date : 2022-02-22  15:10:34
 */
public class SparkIDF {


    // F-IDF特征提取 词频、逆向文件频率 分析

    // 传入jar
    // docker cp /root/tyf/algorithm-v1-jar-with-dependencies.jar tyf-spark-1:/usr/algorithm-v1-jar-with-dependencies.jar

    // 进入主节点容器提交任务
    // docker exec -it tyf-spark-1 /bin/bash
    // cd /opt/bitnami/spark/bin
    // spark-submit --master spark://172.16.5.248:7077 --class org.tyf.SparkIDF /usr/algorithm-v1-jar-with-dependencies.jar


    public static void main(String[] args) {

        SparkConf conf = new SparkConf().setAppName("SparkIDF").setMaster("local[*]");
        JavaSparkContext jsc = new JavaSparkContext(conf);
        SparkSession ss = SparkSession.builder().sparkContext(jsc.sc()).getOrCreate();


        List<Row> data = Arrays.asList(
                RowFactory.create(0.0, "Hi I heard about Spark"),
                RowFactory.create(0.0, "I wish Java could use case classes"),
                RowFactory.create(1.0, "Logistic regression models are neat")
        );
        StructType schema = new StructType(new StructField[]{
                new StructField("label", DataTypes.DoubleType, false, Metadata.empty()),
                new StructField("sentence", DataTypes.StringType, false, Metadata.empty())
        });
        Dataset<Row> sentenceData = ss.createDataFrame(data, schema);

        Tokenizer tokenizer = new Tokenizer().setInputCol("sentence").setOutputCol("words");
        Dataset<Row> wordsData = tokenizer.transform(sentenceData);

        int numFeatures = 20;
        HashingTF hashingTF = new HashingTF()
                .setInputCol("words")
                .setOutputCol("rawFeatures")
                .setNumFeatures(numFeatures);

        Dataset<Row> featurizedData = hashingTF.transform(wordsData);
        // alternatively, CountVectorizer can also be used to get term frequency vectors

        IDF idf = new IDF().setInputCol("rawFeatures").setOutputCol("features");
        IDFModel idfModel = idf.fit(featurizedData);

        Dataset<Row> rescaledData = idfModel.transform(featurizedData);
        rescaledData.select("label", "features").show();



        ss.stop();

    }

}
