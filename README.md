# algorithm



Java 常用算法用例。
傅里叶变换、PID、卡尔曼滤波、分类、回归、聚类、频繁挖掘 、推荐、搜索、nlp(分词、提词、相关性)等任务。
以及 apache-math3、Weka、Spark-ML、Mahout、Smile、apache-OpenNlp 等算法工具库的使用。


|序号|测试类|说明|
|:---------|:---------|:------------------------------------------------------|
|1|ApacheNLPChunkerEvaluator|  ChunkerEvaluator, 使用参考数据测量分块器模型的性能|
|2|ApacheNLPChunkerME|  ChunkerME, 可学习的分块器|
|3|ApacheNLPChunkerTrainerME|  ChunkerTrainerME, 用于可学习分块器的训练器|
|4|ApacheNLPDoccat|  Doccat, 可学习的文档分类器|
|5|ApacheNLPDoccatEvaluator|  DoccatEvaluator, 使用参考数据测量 Doccat 模型的性能|
|6|ApacheNLPDoccatTrainer|  DoccatTrainer, 用于可学习文档分类器的训练器|
|7|ApacheNLPLanguageDetector|  LanguageDetector, 可学习的语言检测器|
|8|ApacheNLPLanguageDetectorEvaluator|  LanguageDetectorEvaluator, 使用参考数据测量语言检测器模型的性能|
|9|ApacheNLPLanguageDetectorTrainer|  LanguageDetectorTrainer, 用于可学习语言检测器的训练器|
|10|ApacheNLPLemmatizerEvaluator|  LemmatizerEvaluator, 使用参考数据测量词形还原器模型的性能|
|11|ApacheNLPLemmatizerME|  LemmatizerME, 可学习的词形还原器|
|12|ApacheNLPLemmatizerTrainerME|  LemmatizerTrainerME, 用于可学习词形还原器的训练器|
|13|ApacheNLPParser|  Parser, 执行完整的句法分析|
|14|ApacheNLPPOSTagger|  POSTagger, 可学习的词性标注器|
|15|ApacheNLPPOSTaggerCrossValidator|  POSTaggerCrossValidator, 可学习词性标注器的 K 折交叉验证器|
|16|ApacheNLPPOSTaggerEvaluator|  POSTaggerEvaluator, 使用参考数据测量词性标注器模型的性能|
|17|ApacheNLPPOSTaggerTrainer|  POSTaggerTrainer, 用于可学习词性标注器的训练器|
|18|ApacheNLPSentenceDetector|  SentenceDetector, 可学习的句子检测器|
|19|ApacheNLPSentenceDetectorEvaluator|  SentenceDetectorEvaluator, 测量可学习句子检测器的性能|
|20|ApacheNLPSentenceDetectorTrainer|  SentenceDetectorTrainer, 用于可学习句子检测器的训练器|
|21|ApacheNLPTokenizerME|  TokenizerME, 可学习的分词器|
|22|ApacheNLPTokenizerMEEvaluator|  TokenizerMEEvaluator, 测量可学习分词器的性能|
|23|ApacheNLPTokenizerModel|  使用 TokenizerModel 进行推理|
|24|ApacheNLPTokenizerTrainer|  TokenizerTrainer, 用于可学习分词器的训练器|
|25|ApacheNLPTokenNameFinder|  TokenNameFinder, 可学习的名称实体识别器|
|26|ApacheNLPTokenNameFinderConverter|  TokenNameFinderConverter, 将外部数据格式（如 evalita、ad、conll03、bionlp2004、conll02、masc、muc6、ontonotes、brat）转换为原生 OpenNLP 格式|
|27|ApacheNLPTokenNameFinderCrossValidator|  TokenNameFinderCrossValidator, 可学习名称实体识别器的 K 折交叉验证器|
|28|ApacheNLPTokenNameFinderEvaluator|  TokenNameFinderEvaluator, 使用参考数据测量名称实体识别器模型的性能|
|29|ApacheNLPTokenNameFinderTrainer|  TokenNameFinderTrainer, 用于可学习名称实体识别器的训练器|
|30|ApacheNLPUsage|  apache-nlp 打印支持的所有工具|
|31|BinarySearch|  二分查找算法|
|32|FFT| 快速傅里叶变换|
|33|FFTLearn| 快速傅里叶变幻|
|34|KalmanFilterLevel1|  实现一阶卡尔曼滤波|
|35|KalmanFilterLevel2|  实现二阶卡尔曼滤波|
|36|MahoutCFTaste|  mahout cf协同过滤|
|37|Math3OLSRegressionLevel1| apache-math3 时序数据最小二乘线性回归|
|38|Math3OLSRegressionLevel3|  apache-math3 多元最小二乘线性回归|
|39|Math3ParametricUnivariateFunctionDemo|  apache-math3 自定义一元多项式目标函数拟合|
|40|Math3SimpleRegression| apache-math3 简单线性回归|
|41|MiniPID|  实现 PID 控制算法|
|42|MovingAverage| 滑动平均|
|43|QuickSort| 快速排序|
|44|SignafloSimpleArima| signaflo 自回归积分滑动平均模型  Signaflo库是专门用于时序数据分析的库|
|45|SmileAdaBoost|  Smile AdaBoost 集成学习|
|46|SmileAR|  Smile AR 自回归模型|
|47|SmileARM|  Smile ARM 关联挖掘|
|48|SmileARMA|  Smile ARMA 自回归滑动平均|
|49|SmileBigram|  Smile Bigram 二元组|
|50|SmileBKTree|  Smile BKTree BK树 临近搜索|
|51|SmileBM25|  Smile BM25 BM25相关性评分|
|52|SmileBoxTest|  Smile BoxTest 盒式检验|
|53|SmileCLARANS|  Smile CLARANS 聚类|
|54|SmileCoverTree|  Smile CoverTree 覆盖树 临近搜索|
|55|SmileDBSCAN|  Smile DBSCAN 聚类|
|56|SmileDecisionTree|  Smile DecisionTree 决策树|
|57|SmileDENCLUE|  Smile DENCLUE 聚类|
|58|SmileDeterministicAnnealing|  Smile DeterministicAnnealing 确定性退火聚类|
|59|SmileDiscreteNaiveBayes|  Smile DiscreteNaiveBayes 朴素贝叶斯文档分类器|
|60|SmileElasticNetReg|  Smile ElasticNetReg ElasticNet回归|
|61|SmileFLD|  Smile FLD Fisher判别分析|
|62|SmileFPGrowth|  Smile FPGrowth 频繁挖掘|
|63|SmileGaussianProcessRegressionReg|  Smile GaussianProcessRegressionReg 高斯过程回归|
|64|SmileGLMReg|  Smile GLMReg 广义线性模型回归|
|65|SmileGMeans|  Smile GMeans 聚类|
|66|SmileGradientTreeBoost|  Smile GradientTreeBoost 梯度提升|
|67|SmileGradientTreeBoostReg|  Smile GradientTreeBoostReg 梯度提升树回归|
|68|SmileHierarchicalClustering|  Smile HierarchicalClustering 层次聚类|
|69|SmileHMM|  Smile HMM 隐马尔可夫模型|
|70|SmileHMMPOSTagger|  Smile HMMPOSTagger 隐马尔可夫模型词性标注器|
|71|SmileICA|  Smile ICA 独立主成分分析|
|72|SmileIsoMap|  Smile IsoMap 等距映射|
|73|SmileIsotonicMDS|  Smile IsotonicMDS 保序多维尺度分析|
|74|SmileKDTree|  Smile KDTree k-d树 临近搜索|
|75|SmileKernelPCA|  Smile KernelPCA 主成分分析|
|76|SmileKMeans|  Smile KMeans 聚类|
|77|SmileKModes|  Smile KModes 聚类|
|78|SmileLancasterStemmer|  Smile LancasterStemmer 兰卡斯特词干提取器|
|79|SmileLaplacianEigenmap|  Smile LaplacianEigenmap 拉普拉斯特征映射|
|80|SmileLASSOReg|  Smile LASSOReg lasso回归|
|81|SmileLDA|  Smile LDA 线性判别分析|
|82|SmileLinearSearch|  Smile LinearSearch 线性搜索 临近搜索|
|83|SmileLLE|  Smile LLE 局部线性嵌入|
|84|SmileLogisticRegression|  Smile LogisticRegression 逻辑回归|
|85|SmileLSH|  Smile LSH 局部敏感哈希 临近搜索|
|86|SmileMaxent|  Smile Maxent 最大熵分类器|
|87|SmileMDS|  Smile MDS 多维尺度分析|
|88|SmileMEC|  Smile MEC 最小熵聚类|
|89|SmileMLP|  Smile MLP 神经网络|
|90|SmileMLPReg|  Smile MLPReg 神经网络回归|
|91|SmileMPLSH|  Smile MPLSH 多平面局部敏感哈希 临近搜索|
|92|SmileNaiveBayes|  Smile NaiveBayes 通用朴素贝叶斯分类器|
|93|SmileNGram|  Smile NGram N元组|
|94|SmileOLSReg|  Smile OLSReg 线性回归|
|95|SmileOneVersusOne|  Smile OneVersusOne 1v1分类器|
|96|SmileOneVersusRest|  Smile OneVersusRest 1vAll分类器|
|97|SmilePCA|  Smile PCA 主成分分析|
|98|SmilePlattScaling|  Smile PlattScaling Platt缩放|
|99|SmilePorterStemmer|  Smile PorterStemmer 波特词干提取器|
|100|SmileProbabilisticPCA|  Smile ProbabilisticPCA 主成分分析|
|101|SmileQDA|  Smile QDA 二次判别分析|
|102|SmileRandomForest|  Smile RandomForest 随机森林|
|103|SmileRandomForestReg|  Smile RandomForestReg 随机森林回归|
|104|SmileRandomProjection|  Smile RandomProjection 随机投影|
|105|SmileRBFNetwork|  Smile RBFNetwork RBF网络|
|106|SmileRBFNetworkReg|  Smile RBFNetworkReg RBF网络回归|
|107|SmileRDA|  Smile RDA 正则判别分析|
|108|SmileRegressionTreeReg|  Smile RegressionTreeReg 回归树|
|109|SmileRidgeRegressionReg|  Smile RidgeRegressionReg 岭回归|
|110|SmileSammonMapping|  Smile SammonMapping Sammon映射|
|111|SmileSIB|  Smile SIB 顺序信息瓶颈聚类|
|112|SmileSimpleSentenceSplitter|  Smile SimpleSentenceSplitter 句子拆分|
|113|SmileSimpleTokenizer|  Smile SimpleTokenizer 分词|
|114|SmileSpectralClustering|  Smile SpectralClustering 谱聚类|
|115|SmileSVM|  Smile SVM 支持向量机|
|116|SmileTSNE|  Smile TSNE t分布随机邻域嵌入|
|117|SmileUMAP|  Smile UMAP 均匀流形逼近投影|
|118|SmileWord2Vec|  Smile Word2Vec 词嵌入|
|119|SmileXMeans|  Smile XMeans 聚类|
|120|SparkAFTSurvivalRegression|  spark 加速失效时间（AFT）生存回归|
|121|SparkALS|  spark 交替最小二乘 协同过滤推荐算法 ALS|
|122|SparkALS2|  spark 交替最小二乘 协同过滤推荐算法 ALS|
|123|SparkArima| spark 时序数据预测模型 自回归移动平均模型|
|124|SparkBinarizer|  spark SparkBinarizer 特征二值化|
|125|SparkBisectingKMeans|  spark 二分K均值算法|
|126|SparkChiSqSelector|  spark 卡方特征选择|
|127|SparkChiSquareTest|  spark 卡方检验|
|128|SparkCorrelation|  spark 相关性计算|
|129|SparkCountVectorizer|  spark 特征工程 文本词频特征提取|
|130|SparkDCT|  spark DCT 离散余弦变换|
|131|SparkDecisionTree|  spark 决策树|
|132|SparkDecisionTreeRegressor|  spark 决策树回归|
|133|SparkElementwiseProduct|  spark 特征转换 乘积转换|
|134|SparkFMClassifier|  spark 因子分解机|
|135|SparkFMRegressor|  spark FM回归|
|136|SparkFPGrowth|  spark 模式挖掘 FPGrowth|
|137|SparkGaussianMixture|  spark GMM 高斯混合聚类|
|138|SparkGBTClassifier|  spark 随机梯度提升树 GBT|
|139|SparkGBTRegressor|  spark 随机梯度提升树 GBT|
|140|SparkGeneralizedLinearRegression|  spark 广义线性回归|
|141|SparkIDF|  spark F-IDF特征提取|
|142|SparkIsotonicRegression|  spark 保序回归（单调回归）分析|
|143|SparkKmeans|  spark K均值算法|
|144|SparkLDA|  spark 主题模型 隐含狄利克雷分布（LDA）|
|145|SparkLinearRegression|  spark 线性回归|
|146|SparkLinearSVC|  spark LinearSVC 线性SVM进行二分类|
|147|SparkLogisticRegression|  spark 逻辑回归|
|148|SparkMaxAbsScaler|  spark 特征工程 特征缩放|
|149|SparkMinHashLSH|  spark 局部敏感哈希 临近搜索|
|150|SparkMinMaxScaler|  spark 特征工程 特征缩放|
|151|SparkMultilayerPerceptronClassifier|  spark 多层感知机|
|152|SparkNaiveBayes|  spark 朴素贝叶斯 分类器|
|153|SparkOneHotEncoder|  spark 独热编码|
|154|SparkOneVsRest|  spark One-vs-Rest 分类|
|155|SparkPCA_SVD|  spark 主成分分析|
|156|SparkPowerIterationClustering|  spark 幂迭代聚类 (PIC)|
|157|SparkPrefixSpan|  spark 时序数据频繁挖掘|
|158|SparkRandomForest|  spark 随机森林|
|159|SparkRandomForestRegressor|  spark 随机森林回归|
|160|SparkWord2Vec|  spark Word2Vec 词嵌入|
|161|SprakBucketedRandomProjectionLSH|  spark 特征工程局部敏感哈希LSH|
|162|SprakBucketizer|  spark 特征工程 特征离散化工具|
|163|WekaAdaBoostM1|  weka AdaBoostM1AdaBoost变体|
|164|WekaAdditiveRegression|  weka AdditiveRegression加法回归|
|165|WekaApriori|  weka Apriori关联规则挖掘|
|166|WekaAttributeSelectedClassifier|  weka AttributeSelectedClassifier使用特征选择的分类器|
|167|WekaBagging|  weka BaggingBagging集成学习|
|168|WekaClassificationViaRegression|  weka ClassificationViaRegression通过回归进行分类|
|169|WekaDecisionTable|  weka DecisionTable决策表模型|
|170|WekaDecisionTree|  weka j48决策树分类|
|171|WekaEM|  weka EM期望最大化聚类。|
|172|WekaFarthestFirst|  weka FarthestFirst初始质心选择聚类。|
|173|WekaFilteredClassifier|  weka FilteredClassifier使用过滤器进行分类。|
|174|WekaFPGrowth|  weka FPGrowth频繁挖掘|
|175|WekaGaussianProcesses|  weka GaussianProcesses高斯过程回归|
|176|WekaHierarchicalClusterer|  weka HierarchicalClusterer层次聚类|
|177|WekaHoeffdingTree|  weka HoeffdingTree霍夫丁树分类器|
|178|WekaIBk|  weka kNN 算法。|
|179|WekaJRip|  weka JRip基于规则的归纳分类算法|
|180|WekaKStar|  weka KStarKStar算法。|
|181|WekaLinearRegression|  weka 线性回归|
|182|WekaLMT|  weka LMTLMT树分类器|
|183|WekaLogistic|  weka Logistic逻辑回归|
|184|WekaLogitBoost|  weka LogitBoost 算法， Boosting 算法的变体。|
|185|WekaLWL|  weka LWL局部加权学习。|
|186|WekaM5P|  weka M5PM5P树回归|
|187|WekaMakeDensityBasedClusterer|  weka MakeDensityBasedClusterer密度的聚类|
|188|WekaMultiClassClassifier|  weka MultiClassClassifier多类别分类器|
|189|WekaMultilayerPerceptron|  weka MultilayerPerceptron多层感知器神经网络分类器|
|190|WekaMultiScheme|  weka MultiScheme使用多个分类器。|
|191|WekaNaiveBayesMultinomialUpdateable|  weka NaiveBayesMultinomialUpdateable可更新的多项式朴素贝叶斯分类器|
|192|WekaNaiveBayesUpdateable|  weka NaiveBayesUpdateable可更新的贝叶斯分类器|
|193|WekaNativeBayes|  weka NativeBayes朴素贝叶斯分类器|
|194|WekaNativeBayesMultinomial|  weka NativeBayesMultinomial多项式朴素贝叶斯分类器|
|195|WekaRandomCommittee|  weka RandomCommittee随机委员会算法|
|196|WekaRandomForest|  weka RandomForest随机森林|
|197|WekaRandomSubSpace|  weka RandomSubSpace随机子空间算法|
|198|WekaRandomTree|  weka RandomTree随机特征选择生成的决策树|
|199|WekaRegressionByDiscretization|  weka RegressionByDiscretization回归问题离散化为分类|
|200|WekaREPTree|  weka REPTreeREPTree决策树|
|201|WekaSimpleKMeans|  weka SimpleKMeansKMeans 聚类|
|202|WekaSimpleLogistic|  weka SimpleLogistic逻辑回归分类|
|203|WekaSMO|  weka SMO序列最小优化算法支持向量机|
|204|WekaSMOreg|  weka SMOreg序列最小优化算法支持向量机 回归任务|
|205|WekaStacking|  weka Stacking堆叠多个分类器进行学习|
|206|WekaVote|  weka Vote投票算法|
|207|WekaVotedPerceptron|  weka VotedPerceptron加权感知器分类器。|
