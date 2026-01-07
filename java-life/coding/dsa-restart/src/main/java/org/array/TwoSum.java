package org.array;

public class TwoSum {
    public static void main(String[] args) {
        int[] arr = {10, 20, -3, 4, 7, 9};
        int targetTwoSum = 4;
        System.out.println(twoSum(arr, targetTwoSum));
    }

    private static boolean twoSum(int[] arr, int targetTwoSum) {
        int n = arr.length;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if(arr[i]+arr[j]==targetTwoSum){
                    return true;
                }
            }
        }
        return false;
    }
}
