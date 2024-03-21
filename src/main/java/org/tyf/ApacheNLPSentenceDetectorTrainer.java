package org.tyf;


import opennlp.tools.cmdline.sentdetect.SentenceDetectorTrainerTool;

/**
 *   @desc : SentenceDetectorTrainer, 用于可学习句子检测器的训练器
 *   @auth : tyf
 *   @date : 2022-03-18  12:06:58
*/
public class ApacheNLPSentenceDetectorTrainer {
    
    //  用于可学习句子检测器的训练器

    public static void main(String[] args) {

        // 每行一句话。空行表示文档边界。如果文档边界未知，建议每隔十句话就有一行空行。
        String data = "";
        String model = "";

        SentenceDetectorTrainerTool trainer = new SentenceDetectorTrainerTool();

        System.out.println("Help：");
        System.out.println(trainer.getHelp());

        // 底层调用 SentenceDetectorME.train
        trainer.run(null,new String[]{
                "-lang","cmn",
                "-model",model,
                "-data",data
        });


    }


}
