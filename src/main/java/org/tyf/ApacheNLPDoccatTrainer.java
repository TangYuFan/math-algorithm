package org.tyf;


import opennlp.tools.cmdline.doccat.DoccatModelLoader;
import opennlp.tools.cmdline.doccat.DoccatTrainerTool;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;

import java.io.File;

/**
 *   @desc : DoccatTrainer, 用于可学习文档分类器的训练器
 *   @auth : tyf
 *   @date : 2022-03-18  12:06:58
*/
public class ApacheNLPDoccatTrainer {
    
    //  用于可学习文档分类器的训练器

    // 文档分类 api
    // https://opennlp.apache.org/docs/2.3.2/manual/opennlp.html#tools.lemmatizer.training.api

    // 文档分类 CLI
    // https://opennlp.apache.org/docs/2.3.2/manual/opennlp.html#tools.cli.doccat

    public static void main(String[] args) {


        // 每行一个样本，label 和 text 空格隔开
        // label1 后面的都是文本（token隔开）...
        // label2 后面的都是文本（token隔开）...
        // label3 后面的都是文本（token隔开）...

        String data = "C:\\Users\\tyf\\Desktop\\data.txt";
        String model = "C:\\Users\\tyf\\Desktop\\model.bin";


        DoccatTrainerTool trainer = new DoccatTrainerTool();
        System.out.println("Help：");
        System.out.println(trainer);

        // 底层调用 DocumentCategorizerME.train
        new DoccatTrainerTool().run(null,new String[]{
            "-lang","chm",
            "-model",model,
            "-data",data,
        });


        // 加载模型
        DoccatModel mm = new DoccatModelLoader().load(new File(model));
        DocumentCategorizerME documentCategorizerME = new DocumentCategorizerME(mm);
        System.out.println("documentCategorizerME："+documentCategorizerME);

        // 推理
        String[] tokens = new String[]{"大师"};
        double[] prob = documentCategorizerME.categorize(tokens);
        String category = documentCategorizerME.getBestCategory(prob);
        DocumentSample sample = new DocumentSample(category, tokens);
        System.out.println("sample:"+sample);

    }


}
