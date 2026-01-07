package org.array;

public class ReverseArray {
    public static void main(String[] args) {
        int[] arr = {4, 8, 10, 10, 6, 3};
        print(reverse(arr));
    }

    private static void print(int[] arr) {
        for (int element : arr) {
            System.out.print(element + " ");
        }
        System.out.println();
    }

    private static int[] reverse(int[] arr) {
        int left = 0;
        int right = arr.length - 1;
        while (left < right) {
            int temp = arr[left];
            arr[left] = arr[right];
            arr[right] = temp;
            left++;
            right--;
        }
        return arr;
    }
}
