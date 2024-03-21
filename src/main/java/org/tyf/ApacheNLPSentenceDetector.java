package org.tyf;


import opennlp.tools.cmdline.sentdetect.SentenceDetectorTool;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.util.Span;

import java.io.File;
import java.util.Arrays;

/**
 *   @desc : SentenceDetector, 可学习的句子检测器
 *   @auth : tyf
 *   @date : 2022-03-18  12:06:58
*/
public class ApacheNLPSentenceDetector {
    
    //  可学习的句子检测器


    // CLI
    // https://opennlp.apache.org/docs/2.3.2/manual/opennlp.html#tools.cli.sentdetect
    // API
    // https://opennlp.apache.org/docs/2.3.2/manual/opennlp.html#tools.sentdetect

    public static void main(String[] args) throws Exception{


        String model = "C:\\Users\\tyf\\Downloads\\en-sent.bin";

//        SentenceDetectorTool detector = new SentenceDetectorTool();
//        System.out.println("Help：");
//        System.out.println(detector.getHelp());
//        detector.run(new String[]{model,"<",sentences});

        SentenceModel mm = new SentenceModel(new File(model));
        SentenceDetectorME detector = new SentenceDetectorME(mm);

        // 句子 text 分割
        String sentences1[] = detector.sentDetect("  First sentence. Second sentence. ");
        // 句子 index 分割
        Span sentences2[] = detector.sentPosDetect("  First sentence. Second sentence. ");

        System.out.println(Arrays.toString(sentences1));
        System.out.println(Arrays.toString(sentences2));

    }


}
