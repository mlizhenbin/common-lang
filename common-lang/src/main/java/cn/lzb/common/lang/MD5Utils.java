package cn.lzb.common.lang;

import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 功能描述：MD5加密工具类
 *
 * @author: Zhenbin.Li
 * email： lizhenbin@oneplus.cn
 * company：一加科技
 * Date: 15/6/12 Time：23:41
 */
public class MD5Utils {

    private MD5Utils() {
    }

    private static final String DEFAULT_CHARSET = "GBK";

    private static final String[] defaultHexDigits = {
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "A", "B", "C", "D", "E", "F"};

    private static final String[] hexDigits = {
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "a", "b", "c", "d", "e", "f"};

    /**
     * 默认MD5加密，字符集GBK
     *
     * @param original 加密内容
     * @return
     */
    public static String encode(String original) {
        return encode(original, DEFAULT_CHARSET);
    }

    /**
     * MD5加密，制定加密字符集
     *
     * @param original 加密内容
     * @param charset  字符集
     * @return
     */
    public static String encode(String original, String charset) {
        return encode(original, charset, true);
    }

    public static String encode(String original, String charset, boolean isUpCaseHexDigits) {
        if (StringUtils.isBlank(original)) {
            throw new RuntimeException("密码加密时，密码为空");
        }

        byte[] oBytes;
        if (StringUtils.isBlank(charset)) {
            oBytes = original.getBytes();
        } else {
            try {
                oBytes = original.getBytes(charset);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }

        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] dBytes = md5.digest(oBytes);
        return byteArrayToHexString(dBytes, isUpCaseHexDigits);
    }

    private static String byteArrayToHexString(byte[] b, boolean isUpCase) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            buffer.append(byteToHexString(b[i], isUpCase));
        }
        return buffer.toString();
    }

    private static String byteToHexString(byte b, boolean isUpCase) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return isUpCase ? (defaultHexDigits[d1] + defaultHexDigits[d2]) : (hexDigits[d1] + hexDigits[d2]);
    }
}
