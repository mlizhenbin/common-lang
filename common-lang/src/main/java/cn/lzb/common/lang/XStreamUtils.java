package cn.lzb.common.lang;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * 一加科技
 *
 * @description: XStream工具类，将XML与JAVA对象之间互相转换工具类
 * @author: Zhenbin.Li
 * @createDate: 15/6/17 16:09
 */
public class XStreamUtils {

    private XStreamUtils() {
    }

    /**
     * 对象直接转换为XML字符串格式
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T> String toXml(T t) {
        XStream xstream = new XStream();
        xstream.processAnnotations(t.getClass());
        return xstream.toXML(t);
    }

    /**
     * XML直接转化为对象
     *
     * @param xml
     * @param clazz
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
	public static <T> T toBean(String xml, Class<T> clazz) {
        XStream xstream = new XStream(new DomDriver());
        xstream.processAnnotations(clazz);
        T obj = (T) xstream.fromXML(xml);
        return obj;
    }
}
