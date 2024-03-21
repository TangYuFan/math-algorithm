package org.tyf;


import java.util.Arrays;
import java.util.Random;

/**
 *  @Desc: 快速排序
 *  @Date: 2021/3/18 16:02
 *  @auth: TYF
 */
public class QuickSort {

    /*
    快速排序（Quicksort）是对冒泡排序的一种改进。
    快速排序由C. A. R. Hoare在1962年提出。
    它的基本思想是：通过一趟排序将要排序的数据分割成独立的两部分，
    其中一部分的所有数据都比另外一部分的所有数据都要小，然后再按此
    方法对这两部分数据分别进行快速排序，整个排序过程可以递归进行，
    以此达到整个数据变成有序序列。
     */

    //交换数组上i和j位置的元素
    public static void swap(int[] arr,int i,int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }


    //通过一趟扫描对数组arr  从下标L到R  进行局部分区
    //以arr[R]为基准 小于arr[R]的数据放左边 大于arr[R]的数据放右边  这两部分各自保持原来的顺序
    public static int[] partition(int[] arr, int L, int R) {
        //基准数
        int basic = arr[R];
        //从基准数向左扫描
        int less = L - 1;
        //从基准数向右扫描
        int more = R + 1;
        while(L < more) {
            if(arr[L] < basic) {
                swap(arr, ++less, L++);
            }
            else if (arr[L] > basic) {
                swap(arr, --more, L);
            } else {
                L++;
            }
        }
        return new int[] { less + 1, more - 1 };
    }

    public static void quickSort(int[] arr) {
        if (arr==null||arr.length<2) {
            return;
        }
        quickSort(arr, 0, arr.length-1);
    }

    /**
     * 快速排序，使得整数数组 arr 的 [L, R] 部分有序
     */
    //对arr数组 下标从L到R 进行局部排序
    public static void quickSort(int[] arr,int L,int R) {
        if(L<R){
            // 把数组中随机的一个元素与最后一个元素交换，这样以最后一个元素作为基准值实际上就是以数组中随机的一个元素作为基准值
            swap(arr, new Random().nextInt(R - L + 1) + L, R);
            int[] p = partition(arr, L, R);
            quickSort(arr, L, p[0] - 1);
            quickSort(arr, p[1] + 1, R);
        }
    }


    public static void main(String[] args) {

        int[] arr = new int[]{1,5,3,4,8,6,2,8,9,0};
        quickSort(arr);
        System.out.println(Arrays.toString(arr));
    }




}
