package org.tyf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 *   @desc : 二分查找算法
 *   @auth : tyf
 *   @date : 2022-03-01  11:14:13
*/
public class BinarySearch {

    public static class FM {
        int frequency;
        int dcxo;

        public FM(int frequency, int dcxo) {
            this.frequency = frequency;
            this.dcxo = dcxo;
        }

        @Override
        public String toString() {
            return "FM{" +
                    "frequency=" + frequency +
                    ", dcxo=" + dcxo +
                    '}';
        }
    }

    public static List<FM> findFMPair(List<FM> fmList, int standardFrequency) {
        List<FM> result = new ArrayList<>();
        int low = 0;
        int high = fmList.size() - 1;
        while (low < high) {
            int mid = (low + high) / 2;
            if (fmList.get(mid).frequency <= standardFrequency) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        if (low > 0) {
            result.add(fmList.get(low - 1));
        }
        if (low < fmList.size()) {
            result.add(fmList.get(low));
        }
        return result;
    }

    public static void main(String[] args) {

        // 下面模拟 频率和dcxo 调整关系
        // 给定的 dcxo 输入可以输出频率 frequency
        // 通过二分查找指定的 dcxo 使得频率达到标准值

        List<FM> fmList = new ArrayList<>();
        fmList.add(new FM(Integer.MIN_VALUE, 100));
        fmList.add(new FM(Integer.MAX_VALUE, 100));
        fmList.add(new FM(40, 400));
        fmList.add(new FM(50, 500));
        fmList.add(new FM(50, 500));
        fmList.add(new FM(50, 500));
        fmList.add(new FM(10, 100));
        fmList.add(new FM(20, 200));
        fmList.add(new FM(30, 300));


        // 对 fotps 按照 frequency 字段进行排序
        Collections.sort(fmList, Comparator.comparingInt(fm -> fm.frequency));

        fmList.stream().forEach(n->{
            System.out.println(n);
        });

        // 标准值
        int standardFrequency = 51;

        // 查找标准值所在区间
        List<FM> result = findFMPair(fmList, standardFrequency);

        System.out.println("Found FM Pair: " + result);
    }
}
