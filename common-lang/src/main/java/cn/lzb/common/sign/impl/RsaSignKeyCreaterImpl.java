package cn.lzb.common.sign.impl;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.List;

import cn.lzb.common.sign.RsaSignKeyCreater;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 功能描述： 获取RSA加密方式公钥与私钥
 *
 * @author: Zhenbin.Li
 * email： zhenbin.li@okhqb.com
 * company：华强北在线
 * Date: 13-2-2 Time：上午9:48
 */
public class RsaSignKeyCreaterImpl implements RsaSignKeyCreater {

    /** 打印CreateRsaSignKey日志信息 */
    private static final Logger LOGGER    = LoggerFactory.getLogger(RsaSignKeyCreaterImpl.class);

    private static final String SIGN_TYPE = "RSA";

    /**
     */
    public List<String> getRsaSignKey() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(SIGN_TYPE);

        keyPairGen.initialize(1024, new SecureRandom());
        KeyPair keyPair = keyPairGen.generateKeyPair();

        // 公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        // 私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        String sPublicKey = encryptBASE64(publicKey.getEncoded());
        List<String> keys = new ArrayList<String>(2);
        keys.add(sPublicKey);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("产生RSA密钥， 获取公钥成功");
        }

        String sPrivateKey = encryptBASE64(privateKey.getEncoded());
        keys.add(sPrivateKey);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("产生RSA密钥， 获取私钥成功");
        }

        return keys;
    }

    private String encryptBASE64(byte[] key) throws Exception {
        return new String(Base64.encodeBase64(key));
    }
}
