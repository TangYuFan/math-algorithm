package org.tyf;


import opennlp.tools.cmdline.doccat.DoccatTool;

/**
 *   @desc : Doccat, 可学习的文档分类器
 *   @auth : tyf
 *   @date : 2022-03-18  12:06:58
*/
public class ApacheNLPDoccat {
    
    //  可学习的文档分类器

    public static void main(String[] args) {


        String data = "C:\\Users\\tyf\\Desktop\\data.txt";
        String model = "C:\\Users\\tyf\\Desktop\\model.bin";


        DoccatTool doccat = new DoccatTool();

        System.out.println("Help：");
        System.out.println(doccat.getHelp());

        // 底层调用进行模型加载和单个样本推理
        // DoccatModel model = new DoccatModelLoader().load(new File(args[0]));
        // DocumentCategorizerME documentCategorizerME = new DocumentCategorizerME(model);
        // double[] prob = documentCategorizerME.categorize(tokens);
        // String category = documentCategorizerME.getBestCategory(prob);
        // DocumentSample sample = new DocumentSample(category, tokens);
        // logger.info(sample.toString());

        doccat.run(new String[]{
                model,
                "<",
                data
        });

    }


}
