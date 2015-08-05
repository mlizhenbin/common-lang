package cn.lzb.common.sign.impl;

import cn.lzb.common.lang.StringUtil;
import cn.lzb.common.sign.DESSignature;
import cn.lzb.common.sign.SignKeyLoader;
import cn.lzb.common.sign.util.KeyEnum;
import cn.lzb.common.sign.util.MD5Utils;
import cn.lzb.common.sign.util.NetDESUtils;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 功能描述：与中民对接，重新DES加密方法，
 *
 * @author: Zhenbin.Li
 * email： zhenbin.li@okhqb.com
 * company：华强北在线
 * Date: 13-12-23 Time：下午4:52
 */
public class NetDESSignatureImpl implements DESSignature {

    /**
     * 密钥加载器
     */
    private SignKeyLoader signKeyLoader;

    @Override
    public String encrypt(String context) {
        if (StringUtil.isBlank(context)) {
            return null;
        }
        try {
            return NetDESUtils.toHexString(
                    NetDESUtils.encrypt(
                            URLEncoder.encode(context, "utf-8").toLowerCase(), getDESKey())).toUpperCase();
        } catch (Exception e) {
            throw new RuntimeException("DES加密异常， context=" + context, e);
        }
    }

    @Override
    public String encrypt(String context, String charset) {
        if (StringUtil.isBlank(context)) {
            return null;
        }
        try {
            return NetDESUtils.toHexString(
                    NetDESUtils.encrypt(
                            URLEncoder.encode(context, charset).toLowerCase(), getDESKey(), charset)
            ).toUpperCase();
        } catch (Exception e) {
            throw new RuntimeException("DES加密异常， context=" + context, e);
        }
    }

    @Override
    public String decrypt(String context) {
        if (StringUtil.isBlank(context)) {
            return null;
        }
        try {
            return URLDecoder.decode(NetDESUtils.decrypt(context, getDESKey()), "utf-8");
        } catch (Exception e) {
            throw new RuntimeException("DES加密异常， context=" + context, e);
        }
    }

    @Override
    public String decrypt(String context, String charset) {
        if (StringUtil.isBlank(context)) {
            return null;
        }
        try {
            return URLDecoder.decode(NetDESUtils.decrypt(context, getDESKey(charset), charset), charset);
        } catch (Exception e) {
            throw new RuntimeException("DES加密异常， context=" + context, e);
        }
    }

    /**
     * 获取DES加密方式KEY
     *
     * @return
     */
    protected String getDESKey() {
        String keyStr = signKeyLoader.getKey(KeyEnum.DES_INSURANCE_KEY);
        if (StringUtil.isBlank(keyStr)) {
            throw new RuntimeException("DES加密获取字符串密钥为空");
        }
        try {
            return MD5Utils.encode(keyStr, "UTF-8").substring(0, 8);
        } catch (Exception e) {
            throw new RuntimeException("DES加密获取字符串密钥异常， keyStr=" + keyStr, e);
        }
    }

    /**
     * 获取DES加密方式KEY
     *
     * @param charset
     * @return
     */
    protected String getDESKey(String charset) {
        String keyStr = signKeyLoader.getKey(KeyEnum.DES_INSURANCE_KEY);
        if (StringUtil.isBlank(keyStr)) {
            throw new RuntimeException("DES加密获取字符串密钥为空");
        }
        try {
            return MD5Utils.encode(keyStr, charset).substring(0, 8);
        } catch (Exception e) {
            throw new RuntimeException("DES加密获取字符串密钥异常， keyStr=" + keyStr, e);
        }
    }

    public void setSignKeyLoader(SignKeyLoader signKeyLoader) {
        this.signKeyLoader = signKeyLoader;
    }

}
