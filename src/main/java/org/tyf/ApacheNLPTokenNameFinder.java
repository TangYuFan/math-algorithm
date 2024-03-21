package org.tyf;


import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.util.Span;

import java.io.File;
import java.util.Arrays;

/**
 *   @desc : TokenNameFinder, 可学习的名称实体识别器
 *   @auth : tyf
 *   @date : 2022-03-18  12:06:58
*/
public class ApacheNLPTokenNameFinder {
    
    //  可学习的名称实体识别器

    public static void main(String[] args) throws Exception{


        // 模型下载
        // https://opennlp.sourceforge.net/models-1.5/
        // https://opennlp.apache.org/models.html

        String module = "C:\\Users\\tyf\\Downloads\\en-ner-person.bin";


        // 命名实体查找器
        TokenNameFinderModel model = new TokenNameFinderModel(new File(module));
        NameFinderME nameFinder = new NameFinderME(model);

        String sentences =
                "If President John F. Kennedy, after visiting France in 1961 with his immensely popular wife,"
                        + " famously described himself as 'the man who had accompanied Jacqueline Kennedy to Paris,'"
                        + " Mr. Hollande has been most conspicuous on this state visit for traveling alone." +
                "Mr. Draghi spoke on the first day of an economic policy conference here organized by"
                        + " the E.C.B. as a sort of counterpart to the annual symposium held in Jackson"
                        + " Hole, Wyo., by the Federal Reserve Bank of Kansas City. " ;

        // 分词器
        Tokenizer tokenizer = SimpleTokenizer.INSTANCE;

        // 分词
        String[] tokens = tokenizer.tokenize(sentences);

        // 实体检测
        Span[] nameSpans = nameFinder.find(tokens);

        System.out.println("Name："+Arrays.toString(nameSpans));
        System.out.println("Text："+Arrays.toString(Span.spansToStrings(nameSpans, tokens)));
    }


}
