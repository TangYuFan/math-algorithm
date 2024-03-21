package org.tyf;

import smile.nlp.tokenizer.SimpleSentenceSplitter;


/**
 *   @desc : Smile SimpleSentenceSplitter 句子拆分
 *   @auth : tyf
 *   @date : 2022-03-08  16:38:14
*/
public class SmileSimpleSentenceSplitter {

    // 句子拆分

    public static void main(String[] args) {

        String text = "This is a simple test. Does it work? Yes, it does! Mr. Smith went to the store. He bought a car. I saw him there. \"Hello, world!\" she exclaimed. Dr. Johnson has a Ph.D. It is raining outside.";

        // 使用 Smile 库中的 SimpleSentenceSplitter 进行句子拆分
        SimpleSentenceSplitter splitter = SimpleSentenceSplitter.getInstance();
        String[] sentences = splitter.split(text);

        // 打印拆分后的句子
        for (String sentence : sentences) {
            System.out.println(sentence);
        }


    }

}
