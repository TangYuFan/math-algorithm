package org.tyf;


import opennlp.tools.lemmatizer.*;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 *   @desc : LemmatizerEvaluator, 使用参考数据测量词形还原器模型的性能
 *   @auth : tyf
 *   @date : 2022-03-18  12:06:58
*/
public class ApacheNLPLemmatizerEvaluator {
    
    //  使用参考数据测量词形还原器模型的性能

    public static void main(String[] args) throws Exception{

        String data = "C:\\Users\\tyf\\Desktop\\data.txt";
        String model = "C:\\Users\\tyf\\Desktop\\model.bin";


        LemmatizerME lemmatizer = new LemmatizerME(new LemmatizerModel(new File(model)));

        //按行读取数据
        InputStreamFactory inputStreamFactory = new MarkableFileInputStreamFactory(new File(data));
        ObjectStream<String> lineStream = new PlainTextByLineStream(inputStreamFactory, StandardCharsets.UTF_8);
        ObjectStream<LemmaSample> sampleStream = new LemmaSampleStream(lineStream);

        //评估模型
        LemmatizerEvaluator evaluator=new LemmatizerEvaluator(lemmatizer);
        evaluator.evaluate(sampleStream);
        System.out.println("正确的词数："+ evaluator.getWordAccuracy());


    }


}
