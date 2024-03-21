package org.tyf;


import opennlp.tools.cmdline.CLI;
import opennlp.tools.cmdline.CmdLineTool;
import opennlp.tools.cmdline.chunker.*;
import opennlp.tools.cmdline.dictionary.DictionaryBuilderTool;
import opennlp.tools.cmdline.doccat.*;
import opennlp.tools.cmdline.entitylinker.EntityLinkerTool;
import opennlp.tools.cmdline.langdetect.*;
import opennlp.tools.cmdline.languagemodel.NGramLanguageModelTool;
import opennlp.tools.cmdline.lemmatizer.LemmatizerEvaluatorTool;
import opennlp.tools.cmdline.lemmatizer.LemmatizerMETool;
import opennlp.tools.cmdline.lemmatizer.LemmatizerTrainerTool;
import opennlp.tools.cmdline.namefind.*;
import opennlp.tools.cmdline.parser.*;
import opennlp.tools.cmdline.postag.POSTaggerConverterTool;
import opennlp.tools.cmdline.postag.POSTaggerCrossValidatorTool;
import opennlp.tools.cmdline.postag.POSTaggerEvaluatorTool;
import opennlp.tools.cmdline.postag.POSTaggerTrainerTool;
import opennlp.tools.cmdline.sentdetect.*;
import opennlp.tools.cmdline.tokenizer.*;
import opennlp.tools.util.DownloadUtil;
import opennlp.tools.util.Version;

import java.util.*;


/**
 *   @desc : apache-nlp 打印支持的所有工具
 *   @auth : tyf
 *   @date : 2022-03-18  11:38:55
*/
public class ApacheNLPUsage {

    public static final String CMD = "opennlp";

    private static Map<String, CmdLineTool> toolLookupMap;

    static {
        toolLookupMap = new LinkedHashMap<>();

        List<CmdLineTool> tools = new LinkedList<>();

        // Document Categorizer
        tools.add(new DoccatTool());
        tools.add(new DoccatTrainerTool());
        tools.add(new DoccatEvaluatorTool());
        tools.add(new DoccatCrossValidatorTool());
        tools.add(new DoccatConverterTool());

        // Language Detector
        tools.add(new LanguageDetectorTool());
        tools.add(new LanguageDetectorTrainerTool());
        tools.add(new LanguageDetectorConverterTool());
        tools.add(new LanguageDetectorCrossValidatorTool());
        tools.add(new LanguageDetectorEvaluatorTool());

        // Dictionary Builder
        tools.add(new DictionaryBuilderTool());

        // Tokenizer
        tools.add(new SimpleTokenizerTool());
        tools.add(new TokenizerMETool());
        tools.add(new TokenizerTrainerTool());
        tools.add(new TokenizerMEEvaluatorTool());
        tools.add(new TokenizerCrossValidatorTool());
        tools.add(new TokenizerConverterTool());
        tools.add(new DictionaryDetokenizerTool());

        // Sentence detector
        tools.add(new SentenceDetectorTool());
        tools.add(new SentenceDetectorTrainerTool());
        tools.add(new SentenceDetectorEvaluatorTool());
        tools.add(new SentenceDetectorCrossValidatorTool());
        tools.add(new SentenceDetectorConverterTool());

        // Name Finder
        tools.add(new TokenNameFinderTool());
        tools.add(new TokenNameFinderTrainerTool());
        tools.add(new TokenNameFinderEvaluatorTool());
        tools.add(new TokenNameFinderCrossValidatorTool());
        tools.add(new TokenNameFinderConverterTool());
        tools.add(new CensusDictionaryCreatorTool());


        // POS Tagger
        tools.add(new opennlp.tools.cmdline.postag.POSTaggerTool());
        tools.add(new POSTaggerTrainerTool());
        tools.add(new POSTaggerEvaluatorTool());
        tools.add(new POSTaggerCrossValidatorTool());
        tools.add(new POSTaggerConverterTool());

        //Lemmatizer
        tools.add(new LemmatizerMETool());
        tools.add(new LemmatizerTrainerTool());
        tools.add(new LemmatizerEvaluatorTool());

        // Chunker
        tools.add(new ChunkerMETool());
        tools.add(new ChunkerTrainerTool());
        tools.add(new ChunkerEvaluatorTool());
        tools.add(new ChunkerCrossValidatorTool());
        tools.add(new ChunkerConverterTool());

        // Parser
        tools.add(new ParserTool());
        tools.add(new ParserTrainerTool()); // trains everything
        tools.add(new ParserEvaluatorTool());
        tools.add(new ParserConverterTool()); // trains everything
        tools.add(new BuildModelUpdaterTool()); // re-trains  build model
        tools.add(new CheckModelUpdaterTool()); // re-trains  build model
        tools.add(new TaggerModelReplacerTool());

        // Entity Linker
        tools.add(new EntityLinkerTool());

        // Language Model
        tools.add(new NGramLanguageModelTool());

        for (CmdLineTool tool : tools) {
            toolLookupMap.put(tool.getName(), tool);
        }

        toolLookupMap = Collections.unmodifiableMap(toolLookupMap);
    }


    private static void usage() {

        System.out.println("OpenNLP "+ Version.currentVersion()+"." );
        System.out.println("Usage: "+CMD+" TOOL");

        // distance of tool name from line start
        int numberOfSpaces = -1;
        for (String toolName : toolLookupMap.keySet()) {
            if (toolName.length() > numberOfSpaces) {
                numberOfSpaces = toolName.length();
            }
        }
        numberOfSpaces = numberOfSpaces + 4;

        final StringBuilder sb = new StringBuilder("where TOOL is one of: \n\n");
        for (CmdLineTool tool : toolLookupMap.values()) {

            sb.append("  ").append(tool.getName());
            sb.append(repeatSpaces(Math.max(0, StrictMath.abs(tool.getName().length() - numberOfSpaces))));
            sb.append(tool.getShortDescription()).append("\n");
        }
        System.out.println(sb);

        System.out.println("All tools print help when invoked with help parameter");
        System.out.println("Example: opennlp SimpleTokenizer help");
    }

    // 替代 String.repeat 方法
    private static String repeatSpaces(int numSpaces) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numSpaces; i++) {
            sb.append(" ");
        }
        return sb.toString();
    }

    public static void main(String[] args) {

        // opennlp.tools.cmdline.CLI 的 usage 函数
//        if (args.length == 0) {
//            usage();
//        }

        // 所有工具如下
        // Doccat	可学习的文档分类器
        // DoccatTrainer	用于可学习文档分类器的训练器
        // DoccatEvaluator	使用参考数据测量 Doccat 模型的性能
        // DoccatCrossValidator	可学习文档分类器的 K 折交叉验证器
        // DoccatConverter	将 20newsgroup 数据格式转换为原生 OpenNLP 格式
        // LanguageDetector	可学习的语言检测器
        // LanguageDetectorTrainer	用于可学习语言检测器的训练器
        // LanguageDetectorConverter	将 Leipzig 数据格式转换为原生 OpenNLP 格式
        // LanguageDetectorCrossValidator	可学习语言检测器的 K 折交叉验证器
        // LanguageDetectorEvaluator	使用参考数据测量语言检测器模型的性能
        // DictionaryBuilder	构建新字典
        // SimpleTokenizer	字符类分词器
        // TokenizerME	可学习的分词器
        // TokenizerTrainer	用于可学习分词器的训练器
        // TokenizerMEEvaluator	测量可学习分词器的性能
        // TokenizerCrossValidator	可学习分词器的 K 折交叉验证器
        // TokenizerConverter	将外部数据格式（如 irishsentencebank、ad、pos、masc、conllx、namefinder、parse、conllu）转换为原生 OpenNLP 格式
        // DictionaryDetokenizer	根据分词器词典中定义的规则对标记文本进行规范化
        // SentenceDetector	可学习的句子检测器
        // SentenceDetectorTrainer	用于可学习句子检测器的训练器
        // SentenceDetectorEvaluator	测量可学习句子检测器的性能
        // SentenceDetectorCrossValidator	可学习句子检测器的 K 折交叉验证器
        // SentenceDetectorConverter	将外部数据格式（如 nkjp、irishsentencebank、ad、pos、masc、conllx、namefinder、parse、moses、conllu、letsmt）转换为原生 OpenNLP 格式
        // TokenNameFinder	可学习的名称实体识别器
        // TokenNameFinderTrainer	用于可学习名称实体识别器的训练器
        // TokenNameFinderEvaluator	使用参考数据测量名称实体识别器模型的性能
        // TokenNameFinderCrossValidator	可学习名称实体识别器的 K 折交叉验证器
        // TokenNameFinderConverter	将外部数据格式（如 evalita、ad、conll03、bionlp2004、conll02、masc、muc6、ontonotes、brat）转换为原生 OpenNLP 格式
        // CensusDictionaryCreator	将 1990 年美国人口普查姓名转换为字典
        // POSTagger	可学习的词性标注器
        // POSTaggerTrainer	用于可学习词性标注器的训练器
        // POSTaggerEvaluator	使用参考数据测量词性标注器模型的性能
        // POSTaggerCrossValidator	可学习词性标注器的 K 折交叉验证器
        // POSTaggerConverter	将外部数据格式（如 ad、masc、conllx、parse、ontonotes、conllu）转换为原生 OpenNLP 格式
        // LemmatizerME	可学习的词形还原器
        // LemmatizerTrainerME	用于可学习词形还原器的训练器
        // LemmatizerEvaluator	使用参考数据测量词形还原器模型的性能
        // ChunkerME	可学习的分块器
        // ChunkerTrainerME	用于可学习分块器的训练器
        // ChunkerEvaluator	使用参考数据测量分块器模型的性能
        // ChunkerCrossValidator	分块器的 K 折交叉验证器
        // ChunkerConverter	将 ad 数据格式转换为原生 OpenNLP 格式
        // Parser	执行完整的句法分析

        // 打印官方提供的现有模型
//        System.out.println("模型："+DownloadUtil.available_models.entrySet().size());
//        DownloadUtil.available_models.entrySet().forEach(n->{
//            System.out.println(n.getKey()+ " => " +Arrays.toString(n.getValue().entrySet().toArray()));
//        });


        // 打印所有 tool 工具
        new opennlp.tools.cmdline.CLI().main(new String[]{});
        // 可以获取单个 tool 工具使用示例
        System.out.println(new TokenizerTrainerTool().getHelp());
        System.out.println(new TokenNameFinderTrainerTool().getHelp());

    }


}
