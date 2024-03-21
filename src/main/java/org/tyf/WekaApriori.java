package org.tyf;

import org.jetbrains.annotations.NotNull;
import weka.associations.Apriori;
import weka.associations.AssociationRule;
import weka.core.FastVector;
import weka.core.Instances;
import weka.core.SparseInstance;
import weka.core.converters.ArffLoader;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;
import weka.core.Attribute;

import java.io.*;
import java.util.Arrays;
import java.util.Random;


/**
 *   @desc : weka Apriori关联规则挖掘
 *   @auth : tyf
 *   @date : 2022-03-04  17:17:22
*/
public class WekaApriori {

    // Apriori 经典关联规则挖掘算法，基于先验性质。它用来找出数据值中对目标结果可能关联的规则。


    // 生成随机训练样本
    public static void generateCsv(String csv){

        // Apriori 算法只能对标称属性也就是枚举值进行分析,如果列中有数字需要进行离散化,下面演示网络流量攻击判别的样本的构造

        Random r = new Random();
        if(new File(csv).exists()){
//            new File(csv).delete();
            return;
        }
        // 写入到本地csv
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csv, true))) {

            // 第一行是title
            writer.write("duration,protocol_type,src_bytes,flag,land,num_failed_logins,root_shell,label");
            writer.newLine();

            for (int i = 0; i < 100000; i++) {

                // duration 时长,将 0~100 ms 离散为5个级别区间： d1、d2、d3、d4、d5
                String duration = Arrays.asList("d1","d2","d3","d4","d5").get(r.nextInt(5));
                // protocol_type 协议类型：tcp、udp、icmp、know
                String protocol_type = Arrays.asList("tcp","udp","icmp","know").get(r.nextInt(4));
                // src_bytes 数据帧,将字节数离散为5个级别区间：s1、s2、s3、s4、s5
                String src_bytes = Arrays.asList("s1","s2","s3","s4","s5").get(r.nextInt(5));
                // flag 连接状态：S0'（未同步）、'REJ'（拒绝）
                String flag = Arrays.asList("S0","REJ").get(r.nextInt(2));
                // land 是否是同一台主机上的连接：（'1'表示是，'0'表示否）
                String land = Arrays.asList("1","0").get(r.nextInt(2));
                // num_failed_logins: 登录失败的次数：
                String num_failed_logins = "num_failed_logins_" + r.nextInt(5);
                // root_shell: 是否成功获取了root权限：（'1'表示是，'0'表示否）
                String root_shell = "root_shell_" + r.nextInt(5);
                // label: 是否是网络攻击，可能是 'normal'（正常）或 'anomaly'（异常）
                String label = Arrays.asList("normal","anomaly").get(r.nextInt(2));

                String line = duration+","+protocol_type+","+src_bytes+","+flag+","+land+","+num_failed_logins+","+root_shell+","+label;
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws Exception{

        // 样本路径
        String csv = "C:\\Users\\tyf\\Desktop\\data.csv";

        // 生成随机训练样本
        generateCsv(csv);

        // 读取数据
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File(csv));

        // 生成随机训练样本
        Instances data = loader.getDataSet();

        // 最后一列是挖掘的目标
        data.setClassIndex(data.numAttributes()-1);

        // 样本离散化
        NumericToNominal n2n = new NumericToNominal();
        n2n.setInputFormat(data);
        Instances newData = Filter.useFilter(data, n2n);

        // 打印样本
        for (int i = 0; i < newData.size(); i++) {
            System.out.println("样本："+newData.get(i));
        }

        // 参数设置
        // -N <要输出的规则数目>：规则的数量，即希望挖掘得到的关联规则的数量，默认为10。
        // -T <0=置信度 | 1=提升度 | 2=支持度增益 | 3=确信度>：规则排序的度量类型，可以选择置信度（confidence）、提升度（lift）、支持度增益（leverage）或确信度（conviction），默认为置信度。
        // -C <规则的最小度量分数>：规则的最小置信度，只有置信度大于等于此值的规则会被输出，默认为0.9。
        // -D <最小支持度的递减值>：每次迭代中最小支持度的递减值，默认为0.05。
        // -U <最大支持度的上界>：最大支持度的上限，默认为1.0。
        // -M <最小支持度的下界>：最小支持度的下限，默认为0.1。
        // -S <显著性水平>：如果使用此选项，将以给定的显著性水平测试规则的显著性。此选项会降低算法速度，默认为不进行显著性测试。
        // -I：如果设置，将输出找到的频繁项集，默认为不输出。
        // -R：删除包含所有缺失值的列，默认为不删除。
        // -V：逐步报告进展，默认为不报告。
        // -A：如果设置，将挖掘类关联规则，默认为不挖掘。
        // -Z：将零值（即标称属性的第一个值）视为缺失值。
        // -B <toString分隔符>：如果使用此选项，将用两个字符作为规则分隔符。第一个字符用于分隔字段，第二个字符用于分隔字段内的项目，默认为传统的toString结果。
        // -c <类别的索引>：类别的索引，默认为最后一个属性。
        Apriori apriori = new Apriori();

        // 设置最小支持度和最小置信度、数据随机生成可能挖掘不到关联规则
        apriori.setOptions(new String[]{"-M", "0.01", "-C", "0.09"});

        // 也可以直接设置
//        apriori.setDelta(0.05);
//        apriori.setLowerBoundMinSupport(0.1);
//        apriori.setUpperBoundMinSupport(1.0);
//        apriori.setNumRules(20);
//        apriori.setMinMetric(0.5);


        // 关联规则挖掘
        apriori.buildAssociations(newData);

        // 规则输出如下：
        // =======Rule=======
        // 前提：[flag=S0]，支持度(出现次数)：50253
        // 结论：[land=1]，支持度(出现次数)：50012
        // 度量名称：Confidence，值：0.5004079358446262
        // 度量名称：Lift，值：1.000575733513209
        // 度量名称：Leverage，值：1.446964000000328E-4
        // 度量名称：Conviction，值：1.00053648942526
        // 主要度量名称：Confidence,值：0.5004079358446262
        // 数据总量：100000，总支持度(满足规则的次数)：25147

        // 上面规则说明flag=S0的样本是50253次、land=1的样本出现的次数时50012次、样本总量是100000个
        // 当flag=S0(记为条件A)、land=1(记为结果B),满足这个规则的样本数是25147次,按照不同的度量名称可以解释为：

        // Confidence（置信度）: 表示在前提条件发生的情况下，结论也会发生的概率。
        // 公式: Confidence(A → B) = P(B|A) = Support(A ∪ B) / Support(A)。
        // 解释: 如果 Confidence 为 0.5，表示在满足前提条件 A 的情况下，结论 B 有50%的概率发生。

        //Lift（提升度）: 度量了结论发生的相对频率，相对于在没有前提的情况下发生的频率。
        // 公式: Lift(A → B) = P(B|A) / P(B) = Confidence(A → B) / Support(B)。
        // 解释: 如果 Lift 等于1，表示前提项 A 对结论项 B 的发生没有影响；大于1表示正相关，小于1表示负相关。

        // Leverage（杠杆率）: 度量了前提项和结论项的共同发生程度，相对于它们在独立情况下的发生程度。
        // 公式: Leverage(A → B) = Support(A ∪ B) - Support(A) * Support(B)。
        // 解释: Leverage 大于0表示前提项和结论项的共同发生程度高于它们独立发生的期望。

        // Conviction（确信度）: 度量了规则中结论项不发生的相对不确定性。
        // 公式: Conviction(A → B) = (1 - Support(B)) / (1 - Confidence(A → B))。
        // 解释: Conviction 等于1表示结论项 B 的发生和不发生一样确定，大于1表示结论项 B 的发生更加确定。

        for (AssociationRule rule : apriori.getAssociationRules().getRules()) {
            // DefaultAssociationRule.toString() 函数
            System.out.println("=======Rule=======");
            System.out.println("前提："+rule.getPremise()+"，支持度(出现次数)："+rule.getPremiseSupport());
            System.out.println("结论："+rule.getConsequence()+"，支持度(出现次数)："+rule.getConsequenceSupport());
            for (int i = 0; i < rule.getMetricNamesForRule().length; i++) {
                System.out.println("度量名称："+rule.getMetricNamesForRule()[i]+"，值："+rule.getMetricValuesForRule()[i]);
            }
            System.out.println("主要度量名称："+rule.getPrimaryMetricName()+",值："+rule.getPrimaryMetricValue());
            System.out.println("数据总量："+rule.getTotalTransactions()+"，总支持度(满足规则的次数)："+rule.getTotalSupport());
        }

        // Print the results
        System.out.println(apriori);

    }

}

