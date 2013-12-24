package cn.lzb.common.sign.impl;

import cn.lzb.common.lang.StringUtil;
import cn.lzb.common.sign.DESSignature;
import cn.lzb.common.sign.SignKeyLoader;
import cn.lzb.common.sign.util.DESUtils;
import cn.lzb.common.sign.util.KeyEnum;

import java.nio.charset.Charset;

/**
 * 功能描述：对称加密算法DES加密解密接口实现
 *
 * @author: Zhenbin.Li
 * email： zhenbin.li@okhqb.com
 * company：华强北在线
 * Date: 13-12-18 Time：下午2:38
 */
public class DESSignatureImpl implements DESSignature {

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
            return DESUtils.getBytesStr(
                    DESUtils.encrypt(context.getBytes(Charset.defaultCharset()), getDESKey()));
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
            return DESUtils.getBytesStr(
                    DESUtils.encrypt(context.getBytes(charset), getDESKey()));
        } catch (Exception e) {
            throw new RuntimeException("DES加密异常， context=" + context + ", charset" + charset, e);
        }
    }

    @Override
    public String decrypt(String context) {
        if (StringUtil.isBlank(context)) {
            return null;
        }
        try {
            return DESUtils.getBytesStr(
                    DESUtils.decrypt(context.getBytes(Charset.defaultCharset()), getDESKey()));
        } catch (Exception e) {
            throw new RuntimeException("DES解密密异常， context=" + context, e);
        }
    }

    @Override
    public String decrypt(String context, String charset) {
        if (StringUtil.isBlank(context)) {
            return null;
        }
        try {
            return DESUtils.getBytesStr(
                    DESUtils.decrypt(context.getBytes(charset), getDESKey()));
        } catch (Exception e) {
            throw new RuntimeException("DES解密异常， context=" + context + ", charset" + charset, e);
        }
    }

    /**
     * 字符串密钥转换为byte数组类型
     *
     * @return
     */
    protected byte[] getDESKey() {
        String keyStr = signKeyLoader.getKey(KeyEnum.DES_INSURANCE_KEY);
        if (StringUtil.isBlank(keyStr)) {
            throw new RuntimeException("DES加密获取字符串密钥为空");
        }
        byte[] desKeys = new byte[keyStr.length()];
        for (int i = 0; i < keyStr.length(); i++) {
            desKeys[i] = Byte.valueOf(keyStr.charAt(i) + "");
        }
        return keyStr.getBytes();
    }

    public void setSignKeyLoader(SignKeyLoader signKeyLoader) {
        this.signKeyLoader = signKeyLoader;
    }
}
