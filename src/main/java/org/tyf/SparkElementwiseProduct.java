package org.tyf;


import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.spark.ml.feature.ElementwiseProduct;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.ml.linalg.VectorUDT;
import org.apache.spark.ml.linalg.Vectors;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

/**
 *   @desc : spark 特征转换 乘积转换
 *   @auth : tyf
 *   @date : 2022-02-22  15:10:34
 */
public class SparkElementwiseProduct {


    // 特征转换 乘积转换

    // 传入jar
    // docker cp /root/tyf/algorithm-v1-jar-with-dependencies.jar tyf-spark-1:/usr/algorithm-v1-jar-with-dependencies.jar

    // 进入主节点容器提交任务
    // docker exec -it tyf-spark-1 /bin/bash
    // cd /opt/bitnami/spark/bin
    // spark-submit --master spark://172.16.5.248:7077 --class org.tyf.SparkElementwiseProduct /usr/algorithm-v1-jar-with-dependencies.jar

    public static void main(String[] args) {

        SparkConf conf = new SparkConf().setAppName("SparkElementwiseProduct").setMaster("local[*]");
        JavaSparkContext jsc = new JavaSparkContext(conf);
        SparkSession ss = SparkSession.builder().sparkContext(jsc.sc()).getOrCreate();

        List<Row> data = Arrays.asList(
                RowFactory.create("a", Vectors.dense(1.0, 2.0, 3.0)),
                RowFactory.create("b", Vectors.dense(4.0, 5.0, 6.0))
        );

        List<StructField> fields = new ArrayList<>(2);
        fields.add(DataTypes.createStructField("id", DataTypes.StringType, false));
        fields.add(DataTypes.createStructField("vector", new VectorUDT(), false));

        StructType schema = DataTypes.createStructType(fields);

        Dataset<Row> dataFrame = ss.createDataFrame(data, schema);

        Vector transformingVector = Vectors.dense(0.0, 1.0, 2.0);

        ElementwiseProduct transformer = new ElementwiseProduct()
                .setScalingVec(transformingVector)
                .setInputCol("vector")
                .setOutputCol("transformedVector");

        // Batch transform the vectors to create new column:
        transformer.transform(dataFrame).show();


        ss.stop();

    }


}
