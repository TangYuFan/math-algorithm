package org.tyf;

import smile.nlp.embedding.Word2Vec;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 *   @desc : Smile Word2Vec 词嵌入
 *   @auth : tyf
 *   @date : 2022-03-08  16:38:14
*/
public class SmileWord2Vec {

    // 词嵌入

    public static void main(String[] args) {

        // 加载预训练的 Word2Vec 模型
        try {
            Path modelFile = Paths.get("path_to_word2vec_model.bin");
            Word2Vec word2Vec = Word2Vec.of(modelFile);

            // 示例查询词汇
            String[] queryWords = {"king", "queen", "man", "woman"};

            // 查询词汇的词向量
            for (String word : queryWords) {
                float[] vector = word2Vec.get(word);
                if (vector != null) {
                    System.out.println("Word: " + word);
                    System.out.print("Vector: ");
                    for (float v : vector) {
                        System.out.print(v + " ");
                    }
                    System.out.println();
                } else {
                    System.out.println("Word '" + word + "' not found in the model.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
