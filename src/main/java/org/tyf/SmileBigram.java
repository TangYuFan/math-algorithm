package org.tyf;

import smile.nlp.Corpus;
import smile.nlp.SimpleCorpus;
import smile.nlp.SimpleText;
import smile.nlp.collocation.Bigram;


/**
 *   @desc : Smile Bigram 二元组
 *   @auth : tyf
 *   @date : 2022-03-08  16:38:14
*/
public class SmileBigram {

    // 二元组

    public static void main(String[] args) {

        // 示例语料库
        SimpleCorpus corpus = new SimpleCorpus();

        // 添加文档到语料库中
        corpus.add(new SimpleText("document1", "This is the first document.", "", new String[]{}));
        corpus.add(new SimpleText("document2", "This is the second document.", "", new String[]{}));


        // 找到前 k 个频率大于 minFrequency 的二元组
        int k = 5;
        int minFrequency = 1;
        Bigram[] topBigrams = Bigram.of(corpus, k, minFrequency);

        // 输出结果
        System.out.println("Top " + k + " bigrams with frequency > " + minFrequency + ":");
        for (Bigram bigram : topBigrams) {
            System.out.println(bigram);
        }

        // 找到 p 值小于指定阈值的二元组
        double p = 0.05;
        Bigram[] significantBigrams = Bigram.of(corpus, p, minFrequency);

        // 输出结果
        System.out.println("\nBigrams with p-value < " + p + ":");
        for (Bigram bigram : significantBigrams) {
            System.out.println(bigram);
        }

    }

}
