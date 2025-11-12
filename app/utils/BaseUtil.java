package utils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseUtil {
    /**
     * 方法1: 正则表达式提取
     */
    private static void regexExtraction(String[] strings) {
        System.out.println("\n=== 方法1: 正则表达式提取 ===");

        for (String str : strings) {
            System.out.println("\n原始字符串: " + str);

            try {
                List<Integer> numbers = extractNumbersWithRegex(str);
                System.out.println("提取结果: " + numbers);

                // 转换为int数组
                int[] intArray = numbers.stream().mapToInt(Integer::intValue).toArray();
                System.out.println("int数组: " + Arrays.toString(intArray));

            } catch (Exception e) {
                System.out.println("提取失败: " + e.getMessage());
            }
        }
    }

    /**
     * 使用正则表达式提取数字
     */
    public static List<Integer> extractNumbersWithRegex(String input) {
        if (input == null || input.trim().isEmpty()) {
            return Collections.emptyList();
        }

        List<Integer> numbers = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\d+"); // 匹配一个或多个数字
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            try {
                int number = Integer.parseInt(matcher.group());
                numbers.add(number);
            } catch (NumberFormatException e) {
                // 忽略格式错误，继续查找
            }
        }

        return numbers;
    }

}
