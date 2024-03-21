package org.tyf;


import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.clustering.LDA;
import org.apache.spark.ml.clustering.LDAModel;
import org.apache.spark.ml.linalg.Matrix;
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

/**
 *   @desc : spark 主题模型 隐含狄利克雷分布（LDA）
 *   @auth : tyf
 *   @date : 2022-02-22  15:10:34
 */
public class SparkLDA {


    // 主题模型 隐含狄利克雷分布（LDA）
    // LDA 由 David M. Blei、Andrew Y. Ng、Michael I. Jordan 于 2003 年提出，是一种主题模型，它可以将文档集中每篇文档的主题以概率分布的形式给出。一种典型的词袋模型。

    // 传入jar
    // docker cp /root/tyf/algorithm-v1-jar-with-dependencies.jar tyf-spark-1:/usr/algorithm-v1-jar-with-dependencies.jar

    // 进入主节点容器提交任务
    // docker exec -it tyf-spark-1 /bin/bash
    // cd /opt/bitnami/spark/bin
    // spark-submit --master spark://172.16.5.248:7077 --class org.tyf.SparkLDA /usr/algorithm-v1-jar-with-dependencies.jar



    public static void main(String[] args) {


        SparkConf conf = new SparkConf().setAppName("SparkLDA").setMaster("local[*]");
        JavaSparkContext jsc = new JavaSparkContext(conf);
        SparkSession ss = SparkSession.builder().sparkContext(jsc.sc()).getOrCreate();


        // 示例数据，文档表示为词频向量
        Dataset<Row> data = ss.createDataFrame(Arrays.asList(
                RowFactory.create(Vectors.sparse(3, new int[]{0, 1}, new double[]{1.0, 2.0})),
                RowFactory.create(Vectors.dense(1.0, 1.0, 1.0)),
                RowFactory.create(Vectors.dense(2.0, 0.1, 1.0)),
                RowFactory.create(Vectors.dense(1.0, 1.5, 1.0)),
                RowFactory.create(Vectors.dense(1.3, 1.3, 1.0))
        ), new StructType(new StructField[]{
                new StructField("features", new VectorUDT(), false, Metadata.empty())
        }));


        // 创建 LDA 模型
        LDA lda = new LDA().setK(3).setMaxIter(10);
        LDAModel model = lda.fit(data);

        // 对数似然性 (Log Likelihood): 用于模型评估
        double ll = model.logLikelihood(data);
        // 对数困惑度 (Log Perplexity): 用于模型评估
        double lp = model.logPerplexity(data);
        System.out.println("The lower bound on the log likelihood of the entire corpus: " + ll);
        System.out.println("The upper bound on perplexity: " + lp);

        // Describe topics.
        Dataset<Row> topics = model.describeTopics(3);
        System.out.println("The topics described by their top-weighted terms:");
        topics.show(false);

        // Shows the result.
        Dataset<Row> transformed = model.transform(data);
        transformed.show(false);

        // 打印文档的主题分布
        System.out.println("Transformed data points:");
        model.transform(data).select("features", "topicDistribution").show();


        ss.stop();

    }





}
