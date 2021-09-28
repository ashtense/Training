package com.ashwani.initial.arrays;

public class Substring {

    public static void main(String[] args) {
        String strToSearchIn = "abchelloevabctrupmabc";
        String strToSearchFor = "abc";
        char[] arrSearchIn = strToSearchIn.toCharArray();
        char[] arrSearchFor = strToSearchFor.toCharArray();
        for(int i = 0; i < arrSearchIn.length; i++){
            if(arrSearchIn[i] == arrSearchFor[0]){
                for (int j = 0; j < arrSearchFor.length; i++){
                    if(arrSearchIn.length != i+j && arrSearchIn[i+j]!=arrSearchFor[j]){
                        System.err.println("GERONIMO");
                    }
                }
            }
        }
    }
}
