package org.array;

public class MissingInArr {
    public static void main(String[] args) {
        int[] arr = {8, 2, 4, 5, 3, 7, 1};
        System.out.println(missingNum(arr));
    }

    private static int missingNum(int[] arr) {
        long n = arr.length + 1;
        long sum = n * (n + 1) / 2;
        long arrSum = 0;
        for (int element : arr) {
            arrSum = arrSum + element;
        }
        return (int)(sum - arrSum);
    }
}