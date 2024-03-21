package org.tyf;


import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


/**
 *   @desc : mahout cf协同过滤
 *   @auth : tyf
 *   @date : 2022-03-21  15:43:05
*/
public class MahoutCFTaste {


    public static void genTxt(String file){
        Random r = new Random();
        if(new File(file).exists()){
            new File(file).delete();
        }
        // 写入到本地csv
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            for (int i = 0; i < 100000; i++) {
                String userId = r.nextInt(100)+"";
                String itemId = r.nextInt(1000)+"";
                String score = r.nextDouble()+"";
                String ts = System.currentTimeMillis()+"";
                writer.write(userId+","+itemId+","+score+","+ts);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) throws Exception{


        String data = "C:\\Users\\tyf\\Desktop\\data.txt";

        // 生成随机样本
        genTxt(data);

        // 数据读取接口有数据库文件、hdfs等接口，在这个包下面 org.apache.mahout.cf.taste.impl.model.*
        // 底层 processFile 中读取每一行 [用户编号、商品编号、评分、时间戳]
        // userID,itemID,preference,timestamp
        FileDataModel fdm = new FileDataModel(new File(data));

        LongPrimitiveIterator userIDs = fdm.getUserIDs(); // 所有用户id
        LongPrimitiveIterator itemIDs = fdm.getItemIDs(); // 所有商品id

        // 一共有4中已实现的推荐器
        // Recommender分为以下几种实现：
        // GenericUserBasedRecommender：基于用户的推荐引擎
        // GenericBooleanPrefUserBasedRecommender：基于用户的无偏好值推荐引擎
        // GenericItemBasedRecommender：基于物品的推荐引擎
        // GenericBooleanPrefItemBasedRecommender：基于物品的无偏好值推荐引擎


        // ----------------------基于用户协同-----------------------------

        // 用户相似度
        UserSimilarity userSimilarity = new EuclideanDistanceSimilarity(fdm);

        UserNeighborhood userNeighborhood1 = new NearestNUserNeighborhood(10, userSimilarity, fdm); // 根据单个用户id 得到 k个相似用户的id
        UserNeighborhood userNeighborhood2 = new ThresholdUserNeighborhood(0.6, userSimilarity, fdm); // 固定阈值邻居

        // 计算相似用户
        System.out.println("userId："+10+"，Similarity userId："+Arrays.toString(userNeighborhood1.getUserNeighborhood(10)));
        System.out.println("userId："+10+"，Similarity userId："+Arrays.toString(userNeighborhood2.getUserNeighborhood(10)));

        // 计算相似用户并计算推荐商品
        Recommender recommender1 = new GenericUserBasedRecommender(fdm, userNeighborhood1, userSimilarity);
        Recommender recommender2 = new GenericUserBasedRecommender(fdm, userNeighborhood2, userSimilarity);

        System.out.println("recommender1：");
        while (userIDs.hasNext()) {
            long userId = userIDs.next();
            // 为用户查找 4个推荐项
            List<RecommendedItem> recommendedItemList = recommender1.recommend(userId, 4);
            StringBuffer sb = new StringBuffer();
            for (RecommendedItem item : recommendedItemList) {
                sb.append("["+item.getItemID() + " , "+item.getValue()+"] ");
            }
            System.out.println(userId + " ==> " + sb);
        }

        System.out.println("recommender2：");
        while (userIDs.hasNext()) {
            long userId = userIDs.next();
            // 为用户查找 4个推荐项
            List<RecommendedItem> recommendedItemList = recommender2.recommend(userId, 4);
            StringBuffer sb = new StringBuffer();
            for (RecommendedItem item : recommendedItemList) {
                sb.append("["+item.getItemID() + " , "+item.getValue()+"] ");
            }
            System.out.println(userId +" ==> " + sb);
        }



        // ----------------------基于商品协同-----------------------------

        // 商品相似度
        ItemSimilarity itemSimilarity = new EuclideanDistanceSimilarity(fdm);

        // 计算相似商品并推荐给不同的用户
        Recommender recommender3 = new GenericItemBasedRecommender(fdm, itemSimilarity);

        System.out.println("recommender3：");
        while (userIDs.hasNext()) {
            Long userId = userIDs.next();
            // 为用户查找 4个推荐项
            List<RecommendedItem> recommendedItemList = recommender3.recommend(userId, 4);
            StringBuffer sb = new StringBuffer();
            for (RecommendedItem item : recommendedItemList) {
                sb.append("["+item.getItemID() + " , "+item.getValue()+"] ");
            }
            System.out.println(userId + " ==> " + sb);
        }

        // ----------------------基于用户商品协同-----------------------------

        GenericItemBasedRecommender recommender4 = new GenericItemBasedRecommender(fdm, itemSimilarity);


        Long userId = 1L;
        Long itemId = 103L;

        // 对用户当前购买的商品 查找2个其他推荐项
        List<RecommendedItem> recommendedItemList = recommender4.recommendedBecause(userId,itemId,2);

        StringBuffer sb = new StringBuffer();
        for (RecommendedItem item : recommendedItemList) {
            sb.append("["+item.getItemID() + " , "+item.getValue()+"] ");
        }

        System.out.println(userId + " ==> " + sb);


    }


}
