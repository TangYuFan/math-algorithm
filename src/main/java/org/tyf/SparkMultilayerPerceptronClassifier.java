package org.tyf;


import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.classification.NaiveBayesModel;
import org.apache.spark.ml.feature.IndexToString;
import org.apache.spark.ml.feature.StringIndexer;
import org.apache.spark.ml.feature.StringIndexerModel;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.ml.classification.MultilayerPerceptronClassificationModel;
import org.apache.spark.ml.classification.MultilayerPerceptronClassifier;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.util.Arrays;
import java.util.Random;

/**
 *   @desc : spark 多层感知机
 *   @auth : tyf
 *   @date : 2022-02-22  15:10:34
 */
public class SparkMultilayerPerceptronClassifier {




    // 多层感知机
    // 不说了、深度学习的基础

    // 传入jar
    // docker cp /root/tyf/algorithm-v1-jar-with-dependencies.jar tyf-spark-1:/usr/algorithm-v1-jar-with-dependencies.jar

    // 进入主节点容器提交任务
    // docker exec -it tyf-spark-1 /bin/bash
    // cd /opt/bitnami/spark/bin
    // spark-submit --master spark://172.16.5.248:7077 --class org.tyf.SparkMultilayerPerceptronClassifier /usr/algorithm-v1-jar-with-dependencies.jar



    public static Random r = new Random();
    // 生成一个随机的样本
    public static Row randomRow(){
        double f1 = r.nextDouble()*100;
        double f2 = r.nextDouble()*100;
        double f3 = r.nextDouble()*100;
        double f4 = r.nextDouble()*100;
        // 以特征的加权求和计算总分,不同特征作出的贡献不同 最高分为 2000 最低分为 -500
        String label = "";
        double score = f1*10 + f2*5 + f3*5 - f4*5;
        if(score>=-500&&score<0){ label = "E";}
        else if(score>=0&&score<500){ label = "D";}
        else if(score>=500&&score<1000){ label = "C";}
        else if(score>=1000&&score<1500){ label = "B";}
        else if(score>=1500&&score<2000){ label = "A";}
        // 返回样本
        return RowFactory.create(f1,f2,f3,f4,label);
    }

    public static void main(String[] args) {



        SparkConf conf = new SparkConf().setAppName("SparkMultilayerPerceptronClassifier").setMaster("local[*]");
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

        MultilayerPerceptronClassifier classifier = new MultilayerPerceptronClassifier()
                .setLabelCol("indexedLabel") // 标签列
                .setFeaturesCol("featuresLabel")
                .setLayers(new int[] {4, 5, 4, 5}) // 输入 4 、中间两个全连接层为 4、3、输出类别数 5。
                .setBlockSize(128)
                .setSeed(1234L)
                .setMaxIter(100); // f1 f2 f3 f4


        // 构建Pipeline
        Pipeline pipeline = new Pipeline().setStages(new PipelineStage[]{
                labelIndexer,
                assembler,
                classifier,
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

        // 获取训练好的多层感知机
        MultilayerPerceptronClassificationModel rfModel = (MultilayerPerceptronClassificationModel) (model.stages()[2]);
        System.out.println("MultilayerPerceptronClassificationModel:\n" + rfModel);

    }







}
