/**
 * okhqb.com.
 * Copyright (c) 2009-2013 All Rights Reserved.
 */
package cn.lzb.common.sign.util;

import org.apache.commons.lang.StringUtils;

import java.security.MessageDigest;

/**
 * 
 * 
 * @author ZhouJun
 * @version $Id: MD5Utils.java, v 0.1 2013-2-20 上午10:43:08 ZhouJun Exp $
 */
public class MD5Utils {

    private static final String   DEFAULT_CHARSET = "GBK";

    private static final String[] hexDigits       = { "0", "1", "2", "3", "4", "5", "6", "7", "8",
            "9", "A", "B", "C", "D", "E", "F"    };

    public static String encode(String original) throws Exception {
        return encode(original, DEFAULT_CHARSET);
    }

    public static String encode(String original, String charset) throws Exception {
        if (StringUtils.isBlank(original)) {
            throw new Exception("密码加密时，密码为空");
        }

        byte[] oBytes;
        if (StringUtils.isBlank(charset)) {
            oBytes = original.getBytes();
        } else {
            oBytes = original.getBytes(charset);
        }

        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] dBytes = md5.digest(oBytes);
        return byteArrayToHexString(dBytes);
    }

    private static String byteArrayToHexString(byte[] b) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            buffer.append(byteToHexString(b[i]));
        }
        return buffer.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }
}
