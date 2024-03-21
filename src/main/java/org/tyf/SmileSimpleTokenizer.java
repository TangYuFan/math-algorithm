package org.tyf;

import smile.nlp.tokenizer.SimpleTokenizer;


/**
 *   @desc : Smile SimpleTokenizer 分词
 *   @auth : tyf
 *   @date : 2022-03-08  16:38:14
*/
public class SmileSimpleTokenizer {

    // 分词

    public static void main(String[] args) {

        // 创建 SimpleTokenizer 实例
        SimpleTokenizer tokenizer = new SimpleTokenizer();

        // 待分词的文本
        String text = "This is a simple text, isn't it? I'm going to the park.";

        // 调用分词方法进行分词
        String[] tokens = tokenizer.split(text);

        // 打印分词结果
        System.out.println("Tokens:");
        for (String token : tokens) {
            System.out.println(token);
        }


    }

}
