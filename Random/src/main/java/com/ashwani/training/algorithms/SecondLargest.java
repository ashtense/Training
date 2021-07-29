package com.ashwani.training.algorithms;

public class SecondLargest {

    /**
     * <p>
     * Most efficient algorithm. There are other ways of sorting and <br>finding the second largest.
     * But the below is the best.</p>
     *
     * @param args
     */
    public static void main(String[] args) {
        int[] arrInput = {12, 35, 1, 10, 34, 34};
        int largest = arrInput[0];
        int secondLargest = 0;
        for (int element : arrInput) {
            if (element > largest) {
                secondLargest = largest;
                largest = element;
            }
            if (element < largest && element > secondLargest) {
                secondLargest = element;
            }
        }
        System.err.println(secondLargest);
    }
}
