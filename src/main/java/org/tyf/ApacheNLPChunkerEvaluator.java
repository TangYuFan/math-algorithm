package org.tyf;


import opennlp.tools.cmdline.chunker.ChunkerEvaluatorTool;

/**
 *   @desc : ChunkerEvaluator, 使用参考数据测量分块器模型的性能
 *   @auth : tyf
 *   @date : 2022-03-18  12:06:58
*/
public class ApacheNLPChunkerEvaluator {
    
    //  使用参考数据测量分块器模型的性能

    public static void main(String[] args) {


        String model = "";
        String data = "";

        ChunkerEvaluatorTool evaluator = new ChunkerEvaluatorTool();

        evaluator.run(null,new String[]{
                "-model",model,
                "-encoding","utf8",
                "-lang","en",
                "-data",data
        });


    }


}
