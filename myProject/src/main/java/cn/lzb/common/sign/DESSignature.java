package com.okhqb.security.sign;

/**
 * 功能描述： 对称加密算法DES加密解密接口
 *
 * @author: Zhenbin.Li
 * email： zhenbin.li@okhqb.com
 * company：华强北在线
 * Date: 13-12-18 Time：下午2:17
 */
public interface DESSignature {

    /**
     * 字符串加密，默认使用utf-8加密方式
     *
     * @param context 需要加密字符串
     * @return
     */
    public String encrypt(String context);

    /**
     * 字符串加密，需要设定字符串集
     *
     * @param context 需要加密字符串
     * @param charset 加密字符串字符集
     * @return
     */
    public String encrypt(String context, String charset);

    /**
     * 字符串解密，默认使用utf-8编码
     *
     * @param context 加密字符串
     * @return
     */
    public String decrypt(String context);

    /**
     * 字符串解密，需要传入字符串集
     *
     * @param context 加密字符串
     * @param charset 解密字符串集
     * @return
     */
    public String decrypt(String context, String charset);
}
