package org.tyf;


import opennlp.tools.cmdline.langdetect.LanguageDetectorTrainerTool;

/**
 *   @desc : LanguageDetectorTrainer, 用于可学习语言检测器的训练器
 *   @auth : tyf
 *   @date : 2022-03-18  12:06:58
*/
public class ApacheNLPLanguageDetectorTrainer {
    
    //  用于可学习语言检测器的训练器

    public static void main(String[] args) {


        // 样本如:
        // 每行一个文档，包含由选项卡分隔的ISO-639-3语言代码和文本，也就是 语言代码 + tab + 文本
        // spa     A la fecha tres calles bonaerenses recuerdan su nombre (en Ituzaingó, Merlo y Campana). A la fecha
        // deu     Alle Jahre wieder: Millionen Spanier haben am Dienstag die Auslosung in der größten Lotterie der Welt verfolgt.

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


        String model = "C:\\Users\\tyf\\Desktop\\model.bin";


        String data = "C:\\Users\\tyf\\Desktop\\data.txt";

        // 可以使用Maxent、Perceptron或Naive Bayes算法来训练模型
        LanguageDetectorTrainerTool trainer = new LanguageDetectorTrainerTool();

        System.out.println("Help：");
        System.out.println(trainer.getHelp());


        trainer.run(null,new String[]{
                "-data",data,
                "-model",model,
        });


    }


}
