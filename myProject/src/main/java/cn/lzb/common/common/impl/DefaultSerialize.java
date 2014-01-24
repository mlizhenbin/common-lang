package cn.lzb.common.common.impl;

import cn.lzb.common.common.SerializeHandler;
import cn.lzb.common.common.SerializePath;
import cn.lzb.common.lang.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;

/**
 * 功能描述：Default implements Serialize
 *
 * @author: Zhenbin.Li
 * email： zhenbin.li@okhqb.com
 * company：华强北在线
 * Date: 14-1-24 Time：上午11:11
 */
public class DefaultSerialize implements SerializeHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSerialize.class);

    @Autowired
    private SerializePath serializePath;

    @Override
    public <T> void write(T t) {

        //
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;

        String filePath = serializePath.getPath();
        if (StringUtil.isBlank(filePath)) {
            throw new RuntimeException("系列化对象Path为空");
        }

        try {
            fileOutputStream = new FileOutputStream(filePath);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(t);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (FileNotFoundException e) {
            LOGGER.error("" + filePath, e);
        } catch (IOException e) {
            LOGGER.error("" + filePath, e);
        } finally {
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    LOGGER.error("关闭ObjectOutputStream异常", e);
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    LOGGER.error("关闭FileOutputStream异常", e);
                }
            }
        }
    }

    @Override
    public <T> T read(Class<T> clazz) {

        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;

        String filePath = serializePath.getPath();
        if (StringUtil.isBlank(filePath)) {
            throw new RuntimeException("反系列化对象Path为空");
        }

        try {
            fileInputStream = new FileInputStream(filePath);
            objectInputStream = new ObjectInputStream(fileInputStream);

            return (T)objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    LOGGER.error("关闭ObjectInputStream异常", e);
                }
            }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    LOGGER.error("关闭FileInputStream异常", e);
                }
            }
        }

        return null;
    }
}
