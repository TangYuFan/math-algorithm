package org.tyf;


import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.classification.GBTClassificationModel;
import org.apache.spark.ml.classification.GBTClassifier;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.ml.feature.*;
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
 *   @desc : spark 随机梯度提升树 GBT
 *   @auth : tyf
 *   @date : 2022-02-22  15:10:34
 */
public class SparkGBTClassifier {


    // 随机梯度提升树

    // 传入jar
    // docker cp /root/tyf/algorithm-v1-jar-with-dependencies.jar tyf-spark-1:/usr/algorithm-v1-jar-with-dependencies.jar

    // 进入主节点容器提交任务
    // docker exec -it tyf-spark-1 /bin/bash
    // cd /opt/bitnami/spark/bin
    // spark-submit --master spark://172.16.5.248:7077 --class org.tyf.SparkGBTClassifier /usr/algorithm-v1-jar-with-dependencies.jar


    // 生成一个随机的样本,目前 spark 的 gbt 仅仅支持2分类
    public static Random r = new Random();
    public static Row randomRow(){
        double f1 = r.nextDouble()*100;
        double f2 = r.nextDouble()*100;
        double f3 = r.nextDouble()*100;
        double f4 = r.nextDouble()*100;
        // 以特征的加权求和计算总分,不同特征作出的贡献不同 最高分为 2000 最低分为 -500
        String label = "";
        double score = f1*10 + f2*5 + f3*5 - f4*5;
        // 目前仅仅支持2分类,超过2个列表在 string => index 时会抛出异常严格索引超过2
        if(score>=1500){
            label = "A";
        }else{
            label = "B";
        }
        // 返回样本
        return RowFactory.create(f1,f2,f3,f4,label);
    }


    public static void main(String[] args) {


        SparkConf conf = new SparkConf().setAppName("SparkGBTClassifier").setMaster("local[*]");
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
                        new StructField("label", DataTypes.StringType, false, Metadata.empty())
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

//        VectorIndexerModel featureIndexer = new VectorIndexer()
//                .setInputCol("featuresLabel")
//                .setOutputCol("featuresLabel")
//                .setMaxCategories(4)
//                .fit(df);

        // 标签 映射转换器(字符串转为数字) 转换后得到新的列名称 indexedLabel
        StringIndexerModel labelIndexer = new StringIndexer()
                .setInputCol("label")
                .setOutputCol("indexedLabel")
                .fit(df);

        // 预测列名称
        IndexToString labelConverter = new IndexToString()
                .setInputCol("prediction")
                .setOutputCol("predictedLabel")
                .setLabels(labelIndexer.labels());

        // 创建 GBTClassifier 模型
        GBTClassifier gbt = new GBTClassifier()
                .setLabelCol("indexedLabel")
                .setFeaturesCol("featuresLabel")
                .setMaxIter(10);

        // 构建Pipeline
        Pipeline pipeline = new Pipeline().setStages(new PipelineStage[]{
                labelIndexer,
                assembler,
                gbt,
                labelConverter
        });

        // 训练
        PipelineModel model = pipeline.fit(trainSet);

        // 预测,打印特征、标签值、预测值 30 条数据
        Dataset<Row> predictions = model.transform(testSet);
        System.out.println("predictions data:");
        predictions.select("f1","f2","f3","f4","label","predictedLabel").show(30);

        // 模型评估
        MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
                .setLabelCol("indexedLabel")
                .setPredictionCol("prediction")
                .setMetricName("accuracy");

        double accuracy = evaluator.evaluate(predictions);
        System.out.println("Test Correct = " + accuracy);
        System.out.println("Test Error = " + (1.0 - accuracy));


        // 获取训练好的 GBTClassificationModel
        GBTClassificationModel gbtModel = (GBTClassificationModel) (model.stages()[2]);
        System.out.println("Learned classification GBT model:\n" + gbtModel.toDebugString());

        // 打印不同特征 f1 f2 f3 f4 对类别 label 的重要性
        System.out.println("featureImportances:\n"+gbtModel.featureImportances());

        // 关闭 Spark 上下文
        jsc.stop();


    }

}
