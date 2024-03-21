package org.tyf;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.feature.Word2Vec;
import org.apache.spark.ml.feature.Word2VecModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.*;

import java.util.Arrays;
import java.util.List;

/**
 *   @desc : spark Word2Vec 词嵌入
 *   @auth : tyf
 *   @date : 2022-02-22  15:10:34
 */
public class SparkWord2Vec {


    // 词嵌入
    // Word2Vec是一个词嵌入方法，可以计算每个单词在给定的语料库环境下的分布式向量，如果两个单词的语义相近，
    // 那么词向量在向量空间中也相互接近，判断向量空间的接近程度来判断来两个单词是否相似
    // 多用于翻译、预测当前语言环境的下一个词等等

    // 传入jar
    // docker cp /root/tyf/algorithm-v1-jar-with-dependencies.jar tyf-spark-1:/usr/algorithm-v1-jar-with-dependencies.jar

    // 进入主节点容器提交任务
    // docker exec -it tyf-spark-1 /bin/bash
    // cd /opt/bitnami/spark/bin
    // spark-submit --master spark://172.16.5.248:7077 --class org.tyf.SparkWord2Vec /usr/algorithm-v1-jar-with-dependencies.jar


    public static void main(String[] args) {


        SparkConf conf = new SparkConf().setAppName("SparkRandomForest").setMaster("local[*]");
        JavaSparkContext jsc = new JavaSparkContext(conf);
        SparkSession ss = SparkSession.builder().sparkContext(jsc.sc()).getOrCreate();

        // 准备数据集
        List<Row> data = Arrays.asList(
                RowFactory.create(Arrays.asList("I", "love", "Spark")),
                RowFactory.create(Arrays.asList("Word2Vec", "is", "cool")),
                RowFactory.create(Arrays.asList("Spark", "MLlib", "is", "awesome"))
        );

        StructType schema = new StructType(new StructField[]{
                new StructField("text", new ArrayType(DataTypes.StringType, true), false, Metadata.empty())
        });

        Dataset<Row> sentenceData = ss.createDataFrame(data, schema);

        // 创建 Word2Vec 模型
        Word2Vec word2Vec = new Word2Vec()
                .setInputCol("text")
                .setOutputCol("result")
                .setVectorSize(3)  // 设置词向量的维度
                .setMinCount(0);

        // 训练 Word2Vec 模型
        Word2VecModel model = word2Vec.fit(sentenceData);

        // 对数据进行转换，得到包含词向量的新列
        Dataset<Row> result = model.transform(sentenceData);
        result.select("result").show(false);

        // 查找与给定单词最相似的词
        Dataset<Row> synonyms = model.findSynonyms("Spark", 5);
        synonyms.show(false);

        // 关闭 Spark 上下文
        jsc.stop();

    }



}
