/**
 * okhqb.com.
 * Copyright (c) 2009-2013 All Rights Reserved.
 */
package cn.lzb.common.sign;


import cn.lzb.common.sign.util.KeyEnum;

/**
 * 密钥加载器
 * 
 * @author ZhouJun
 * @version $Id: SignKeyLoader.java, v 0.1 2013-2-20 上午10:36:01 ZhouJun Exp $
 */
public interface SignKeyLoader {

    /**
     * 获取指定密钥值的秘钥
     * 
     * @param keyEnum 指定密钥值
     * @return 密钥
     */
    public String getKey(KeyEnum keyEnum);

}
