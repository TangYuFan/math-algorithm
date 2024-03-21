package org.tyf;


import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.clustering.GaussianMixture;
import org.apache.spark.ml.clustering.GaussianMixtureModel;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.ml.linalg.VectorUDT;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.util.Arrays;
import java.util.Random;

/**
 *   @desc : spark GMM 高斯混合聚类
 *   @auth : tyf
 *   @date : 2022-02-22  15:10:34
 */
public class SparkGaussianMixture{


    // GMM 高斯混合聚类



    public static Random r = new Random();
    public static Row randomRow(){
        double f1 = r.nextGaussian();
        double f2 = r.nextGaussian();
        double f3 = r.nextGaussian();
        double f4 = r.nextGaussian();
        double f5 = r.nextGaussian();
        // 返回样本
        return RowFactory.create(f1,f2,f3,f4,f5);
    }

    // 传入jar
    // docker cp /root/tyf/algorithm-v1-jar-with-dependencies.jar tyf-spark-1:/usr/algorithm-v1-jar-with-dependencies.jar

    // 进入主节点容器提交任务
    // docker exec -it tyf-spark-1 /bin/bash
    // cd /opt/bitnami/spark/bin
    // spark-submit --master spark://172.16.5.248:7077 --class org.tyf.SparkGaussianMixture /usr/algorithm-v1-jar-with-dependencies.jar



    public static void main(String[] args) {



        SparkConf conf = new SparkConf().setAppName("SparkGaussianMixture").setMaster("local[*]");
        JavaSparkContext jsc = new JavaSparkContext(conf);
        SparkSession ss = SparkSession.builder().sparkContext(jsc.sc()).getOrCreate();


        // 随机生成样本数据
        // f1 f2 f3 f4
        Row[] data = new Row[1000];
        for (int i = 0; i < data.length; i++) {
            Row row = randomRow();
            System.out.println("Random Row: "+row);
            data[i] = row;
        }

        StructType schema = new StructType(
                new StructField[]{
                        new StructField("f1", DataTypes.DoubleType, false, Metadata.empty()),
                        new StructField("f2", DataTypes.DoubleType, false, Metadata.empty()),
                        new StructField("f3", DataTypes.DoubleType, false, Metadata.empty()),
                        new StructField("f4", DataTypes.DoubleType, false, Metadata.empty()),
                        new StructField("f5", DataTypes.DoubleType, false, Metadata.empty())
                });

        // 创建DataFrame
        Dataset<Row> df = ss.createDataFrame(Arrays.asList(data),schema);


        // 将训练集 f1 f2 f3 f4 f5 合并成一个列到 featuresLabel 中
        VectorAssembler assembler = new VectorAssembler()
                .setInputCols(new String[]{"f1","f2","f3","f4","f5"})
                .setOutputCol("featuresLabel");

        // GMM
        GaussianMixture gmm = new GaussianMixture()
                .setK(3)  // 设置聚类的簇数
                .setFeaturesCol("featuresLabel")
                .setMaxIter(100);

        // 构建Pipeline
        Pipeline pipeline = new Pipeline().setStages(new PipelineStage[]{
                assembler,
                gmm
        });

        // 训练
        PipelineModel model = pipeline.fit(df);

        // 在数据集上进行预测、打印聚类编号
        Dataset<Row> predictions = model.transform(df);
        predictions.show(30);

        // 获取训练好的GaussianMixture模型
        GaussianMixtureModel gmmModel = (GaussianMixtureModel) model.stages()[1];

        // 打印每个簇的权重、均值和协方差矩阵
        for (int i = 0; i < gmmModel.getK(); i++) {
            System.out.println("Cluster " + i + " weight: " + gmmModel.weights()[i]);
            System.out.println("Cluster " + i + " mean: " + gmmModel.gaussians()[i].mean());
            System.out.println("Cluster " + i + " covariance matrix: \n" + gmmModel.gaussians()[i].cov());
        }

        // 停止Spark上下文
        jsc.stop();
    }




}
