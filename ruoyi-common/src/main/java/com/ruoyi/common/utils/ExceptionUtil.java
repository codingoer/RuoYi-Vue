package com.ruoyi.common.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * 错误信息处理类。
 *
 * @author ruoyi
 */
public class ExceptionUtil
{
    /**
     * 获取exception的详细错误信息。
     */
    public static String getExceptionMessage(Throwable e)
    {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw, true));
        return sw.toString();
    }

    public static String getRootErrorMessage(Exception e)
    {
        Throwable root = ExceptionUtils.getRootCause(e);
        root = (root == null ? e : root);
        if (root == null)
        {
            return "";
        }
        String msg = root.getMessage();
        if (msg == null)
        {
            return "null";
        }
        return StringUtils.defaultString(msg);
    }

    public static RuntimeException uncheck(Throwable e){
        if(e instanceof RuntimeException){
            return (RuntimeException) e;
        } else if(e instanceof InvocationTargetException){
            e = ((InvocationTargetException) e).getTargetException();
            return (e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException(e));
        } else {
            return new RuntimeException(e);
        }
    }
}
