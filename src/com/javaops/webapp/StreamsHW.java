package com.javaops.webapp;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StreamsHW {
    public static void main(String[] args) {
        System.out.println(minValue(new int[]{1, 2, 3, 3, 2, 3})); // 123
        System.out.println(minValue(new int[]{9, 8})); // 89

        System.out.println(oddOrEven(Arrays.asList(1, 2, 3, 4, 5, 6, 7))); // [1, 3, 5, 7]
        System.out.println(oddOrEven(Arrays.asList(1, 2, 3, 4, 5, 6))); // [2, 4, 6]
    }

    public static int minValue(int[] values) {
        return Arrays.stream(values).distinct().sorted().reduce((x, y) -> x * 10 + y).orElse(-1);
    }

    public static List<Integer> oddOrEven(List<Integer> integers) {
        Map<Boolean, List<Integer>> oddAndEven = integers.stream()
                .collect(Collectors.partitioningBy(n -> n % 2 == 0));
        return oddAndEven.get(oddAndEven.get(false).size() % 2 != 0);
    }
}
