package org.tyf;


import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.classification.RandomForestClassificationModel;
import org.apache.spark.ml.classification.RandomForestClassifier;
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
import org.apache.spark.ml.PipelineModel;
import java.util.Arrays;
import java.util.Random;

/**
 *   @desc : spark 随机森林
 *   @auth : tyf
 *   @date : 2022-02-23  15:48:32
*/
public class SparkRandomForest {


    // 随机森林算法,将多个决策树结合在一起每次数据集是随机有放回的选出，随机选出部分特征作为输入是以多个决策树为估计器的Bagging集成学习算法。

    // 传入jar
    // docker cp /root/tyf/algorithm-v1-jar-with-dependencies.jar tyf-spark-1:/usr/algorithm-v1-jar-with-dependencies.jar

    // 进入主节点容器提交任务
    // docker exec -it tyf-spark-1 /bin/bash
    // cd /opt/bitnami/spark/bin
    // spark-submit --master spark://172.16.5.248:7077 --class org.tyf.SparkRandomForest /usr/algorithm-v1-jar-with-dependencies.jar

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

        SparkConf conf = new SparkConf().setAppName("SparkRandomForest").setMaster("local[*]");
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

        // 随机森林分类器
        RandomForestClassifier rf = new RandomForestClassifier()
                .setSeed(r.nextLong())  // 随机数种子
                .setLabelCol("indexedLabel") // 标签列
                .setFeaturesCol("featuresLabel") // f1 f2 f3 f4
                .setNumTrees(10); // 决策树个数

        // 构建Pipeline
        Pipeline pipeline = new Pipeline().setStages(new PipelineStage[]{
                labelIndexer,
                assembler,
                rf,
                labelConverter
        });

        // 训练
        PipelineModel model = pipeline.fit(trainSet);

        // 预测,打印特征、标签值、预测值 30 条数据
        Dataset<Row> predictions = model.transform(testSet);
        System.out.println("predictions data:");
        predictions.select("f1","f2","f3","f4","label","predictedLabel").show(30);

        // 预测还是较为准确
        // +------------------+------------------+------------------+------------------+-----+--------------+
        // |                f1|                f2|                f3|                f4|label|predictedLabel|
        // +------------------+------------------+------------------+------------------+-----+--------------+
        // | 1.245100086715889| 47.69323627087848|  81.2898041675854| 83.61971411721518|    D|             D|
        // | 1.669139856196633|30.866987987973637|  68.1549353945264| 82.05152620044515|    D|             D|
        // |  4.03546217439631| 45.74147669401767|3.6514047876133215| 9.725239198735135|    D|             D|
        // | 5.431635017652248| 59.60818217708551| 55.13821120966777| 61.15045647751738|    D|             D|
        // | 9.075459040792833| 78.03544548377668|62.842113769028416|2.4752510168272734|    C|             C|
        // | 9.199594978345827| 86.55758101027999| 66.77086035941981| 32.73908673073288|    C|             C|
        // |10.742132982031706| 4.974265656720767| 32.76370159190263| 89.09399294732768|    E|             D|
        // | 16.65224443751222| 59.15083710390011|25.531269778274847|21.435160319529345|    D|             D|
        // |17.417918354649398|  72.4886577389668|30.391659853230735|29.889834732958633|    C|             C|
        // | 21.07920737672122| 50.60878018500078| 85.15839765086415|15.359119804109278|    C|             C|
        // ...省略...
        // +------------------+------------------+------------------+------------------+-----+--------------+

        // 模型评估
        MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
                .setLabelCol("indexedLabel")
                .setPredictionCol("prediction")
                .setMetricName("accuracy");

        double accuracy = evaluator.evaluate(predictions);
        System.out.println("Test Correct = " + accuracy);
        System.out.println("Test Error = " + (1.0 - accuracy));

        // 获取训练好的随机森林模型
        RandomForestClassificationModel rfModel = (RandomForestClassificationModel) (model.stages()[2]);
        System.out.println("Learned classification forest model:\n" + rfModel.toDebugString());

        // 打印不同特征 f1 f2 f3 f4 对类别 label 的重要性
        System.out.println("featureImportances:\n"+rfModel.featureImportances());

        // (4,[0,1,2,3],[0.4918430864419725,0.1708333619454136,0.17161149590287966,0.16571205570973427])
        // 0 => f1 => 0.4918430864419725
        // 1 => f2 => 0.1708333619454136
        // 2 => f3 => 0.17161149590287966
        // 3 => f4 => 0.16571205570973427

        // 关闭Spark上下文
        jsc.stop();
    }


}
