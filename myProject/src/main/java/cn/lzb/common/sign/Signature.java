/**
 * okhqb.com.
 * Copyright (c) 2009-2013 All Rights Reserved.
 */
package com.okhqb.security.sign;

import java.security.SignatureException;

import com.okhqb.security.sign.util.KeyEnum;

/**
 * 
 * 
 * @author ZhouJun
 * @version $Id: Signature.java, v 0.1 2013-2-20 上午10:54:12 ZhouJun Exp $
 */
public interface Signature {

    public String sign(String content, KeyEnum keyEnum, String charset) throws SignatureException;

    public boolean verify(String content, KeyEnum keyEnum, String charset, String sign)
                                                                                       throws SignatureException;

    public String sign(String content, String charset) throws SignatureException;

    public boolean verify(String content, String charset, String sign) throws SignatureException;

    public String sign(String content, String privateKey, String charset) throws SignatureException;

    public boolean verify(String content, String publicKey, String charset, String sign)
                                                                                        throws SignatureException;

}
