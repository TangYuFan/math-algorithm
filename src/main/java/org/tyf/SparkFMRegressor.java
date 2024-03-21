package org.tyf;


import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.evaluation.RegressionEvaluator;
import org.apache.spark.ml.feature.MinMaxScaler;
import org.apache.spark.ml.feature.MinMaxScalerModel;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.ml.regression.FMRegressionModel;
import org.apache.spark.ml.regression.FMRegressor;
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
 *   @desc : spark FM回归
 *   @auth : tyf
 *   @date : 2022-02-22  15:10:34
 */
public class SparkFMRegressor {



    // FM回归


    // 传入jar
    // docker cp /root/tyf/algorithm-v1-jar-with-dependencies.jar tyf-spark-1:/usr/algorithm-v1-jar-with-dependencies.jar

    // 进入主节点容器提交任务
    // docker exec -it tyf-spark-1 /bin/bash
    // cd /opt/bitnami/spark/bin
    // spark-submit --master spark://172.16.5.248:7077 --class org.tyf.SparkFMRegressor /usr/algorithm-v1-jar-with-dependencies.jar



    public static Random r = new Random();
    public static Row randomRow(){
        double f1 = r.nextDouble()*100;
        double f2 = r.nextDouble()*100;
        double f3 = r.nextDouble()*100;
        double f4 = r.nextDouble()*100;
        double label = f1*10 + f2*5 + f3*5 - f4*5 + r.nextInt(40);
        // 返回样本
        return RowFactory.create(f1,f2,f3,f4,label);
    }



    public static void main(String[] args) {


        SparkConf conf = new SparkConf().setAppName("SparkFMRegressor").setMaster("local[*]");
        JavaSparkContext jsc = new JavaSparkContext(conf);
        SparkSession ss = SparkSession.builder().sparkContext(jsc.sc()).getOrCreate();

        // 随机生成样本数据
        // f1 f2 f3 f4 label
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
                        new StructField("label", DataTypes.DoubleType, false, Metadata.empty())
                });

        Dataset<Row> df = ss.createDataFrame(Arrays.asList(data), schema);

        // 训练验证集划分
        Dataset<Row>[] splits = df.randomSplit(new double[]{0.8, 0.2});
        Dataset<Row> trainSet = splits[0];
        Dataset<Row> testSet = splits[1];


        // 将训练集 f1 f2 f3 f4 合并成一个列到 featuresLabel 中
        VectorAssembler assembler = new VectorAssembler()
                .setInputCols(new String[]{"f1","f2","f3","f4"})
                .setOutputCol("featuresLabel");

        // Scale features.
        MinMaxScaler featureScaler = new MinMaxScaler()
                .setInputCol("featuresLabel")
                .setOutputCol("scaledFeatures");

        FMRegressor fm = new FMRegressor()
                .setLabelCol("label")
                .setFeaturesCol("scaledFeatures")
                .setStepSize(0.001);

        // 构建Pipeline
        Pipeline pipeline = new Pipeline().setStages(new PipelineStage[]{
                assembler,
                featureScaler, // 特征转换
                fm
        });

        // 训练
        PipelineModel model = pipeline.fit(trainSet);

        // 预测,打印特征、标签值、预测值 30 条数据
        Dataset<Row> predictions = model.transform(testSet);
        System.out.println("predictions data:");
        predictions.select("f1","f2","f3","f4","label","prediction").show(30);

        // 模型评估
        RegressionEvaluator evaluator = new RegressionEvaluator()
                .setLabelCol("label")
                .setPredictionCol("prediction")
                .setMetricName("rmse");

        double rmse = evaluator.evaluate(predictions);
        System.out.println("Root Mean Squared Error (RMSE) on test data = " + rmse);

        FMRegressionModel fmModel = (FMRegressionModel)(model.stages()[2]);
        System.out.println("Factors: " + fmModel.factors());
        System.out.println("Linear: " + fmModel.linear());
        System.out.println("Intercept: " + fmModel.intercept());

        // 关闭 Spark 上下文
        jsc.stop();
    }

}
