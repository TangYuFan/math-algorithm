package org.tyf;

import weka.associations.AssociationRule;
import weka.associations.BinaryItem;
import weka.associations.FPGrowth;
import weka.core.*;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;

/**
 *   @desc : weka FPGrowth频繁挖掘
 *   @auth : tyf
 *   @date : 2022-03-04  17:17:22
*/
public class WekaFPGrowth {

    // FPGrowth 高效的频繁模式挖掘算法，通过构建 FP 树实现频繁项集的发现。


    // 生成随机训练样本
    public static Instances generateArff(String arff) throws Exception{


        // 每个商品类别的取值范围，取值范围第一个值虚拟字符串这是为了解决官方的一个bug
        // 警告：从具有字符串属性的数据集中保存 SparseInstance 对象时存在一个已知问题。
        // 在 Weka 中，字符串和标称数据值存储为数字；这些数字充当可能属性值数组的索引（这是非常有效的）。
        // 但是，第一个字符串值被分配索引 0：这意味着，在内部，该值存储为 0。当写入 SparseInstance 时，
        // 不会输出内部值为 0 的字符串实例，因此它们的字符串值会丢失（并且当再次读取arff文件，默认值0是不同字符串值的索引，因此属性值出现变化）。
        // 要解决此问题，请在索引 0 处添加一个虚拟字符串值，每当您声明可能在 SparseInstance 对象中使用并保存为 Sparse ARFF 文件的字符串属性时，
        // 都不会使用该虚拟字符串值。

        // 属性名称
        List<String> goodsName = Arrays.asList(
                "Apple",
                "Book",
                "Chair",
                "Shoes",
                "Tablet",
                "Lamp",
                "Shirt Large",
                "Shirt Small",
                "Shirt Medium",
                "Backpack",
                "Watch",
                "Pen",
                "Desk",
                "Trousers",
                "Glasses",
                "Hat",
                "Jacket L",
                "Jacket X",
                "Jacket XL",
                "Jacket XXL",
                "Umbrella",
                "Ring",
                "Perfume",
                "Gloves"
        );


        // 属性取值范围
        // 每个属性的取值范围中索引为0的值会因为bug设置失败
        // 所以这里将F(表示不存在于购物车中)放到索引0的位置,设置F失败这样构造稀疏矩阵只保存T的列
        // 另外 FPGrowth 算法只能处理有2个取值的标称类型
        List<List<String>> goodsValue = Arrays.asList(
                Arrays.asList("F","T"),
                Arrays.asList("F","T"),
                Arrays.asList("F","T"),
                Arrays.asList("F","T"),
                Arrays.asList("F","T"),
                Arrays.asList("F","T"),
                Arrays.asList("F","T"),
                Arrays.asList("F","T"),
                Arrays.asList("F","T"),
                Arrays.asList("F","T"),
                Arrays.asList("F","T"),
                Arrays.asList("F","T"),
                Arrays.asList("F","T"),
                Arrays.asList("F","T"),
                Arrays.asList("F","T"),
                Arrays.asList("F","T"),
                Arrays.asList("F","T"),
                Arrays.asList("F","T"),
                Arrays.asList("F","T"),
                Arrays.asList("F","T"),
                Arrays.asList("F","T"),
                Arrays.asList("F","T"),
                Arrays.asList("F","T"),
                Arrays.asList("F","T")
        );

        // 属性集合
        ArrayList<Attribute> artributes = new ArrayList<>();
        for (int i = 0; i < goodsName.size(); i++) {
            artributes.add(new Attribute(goodsName.get(i),goodsValue.get(i)));
        }


        // 创建购物车数据集
        int num = 100;
        Instances dataset = new Instances("Dataset", artributes, num);

        // 随机将商品加入购物车
        Random r = new Random();

        for (int i = 0; i < num; i++) {

            // 一个购物车样本 sparseInstance 是稀疏数据类、创建一个并加入数据集
            SparseInstance shopping = new SparseInstance(goodsName.size());
            shopping.setDataset(dataset);

            // 从1开始随机设置商品的属性
            for (int j = 0; j < goodsName.size(); j++) {
                // 当前商品的取值范围
                List<String> attrValue = goodsValue.get(j);
                // 如果取到 F(索引为0的取值) 因为bug会设置失败就相当于不存在于购物车中、取到T说明在购物车中
                String str = attrValue.get(r.nextInt(attrValue.size()));
                shopping.setValue(j,str);
            }
            dataset.add(shopping);
        }
        // 保存到本地
        ArffSaver arffSaver = new ArffSaver();
        arffSaver.setInstances(dataset);
        arffSaver.setFile(new File(arff));
        arffSaver.writeBatch();

        return dataset;
    }

    // weka官方超市购物篮数据 商品1、商品2、...、商品n、总价
    // 商品是单个枚举值 t 表示存在、总价是两个枚举值 low,high
    // arff 是存储的稀疏数据,如果不存在的商品直接 ?
    public static Instances readArff(String path) throws Exception{
        ConverterUtils.DataSource source = new ConverterUtils.DataSource(path);
        Instances data = source.getDataSet();
        return data;
    }

    public static void main(String[] args) throws Exception{


        // 生成随机训练样本
        Instances data = generateArff("C:\\Users\\tyf\\Downloads\\data.arff");

        // 使用官方超市数据
//        Instances data = readArff("C:\\Users\\tyf\\Downloads\\supermarket.arff");

        // 打印数据
//        System.out.println(data);

        // 设置支持度的阈值，可以根据需要调整
        double minSup = 0.1;
        // 设置要找到的规则数、数据量越大隐含的规则越多
        int numRules = 100000;

        FPGrowth fpGrowth = new FPGrowth();
        fpGrowth.setLowerBoundMinSupport(minSup);
        fpGrowth.setNumRulesToFind(numRules);
        fpGrowth.setMinMetric(0);

        // 频繁挖掘
        fpGrowth.buildAssociations(data);

        System.out.println("规则个数："+fpGrowth.getAssociationRules().getRules().size());

        // 封闭模式(避免了相同支持度的冗余模式，保留了更精炼的频繁项集)
        ArrayList<Collection<BinaryItem>> cPattern = new ArrayList<>();

        // 最大模式(避免了包含关系的冗余模式，提供了更紧凑的频繁项集)
        ArrayList<Collection<BinaryItem>> mPattern = new ArrayList<>();

        // 提取频繁模式、每个 Item 就是一个商品
        ArrayList<Collection<BinaryItem>> frequentPatterns = new ArrayList<>();
        ArrayList<Double> supports = new ArrayList<>();

        // 所有规则
        List<AssociationRule> rules = fpGrowth.getAssociationRules().getRules();

        // 遍历所有规则查找
        for (int i = 0; i < rules.size(); i++) {
            AssociationRule AR = rules.get(i);
            Collection premise = AR.getPremise();
            int premiseSupport = AR.getPremiseSupport();
            Collection consequence = AR.getConsequence();
            int consequenceSupport = AR.getConsequenceSupport();
            int totalSupport = AR.getTotalSupport();
            Collection<BinaryItem> baseFrequentPattern = new HashSet<BinaryItem>();
            Iterator iterator = premise.iterator();
            while (iterator.hasNext()) {
                baseFrequentPattern.add((BinaryItem) iterator.next());
            }
            iterator = consequence.iterator();
            while (iterator.hasNext()) {
                baseFrequentPattern.add((BinaryItem) iterator.next());
            }
            if (!frequentPatterns.contains(baseFrequentPattern)) {
                frequentPatterns.add(baseFrequentPattern);
                supports.add((double) totalSupport / data.size());
            }
        }

        for (int i = 0; i < frequentPatterns.size(); i++) {
            Collection<BinaryItem> base = frequentPatterns.get(i);
            boolean closed = true;
            for (int j = 0; j < frequentPatterns.size(); j++) {

                if (frequentPatterns.get(j).equals(base)) {
                    continue;
                } else {
                    if (checkSupport(base, frequentPatterns.get(j))) {
                        if (supports.get(i) <= supports.get(j)) {
                            closed = false;
                            break;
                        }
                    }
                }
            }
            if (closed) {
                cPattern.add(base);
            }

        }

        for (int i = 0; i < frequentPatterns.size(); i++) {
            Collection<BinaryItem> base = frequentPatterns.get(i);
            boolean max = true;
            for (int j = 0; j < frequentPatterns.size(); j++) {
                if (frequentPatterns.get(j).equals(base)) {
                    continue;
                } else {
                    if (checkSupport(base, frequentPatterns.get(j))) {
                        if (supports.get(j) >= minSup) {
                            max = false;
                            break;
                        }
                    }
                }
            }
            if (max) {
                mPattern.add(base);
            }
        }

        System.out.println("--------------------");
        for (int i = 0; i < cPattern.size(); i++) {
            System.out.println("closedPatterns:"+cPattern.get(i).toString());
        }
        System.out.println("--------------------");
        for (int i = 0; i < mPattern.size(); i++) {
            System.out.println("maxPatterns:"+mPattern.get(i).toString());
        }

    }

    public static boolean checkSupport(Collection<BinaryItem> base, Collection<BinaryItem> super_s) {
        for (BinaryItem b : base) {
            if (!super_s.contains(b)) return false;
        }
        return true;
    }

}

