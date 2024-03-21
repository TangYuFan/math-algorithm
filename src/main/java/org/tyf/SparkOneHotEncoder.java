package org.tyf;


import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.ml.feature.OneHotEncoder;
import org.apache.spark.ml.feature.OneHotEncoderModel;
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
 *   @desc : spark 独热编码
 *   @auth : tyf
 *   @date : 2022-02-22  15:10:34
 */
public class SparkOneHotEncoder {



    // 独热编码

    // 传入jar
    // docker cp /root/tyf/algorithm-v1-jar-with-dependencies.jar tyf-spark-1:/usr/algorithm-v1-jar-with-dependencies.jar

    // 进入主节点容器提交任务
    // docker exec -it tyf-spark-1 /bin/bash
    // cd /opt/bitnami/spark/bin
    // spark-submit --master spark://172.16.5.248:7077 --class org.tyf.SparkOneHotEncoder /usr/algorithm-v1-jar-with-dependencies.jar


    public static void main(String[] args) {

        SparkConf conf = new SparkConf().setAppName("SparkOneHotEncoder").setMaster("local[*]");
        JavaSparkContext jsc = new JavaSparkContext(conf);
        SparkSession ss = SparkSession.builder().sparkContext(jsc.sc()).getOrCreate();

        List<Row> data = Arrays.asList(
                RowFactory.create(0.0, 1.0),
                RowFactory.create(1.0, 0.0),
                RowFactory.create(2.0, 1.0),
                RowFactory.create(0.0, 2.0),
                RowFactory.create(0.0, 1.0),
                RowFactory.create(2.0, 0.0)
        );

        StructType schema = new StructType(new StructField[]{
                new StructField("categoryIndex1", DataTypes.DoubleType, false, Metadata.empty()),
                new StructField("categoryIndex2", DataTypes.DoubleType, false, Metadata.empty())
        });

        Dataset<Row> df = ss.createDataFrame(data, schema);

        OneHotEncoder encoder = new OneHotEncoder()
                .setInputCols(new String[] {"categoryIndex1", "categoryIndex2"})
                .setOutputCols(new String[] {"categoryVec1", "categoryVec2"});

        OneHotEncoderModel model = encoder.fit(df);
        Dataset<Row> encoded = model.transform(df);
        encoded.show();

        ss.stop();

    }


}
