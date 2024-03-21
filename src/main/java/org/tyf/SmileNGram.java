package org.tyf;

import smile.nlp.collocation.NGram;


/**
 *   @desc : Smile NGram N元组
 *   @auth : tyf
 *   @date : 2022-03-08  16:38:14
*/
public class SmileNGram {

    // N元组

    public static void main(String[] args) {

        // 创建示例句子集合
        String[] sentence1 = {"This", "is", "a", "good", "book"};
        String[] sentence2 = {"Good", "morning", "everyone"};
        String[] sentence3 = {"I", "like", "to", "read", "books", "every", "morning"};
        String[] sentence4 = {"This", "book", "is", "very", "interesting"};
        String[] sentence5 = {"I", "love", "reading"};

        // 构建句子集合
        java.util.Collection<String[]> sentences = new java.util.ArrayList<>();
        sentences.add(sentence1);
        sentences.add(sentence2);
        sentences.add(sentence3);
        sentences.add(sentence4);
        sentences.add(sentence5);

        // 提取2-gram短语
        NGram[][] ngrams = NGram.of(sentences, 2, 2);

        // 打印结果
        for (int i = 0; i < ngrams.length; i++) {
            System.out.printf("%d-gram phrases:\n", i + 1);
            for (NGram ngram : ngrams[i]) {
                System.out.println(ngram);
            }
            System.out.println();
        }


    }

}
