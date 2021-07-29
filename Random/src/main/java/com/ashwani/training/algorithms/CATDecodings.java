package com.ashwani.training.algorithms;

public class CATDecodings {

    public static void main(String[] args) {
        String strInput = "1234";
        char[] arrInputChar = strInput.toCharArray();
        int inputLength = arrInputChar.length;
    }

    static int countCombos(char[] arrInputChar, int inputLength){
        if(inputLength == 0 || (inputLength==1 && arrInputChar[0] == '0')){
            return 0;
        }
        return countDecodings(arrInputChar, inputLength);
    }

    private static int countDecodings(char[] arrInputChar, int n) {
        return 0;
    }
}
