package org.tyf;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.classification.DecisionTreeClassificationModel;
import org.apache.spark.ml.classification.LinearSVC;
import org.apache.spark.ml.classification.LinearSVCModel;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.ml.feature.IndexToString;
import org.apache.spark.ml.feature.StringIndexer;
import org.apache.spark.ml.feature.StringIndexerModel;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.ml.linalg.Vectors;
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
 *   @desc : spark LinearSVC 线性SVM进行二分类
 *   @auth : tyf
 *   @date : 2022-02-22  15:10:34
 */
public class SparkLinearSVC {



    // 支持向量机，目前只支持二分类

    // 传入jar
    // docker cp /root/tyf/algorithm-v1-jar-with-dependencies.jar tyf-spark-1:/usr/algorithm-v1-jar-with-dependencies.jar

    // 进入主节点容器提交任务
    // docker exec -it tyf-spark-1 /bin/bash
    // cd /opt/bitnami/spark/bin
    // spark-submit --master spark://172.16.5.248:7077 --class org.tyf.SparkLinearSVC /usr/algorithm-v1-jar-with-dependencies.jar


    public static Random r = new Random();

    // 生成一个随机的样本
    public static Row randomRow(){
        double f1 = r.nextDouble()*100;
        double f2 = r.nextDouble()*100;
        double f3 = r.nextDouble()*100;
        double f4 = r.nextDouble()*100;
        // 以特征的加权求和计算总分,不同特征作出的贡献不同 最高分为 2000 最低分为 -500
        String label = "";
        double score = f1*10 + f2*5 + f3*5 - f4*5 + r.nextInt(300);
        if(score>=1000){
            label = "A";
        }else{
            label = "B";
        }
        // 返回样本
        return RowFactory.create(f1,f2,f3,f4,label);
    }

    public static void main(String[] args) {


        SparkConf conf = new SparkConf().setAppName("SparkLinearSVC").setMaster("local[*]");
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

        // 将训练集 f1 f2 f3 f4 合并成一个列到 features 中
        VectorAssembler assembler = new VectorAssembler()
                .setInputCols(new String[]{"f1","f2","f3","f4"})
                .setOutputCol("featuresLabel");

        // 将标签列（label）转换为数值型索引
        StringIndexerModel labelIndexer = new StringIndexer()
                .setInputCol("label")
                .setOutputCol("indexedLabel")
                .fit(df);

        // 预测列名称
        IndexToString labelConverter = new IndexToString()
                .setInputCol("prediction")
                .setOutputCol("predictedLabel")
                .setLabels(labelIndexer.labels());

        // 创建线性支持向量机模型
        LinearSVC lsvc = new LinearSVC()
                .setFeaturesCol("featuresLabel")
                .setLabelCol("indexedLabel")
                .setMaxIter(10)
                .setRegParam(0.1);


        // 构建Pipeline
        Pipeline pipeline = new Pipeline().setStages(new PipelineStage[]{
                labelIndexer,
                assembler,
                lsvc,
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


        // 获取训练好的支持向量机模型、用模型进行单个推理
//        model.save("path");

        for (int i = 0; i < 20; i++) {
            // 随机生成一个样本
            Dataset<Row> v = ss.createDataFrame(Arrays.asList(randomRow()), schema);
            Dataset<Row> p = model.transform(v);
            // 打印预测结果
            p.select("f1","f2","f3","f4","label","predictedLabel").show(1);
        }


        // 关闭Spark上下文
        jsc.stop();

    }


}
