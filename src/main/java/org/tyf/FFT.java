package org.tyf;

import static java.lang.Math.*;

/**
 * @auth: Tang YuFan
 * @date: 18/10/2022 下午4:09
 * @desc: 快速傅里叶变换
*/
public class FFT {

    /**
     * @auth: Tang YuFan
     * @date: 18/10/2022 下午4:09
     * @desc: 复数类
    */
    static class Complex {
        public final double re;
        public final double im;

        public Complex() {
            this(0, 0);
        }

        public Complex(double r, double i) {
            re = r;
            im = i;
        }

        public Complex add(Complex b) {
            return new Complex(this.re + b.re, this.im + b.im);
        }

        public Complex sub(Complex b) {
            return new Complex(this.re - b.re, this.im - b.im);
        }

        public Complex mult(Complex b) {
            return new Complex(this.re * b.re - this.im * b.im,
                    this.re * b.im + this.im * b.re);
        }

        @Override
        public String toString() {
            return String.format("(%f,%f)", re, im);
        }
    }

    public static int bitReverse(int n, int bits) {
        int reversedN = n;
        int count = bits - 1;
        n >>= 1;
        while (n > 0) {
            reversedN = (reversedN << 1) | (n & 1);
            count--;
            n >>= 1;
        }
        return ((reversedN << count) & ((1 << bits) - 1));
    }


    /**
     * @auth: Tang YuFan
     * @date: 18/10/2022 下午4:10
     * @desc: 快速傅里叶变换
    */
    static void fft(Complex[] buffer) {

        // 每个 double 占用的位数
        int bits = (int) (log(buffer.length) / log(2));

        System.out.println("bits:"+bits);

        // 先计算所有奇数项 偶数项是他的常数倍
        for (int j = 1; j < buffer.length / 2; j++) {
            int swapPos = bitReverse(j, bits);
            Complex temp = buffer[j];
            buffer[j] = buffer[swapPos];
            buffer[swapPos] = temp;
        }

        System.out.println("样本点:"+buffer.length);

        // N 左移1位 也就是除2
        // buffer.length 是样本点数
        for (int N = 2; N <= buffer.length; N <<= 1) {
            System.out.println("---------------------");
            System.out.println("N:"+N);
            for (int i = 0; i < buffer.length; i += N) {
                System.out.println("[i]:"+i);
                for (int k = 0; k < N / 2; k++) {
                    System.out.println("[k]"+k);
                    int evenIndex = i + k;
                    int oddIndex = i + k + (N / 2);
                    Complex even = buffer[evenIndex];
                    Complex odd = buffer[oddIndex];
                    double term = (-2 * PI * k) / (double) N;
                    Complex exp = (new Complex(cos(term), sin(term)).mult(odd));
                    buffer[evenIndex] = even.add(exp);
                    buffer[oddIndex] = even.sub(exp);
                }
            }
        }
    }

    public static void main(String[] args) {



        // 时域信息 fft要求 一帧的样本点是2的整数倍不足则补0
        double[] input = {1.0, 1.0, 1.0, 1.0, 2.0, 1.0, 3.4, 5.0};
        Complex[] cinput = new Complex[input.length];

        for (int i = 0; i < input.length; i++){
            cinput[i] = new Complex(input[i], 0.0);
        }

        // 快速傅里叶变换
        fft(cinput);

        // 频域信息、复数
        System.out.println("Results:");
        for (Complex c : cinput) {
            System.out.println(c);
        }
    }

}
