package com.ruoyi.framework.compiler.utils;

import com.ruoyi.common.utils.RegexUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 获取类的全限定名工具类
 *
 * @author Lionel
 * @date 2022/9/22 16:26
 */
public class ClassNameUtils {

    private static final String PACKAGE_DEFINE_PATTERN = "package\\s+([\\w.]+)\\s*;";
    private static final String CLASS_NAME_PATTERN = "(?:class|interface|enum)\\s+([$\\w]+)[\\s\\{]";

    public static final String getShortClassName(String className) {
        if(StringUtils.isBlank(className)) {
            return className;
        }
        int index = className.lastIndexOf('.');
        return index > -1 ? className.substring(index + 1) : className;
    }

    public static final String findClassName(String source) {
        String packName = RegexUtils.findMatch(source, PACKAGE_DEFINE_PATTERN);
        String className = RegexUtils.findMatch(source, CLASS_NAME_PATTERN);
        return StringUtils.isBlank(packName) ? className : packName + "." + className;
    }
}
