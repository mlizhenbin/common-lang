package cn.lzb.common.sign.util;

import com.google.common.collect.Maps;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;

/**
 * 功能描述：RSA加密工具类
 *
 * @author: Zhenbin.Li
 * email： lizhenbin08@sina.cn
 * company：lzbruby.org
 * Date: 15/6/12 Time：23:41
 */
public class RSAUtils {

    private RSAUtils() {
    }

    /**
     * sl4j
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RSAUtils.class);

    private static final String SIGN_TYPE = "RSA";

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /** */
    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 获取一对RSA公钥和密钥
     *
     * @return
     * @throws Exception
     */
    public static Map<KeyEnum, String> getRsaSignKey() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(SIGN_TYPE);

        keyPairGen.initialize(1024, new SecureRandom());
        KeyPair keyPair = keyPairGen.generateKeyPair();

        // 公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        // 私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        String sPublicKey = encryptBASE64(publicKey.getEncoded());

        Map<KeyEnum, String> keys = Maps.newLinkedHashMap();
        keys.put(KeyEnum.RSA_PUBLIC_KEY, sPublicKey);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("产生RSA密钥， 获取公钥成功");
        }

        String sPrivateKey = encryptBASE64(privateKey.getEncoded());
        keys.put(KeyEnum.RSA_PRIVATE_KEY, sPrivateKey);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("产生RSA密钥， 获取私钥成功");
        }

        return keys;
    }

    /**
     * RSA加密
     *
     * @param keyEnum
     * @param key
     * @param plainText
     * @return
     * @throws Exception
     */
    public static String encrypt(KeyEnum keyEnum, String key, String plainText) throws Exception {
        if (keyEnum == null || StringUtils.isBlank(key) || StringUtils.isBlank(plainText)) {
            throw new RuntimeException("RSA加密参数错误!");
        }

        switch (keyEnum) {
            case RSA_PRIVATE_KEY:
                RSAPrivateKey rsaPrivateKey = loadPrivateKey(key);
                return encryptBASE64(encrypt(rsaPrivateKey, plainText.getBytes()));
            case RSA_PUBLIC_KEY:
                RSAPublicKey rsaPublicKey = loadPublicKey(key);
                return encryptBASE64(encrypt(rsaPublicKey, plainText.getBytes()));
            default:
                throw new RuntimeException("RSA不支持的加密类型, keyEnum=" + keyEnum);
        }
    }

    /**
     * RSA加密
     *
     * @param keyEnum
     * @param key
     * @param plainText
     * @return
     * @throws Exception
     */
    public static String encrypt(KeyEnum keyEnum, String key, String plainText, String charset) throws Exception {
        if (keyEnum == null || StringUtils.isBlank(key) || StringUtils.isBlank(plainText) || StringUtils.isBlank(charset)) {
            throw new RuntimeException("RSA加密参数错误!");
        }

        switch (keyEnum) {
            case RSA_PRIVATE_KEY:
                RSAPrivateKey rsaPrivateKey = loadPrivateKey(key);

                byte[] encrypt = encrypt(rsaPrivateKey, plainText.getBytes(charset));
                return encryptBASE64(encrypt);
            case RSA_PUBLIC_KEY:
                RSAPublicKey rsaPublicKey = loadPublicKey(key);
                return encryptBASE64(encrypt(rsaPublicKey, plainText.getBytes(charset)));
            default:
                throw new RuntimeException("RSA不支持的加密类型, keyEnum=" + keyEnum);
        }
    }

    /**
     * 公钥加密过程
     *
     * @param publicKey     公钥
     * @param plainTextData 明文数据
     * @return
     * @throws Exception 加密过程中的异常信息
     */
    public static byte[] encrypt(RSAPublicKey publicKey, byte[] plainTextData) throws Exception {
        if (publicKey == null) {
            throw new RuntimeException("加密公钥为空!");
        }

        ByteArrayOutputStream out = null;
        try {
            Cipher cipher = Cipher.getInstance(SIGN_TYPE);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            int inputLen = plainTextData.length;
            out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(plainTextData, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(plainTextData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("公钥加密异常!", e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * 私钥加密过程
     *
     * @param privateKey    私钥
     * @param plainTextData 明文数据
     * @return
     * @throws Exception 加密过程中的异常信息
     */
    public static byte[] encrypt(RSAPrivateKey privateKey, byte[] plainTextData) throws Exception {
        if (privateKey == null) {
            throw new RuntimeException("加密私钥为空!");
        }

        ByteArrayOutputStream out = null;
        try {
            Cipher cipher = Cipher.getInstance(SIGN_TYPE);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);

            int inputLen = plainTextData.length;
            out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(plainTextData, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(plainTextData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] output = out.toByteArray();
            return output;
        } catch (Exception e) {
            throw new RuntimeException("私钥加密异常!", e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * RSA加密
     *
     * @param keyEnum
     * @param key
     * @param plainText
     * @return
     * @throws Exception
     */
    public static String decrypt(KeyEnum keyEnum, String key, String plainText) throws Exception {
        if (keyEnum == null || StringUtils.isBlank(key) || StringUtils.isBlank(plainText)) {
            throw new RuntimeException("RSA解密参数错误!");
        }

        byte[] bytes = decryptBASE64(plainText);
        switch (keyEnum) {
            case RSA_PRIVATE_KEY:
                RSAPrivateKey rsaPrivateKey = loadPrivateKey(key);
                return new String(decrypt(rsaPrivateKey, bytes));
            case RSA_PUBLIC_KEY:
                RSAPublicKey rsaPublicKey = loadPublicKey(key);
                return new String(decrypt(rsaPublicKey, bytes));
            default:
                throw new RuntimeException("RSA不支持的解密类型, keyEnum=" + keyEnum);
        }
    }

    /**
     * RSA加密
     *
     * @param keyEnum
     * @param key
     * @param plainText
     * @param charset
     * @return
     * @throws Exception
     */
    public static String decrypt(KeyEnum keyEnum, String key, String plainText, String charset) throws Exception {
        if (keyEnum == null || StringUtils.isBlank(key) || StringUtils.isBlank(plainText)) {
            throw new RuntimeException("RSA解密参数错误!");
        }

        byte[] bytes = decryptBASE64(plainText);
        switch (keyEnum) {
            case RSA_PRIVATE_KEY:
                RSAPrivateKey rsaPrivateKey = loadPrivateKey(key);
                return new String(decrypt(rsaPrivateKey, bytes), charset);
            case RSA_PUBLIC_KEY:
                RSAPublicKey rsaPublicKey = loadPublicKey(key);
                return new String(decrypt(rsaPublicKey, bytes), charset);
            default:
                throw new RuntimeException("RSA不支持的解密类型, keyEnum=" + keyEnum);
        }
    }

    /**
     * 私钥解密过程
     *
     * @param privateKey 私钥
     * @param cipherData 密文数据
     * @return 明文
     * @throws Exception 解密过程中的异常信息
     */
    public static byte[] decrypt(RSAPrivateKey privateKey, byte[] cipherData) throws Exception {
        if (privateKey == null) {
            throw new RuntimeException("解密私钥为空!");
        }

        ByteArrayOutputStream out = null;
        try {
            Cipher cipher = Cipher.getInstance(SIGN_TYPE);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            int inputLen = cipherData.length;
            out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(cipherData, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(cipherData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] output = out.toByteArray();
            return output;
        } catch (Exception e) {
            throw new RuntimeException("私钥解密失败!", e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * 公钥解密过程
     *
     * @param publicKey  公钥
     * @param cipherData 密文数据
     * @return 明文
     * @throws Exception 解密过程中的异常信息
     */
    public static byte[] decrypt(RSAPublicKey publicKey, byte[] cipherData) throws Exception {
        if (publicKey == null) {
            throw new RuntimeException("解密公钥为空!");
        }

        ByteArrayOutputStream out = null;
        try {
            Cipher cipher = Cipher.getInstance(SIGN_TYPE);
            cipher.init(Cipher.DECRYPT_MODE, publicKey);

            int inputLen = cipherData.length;
            out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(cipherData, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(cipherData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("公钥解密失败!", e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * 16进制数组变成字符串
     *
     * @param key
     * @return
     * @throws Exception
     */
    private static String encryptBASE64(byte[] key) throws Exception {
        return new String(Base64.encodeBase64(key));
    }

    /**
     * 字符串转换为16进制
     *
     * @param key
     * @return
     * @throws Exception
     */
    private static byte[] decryptBASE64(String key) throws Exception {
        return Base64.decodeBase64(key.getBytes());
    }

    /**
     * 从字符串中加载公钥
     *
     * @param publicKey 公钥数据字符串
     * @throws Exception 加载公钥时产生的异常
     */
    private static RSAPublicKey loadPublicKey(String publicKey) throws Exception {

        try {
            byte[] buffer = Base64.decodeBase64(publicKey.getBytes());
            KeyFactory keyFactory = KeyFactory.getInstance(SIGN_TYPE);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            throw new RuntimeException("获取RSAPublicKey失败, publicKey=" + publicKey, e);
        }
    }

    /**
     * 从字符串中加载私钥
     *
     * @param privateKey 私钥数据字符串
     * @throws Exception 加载公钥时产生的异常
     */
    private static RSAPrivateKey loadPrivateKey(String privateKey) throws Exception {
        try {
            byte[] buffer = Base64.decodeBase64(privateKey.getBytes());
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance(SIGN_TYPE);
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            throw new RuntimeException("获取RSAPrivateKey失败, privateKey=" + privateKey, e);
        }
    }

    /**
     * RSA KEY类型
     */
    public enum KeyEnum {
        RSA_PUBLIC_KEY,
        RSA_PRIVATE_KEY;
    }
}
