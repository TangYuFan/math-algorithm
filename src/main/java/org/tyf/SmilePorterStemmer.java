package org.tyf;

import smile.nlp.stemmer.PorterStemmer;


/**
 *   @desc : Smile PorterStemmer 波特词干提取器
 *   @auth : tyf
 *   @date : 2022-03-08  16:38:14
*/
public class SmilePorterStemmer {

    // 波特词干提取器

    public static void main(String[] args) {

        // 创建一个 PorterStemmer 实例
        PorterStemmer stemmer = new PorterStemmer();

        // 示例单词
        String[] words = {"running", "quickly", "happily"};

        // 对示例单词进行词干提取
        for (String word : words) {
            String stemmedWord = stemmer.stem(word);
            System.out.println(word + " -> " + stemmedWord);
        }


    }

}
