package org.tyf;

import smile.math.distance.EditDistance;
import smile.math.distance.LeeDistance;
import smile.neighbor.BKTree;
import smile.neighbor.LinearSearch;
import smile.neighbor.Neighbor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 *   @desc : Smile BKTree BK树 临近搜索
 *   @auth : tyf
 *   @date : 2022-03-08  16:38:14
*/
public class SmileBKTree {

    // BK树 临近搜索

    public static void main(String[] args) {

        // 创建一个字符串数组作为测试数据
        String[] words = {"book", "back", "bark", "hack", "hackerrank"};


        BKTree bktree = BKTree.of(words, new EditDistance(50, true));


        // 范围搜索测试
        for (int i = 0; i < 100; i++) {

            List<Neighbor<String, String>> n1 = new ArrayList<>();
            bktree.search(words[i], 1, n1);

            System.out.println(i+" "+words[i]);
            java.util.Collections.sort(n1);
            System.out.println(n1.stream().map(Objects::toString).collect(Collectors.joining(", ")));

        }



    }

}
