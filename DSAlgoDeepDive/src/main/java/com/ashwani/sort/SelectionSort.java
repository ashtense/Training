package com.ashwani.sort;

public class SelectionSort {

    public static void main(String[] args) {
        int[] arrToSort = {20, 35, -15, 7, 55, 1, -22};
        for (int lastUnsortedIndex = arrToSort.length - 1; lastUnsortedIndex > 0;
             lastUnsortedIndex--) {
            int largestIndex = 0;
            for (int i = 0; i <= lastUnsortedIndex; i++) {
                if (arrToSort[i] > arrToSort[largestIndex]) {
                    largestIndex = i;
                }
            }
            swapElements(arrToSort, lastUnsortedIndex, largestIndex);
        }
        printArray(arrToSort);
    }

    public static void swapElements(int[] array, int i, int j) {
        if (i == j) {
            return;
        }
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    private static void printArray(int[] arrToSort) {
        for (int i = 0; i < arrToSort.length; i++) {
            System.out.print(arrToSort[i] + ",");
        }
    }
}
