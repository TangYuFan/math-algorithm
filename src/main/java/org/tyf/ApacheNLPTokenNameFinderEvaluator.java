package org.tyf;


import opennlp.tools.cmdline.namefind.TokenNameFinderEvaluatorTool;

/**
 *   @desc : TokenNameFinderEvaluator, 使用参考数据测量名称实体识别器模型的性能
 *   @auth : tyf
 *   @date : 2022-03-18  12:06:58
*/
public class ApacheNLPTokenNameFinderEvaluator {
    
    //  使用参考数据测量名称实体识别器模型的性能

    public static void main(String[] args) {


        // 模型保存
        String module = "C:\\Users\\tyf\\Desktop\\custom.bin";

        // 语料格式
        // <START:person> Pierre Vinken <END> , 61 years old , will join the board as a nonexecutive director Nov. 29 .
        // Mr . <START:person> Vinken <END> is chairman of Elsevier N.V. , the Dutch publishing group .

        // 每个单词隔开、每个实体使用起始符号包围
        // <START:person> xxx <END>

        String data = "C:\\Users\\tyf\\Desktop\\data.txt";



        TokenNameFinderEvaluatorTool evaluator = new TokenNameFinderEvaluatorTool();

        System.out.println("Help：");
        System.out.println(evaluator.getHelp());


        evaluator.run(null,new String[]{
                "-model",module,
                "-data",data
        });




    }


}
