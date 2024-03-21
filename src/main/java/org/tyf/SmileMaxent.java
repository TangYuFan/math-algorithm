package org.tyf;

import smile.classification.Maxent;


/**
 *   @desc : Smile Maxent 最大熵分类器
 *   @auth : tyf
 *   @date : 2022-03-08  16:38:14
*/
public class SmileMaxent {

    // 最大熵分类器

    public static void main(String[] args) {

        int p = 6;
        int[][] x = {
                {0,1,0,1,0,1,0,1},
                {1,0,1,0,1,0,1,0},
                {1,0,1,0,1,0,1,0},
                {1,0,0,1,0,1,0,1},
                {1,0,1,0,1,0,1,0},
                {1,0,0,1,0,1,0,1},
                {1,0,0,1,0,1,0,1},
                {1,0,0,1,0,1,0,1},
                {1,0,0,1,0,1,0,1},
                {1,0,0,1,0,1,0,1},
                {1,0,0,1,0,1,0,1},
                {1,0,0,1,0,1,0,1},
                {1,0,0,1,0,1,0,1},
                {1,0,0,1,0,1,0,1},
                {1,0,0,1,0,1,0,1}
        };
        int[] y = {0,1,0,0,1,0,1,0,0,1,0,1,0,0,1};

        Maxent ent = Maxent.fit(p,x,y);

        System.out.println("Maxent："+ent);

        int label = ent.predict(new int[]{1,0,1,0,1,0,1,0});
        System.out.println("Label："+label);


    }

}
