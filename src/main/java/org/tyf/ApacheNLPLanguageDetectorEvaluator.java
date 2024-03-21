package org.tyf;


import opennlp.tools.cmdline.langdetect.LanguageDetectorEvaluatorTool;

/**
 *   @desc : LanguageDetectorEvaluator, 使用参考数据测量语言检测器模型的性能
 *   @auth : tyf
 *   @date : 2022-03-18  12:06:58
*/
public class ApacheNLPLanguageDetectorEvaluator {
    
    //  使用参考数据测量语言检测器模型的性能

    public static void main(String[] args) {

        String model = "C:\\Users\\tyf\\Desktop\\model.bin";
        String data = "C:\\Users\\tyf\\Desktop\\data.txt";


        LanguageDetectorEvaluatorTool evaluator = new LanguageDetectorEvaluatorTool();

        evaluator.run(null,new String[]{
                "-data",data,
                "-model",model,
        });


    }


}
