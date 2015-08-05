package cn.lzb.common.common.impl;

import cn.lzb.common.common.SerializeHandler;
import cn.lzb.common.common.SerializePath;
import cn.lzb.common.lang.StringUtil;
import com.thoughtworks.xstream.XStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 功能描述：XML方式的系列化与反序列化
 *
 * @author: Zhenbin.Li
 * email： zhenbin.li@okhqb.com
 * company：华强北在线
 * Date: 14-1-24 Time：上午11:56
 */
public class XmlSerialize implements SerializeHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(XmlSerialize.class);

    @Autowired
    private SerializePath serializePath;

    @Override
    public <T> void write(T t) {

        //
        FileOutputStream fileOutputStream = null;
        XStream xStream = new XStream();
        xStream.alias(t.getClass().getSimpleName(), t.getClass());

        String filePath = serializePath.getPath();
        if (StringUtil.isBlank(filePath)) {
            throw new RuntimeException("XML系列化对象Path为空");
        }

        try {
            fileOutputStream = new FileOutputStream(filePath);
            xStream.toXML(t, fileOutputStream);
        } catch (FileNotFoundException e) {
            LOGGER.error("" + filePath, e);
        } finally {
            if (fileOutputStream == null) {
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

        String filePath = serializePath.getPath();
        if (StringUtil.isBlank(filePath)) {
            throw new RuntimeException("XML反系列化对象Path为空");
        }

        XStream xStream = new XStream();
        xStream.alias(clazz.getSimpleName(), clazz);

        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(filePath);
            return (T) xStream.fromXML(fileInputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream == null) {
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
