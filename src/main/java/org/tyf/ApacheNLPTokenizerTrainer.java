package org.tyf;

import opennlp.tools.cmdline.tokenizer.TokenizerTrainerTool;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import java.io.File;
import java.util.Arrays;


/**
 *   @desc : TokenizerTrainer, 用于可学习分词器的训练器
 *   @auth : tyf
 *   @date : 2022-03-18  12:06:58
*/
public class ApacheNLPTokenizerTrainer {
    
    //  用于可学习分词器的训练器

    public static void main(String[] args) throws Exception{

        // 模型保存路径
        String module = "C:\\Users\\tyf\\Desktop\\custom.bin";
        // 训练样本、一个句子用 <SPLIT> 隔开、句子内的单词用空格隔开：
        // 我 爱 祖国 ， 因为 祖国 是 我 生长 的 地方 ， 是 我 的 家园 。<SPLIT>在 这 片 神奇 的 土地 上 ， 有 着 五彩 缤纷 的 文化 ， 有 着 壮美 的 山川 河流 ， 有 着 广袤 的 草原 森林 ， 还有 灿烂 的 历史 文明 。<SPLIT>我 爱 祖国 的 大好 河山 ， 爱 祖国 的 民族 文化 ， 更 爱 祖国 的 人民 。<SPLIT>祖国 的 培养 ， 是 我们 无比 自豪 的 荣耀 。
        String data = "C:\\Users\\tyf\\Desktop\\data.txt";


        // 使用 CLI 工具进行训练，底层也是调用 TokenizerME 训练的
        TokenizerTrainerTool trainer = new TokenizerTrainerTool();
        // 设置训练参数
        String[] trainArgs = {
                "-encoding","UTF-8",
                "-lang","ch",
                "-model", module,
                "-data", data,
        };
        trainer.run(null,trainArgs);


        // 加载模型推理
        TokenizerModel m = new TokenizerModel(new File(module));
        Tokenizer tokenizer = new TokenizerME(m);
        String text = "因为祖国是我生长的地方";
        String[] out = tokenizer.tokenize(text);
        System.out.println(Arrays.toString(out));


    }


}
