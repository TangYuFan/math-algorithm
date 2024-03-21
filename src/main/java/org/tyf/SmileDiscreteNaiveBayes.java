package org.tyf;

import smile.classification.DiscreteNaiveBayes;

import javax.sound.sampled.Line;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


/**
 *   @desc : Smile DiscreteNaiveBayes 朴素贝叶斯文档分类器
 *   @auth : tyf
 *   @date : 2022-03-08  16:38:14
*/
public class SmileDiscreteNaiveBayes {

    // 朴素贝叶斯文档分类器


    // 样本
    public static class LineData{
        int[] x;
        int y;
        public LineData(int[] x,int y) {
            this.x = x;
            this.y = y;
        }
        // 随机生成一个样本
        public static LineData one(){
            Random r = new Random();
            // 文档索引
            int f1 = r.nextInt(100);
            int f2 = r.nextInt(100);
            int f3 = r.nextInt(100);
            int f4 = r.nextInt(100);
            int f5 = r.nextInt(100);
            // 分类
            int label = r.nextInt(10);
            // 返回一行数据
            return new LineData(new int[]{f1,f2,f3,f4,f5},label);
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

        int k = 10;// 类别数
        int p = 5;// 特征数
        DiscreteNaiveBayes.Model m = DiscreteNaiveBayes.Model.MULTINOMIAL;

        // 分类器
        DiscreteNaiveBayes nb = new DiscreteNaiveBayes(m, k, p);

        // 在线学习
        for (int i = 0; i < 100; i++) {
            // 随机生成n个样本
            List<LineData> datas = LineData.getN(100);
            // x
            int[][] xs = new int[datas.size()][];
            // y
            int[] ys = new int[datas.size()];
            for (int j = 0; j < datas.size(); j++) {
                xs[j] = datas.get(j).x;
                ys[j] = datas.get(j).y;
            }
            // 更新模型
            nb.update(xs,ys);
            System.out.println("模型更新成功!");
            // 执行推理
            LineData d = LineData.one();
            int[] x = d.x;
            int y = d.y;
            int yy = nb.predict(x);
            System.out.println("X："+ Arrays.toString(x)+"，Y："+y+"，YY："+yy);
        }


    }

}
