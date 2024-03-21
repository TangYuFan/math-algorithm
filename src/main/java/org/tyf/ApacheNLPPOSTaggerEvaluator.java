package org.tyf;


import opennlp.tools.cmdline.postag.POSTaggerEvaluatorTool;

/**
 *   @desc : POSTaggerEvaluator, 使用参考数据测量词性标注器模型的性能
 *   @auth : tyf
 *   @date : 2022-03-18  12:06:58
*/
public class ApacheNLPPOSTaggerEvaluator {
    
    //  使用参考数据测量词性标注器模型的性能

    public static void main(String[] args) {


        String model = "C:\\Users\\tyf\\Desktop\\model.bin";
        String data = "C:\\Users\\tyf\\Desktop\\data.txt";

        POSTaggerEvaluatorTool evaluator = new POSTaggerEvaluatorTool();
        evaluator.run(null,new String[]{
                "-model",model,
                "-data",data,
        });

    }


}
