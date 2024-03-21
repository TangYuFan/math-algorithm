package org.tyf;


import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import java.util.Arrays;
import java.util.List;

import org.apache.spark.ml.fpm.FPGrowth;
import org.apache.spark.ml.fpm.FPGrowthModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.*;

/**
 *   @desc : spark 模式挖掘 FPGrowth
 *   @auth : tyf
 *   @date : 2022-02-22  15:10:34
 */
public class SparkFPGrowth {


    // FP树 模式挖掘 FPGrowth
    // FPGrowth（Frequent Pattern Growth）是一种用于发现频繁项集的数据挖掘算法。它主要用于关联规则挖掘，其中频繁项集表示在数据集中经常出现的一组物品。
    // 具体来说，FPGrowth的作用包括：
    // 发现频繁项集： FPGrowth通过建立FP树（频繁模式树）的数据结构来高效地发现频繁项集。FP树的构建过程将原始数据集转换为一种紧凑的形式，然后通过树结构进行频繁项集的挖掘。
    // 生成关联规则： 通过发现频繁项集，FPGrowth可以生成关联规则。关联规则描述了数据中物品之间的关系，例如购物篮分析中的“如果购买了商品A，那么购买商品B的概率较大”。
    // 提供推荐系统支持： 基于频繁项集和关联规则，可以构建推荐系统，帮助用户发现可能感兴趣的物品。
    // 数据压缩和快速查询： FP树结构的构建可以将原始数据集进行压缩，从而加速频繁项集的查询和挖掘。
    // 总体而言，FPGrowth是一种强大的关联规则挖掘算法，适用于从大规模数据集中高效地提取频繁项集和关联规则的场景。

    // 一般的常用在购物车商品数据、购买历史等等挖掘商品之间的关联关系。

    // 传入jar
    // docker cp /root/tyf/algorithm-v1-jar-with-dependencies.jar tyf-spark-1:/usr/algorithm-v1-jar-with-dependencies.jar

    // 进入主节点容器提交任务
    // docker exec -it tyf-spark-1 /bin/bash
    // cd /opt/bitnami/spark/bin
    // spark-submit --master spark://172.16.5.248:7077 --class org.tyf.SparkFPGrowth /usr/algorithm-v1-jar-with-dependencies.jar



    public static void main(String[] args) {


        SparkConf conf = new SparkConf().setAppName("SparkFPGrowth").setMaster("local[*]");
        JavaSparkContext jsc = new JavaSparkContext(conf);
        SparkSession ss = SparkSession.builder().sparkContext(jsc.sc()).getOrCreate();

        List<Row> data = Arrays.asList(
                RowFactory.create(Arrays.asList("1 2 5".split(" "))),
                RowFactory.create(Arrays.asList("1 2 3 5".split(" "))),
                RowFactory.create(Arrays.asList("1 2".split(" ")))
        );
        StructType schema = new StructType(new StructField[]{ new StructField(
                "items", new ArrayType(DataTypes.StringType, true), false, Metadata.empty())
        });
        Dataset<Row> itemsDF = ss.createDataFrame(data, schema);

        FPGrowthModel model = new FPGrowth()
                .setItemsCol("items")
                .setMinSupport(0.5)
                .setMinConfidence(0.6)
                .fit(itemsDF);

        // Display frequent itemsets.
        model.freqItemsets().show();

        // Display generated association rules.
        model.associationRules().show();

        // transform examines the input items against all the association rules and summarize the
        // consequents as prediction
        model.transform(itemsDF).show();

        ss.stop();

    }


}
