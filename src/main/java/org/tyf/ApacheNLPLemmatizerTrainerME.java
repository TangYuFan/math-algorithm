package org.tyf;


import opennlp.tools.lemmatizer.*;
import opennlp.tools.util.*;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 *   @desc : LemmatizerTrainerME, 用于可学习词形还原器的训练器
 *   @auth : tyf
 *   @date : 2022-03-18  12:06:58
*/
public class ApacheNLPLemmatizerTrainerME {
    
    //  用于可学习词形还原器的训练器

    public static void main(String[] args) throws Exception{


        // He        PRP  he
        // reckons   VBZ  reckon
        // the       DT   the
        // current   JJ   current
        // accounts  NNS  account
        // deficit   NN   deficit
        // will      MD   will
        // narrow    VB   narrow
        // to        TO   to
        // only      RB   only
        // #         #    #
        // 1.8       CD   1.8
        // millions  CD   million
        // in        IN   in
        // September NNP  september
        // .         .    O

        String data = "C:\\Users\\tyf\\Desktop\\data.txt";
        String model = "C:\\Users\\tyf\\Desktop\\model.bin";

        //按行读取数据
        InputStreamFactory inputStreamFactory = new MarkableFileInputStreamFactory(new File(data));
        ObjectStream<String> lineStream = new PlainTextByLineStream(inputStreamFactory, StandardCharsets.UTF_8);
        ObjectStream<LemmaSample> sampleStream = new LemmaSampleStream(lineStream);

        LemmatizerModel mm = LemmatizerME.train("en",sampleStream,  TrainingParameters.defaultParams(),new LemmatizerFactory());

        //保存模型
        FileOutputStream fos= new FileOutputStream(model);
        OutputStream modelOut = new BufferedOutputStream(fos);
        mm.serialize(modelOut);

        //评估模型
        LemmatizerEvaluator evaluator=new LemmatizerEvaluator(new LemmatizerME(mm));
        evaluator.evaluate(sampleStream);
        System.out.println("正确的词数："+ evaluator.getWordAccuracy());

    }


}
