package org.array;

public class SecondMax {
    public static void main(String[] args) {
        int[] arr = {4, 8, 10, 10, 6, 3};
        System.out.println(findSecondMax(arr));
    }

    private static int findSecondMax(int[] arr) {
        int max = Integer.MIN_VALUE;
        int sMax = Integer.MIN_VALUE;
        boolean isMaxChanged = false;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > max) {
                sMax = max;
                max = arr[i];
            }
            if (arr[i] > sMax && arr[i] != max) {
                sMax = arr[i];
            }
        }
        return sMax;
    }
}
