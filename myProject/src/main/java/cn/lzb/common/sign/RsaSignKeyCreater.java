package com.okhqb.security.sign;

import java.util.List;

/**
 * 功能描述： 获取RSA加密方式公钥与私钥
 *
 * @author: Zhenbin.Li
 * email： zhenbin.li@okhqb.com
 * company：华强北在线
 * Date: 13-2-2 Time：上午9:48
 */
public interface RsaSignKeyCreater {

    /**
     * 获取RSA密钥，密钥包括公钥，私钥
     *
     * @return
     *      第0位：公钥
     *      第1位：私钥
     */
    public List<String> getRsaSignKey() throws Exception;
}
