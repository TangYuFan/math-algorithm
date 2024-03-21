package org.tyf;


import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.util.Sequence;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;

/**
 *   @desc : ChunkerME, 可学习的分块器
 *   @auth : tyf
 *   @date : 2022-03-18  12:06:58
*/
public class ApacheNLPChunkerME {
    
    //  可学习的分块器

    // API
    // https://opennlp.apache.org/docs/2.3.2/manual/opennlp.html#tools.chunker
    // CLI
    // https://opennlp.apache.org/docs/2.3.2/manual/opennlp.html#tools.cli.chunker

    public static void main(String[] args) throws Exception{


        // 	conll2000 数据集训练的官方模型
        String model = "C:\\Users\\tyf\\Downloads\\en-chunker.bin";

        // 语料分词
        String sent[] = new String[] { "Rockwell", "International", "Corp.", "'s",
                "Tulsa", "unit", "said", "it", "signed", "a", "tentative", "agreement",
                "extending", "its", "contract", "with", "Boeing", "Co.", "to",
                "provide", "structural", "parts", "for", "Boeing", "'s", "747",
                "jetliners", "." };

        // 语料词性
        String pos[] = new String[] { "NNP", "NNP", "NNP", "POS", "NNP", "NN",
                "VBD", "PRP", "VBD", "DT", "JJ", "NN", "VBG", "PRP$", "NN", "IN",
                "NNP", "NNP", "TO", "VB", "JJ", "NNS", "IN", "NNP", "POS", "CD", "NNS",
                "." };

        // 块分析
        ChunkerME chunker = new ChunkerME(new ChunkerModel(new File(model)));

        // 每个词对应一个标签，标签表示该词所属的短语块
        String tag[] = chunker.chunk(sent, pos);


        double probs[] = chunker.probs();
        Sequence topSequences[] = chunker.topKSequences(sent, pos);

        System.out.println("tag："+Arrays.toString(tag));
        System.out.println("probs："+Arrays.toString(probs));
        System.out.println("topSequences："+Arrays.toString(topSequences));

        // tag 说明：
        // [B-NP, I-NP, I-NP, B-NP, I-NP, I-NP, B-VP, B-NP, B-VP, B-NP, I-NP, I-NP, B-VP, B-NP, I-NP, B-PP, B-NP, I-NP, B-VP, I-VP, B-NP, I-NP, B-PP, B-NP, B-NP, I-NP, I-NP, O]

        // B-NP：表示名词短语的开始。
        // I-NP：表示名词短语中的词。
        // B-VP：表示动词短语的开始。
        // I-VP：表示动词短语中的词。
        // B-PP：表示介词短语的开始。
        // O：表示其他，通常用于表示不属于任何特定类型的短语块。
    }


}
