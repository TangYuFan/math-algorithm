package org.tyf;


import opennlp.tools.tokenize.*;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 *   @desc : TokenizerME, 可学习的分词器
 *   @auth : tyf
 *   @date : 2022-03-18  12:06:58
*/
public class ApacheNLPTokenizerME {
    
    //  可学习的分词器

    // Usage:
    // $ opennlp TokenizerTrainer -model en-token.bin -alphaNumOpt true -lang en -data en-token.train -encoding UTF-8
    //
    // Indexing events with TwoPass using cutoff of 5
    //
    //	Computing event counts...  done. 45 events
    //	Indexing...  done.
    // Sorting and merging events... done. Reduced 45 events to 25.
    // Done indexing in 0,09 s.
    // Incorporating indexed data for training...
    // done.
    //	Number of Event Tokens: 25
    //	    Number of Outcomes: 2
    //	  Number of Predicates: 18
    // ...done.
    // Computing model parameters ...
    // Performing 100 iterations.
    //  1:  ... loglikelihood=-31.191623125197527	0.8222222222222222
    //  2:  ... loglikelihood=-21.036561339080343	0.8666666666666667
    //  3:  ... loglikelihood=-16.397882721809086	0.9333333333333333
    //  4:  ... loglikelihood=-13.624159882595462	0.9333333333333333
    //  5:  ... loglikelihood=-11.762067054883842	0.9777777777777777
    //
    // ...<skipping a bunch of iterations>...
    //
    // 95:  ... loglikelihood=-2.0234942537226366	1.0
    // 96:  ... loglikelihood=-2.0107265117555935	1.0
    // 97:  ... loglikelihood=-1.998139365828305	1.0
    // 98:  ... loglikelihood=-1.9857283791639697	1.0
    // 99:  ... loglikelihood=-1.9734892753591327	1.0
    // 100:  ... loglikelihood=-1.9614179307958106	1.0
    // Writing tokenizer model ... done (0,044s)
    //
    // Wrote tokenizer model to
    // Path: en-token.bin


    public static void main(String[] args) throws Exception{


        // 模型保存路径
        String module = "C:\\Users\\tyf\\Desktop\\custom.bin";
        // 训练样本、一个句子用 <SPLIT> 隔开、句子内的单词用空格隔开：
        // 我 爱 祖国 ， 因为 祖国 是 我 生长 的 地方 ， 是 我 的 家园 。<SPLIT>在 这 片 神奇 的 土地 上 ， 有 着 五彩 缤纷 的 文化 ， 有 着 壮美 的 山川 河流 ， 有 着 广袤 的 草原 森林 ， 还有 灿烂 的 历史 文明 。<SPLIT>我 爱 祖国 的 大好 河山 ， 爱 祖国 的 民族 文化 ， 更 爱 祖国 的 人民 。<SPLIT>祖国 的 培养 ， 是 我们 无比 自豪 的 荣耀 。
        String data = "C:\\Users\\tyf\\Desktop\\data.txt";

        ObjectStream<String> lineStream = new PlainTextByLineStream(
                new MarkableFileInputStreamFactory(new File(data)),
                StandardCharsets.UTF_8);

        ObjectStream<TokenSample> sampleStream = new TokenSampleStream(lineStream);


        // 模型训练
        TokenizerModel model;

        try {
            model = TokenizerME.train(sampleStream, TokenizerFactory.create(
                    null,
                    "cmn",
                    null,
                    true,
                    null
            ), TrainingParameters.defaultParams());
        }
        finally {
            sampleStream.close();
        }

        // 保存本地文件
        OutputStream modelOut = null;
        try {
            modelOut = new BufferedOutputStream(new FileOutputStream(module));
            model.serialize(modelOut);
        } finally {
            if (modelOut != null)
                modelOut.close();
        }


        // 加载模型推理
        TokenizerModel m = new TokenizerModel(new File(module));
        Tokenizer tokenizer = new TokenizerME(m);
        String text = "";
        String[] out = tokenizer.tokenize(text);
        System.out.println(Arrays.toString(out));

    }


}
