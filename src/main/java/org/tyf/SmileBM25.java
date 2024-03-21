package org.tyf;

import smile.nlp.*;
import smile.nlp.relevance.BM25;


/**
 *   @desc : Smile BM25 BM25相关性评分
 *   @auth : tyf
 *   @date : 2022-03-08  16:38:14
*/
public class SmileBM25 {

    // BM25相关性评分
    // BM25是一种用于构建对词频和文档长度敏感的概率模型的方式，同时不引入太多额外的参数

    public static void main(String[] args) {

        // 创建BM25实例
        BM25 bm25 = new BM25();

        // 创建语料库示例
        SimpleCorpus corpus = new SimpleCorpus();

        // 添加文档到语料库中
        corpus.add(new SimpleText("document1", "This is the first document.", "", new String[]{}));
        corpus.add(new SimpleText("document2", "This is the second document.", "", new String[]{}));

        // 创建待评分的文档
        SimpleText document = new SimpleText("document", "", "This is the document to rank.", new String[]{"This", "is", "the", "document", "to", "rank"});

        // 待评分的词频
        int termFrequency = 2;

        // 包含词的文档数量
        int n = 2;

        // 计算BM25相关性评分
        double relevanceScore = bm25.rank(corpus, document, "term", termFrequency, n);

        // 输出结果
        System.out.println("Relevance Score: " + relevanceScore);


    }

}
