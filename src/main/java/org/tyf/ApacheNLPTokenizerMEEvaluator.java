package org.tyf;


import opennlp.tools.cmdline.tokenizer.TokenizerMEEvaluatorTool;

/**
 *   @desc : TokenizerMEEvaluator, 测量可学习分词器的性能
 *   @auth : tyf
 *   @date : 2022-03-18  12:06:58
*/
public class ApacheNLPTokenizerMEEvaluator {
    
    //  测量可学习分词器的性能

    public static void main(String[] args) {


        // Usage: opennlp TokenizerMEEvaluator[.irishsentencebank|.ad|.pos|.conllx|.namefinder|.parse|.conllu] -model
        //        model [-misclassified true|false] -data sampleData [-encoding charsetName]
        // Arguments description:
        //	-model model
        //		the model file to be evaluated.
        //	-misclassified true|false
        //		if true will print false negatives and false positives.
        //	-data sampleData
        //		data to be used, usually a file name.
        //	-encoding charsetName
        //		encoding for reading and writing text, if absent the system default is used.

        String module = "C:\\Users\\tyf\\Downloads\\opennlp-en-ud-ewt-tokens-1.0-1.9.3.bin";
        String data = "C:\\Users\\tyf\\Desktop\\data.txt";


        TokenizerMEEvaluatorTool evaluator = new TokenizerMEEvaluatorTool();

        evaluator.run(null,new String[]{
                "-encoding","UTF-8",
                "-model", module,
                "-data", data,
        });




    }


}
