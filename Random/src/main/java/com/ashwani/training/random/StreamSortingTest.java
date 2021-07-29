package com.ashwani.training.random;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamSortingTest {

    public static void main(String[] args) {
        int[] arrIntegers = {1,2,5,8,3,5};
        List<Integer> collect = Arrays.stream(arrIntegers).boxed().collect(Collectors.toList());
        Collections.sort(collect);
        System.err.println(collect);
    }
}
