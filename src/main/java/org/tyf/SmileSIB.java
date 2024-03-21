package org.tyf;

import smile.clustering.SIB;
import smile.util.SparseArray;

import java.util.Arrays;
import java.util.Random;


/**
 *   @desc : Smile SIB 顺序信息瓶颈聚类
 *   @auth : tyf
 *   @date : 2022-03-08  16:38:14
*/
public class SmileSIB {

    // 顺序信息瓶颈聚类


    // 创建稀疏矩阵
    public static SparseArray[] genData(int numTransactions,int numItems){
        SparseArray[] data = new SparseArray[numTransactions];
        Random random = new Random();
        for (int i = 0; i < numTransactions; i++) {
            int transactionSize = random.nextInt(numItems / 2) + numItems / 2; // Vary transaction size
            int[] transaction = new int[transactionSize];
            for (int j = 0; j < transactionSize; j++) {
                transaction[j] = random.nextInt(numItems);
            }
            // 去重
            transaction = Arrays.stream(transaction).distinct().toArray();
            // 创建一个稀疏数据行
            SparseArray line = new SparseArray();
            Arrays.stream(transaction).forEach(ii->{
                line.set(ii,random.nextInt(100));
            });
            data[i] = line;
        }
        return data;
    }

    public static void main(String[] args) {

        int numTransactions = 1000;
        int numItems = 10;

        SparseArray[] data = genData(numTransactions,numItems);

        // 打印每行稀疏样本
        Arrays.stream(data).forEach(n->{
            System.out.println("样本："+n);
        });

        int k = 3;
        SIB sib = SIB.fit(data,k);

        System.out.println("Centroids：");
        for (int i = 0; i < sib.centroids.length; i++) {
            System.out.println(Arrays.toString(sib.centroids[i]));
        }

        // 所有样本以及聚类结果
        System.out.println("聚类结果：");
        for (int i = 0; i < 10; i++) {
            System.out.println("样本："+data[i].toString()+"，聚类："+sib.y[i]);
        }

        // 预测
        System.out.println("聚类预测：");
        for (int i = 0; i < 10; i++) {
            System.out.println("样本："+data[i].toString()+"，聚类："+sib.predict(data[i]));
        }

    }

}
