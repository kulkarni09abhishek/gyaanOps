package org.array;


import java.util.ArrayList;
import java.util.Collections;

/*Input: arr[] = [5, 6, 7, 8]
  Output: [5, 6, 7, 9]
  Explanation: 5678 + 1 = 5679
  Input: arr[] = [9, 9, 9]
  Output: [1, 0, 0, 0]
  Explanation: 999 + 1 = 1000 */
public class AddingOne {
    public static void main(String[] args) {
        int[] arr = {5, 6, 7, 8};
        System.out.println(addOne(arr));
    }

    private static ArrayList<Integer> addOne(int[] arr) {
        ArrayList<Integer> ans = new ArrayList<>();
        int carry = 1;
        for (int i = arr.length - 1; i >= 0; i--) {
            if (arr[i] + carry > 9) {
                ans.add(0);
                carry = 1;
            } else {
                ans.add(arr[i]+carry);
                carry = 0;
            }
        }
        if(carry==1) ans.add(1);
        Collections.reverse(ans);
        return ans;
    }
}
