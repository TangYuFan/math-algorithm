package org.tyf;


import opennlp.tools.chunker.*;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 *   @desc : ChunkerTrainerME, 用于可学习分块器的训练器
 *   @auth : tyf
 *   @date : 2022-03-18  12:06:58
*/
public class ApacheNLPChunkerTrainerME {
    
    //  用于可学习分块器的训练器

    public static void main(String[] args) throws Exception{


        // 样本
        // He        PRP  B-NP
        // reckons   VBZ  B-VP
        // the       DT   B-NP
        // current   JJ   I-NP
        // account   NN   I-NP
        // deficit   NN   I-NP
        // will      MD   B-VP
        // narrow    VB   I-VP
        // to        TO   B-PP
        // only      RB   B-NP
        // #         #    I-NP
        // 1.8       CD   I-NP
        // billion   CD   I-NP
        // in        IN   B-PP
        // September NNP  B-NP
        // .         .    O


        String data = "";
        String model = "";

        ObjectStream<String> lineStream = new PlainTextByLineStream(new MarkableFileInputStreamFactory(new File(data)), StandardCharsets.UTF_8);

        ObjectStream<ChunkSample> sampleStream = new ChunkSampleStream(lineStream);
        ChunkerModel mm = ChunkerME.train("eng", sampleStream, TrainingParameters.defaultParams(), new ChunkerFactory());


        // 序列化
        mm.serialize(new BufferedOutputStream(new FileOutputStream(model)));


    }


}
