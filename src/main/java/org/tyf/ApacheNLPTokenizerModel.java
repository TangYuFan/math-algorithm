package org.tyf;


import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import java.io.File;
import java.util.Arrays;

/**
 *   @desc : 使用 TokenizerModel 进行推理
 *   @auth : tyf
 *   @date : 2022-03-19  10:10:51
*/
public class ApacheNLPTokenizerModel {


    public static void main(String[] args) throws Exception{


        // 模型下载
        // https://opennlp.apache.org/models.html

        // Tokenizer model for English
        String module = "C:\\Users\\tyf\\Downloads\\opennlp-en-ud-ewt-tokens-1.0-1.9.3.bin";


        TokenizerModel m = new TokenizerModel(new File(module));
        Tokenizer tokenizer = new TokenizerME(m);


        String text = "OpenNLP has a command line tool which is used to train the models available from the model download page on various corpora. The data can be converted to the OpenNLP Tokenizer training format or used directly. The OpenNLP format contains one sentence per line. Tokens are either separated by a whitespace or by a special <SPLIT> tag. Tokens are split automatically on whitespace and at least one <SPLIT> tag must be present in the training text. The following sample shows the sample from above in the correct format.";

        String[] out = tokenizer.tokenize(text);

        System.out.println(Arrays.toString(out));

    }


}
