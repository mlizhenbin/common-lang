package cn.lzb.common.sign.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.net.URLEncoder;

/**
 * 功能描述：DES加密工具类解决与.NET对应的加密方式，
 * <p/>
 * 其中.net使用的工作方式是:CBC， JAVA的Byte数组范围为-128~127，.net的Byte数组范围为0~256需要做转换
 * <p/>
 * <p/>
 * .NET实现DES加密方式，对应的源代码:
 * DESCryptoServiceProvider des = new DESCryptoServiceProvider();
 * byte[] inputByteArray;
 * inputByteArray = encode.GetBytes(Text);
 * des.Key = ASCIIEncoding.ASCII.GetBytes(System.Web.Security.FormsAuthentication.HashPasswordForStoringInConfigFile(sKey, "md5").Substring(0, 8));
 * des.IV = ASCIIEncoding.ASCII.GetBytes(System.Web.Security.FormsAuthentication.HashPasswordForStoringInConfigFile(sKey, "md5").Substring(0, 8));
 * System.IO.MemoryStream ms = new System.IO.MemoryStream();
 * CryptoStream cs = new CryptoStream(ms, des.CreateEncryptor(), CryptoStreamMode.Write);
 * cs.Write(inputByteArray, 0, inputByteArray.Length);
 * cs.FlushFinalBlock();
 * StringBuilder ret = new StringBuilder();
 * foreach (byte b in ms.ToArray())
 * {
 * ret.AppendFormat("{0:X2}", b);
 * }
 * return ret.ToString();
 * <p/>
 * <p/>
 * JAVA代码需要做的对应转换加密方式
 *
 * @author: Zhenbin.Li
 * email： zhenbin.li@okhqb.com
 * company：华强北在线
 * Date: 13-12-18 Time：下午3:05
 */
public class NetDESUtils {

    /**
     * 加密方式
     */
    protected static final String KEY_ALGORITHM = "DES";

    /**
     * 默认填充工作方式
     */
    protected static final String DEFAULT_CIPHER_ALGORITHM = "DES/CBC/PKCS5Padding";

    /**
     * 字符集
     */
    protected static final String Charset = "UTF-8";

    /**
     * 解密
     *
     * @param message
     * @param key
     * @return
     * @throws Exception
     */
    public static String decrypt(String message, String key) throws Exception {
        byte[] byteSrc = convertHexString(message);
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, getKey(key, Charset), getIvParameterSpec(key, Charset));
        return new String(cipher.doFinal(byteSrc));
    }

    /**
     * 解密
     *
     * @param message
     * @param key
     * @param charset
     * @return
     * @throws Exception
     */
    public static String decrypt(String message, String key, String charset) throws Exception {
        byte[] byteSrc = convertHexString(message);
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, getKey(key, charset), getIvParameterSpec(key, charset));
        return new String(cipher.doFinal(byteSrc));
    }

    /**
     * 加密
     *
     * @param message
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(String message, String key) throws Exception {
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, getKey(key, Charset), getIvParameterSpec(key, Charset));
        return cipher.doFinal(message.getBytes(Charset));
    }

    /**
     * 加密
     *
     * @param message
     * @param key
     * @param charset
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(String message, String key, String charset) throws Exception {
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, getKey(key, charset), getIvParameterSpec(key, charset));
        return cipher.doFinal(message.getBytes(Charset));
    }

    /**
     * 获取IvParameterSpec
     *
     * @param key
     * @param charset
     * @return
     * @throws Exception
     */
    public static IvParameterSpec getIvParameterSpec(String key, String charset) throws Exception {
        return new IvParameterSpec(key.getBytes(charset));
    }

    /**
     * 获取KEY
     *
     * @param key
     * @param charset
     * @return
     * @throws Exception
     */
    public static SecretKey getKey(String key, String charset) throws Exception {
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes(charset));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
        return keyFactory.generateSecret(desKeySpec);
    }

    /**
     * 转换为十六进制
     *
     * @param ss
     * @return
     */
    public static byte[] convertHexString(String ss) {
        byte digest[] = new byte[ss.length() / 2];
        for (int i = 0; i < digest.length; i++) {
            String byteString = ss.substring(2 * i, 2 * i + 2);
            int byteValue = Integer.parseInt(byteString, 16);
            digest[i] = (byte) byteValue;
        }
        return digest;
    }

    /**
     * 转换为十六进制数组
     *
     * @param b
     * @return
     */
    public static String toHexString(byte b[]) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String plainText = Integer.toHexString(0xff & b[i]);
            if (plainText.length() < 2)
                plainText = "0" + plainText;
            hexString.append(plainText);
        }
        return hexString.toString();
    }

    public static void main(String[] args) throws Exception {

        String key = MD5Utils.encode("testkey", "utf-8").substring(0, 8);
        System.out.println("密钥明文key=" + key);

        String value = "www.okhqb.com";
        String urlEncoderValue = URLEncoder.encode(value, "utf-8").toLowerCase();
        System.out.println("加密数据:" + urlEncoderValue);
        String encryptStr = toHexString(encrypt(urlEncoderValue, key)).toUpperCase();

        System.out.println("加密后的数据为:" + encryptStr);
        String b = java.net.URLDecoder.decode(decrypt(encryptStr, key), "utf-8");
        System.out.println("解密后的数据:" + b);
    }
}
