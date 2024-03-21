package org.tyf;

import smile.classification.NaiveBayes;
import smile.stat.distribution.Distribution;
import smile.stat.distribution.LogNormalDistribution;
import smile.stat.distribution.MultivariateGaussianDistribution;
import smile.util.IntSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


/**
 *   @desc : Smile NaiveBayes 通用朴素贝叶斯分类器
 *   @auth : tyf
 *   @date : 2022-03-08  16:38:14
*/
public class SmileNaiveBayes {

    // 通用朴素贝叶斯分类器

    // 样本
    public static class LineData{
        double[] x;
        int y;
        public LineData(double[] x,int y) {
            this.x = x;
            this.y = y;
        }
        // 随机生成一个样本
        public static LineData one(){
            Random r = new Random();
            // 文档索引
            double f1 = r.nextDouble()*100;
            double f2 = r.nextDouble()*100;
            double f3 = r.nextDouble()*100;
            double f4 = r.nextDouble()*100;
            double f5 = r.nextDouble()*100;
            // 分类
            int label = r.nextInt(10);
            // 返回一行数据
            return new LineData(new double[]{f1,f2,f3,f4,f5},label);
        }
        // 随机生成n个样本
        public static List<LineData> getN(int n){
            List<LineData> rt = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                rt.add(one());
            }
            return rt;
        }
    }

    public static void main(String[] args) {

        // 类别个数
        IntSet labels = IntSet.of(10);
        // 每个类别的先验概率
        double[] priori = new double[]{0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1};
        // 每个类中每个变量的条件分布 10个类别、5个属性
        Distribution[][] condprob = new Distribution[10][5];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 5; j++) {
                // 均值为i+j，标准差为1.0的多元高斯分布
                condprob[i][j] = new LogNormalDistribution(1,1);
            }
        }

        // 需要实现子类
        NaiveBayes nb = new NaiveBayes(priori,condprob,labels);

        // 在线学习
        for (int i = 0; i < 100; i++) {
            // 随机生成n个样本
            List<LineData> datas = LineData.getN(100);
            // x
            double[][] xs = new double[datas.size()][];
            // y
            int[] ys = new int[datas.size()];
            for (int j = 0; j < datas.size(); j++) {
                xs[j] = datas.get(j).x;
                ys[j] = datas.get(j).y;
            }
            // 更新模型
            nb.update(xs,ys);
            // 执行推理
            LineData d = LineData.one();
            double[] x = d.x;
            int y = d.y;
            int yy = nb.predict(x);
            System.out.println("X："+ Arrays.toString(x)+"，Y："+y+"，YY："+yy);
        }

    }

}
