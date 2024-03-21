package org.tyf;


import opennlp.tools.cmdline.parser.ParserModelLoader;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;

import java.io.File;

/**
 *   @desc : Parser, 执行完整的句法分析
 *   @auth : tyf
 *   @date : 2022-03-18  12:06:58
*/
public class ApacheNLPParser {
    
    //  执行完整的句法分析
    // https://opennlp.apache.org/docs/2.3.2/manual/opennlp.html#tools.parser


    public static void main(String[] args) {

        String model = "en-parser-chunking.bin";

//        ParserTool parser = new ParserTool();
//        System.out.println("Help：");
//        System.out.println(parser.getHelp());
//        parser.run(new String[]{});

        ParserModel mm = new ParserModelLoader().load(new File(model));
        Parser parser = ParserFactory.create(mm);

        // 解析语法树
        String sentence = "The quick brown fox jumps over the lazy dog .";
        Parse topParses[] = ParserTool.parseLine(sentence, parser, 1);

    }


}
