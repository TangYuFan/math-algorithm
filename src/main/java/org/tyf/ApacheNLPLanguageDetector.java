package org.tyf;


import opennlp.tools.langdetect.Language;
import opennlp.tools.langdetect.LanguageDetector;
import opennlp.tools.langdetect.LanguageDetectorME;
import opennlp.tools.langdetect.LanguageDetectorModel;

import java.io.File;
import java.util.Arrays;

/**
 *   @desc : LanguageDetector, 可学习的语言检测器
 *   @auth : tyf
 *   @date : 2022-03-18  12:06:58
*/
public class ApacheNLPLanguageDetector {
    
    //  可学习的语言检测器

    public static void main(String[] args) throws Exception{


        String module = "C:\\Users\\tyf\\Downloads\\langdetect-183.bin";

        // 检测器
        LanguageDetectorModel m = new LanguageDetectorModel(new File(module));
        LanguageDetector detector = new LanguageDetectorME(m);

        // API或CLI都会考虑完整的文本来选择最可能的语言。为了处理混合语言，可以分析较小的文本块以找到语言区域。
        //        detector.predictLanguage("");
        Language[] languages = detector.predictLanguages("OpenNLP有一个命令行工具，用于训练各种语料库上模型下载页面上可用的模型。数据可以转换为OpenNLP Tokenizer训练格式，也可以直接使用。OpenNLP格式每行包含一个句子。标记要么用空白隔开，要么用一个特殊的＜SPLIT＞标记隔开。标记在空白处自动拆分，并且训练文本中必须至少存在一个<split>标记。以下示例以正确的格式显示了上面的示例。");

        // 语言短语
        // afr	Afrikaans
        // ara	Arabic
        // ast	Asturian
        // aze	Azerbaijani
        // bak	Bashkir
        // bel	Belarusian
        // ben	Bengali
        // bos	Bosnian
        // bre	Breton
        // bul	Bulgarian
        // cat	Catalan
        // ceb	Cebuano
        // ces	Czech
        // che	Chechen
        // cmn	Mandarin Chinese
        // cym	Welsh
        // dan	Danish
        // deu	German
        // ekk	Standard Estonian
        // ell	Greek, Modern
        // eng	English
        // epo	Esperanto
        // est	Estonian
        // eus	Basque
        // fao	Faroese
        // fas	Persian
        // fin	Finnish
        // fra	French
        // fry	Western Frisian
        // gle	Irish
        // glg	Galician
        // gsw	Swiss German
        // guj	Gujarati
        // heb	Hebrew
        // hin	Hindi
        // hrv	Croatian
        // hun	Hungarian
        // hye	Armenian
        // ind	Indonesian
        // isl	Icelandic
        // ita	Italian
        // jav	Javanese
        // jpn	Japanese
        // kan	Kannada
        // kat	Georgian
        // kaz	Kazakh
        // kir	Kirghiz
        // kor	Korean
        // lat	Latin
        // lav	Latvian
        // lim	Limburgan
        // lit	Lithuanian
        // ltz	Luxembourgish
        // lvs	Standard Latvian
        // mal	Malayalam
        // mar	Marathi
        // min	Minangkabau
        // mkd	Macedonian
        // mlt	Maltese
        // mon	Mongolian
        // mri	Maori
        // msa	Malay
        // nan	Min Nan Chinese
        // nds	Low German
        // nep	Nepali
        // nld	Dutch
        // nno	Norwegian Nynorsk
        // nob	Norwegian Bokmål
        // oci	Occitan
        // pan	Panjabi
        // pes	Iranian Persian
        // plt	Plateau Malagasy
        // pnb	Western Panjabi
        // pol	Polish
        // por	Portuguese
        // pus	Pushto
        // ron	Romanian
        // rus	Russian
        // san	Sanskrit
        // sin	Sinhala
        // slk	Slovak
        // slv	Slovenian
        // som	Somali
        // spa	Spanish
        // sqi	Albanian
        // srp	Serbian
        // sun	Sundanese
        // swa	Swahili
        // swe	Swedish
        // tam	Tamil
        // tat	Tatar
        // tel	Telugu
        // tgk	Tajik
        // tgl	Tagalog
        // tha	Thai
        // tur	Turkish
        // ukr	Ukrainian
        // urd	Urdu
        // uzb	Uzbek
        // vie	Vietnamese
        // vol	Volapük
        // war	Waray
        // zul	Zulu

        // 按照置信度排序
        Arrays.stream(languages).forEach(n->{
            System.out.println("Lang："+n.getLang()+"，Confidence"+n.getConfidence());
        });

    }


}
