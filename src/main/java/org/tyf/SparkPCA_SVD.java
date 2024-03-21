package org.tyf;


import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.feature.PCA;
import org.apache.spark.ml.feature.PCAModel;
import org.apache.spark.ml.linalg.DenseVector;
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
import java.util.Random;

/**
 *   @desc : spark 主成分分析
 *   @auth : tyf
 *   @date : 2022-02-22  15:10:34
*/
public class SparkPCA_SVD {

    // PCA的目标：在高维数据中找到最大方差的方向，并将数据映射到一个维度不大于原始数据的新的子空间上。

    // 传入jar
    // docker cp /root/tyf/algorithm-v1-jar-with-dependencies.jar tyf-spark-1:/usr/algorithm-v1-jar-with-dependencies.jar

    // 进入主节点容器提交任务
    // docker exec -it tyf-spark-1 /bin/bash
    // cd /opt/bitnami/spark/bin
    // spark-submit --master spark://172.16.5.248:7077 --class org.tyf.SparkPCA_SVD /usr/algorithm-v1-jar-with-dependencies.jar


    public static void main(String[] args) {


        SparkConf conf = new SparkConf().setAppName("SparkPCA_SVD").setMaster("local[*]");
        JavaSparkContext jsc = new JavaSparkContext(conf);
        SparkSession ss = SparkSession.builder().sparkContext(jsc.sc()).getOrCreate();

        // 直接模拟数据
        Random r = new Random();
        List<Row> data = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            double[] line = new double[5];
            for (int j = 0; j < line.length; j++) {
                line[j] = r.nextGaussian();
            }
            data.add(RowFactory.create(Vectors.dense(line)));
        }

        StructType schema = new StructType(new StructField[]{
                new StructField("features", new VectorUDT(), false, Metadata.empty()),
        });

        // 创建DataFrame
        Dataset<Row> df = ss.createDataFrame(data,schema);


        // 主成分分析
        PCA pca = new PCA()
                .setInputCol("features")
                .setOutputCol("pcaFeatures")
                .setK(3)
                ; // 主成分数量

        // 输入高维向量fit得到模型
        PCAModel pcaModel = pca.fit(df);

        // 计算主成分,得到低纬向量
        Dataset<Row> transformed = pcaModel.transform(df).select("pcaFeatures");

        // 将低纬向量显示
        boolean truncate = true;
        transformed.show(truncate);

        // 也可以用下面的方式进行遍历 1000 * 3
//        for (int i = 0; i < transformed.count(); i++) {
//            System.out.println("Row:"+((Row[])transformed.collect())[i]);
//        }

        // 转换矩阵 5 * 3
        // (n * 5) * (5 * 3) *  = n * 3
        Matrix pc = pcaModel.pc();
        System.out.println("Principal components: \n" + pc.toString());

        // 方差贡献
        DenseVector explainedVariance = pcaModel.explainedVariance();
        System.out.println("Explained variance: \n" + explainedVariance.toString());

        // 退出
        jsc.stop();
    }


}
