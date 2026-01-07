package org.array;


/*
Input: a[] = [2, 4, 7, 10], b[] = [2, 3]
Output: a[] = [2, 2, 3, 4], b[] = [7, 10]
Explanation: After merging the two non-decreasing arrays, we get, [2, 2, 3, 4, 7, 10]
*/

import java.util.Arrays;

public class MergeSortedArrays {
    public static void main(String[] args) {
        int[] a = {2, 4, 7, 10};
        int[] b = {2, 2, 3, 4};
        merge(a, b);
    }

    private static void merge(int[] a, int[] b) {
        int[] c = new int[a.length + b.length];
        int i = 0, j = 0, k = 0;
        while (i < a.length && j < b.length) {
            if (a[i] <= b[j]) {
                c[k++] = a[i++];
            } else {
                c[k++] = b[j++];
            }
        }
        while (i < a.length) {
            c[k++] = a[i++];
        }
        while (j < b.length) {
            c[k++] = b[j++];
        }
        int count = 0;
        for(i=0;i<a.length;i++){
            a[i] = c[count++];
        }
        for(i=0;i<b.length;i++){
            b[i] = c[count++];
        }
        Arrays.stream(c).forEach(x -> System.out.print(x + " "));
    }

}
