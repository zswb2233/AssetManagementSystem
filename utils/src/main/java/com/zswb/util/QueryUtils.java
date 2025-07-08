package com.zswb.util;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryUtils {

    // 正则表达式模式，用于匹配条件字符串中的操作符和值
    private static final Pattern OPERATOR_PATTERN = Pattern.compile("(.*?)([><>=]{1,2})(.*)");

    /**
     * 构建查询参数Map
     * @param require 条件列表，每个条件格式为"属性名操作符值"，如"age>20"
     * @return 包含查询条件的Map
     */
    public static Map<String, Object> buildQueryParams(List<String> require) {
        Map<String, Object> queryParams = new HashMap<>();

        if (require == null || require.isEmpty()) {
            return queryParams;
        }

        for (String condition : require) {
            if (condition == null || condition.trim().isEmpty()) {
                continue;
            }

            Matcher matcher = OPERATOR_PATTERN.matcher(condition.trim());
            if (matcher.matches()) {
                String field = matcher.group(1).trim();
                String operator = matcher.group(2).trim();
                String value = matcher.group(3).trim();

                // 根据操作符构建查询条件
                switch (operator) {
                    case ">":
                        queryParams.put(field + "_gt", value);
                        break;
                    case ">=":
                        queryParams.put(field + "_gte", value);
                        break;
                    case "<":
                        queryParams.put(field + "_lt", value);
                        break;
                    case "<=":
                        queryParams.put(field + "_lte", value);
                        break;
                    case "=":
                        queryParams.put(field, value);
                        break;
                    default:
                        // 处理无效操作符
                        System.err.println("无效的操作符: " + operator);
                }
            } else {
                // 处理格式不正确的条件
                System.err.println("条件格式不正确: " + condition);
            }
        }

        return queryParams;
    }
}