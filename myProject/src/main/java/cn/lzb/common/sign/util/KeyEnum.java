/**
 * okhqb.com.
 * Copyright (c) 2009-2013 All Rights Reserved.
 */
package com.okhqb.security.sign.util;

/**
 * 
 * 
 * @author ZhouJun
 * @version $Id: KeyEnum.java, v 0.1 2013-2-20 上午10:43:28 ZhouJun Exp $
 */
public enum KeyEnum {

    /** RSA加密共用 */
    RSA_PUBLIC_KEY("rsa-publicKey"),

    /** RAS加密私钥 */
    RSA_PRIVATE_KEY("rsa-privateKey"),

    /** 平安保险加密密钥 */
    DES_INSURANCE_KEY("des_insurance_key");

    ;

    private String key;

    private KeyEnum(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public static KeyEnum getKeyEnum(String key) {
        if (key == null) {
            return null;
        }

        for (KeyEnum keyEnum : values()) {
            if (key.equals(keyEnum.getKey())) {
                return keyEnum;
            }
        }
        return null;
    }
}
