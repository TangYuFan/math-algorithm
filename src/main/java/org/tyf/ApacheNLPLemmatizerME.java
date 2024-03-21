package org.tyf;


import opennlp.tools.lemmatizer.LemmatizerME;
import opennlp.tools.lemmatizer.LemmatizerModel;

import java.io.File;

/**
 *   @desc : LemmatizerME, 可学习的词形还原器
 *   @auth : tyf
 *   @date : 2022-03-18  12:06:58
*/
public class ApacheNLPLemmatizerME {
    
    //  可学习的词形还原器，把用 POS tagger 标注格式的词还原为标注前的格式。

    public static void main(String[] args) throws Exception{


        String model = "C:\\Users\\tyf\\Desktop\\model.bin";

        LemmatizerME lemmatizer = new LemmatizerME(new LemmatizerModel(new File(model)));

        // 单词
        String[] tokens = new String[] { "Rockwell", "International", "Corp.", "'s",
                "Tulsa", "unit", "said", "it", "signed", "a", "tentative", "agreement",
                "extending", "its", "contract", "with", "Boeing", "Co.", "to",
                "provide", "structural", "parts", "for", "Boeing", "'s", "747",
                "jetliners", "." };

        // 单词的词性
        String[] postags = new String[] { "NNP", "NNP", "NNP", "POS", "NNP", "NN",
                "VBD", "PRP", "VBD", "DT", "JJ", "NN", "VBG", "PRP$", "NN", "IN",
                "NNP", "NNP", "TO", "VB", "JJ", "NNS", "IN", "NNP", "POS", "CD", "NNS",
                "." };

        String[] lemmas = lemmatizer.lemmatize(tokens, postags);

    }


}
