package org.tyf;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkFiles;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


/**
 *   @desc : spark K均值算法
 *   @auth : tyf
 *   @date : 2022-02-22  15:10:34
 */
public class SparkKmeans {

    // 传入jar
    // docker cp /root/tyf/algorithm-v1-jar-with-dependencies.jar tyf-spark-1:/usr/algorithm-v1-jar-with-dependencies.jar

    // 进入主节点容器提交任务
    // docker exec -it tyf-spark-1 /bin/bash
    // cd /opt/bitnami/spark/bin
    // spark-submit --master spark://172.16.5.248:7077 --class org.tyf.SparkKmeans /usr/algorithm-v1-jar-with-dependencies.jar


    public static void main(String[] args) {

        // 创建Spark配置和Spark上下文,数据在hdfs时可以配置集群运行,local配置本地单节点运行,任务在spark-webui上看不到
        SparkConf conf = new SparkConf().setAppName("SparkKmeans").setMaster("local[*]");
        JavaSparkContext jsc = new JavaSparkContext(conf);

        // 加载本地数据文件、hdfs等
//        JavaRDD<String> data = jsc.textFile(data.txt);

        // 直接模拟数据
        Random r = new Random();
        List<String> l = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            // 数据行
            l.add(r.nextInt(100)+","+r.nextInt(100)+","+r.nextInt(100));
        }
        JavaRDD<String> data = jsc.parallelize(l);

        // 空格分隔值转换为向量
        JavaRDD<Vector> parsedData = data.map(
                // 遍历每一行
                (Function<String, Vector>) s -> {
                    String[] sarray = s.split(",");
                    double[] values = new double[sarray.length];
                    for (int i = 0; i < sarray.length; i++) {
                        values[i] = Double.parseDouble(sarray[i]);
                    }
                    return Vectors.dense(values);
                }
        );
        parsedData.cache();

        // 簇的数量、迭代次数
        int numClusters = 5;
        int numIterations = 20;
        KMeansModel clusters = KMeans.train(parsedData.rdd(), numClusters, numIterations);

        System.out.println("Cluster centers:");
        for (Vector center: clusters.clusterCenters()) {
            System.out.println(" " + center);
        }
        double cost = clusters.computeCost(parsedData.rdd());
        System.out.println("Cost: " + cost);

        // Evaluate clustering by computing Within Set Sum of Squared Errors
        double WSSSE = clusters.computeCost(parsedData.rdd());
        System.out.println("Within Set Sum of Squared Errors = " + WSSSE);

        // 保存模型
        String modulePath = "/usr/kmeans-model/"+System.currentTimeMillis()+"/";
        if(new File(modulePath).exists()){
            System.out.println("delete:"+new File(modulePath).delete());;
        }
        clusters.save(jsc.sc(), modulePath);
        System.out.println("save module success!!");

        // 加载模型
        KMeansModel sameModel = KMeansModel.load(jsc.sc(), modulePath);

        System.out.println("K:"+sameModel.k());
        System.out.println("numIter:"+sameModel.numIter());
        System.out.println("trainingCost:"+sameModel.trainingCost());

        // 推理
        for (int i = 1; i <= 10; i++) {
            double[] d = new double[]{r.nextInt(100),r.nextInt(100),r.nextInt(100)};
            int c = sameModel.predict(Vectors.dense(d));
            System.out.println("predict: "+ Arrays.toString(d)+" => "+c);
        }

        jsc.stop();

    }



}