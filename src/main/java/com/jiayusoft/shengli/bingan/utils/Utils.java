package com.jiayusoft.shengli.bingan.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;

/**
 * Created by Hi on 2016-1-4.
 */
public class Utils {
    public static String getBase64Data(String string){
        String result = null;
        if (StringUtils.isNotEmpty(string)){
            result = StringUtils.toEncodedString(Base64.decodeBase64(string.getBytes()), Charset.forName("UTF-8"));
        }
        return result;
    }

    public static String getSignalImageUrl(String url){
        String[] logoNames = StringUtils.split(url,";");
        if (logoNames!=null && logoNames.length>0)
            return logoNames[0];
        else
            return "";
    }
}
