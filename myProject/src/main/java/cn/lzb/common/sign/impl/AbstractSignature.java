/**
 * okhqb.com.
 * Copyright (c) 2009-2013 All Rights Reserved.
 */
package com.okhqb.security.sign.impl;

import com.okhqb.security.sign.SignKeyLoader;
import com.okhqb.security.sign.Signature;
import com.okhqb.security.sign.util.KeyEnum;
import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;

/**
 * 
 * 
 * @author ZhouJun
 * @version $Id: AbstractSignature.java, v 0.1 2013-2-20 上午10:43:52 ZhouJun Exp $
 */
public abstract class AbstractSignature implements Signature {

    private SignKeyLoader signKeyLoader;

    @Override
    public String sign(String content, String charset) throws SignatureException {
        String privateKey = signKeyLoader.getKey(KeyEnum.RSA_PRIVATE_KEY);
        return sign(content, privateKey, charset);
    }

    @Override
    public boolean verify(String content, String charset, String sign) throws SignatureException {
        String publicKey = signKeyLoader.getKey(KeyEnum.RSA_PUBLIC_KEY);
        return verify(content, publicKey, charset, sign);
    }

    @Override
    public String sign(String content, KeyEnum keyEnum, String charset) throws SignatureException {
        String privateKey = signKeyLoader.getKey(keyEnum);
        if (null == content) {
            throw new SignatureException("内容为null");
        }
        if (StringUtils.isBlank(privateKey)) {
            throw new SignatureException("私钥为空");
        }
        return sign(content, privateKey, charset);
    }

    @Override
    public boolean verify(String content, KeyEnum keyEnum, String charset, String sign)
                                                                                       throws SignatureException {
        String publicKey = signKeyLoader.getKey(keyEnum);
        if (null == content) {
            throw new SignatureException("内容为null");
        }
        if (StringUtils.isBlank(sign)) {
            throw new SignatureException("签名为空");
        }
        if (StringUtils.isBlank(publicKey)) {
            throw new SignatureException("公钥为空");
        }
        return doVerify(content, publicKey, charset, sign);
    }

    @Override
    public String sign(String content, String privateKey, String charset) throws SignatureException {

        if (null == content) {
            throw new SignatureException("内容为null");
        }
        if (StringUtils.isBlank(privateKey)) {
            throw new SignatureException("私钥为空");
        }
        return doSign(content, privateKey, charset);
    }

    @Override
    public boolean verify(String content, String publicKey, String charset, String sign)
                                                                                        throws SignatureException {
        if (null == content) {
            throw new SignatureException("内容为null");
        }
        if (StringUtils.isBlank(sign)) {
            throw new SignatureException("签名为空");
        }
        if (StringUtils.isBlank(publicKey)) {
            throw new SignatureException("公钥为空");
        }
        return doVerify(content, publicKey, charset, sign);
    }

    protected abstract boolean doVerify(String content, String publicKey, String charset,
                                        String sign) throws SignatureException;

    protected abstract String doSign(String content, String privateKey, String charset)
                                                                                       throws SignatureException;

    protected byte[] getCotentBytes(String content, String charset)
                                                                   throws UnsupportedEncodingException {
        if (StringUtils.isBlank(charset)) {
            return content.getBytes();
        }
        return content.getBytes(charset);
    }

    public void setSignKeyLoader(SignKeyLoader signKeyLoader) {
        this.signKeyLoader = signKeyLoader;
    }

}
