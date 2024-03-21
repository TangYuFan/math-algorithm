package org.tyf;


import opennlp.tools.cmdline.namefind.TokenNameFinderConverterTool;

/**
 *   @desc : TokenNameFinderConverter, 将外部数据格式（如 evalita、ad、conll03、bionlp2004、conll02、masc、muc6、ontonotes、brat）转换为原生 OpenNLP 格式
 *   @auth : tyf
 *   @date : 2022-03-18  12:06:58
*/
public class ApacheNLPTokenNameFinderConverter {
    
    //  将外部数据格式（如 evalita、ad、conll03、bionlp2004、conll02、masc、muc6、ontonotes、brat）转换为原生 OpenNLP 格式

    public static void main(String[] args) {

        // conll 2003 是命名实体中最常见的公开数据集。其官网： https://www.clips.uantwerpen.be/conll2003/ner/
        // 每一行包含4列（单词、词性、句法块标记、实体标记），如：
        // sheep NN B-NP O
        // 多个行组成一个句子，只看第一列即可（EU rejects German call to boycott British lamb）：
        // EU NNP B-NP B-ORG
        // rejects VBZ B-VP O
        // German JJ B-NP B-MISC
        // call NN I-NP O
        // to TO B-VP O
        // boycott VB I-VP O
        // British JJ B-NP B-MISC
        // lamb NN I-NP O

        String data = "C:\\Users\\tyf\\Downloads\\conll2003\\train.txt";


        TokenNameFinderConverterTool converter = new TokenNameFinderConverterTool();

//        System.out.println("Help：");
//        System.out.println(converter.getHelp());

        converter.run(null,new String[]{
                "conll03",
                "-types","per",   // per,loc,org,misc
                "-lang","eng", // eng|deu
                "-data",data
        });



    }


}
