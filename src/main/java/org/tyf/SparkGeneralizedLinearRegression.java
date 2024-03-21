package org.tyf;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.classification.RandomForestClassificationModel;
import org.apache.spark.ml.evaluation.RegressionEvaluator;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.ml.regression.GeneralizedLinearRegression;
import org.apache.spark.ml.regression.GeneralizedLinearRegressionModel;
import org.apache.spark.ml.regression.GeneralizedLinearRegressionTrainingSummary;
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
 *   @desc : spark 广义线性回归
 *   @auth : tyf
 *   @date : 2022-02-22  15:10:34
 */

public class SparkGeneralizedLinearRegression {


    // 广义线性回归

    // 传入jar
    // docker cp /root/tyf/algorithm-v1-jar-with-dependencies.jar tyf-spark-1:/usr/algorithm-v1-jar-with-dependencies.jar

    // 进入主节点容器提交任务
    // docker exec -it tyf-spark-1 /bin/bash
    // cd /opt/bitnami/spark/bin
    // spark-submit --master spark://172.16.5.248:7077 --class org.tyf.SparkGeneralizedLinearRegression /usr/algorithm-v1-jar-with-dependencies.jar



    // 生成一个随机的样本,目前 spark 的 gbt
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

        SparkConf conf = new SparkConf().setAppName("SparkGeneralizedLinearRegression").setMaster("local[*]");
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

        GeneralizedLinearRegression glr = new GeneralizedLinearRegression()
                .setLabelCol("label")
                .setFeaturesCol("featuresLabel")
                // "gaussian"：正态分布，适用于连续的数值型响应变量。
                // "binomial"：二项分布，适用于二分类问题。
                // "poisson"：泊松分布，适用于计数型的响应变量。
                .setFamily("gaussian")  // 设置GLM的概率分布族
                // 常见的链接函数包括：
                // "identity"：恒等链接，适用于高斯分布，表示线性关系。
                // "logit"：对数几率链接，适用于二项分布，用于二分类问题。
                // "log"：对数链接，适用于泊松分布。
                .setLink("identity") // 指定链接函数，它描述了响应变量的期望和预测值之间的关系
                .setMaxIter(10)
                .setRegParam(0.3);


        // 构建Pipeline
        Pipeline pipeline = new Pipeline().setStages(new PipelineStage[]{
                assembler,
                glr
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

        // 打印模型参数
        GeneralizedLinearRegressionModel rfModel = (GeneralizedLinearRegressionModel) (model.stages()[1]);
        GeneralizedLinearRegressionTrainingSummary summary = rfModel.summary();
        System.out.println("Coefficient Standard Errors: " + Arrays.toString(summary.coefficientStandardErrors()));
        System.out.println("T Values: " + Arrays.toString(summary.tValues()));
        System.out.println("P Values: " + Arrays.toString(summary.pValues()));
        System.out.println("Dispersion: " + summary.dispersion());
        System.out.println("Null Deviance: " + summary.nullDeviance());
        System.out.println("Residual Degree Of Freedom Null: " + summary.residualDegreeOfFreedomNull());
        System.out.println("Deviance: " + summary.deviance());
        System.out.println("Residual Degree Of Freedom: " + summary.residualDegreeOfFreedom());
        System.out.println("AIC: " + summary.aic());
        System.out.println("Deviance Residuals: ");
        summary.residuals().show();


        // 关闭 Spark 上下文
        jsc.stop();

    }


}
