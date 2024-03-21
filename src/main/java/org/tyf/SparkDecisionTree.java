package org.tyf;


import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.classification.DecisionTreeClassificationModel;
import org.apache.spark.ml.classification.DecisionTreeClassifier;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.ml.feature.IndexToString;
import org.apache.spark.ml.feature.StringIndexer;
import org.apache.spark.ml.feature.StringIndexerModel;
import org.apache.spark.ml.feature.VectorAssembler;
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
 *   @desc : spark 决策树
 *   @auth : tyf
 *   @date : 2022-02-22  15:10:34
 */
public class SparkDecisionTree {


    // 决策树是一种常见的机器学习算法。在一组包含多个特征以及特征取值样本中，不同的特征对结果的影响不同，
    // 通过自顶向下的贪心法则，寻找一种特征顺序，将对结果影响大的特征放前面，对结果影响小的放后面 得到决策树。

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



        SparkConf conf = new SparkConf().setAppName("SparkDecisionTree").setMaster("local[*]");
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

        // 创建决策树分类器
        DecisionTreeClassifier dt = new DecisionTreeClassifier()
                .setLabelCol("indexedLabel")
                .setFeaturesCol("featuresLabel");

        // 构建Pipeline
        Pipeline pipeline = new Pipeline().setStages(new PipelineStage[]{
                labelIndexer,
                assembler,
                dt,
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

        // 获取训练好的决策树模型
        DecisionTreeClassificationModel treeModel = (DecisionTreeClassificationModel) (model.stages()[2]);
        System.out.println("Learned classification tree model:\n" + treeModel.toDebugString());

        // 打印不同特征 f1 f2 f3 f4 对类别 label 的重要性
        System.out.println("featureImportances:\n"+treeModel.featureImportances());

        // 关闭Spark上下文
        jsc.stop();


    }


}
