package org.tyf;


import opennlp.tools.cmdline.sentdetect.SentenceDetectorEvaluatorTool;
import opennlp.tools.sentdetect.SentenceDetectorEvaluator;

/**
 *   @desc : SentenceDetectorEvaluator, 测量可学习句子检测器的性能
 *   @auth : tyf
 *   @date : 2022-03-18  12:06:58
*/
public class ApacheNLPSentenceDetectorEvaluator {
    
    //  测量可学习句子检测器的性能

    public static void main(String[] args) {

        // 每行一句话。空行表示文档边界。如果文档边界未知，建议每隔十句话就有一行空行。
        String data = "";
        String model = "";

        SentenceDetectorEvaluatorTool evaluator = new SentenceDetectorEvaluatorTool();

        System.out.println("Help：");
        System.out.println(evaluator.getHelp());

        evaluator.run(null,new String[]{
                "-model",model,
                "-data",data
        });

    }


}
