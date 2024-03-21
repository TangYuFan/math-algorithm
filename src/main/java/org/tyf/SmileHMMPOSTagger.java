package org.tyf;

import smile.nlp.pos.HMMPOSTagger;
import smile.nlp.pos.PennTreebankPOS;


/**
 *   @desc : Smile HMMPOSTagger 隐马尔可夫模型词性标注器
 *   @auth : tyf
 *   @date : 2022-03-08  16:38:14
*/
public class SmileHMMPOSTagger {

    // 隐马尔可夫模型词性标注器

    public static void main(String[] args) {

        // 创建一个示例句子
        String[] sentence = {"I", "like", "to", "eat", "apples"};

        // 获取默认的 HMM 词性标注器
        HMMPOSTagger tagger = HMMPOSTagger.getDefault();

        // 使用词性标注器进行词性标注
        PennTreebankPOS[] tags = tagger.tag(sentence);

        // 输出词性标注结果
        for (int i = 0; i < sentence.length; i++) {
            System.out.println(sentence[i] + " -> " + tags[i]);
        }


    }

}
