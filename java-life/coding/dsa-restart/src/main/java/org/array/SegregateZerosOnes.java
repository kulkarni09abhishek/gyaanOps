package org.array;

import java.util.Arrays;

public class SegregateZerosOnes {
    public static void main(String[] args) {
        int[] arr = {0, 0, 1, 1, 0};
        Arrays.stream(segregate(arr)).forEach(element -> System.out.print(element + " "));
    }

    private static int[] segregate(int[] arr) {
        int countZeros = 0;
        int countOnes = 0;
        for(int element : arr){
            if(element==0) countZeros++;
            else countOnes++;
        }

        int counter = 0;
        while (countZeros>0){
            arr[counter] = 0;
            counter++;
            countZeros--;
        }
        while (countOnes>0){
            arr[counter] = 1;
            counter++;
            countOnes--;
        }
        return arr;
    }
}
