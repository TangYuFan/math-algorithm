package org.tyf;

import smile.association.ARM;
import smile.association.AssociationRule;
import smile.association.FPTree;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.stream.Stream;


/**
 *   @desc : Smile ARM 关联挖掘
 *   @auth : tyf
 *   @date : 2022-03-08  16:38:14
*/
public class SmileARM {

    // 关联挖掘


    // 生成一些示例数据
    private static int[][] generateData(int numTransactions, int numItems) {

        Random random = new Random();
        int[][] data = new int[numTransactions][];

        for (int i = 0; i < numTransactions; i++) {
            int transactionSize = random.nextInt(numItems / 2) + numItems / 2; // Vary transaction size
            int[] transaction = new int[transactionSize];

            for (int j = 0; j < transactionSize; j++) {
                transaction[j] = random.nextInt(numItems);
            }

            // 去重
            data[i] = Arrays.stream(transaction).distinct().toArray();
        }

        return data;
    }


    public static void main(String[] args) {

        // 样本集合
        // 这里构造一个稀疏矩阵、单个项目需要去重
        // 样本：[3, 7, 6]
        // 样本：[3, 1, 6, 9, 5, 8]
        // 样本：[1, 4, 2, 3]
        // 样本：[6, 4, 3, 2, 1, 8]
        // 样本：[4, 8, 6, 3, 0, 9, 7]
        // 样本：[9, 4, 7, 6, 8]
        // 样本：[1, 0, 8, 2, 3, 7, 4]
        // 样本：[8, 3, 6, 5, 2]
        int[][] itemsets = generateData(1000,10);

        // 最小支持度 double 是百分比看源码可以看到是乘了100、int 就是支持度个数
//        double minSupport = 0.3;
        int minSupport = 100;

        // 频繁挖掘
        FPTree tree = FPTree.of(minSupport,itemsets);

        // 规则置信度
        double confidence = 0.3;

        Stream<AssociationRule> rules =  ARM.apply(confidence,tree);

        // 按照置信度排序
        rules.sorted((o1, o2) -> Double.compare(o2.confidence,o1.confidence)).forEach(rule ->{
            int[] antecedent = rule.antecedent;
            double confidenceS = rule.confidence;
            double support = rule.support;
            double lift = rule.lift;
            int[] consequent = rule.consequent;
            double leverage = rule.leverage;
            System.out.println("前提："+Arrays.toString(antecedent)+"，结果："+Arrays.toString(consequent)+"，置信度："+confidenceS+"，支持度："+support+"，提升度："+lift+"，杠杆率："+leverage);
        });

    }

}
