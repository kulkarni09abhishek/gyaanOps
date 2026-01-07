package org.array;

import java.util.Arrays;

public class RotateArray {
    public static void main(String[] args) {
        int[] arr = {6, 8, 1, 2, 4, 9, 0};
        int d = 3;
        int n = arr.length;
        d = d % n;
        reverse(arr, 0, d - 1);
        reverse(arr, d, n - 1);
        reverse(arr, 0, n - 1);
        Arrays.stream(arr).forEach(element -> System.out.print(element + " "));
    }

    private static void reverse(int[] arr, int start, int end) {
        while (start < end) {
            int temp = arr[start];
            arr[start] = arr[end];
            arr[end] = temp;
            start++;
            end--;
        }
    }
}
