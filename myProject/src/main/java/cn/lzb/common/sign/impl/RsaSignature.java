/**
 * okhqb.com.
 * Copyright (c) 2009-2013 All Rights Reserved.
 */
package com.okhqb.security.sign.impl;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * 
 * 
 * @author ZhouJun
 * @version $Id: RsaSignature.java, v 0.1 2013-2-20 上午10:43:39 ZhouJun Exp $
 */
public class RsaSignature extends AbstractSignature {

    private static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    private static final String ALGORITHM           = "RSA";

    @Override
    public String doSign(String content, String privateKey, String charset)
            throws SignatureException {
        try {
            byte[] keyBytes = Base64.decodeBase64(privateKey.getBytes());
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);

            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);

            java.security.Signature signature = java.security.Signature
                    .getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(priKey);
            signature.update(getCotentBytes(content, charset));
            byte[] signBytes = signature.sign();

            return new String(Base64.encodeBase64(signBytes));
        } catch (Exception e) {
            throw new SignatureException("签名时发生异常，content = " + content + "charset = " + charset, e);
        }
    }

    @Override
    public boolean doVerify(String content, String publicKey, String charset, String sign)
            throws SignatureException {
        try {
            byte[] keyBytes = Base64.decodeBase64(publicKey.getBytes());
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            PublicKey pubKey = keyFactory.generatePublic(keySpec);

            java.security.Signature signature = java.security.Signature
                    .getInstance(SIGNATURE_ALGORITHM);
            signature.initVerify(pubKey);
            signature.update(getCotentBytes(content, charset));

            // 验证签名是否正常   
            byte[] signBytes = sign.getBytes();
            return signature.verify(Base64.decodeBase64(signBytes));
        } catch (Exception e) {
            throw new SignatureException("验签时发生异常，content = " + content + "charset = " + charset
                    + ", sign = " + sign, e);
        }
    }

}
