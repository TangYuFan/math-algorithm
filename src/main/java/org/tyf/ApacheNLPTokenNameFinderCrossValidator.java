package org.tyf;


import opennlp.tools.cmdline.namefind.TokenNameFinderCrossValidatorTool;

/**
 *   @desc : TokenNameFinderCrossValidator, 可学习名称实体识别器的 K 折交叉验证器
 *   @auth : tyf
 *   @date : 2022-03-18  12:06:58
*/
public class ApacheNLPTokenNameFinderCrossValidator {
    
    //  可学习名称实体识别器的 K 折交叉验证器

    public static void main(String[] args) {


        // 语料格式
        // <START:person> Pierre Vinken <END> , 61 years old , will join the board as a nonexecutive director Nov. 29 .
        // Mr . <START:person> Vinken <END> is chairman of Elsevier N.V. , the Dutch publishing group .

        // 每个单词隔开、每个实体使用起始符号包围
        // <START:person> xxx <END>

        String data = "C:\\Users\\tyf\\Desktop\\data.txt";


        TokenNameFinderCrossValidatorTool validator = new TokenNameFinderCrossValidatorTool();

        System.out.println("Help：");
        System.out.println(validator.getHelp());


        validator.run(null,new String[]{
                "-data",data,
                "-lang","eng"
        });


    }


}
