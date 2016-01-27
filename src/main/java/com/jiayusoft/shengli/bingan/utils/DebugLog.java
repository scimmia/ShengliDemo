package com.jiayusoft.shengli.bingan.utils;

/**
 * Created by Administrator on 15-2-13.
 */
public class DebugLog {
    public static String info(){
        StackTraceElement[] sElements = new Throwable().getStackTrace();
        String className = sElements[1].getClassName();
        String methodName = sElements[1].getMethodName();
        int lineNumber = sElements[1].getLineNumber();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("[")
//                .append(className)
//                .append(":")
                .append(methodName)
                .append(":")
                .append(lineNumber)
                .append("]");
        return stringBuffer.toString();
    }
}
