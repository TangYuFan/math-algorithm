package org.tyf;


import opennlp.tools.cmdline.postag.POSTaggerTrainerTool;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 *   @desc : POSTaggerTrainer, 用于可学习词性标注器的训练器
 *   @auth : tyf
 *   @date : 2022-03-18  12:06:58
*/
public class ApacheNLPPOSTaggerTrainer {
    
    //  用于可学习词性标注器的训练器

    public static void main(String[] args) throws IOException {

        // 训练样本需要分词、单词和词性用_链接、单词之间使用空格隔开。每行一个句子

        // 比如原始样本
        // About 10 Euro, I reckon.
        // That sounds good.

        // 对应的每次词的标记
        // About_IN
        // 10_CD
        // Euro_NNP
        // ,_,
        // I_PRP
        // reckon_VBP
        // ._.
        // That_DT
        // sounds_VBZ
        // good_JJ
        // ._.

        // 对应的样本如下：
        // About_IN 10_CD Euro_NNP ,_, I_PRP reckon_VBP ._.
        // That_DT sounds_VBZ good_JJ ._.

        String model = "C:\\Users\\tyf\\Desktop\\model.bin";
        String data = "C:\\Users\\tyf\\Desktop\\data.txt";

        POSTaggerTrainerTool trainer = new POSTaggerTrainerTool();

        System.out.println("Help：");
        System.out.println(trainer.getHelp());

        trainer.run(null,new String[]{
            "-model",model,
            "-data",data,
            "-lang","eng", //
        });


        // 加载模型
        POSModel mm = new POSModel(new File(model));
        POSTaggerME tagger = new POSTaggerME(mm);

        // 打印模型中记录的所有词性
        System.out.println("AllPosTags:"+ Arrays.toString(tagger.getAllPosTags()));


    }


}
