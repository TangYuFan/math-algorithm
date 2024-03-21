package org.tyf;


import opennlp.tools.cmdline.namefind.TokenNameFinderTrainerTool;

/**
 *   @desc : TokenNameFinderTrainer, 用于可学习名称实体识别器的训练器
 *   @auth : tyf
 *   @date : 2022-03-18  12:06:58
*/
public class ApacheNLPTokenNameFinderTrainer {
    
    //  用于可学习名称实体识别器的训练器

    public static void main(String[] args) {


        // 模型保存
        String module = "C:\\Users\\tyf\\Desktop\\custom.bin";

        // 语料格式
        // <START:person> Pierre Vinken <END> , 61 years old , will join the board as a nonexecutive director Nov. 29 .
        // Mr . <START:person> Vinken <END> is chairman of Elsevier N.V. , the Dutch publishing group .

        // 每个单词隔开、每个实体使用起始符号包围
        // <START:person> xxx <END>

        String data = "C:\\Users\\tyf\\Desktop\\data.txt";

        // 训练数据应包含至少15000个句子，以创建性能良好的模型
        TokenNameFinderTrainerTool trainer = new TokenNameFinderTrainerTool();

        System.out.println("Help：");
        System.out.println(trainer.getHelp());

        // 底层调用训练函数 opennlp.tools.namefind.NameFinderME.train()
        trainer.run(null,new String[]{
                "-lang","eng",
                "-model",module,
                "-data",data
        });


    }


}
