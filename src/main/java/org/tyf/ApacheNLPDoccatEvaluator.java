package org.tyf;


import opennlp.tools.cmdline.doccat.DoccatEvaluatorTool;

/**
 *   @desc : DoccatEvaluator, 使用参考数据测量 Doccat 模型的性能
 *   @auth : tyf
 *   @date : 2022-03-18  12:06:58
*/
public class ApacheNLPDoccatEvaluator {
    
    //  使用参考数据测量 Doccat 模型的性能

    public static void main(String[] args) {

        String data = "C:\\Users\\tyf\\Desktop\\data.txt";
        String model = "C:\\Users\\tyf\\Desktop\\model.bin";


        DoccatEvaluatorTool evaluator = new DoccatEvaluatorTool();

        System.out.println("Help：");
        System.out.println(evaluator.getHelp());

        // 底层调用 DocumentCategorizerEvaluator
        evaluator.run(null,new String[]{
                "-model",model,
                "-data",data,
        });

    }


}
