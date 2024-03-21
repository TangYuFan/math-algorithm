package org.tyf;

import smile.nlp.stemmer.LancasterStemmer;


/**
 *   @desc : Smile LancasterStemmer 兰卡斯特词干提取器
 *   @auth : tyf
 *   @date : 2022-03-08  16:38:14
*/
public class SmileLancasterStemmer {

    // 兰卡斯特词干提取器

    public static void main(String[] args) {

        // 创建兰卡斯特词干提取器实例
        LancasterStemmer stemmer = new LancasterStemmer();

        // 待处理的单词
        String word = "running";

        // 对单词进行词干提取
        String stemmedWord = stemmer.stem(word);

        // 输出结果
        System.out.println("Original Word: " + word);
        System.out.println("Stemmed Word: " + stemmedWord);

    }

}
