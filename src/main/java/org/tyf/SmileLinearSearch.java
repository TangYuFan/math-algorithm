package org.tyf;

import smile.math.distance.EditDistance;
import smile.neighbor.LinearSearch;
import smile.neighbor.Neighbor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 *   @desc : Smile LinearSearch 线性搜索 临近搜索
 *   @auth : tyf
 *   @date : 2022-03-08  16:38:14
*/
public class SmileLinearSearch {

    // 线性搜索 临近搜索

    public static void main(String[] args) {


        String[] words = {"book", "back", "bark", "hack", "hackerrank"};


        LinearSearch<String, String> search = LinearSearch.of(words, new EditDistance(true));

        // 范围搜索测试
        for (int i = 0; i < 100; i++) {

            List<Neighbor<String, String>> n1 = new ArrayList<>();
            search.search(words[i], 1, n1);

            System.out.println(i+" "+words[i]);
            java.util.Collections.sort(n1);
            System.out.println(n1.stream().map(Objects::toString).collect(Collectors.joining(", ")));

        }

    }

}
