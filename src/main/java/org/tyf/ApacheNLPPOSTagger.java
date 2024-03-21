package org.tyf;


import opennlp.tools.lemmatizer.LemmatizerME;
import opennlp.tools.lemmatizer.LemmatizerModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.util.Sequence;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;

/**
 *   @desc : POSTagger, 可学习的词性标注器
 *   @auth : tyf
 *   @date : 2022-03-18  12:06:58
*/
public class ApacheNLPPOSTagger {
    
    //  可学习的词性标注器

    public static void main(String[] args) throws Exception{

//        String module = "C:\\Users\\tyf\\Downloads\\en-pos-maxent.bin";
        String module = "C:\\Users\\tyf\\Downloads\\en-pos-perceptron.bin";

        POSModel model = new POSModel(new File(module));
        POSTaggerME tagger = new POSTaggerME(model);

        String sent[] = new String[]{
                "Most",
                "large",
                "cities",
                "in",
                "the",
                "US",
                "had",
                "morning",
                "and",
                "afternoon",
                "newspapers",
                "."
        };

        // 得到每个单词的词性
        String tags[] = tagger.tag(sent);
        // 得到每个单词词性的置信度
        double probs[] = tagger.probs();
        // 获取了句子中可能的词性标注序列
        Sequence topSequences[] = tagger.topKSequences(sent);

        // 打印模型中记录的所有词性
        System.out.println("AllPosTags:"+Arrays.toString(tagger.getAllPosTags()));

        // Tags:[RBS, JJ, NNS, IN, DT, NNP, VBD, NN, CC, NN, NNS, .]
        System.out.println("Tags:"+ Arrays.toString(tags));
        System.out.println("Probs:"+ Arrays.toString(probs));
        System.out.println("TopSequences:"+ Arrays.toString(topSequences));

        // 词性列表
        // RBS: 副词最高级（Adverb, superlative）
        // JJ: 形容词（Adjective）
        // NNS: 名词复数形式（Noun, plural）
        // IN: 介词或从属连词（Preposition or subordinating conjunction）
        // DT: 限定词（Determiner）
        // NNP: 单数专有名词（Proper noun, singular）
        // VBD: 动词过去式（Verb, past tense）
        // NN: 名词单数形式或未知的名词类型（Noun, singular or unknown）
        // CC: 连词（Coordinating conjunction）

    }


}
