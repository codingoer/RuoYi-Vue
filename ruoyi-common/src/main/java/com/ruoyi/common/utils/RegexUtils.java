package com.ruoyi.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式工具类
 *
 * @author Lionel
 * @date 2022/9/22 16:30
 */
public class RegexUtils {

    public static String findMatch(String text, String regex){
        return findMatch(text, regex, 0, text == null ? 0 : text.length());
    }

    public static String findMatch(String text, String regex, int start, int end) {
        if(text == null) {
            return null;
        }
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);
        m.region(start, end);
        while(m.find()){
            int len = m.groupCount();
            if(len > 0){
                return m.group(1);
            }
        }
        return null;
    }
}
