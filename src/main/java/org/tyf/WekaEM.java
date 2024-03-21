package org.tyf;

import weka.clusterers.ClusterEvaluation;
import weka.clusterers.EM;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.RemovePercentage;
import weka.gui.explorer.ClustererAssignmentsPlotInstances;
import weka.gui.visualize.VisualizePanel;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 *   @desc : weka EM期望最大化聚类。
 *   @auth : tyf
 *   @date : 2022-03-04  17:17:22
*/
public class WekaEM {

    // EM 期望最大化算法，用于高斯混合模型的聚类。


    // 生成随机训练样本
    public static void generateCsv(String csv){
        Random r = new Random();
        if(new File(csv).exists()){
            new File(csv).delete();
        }
        // 写入到本地csv
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csv, true))) {
            writer.write("f1" +","+ "f2" + ","+"f3" + ","+"f4" + ","+"f5");
            writer.newLine();
            for (int i = 0; i < 10000; i++) {
                double f1 = r.nextDouble()*100;
                double f2 = r.nextDouble()*100;
                double f3 = r.nextDouble()*100;
                double f4 = r.nextDouble()*100;
                double f5 = r.nextDouble()*100;
                String line = f1 +","+ f2 + ","+f3 + ","+f4 + ","+f5;
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void visualizeClusters(Instances data, EM clusterer) throws Exception {
        ClusterEvaluation eval = new ClusterEvaluation();
        eval.setClusterer(clusterer);
        eval.evaluateClusterer(data);

        ClustererAssignmentsPlotInstances plotInstances = new ClustererAssignmentsPlotInstances();
        plotInstances.setClusterer(clusterer);
        plotInstances.setInstances(data);
        plotInstances.setClusterEvaluation(eval);
        plotInstances.setUp();

        VisualizePanel vp = new VisualizePanel();
        vp.setName("Cluster Visualization");
        vp.addPlot(plotInstances.getPlotData(clusterer.getClass().getName()));

        JFrame jf = new JFrame("Weka Clusterer Visualize: " + vp.getName());
        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jf.setSize(800, 600);
        jf.getContentPane().setLayout(new BorderLayout());
        jf.getContentPane().add(vp, BorderLayout.CENTER);
        jf.setVisible(true);
    }

    public static void main(String[] args) throws Exception{

        // 样本路径
        String csv = "C:\\Users\\tyf\\Desktop\\data.csv";
        // 模型保存和加载路径
        String module = "C:\\Users\\tyf\\Desktop\\em.model";

        // 生成随机训练样本
        generateCsv(csv);

        // 读取数据
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File(csv));

        // 数据集 Instances
        Instances data = loader.getDataSet();
//        System.out.println(data);
        System.out.println("行:"+data.size()+",列:"+data.numAttributes());


        // 划分训练集
        RemovePercentage data_08 = new RemovePercentage();
        data_08.setInputFormat(data);
        data_08.setPercentage(80); // 百分比
        data_08.setInvertSelection(true);

        // 划分验证集
        RemovePercentage data_02 = new RemovePercentage();
        data_02.setInputFormat(data);
        data_02.setPercentage(20); // 百分比
        data_02.setInvertSelection(true);

        Instances trainingData = Filter.useFilter(data, data_08);
        Instances validationData  = Filter.useFilter(data, data_02);

        System.out.println("训练集: "+trainingData.size()+" x "+trainingData.numAttributes());
        System.out.println("验证集: "+validationData.size()+" x "+validationData.numAttributes());


        EM clusterer = new EM();
        clusterer.setNumClusters(3);  // 设置簇的数量，根据需要进行调整

        // 构建聚类模型
        clusterer.buildClusterer(trainingData);


        // 进行预测，返回每个实例所属的簇
        for (int i = 0; i < validationData.numInstances(); i++) {
            int cluster = clusterer.clusterInstance(validationData.instance(i));
            System.out.println("Instance " + (i + 1) + " belongs to cluster " + (cluster + 1));
        }


        // 详细模型信息
        System.out.println(clusterer);

        // 可视化
        visualizeClusters(trainingData,clusterer);


    }

}

