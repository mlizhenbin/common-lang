package cn.lzb.common.common;

/**
 * 功能描述：系列化与反系列化读写接口
 *
 * @author: Zhenbin.Li
 * email： zhenbin.li@okhqb.com
 * company：华强北在线
 * Date: 14-1-24 Time：上午11:03
 */
public interface SerializeHandler {

    /**
     * 对象系列化，持久化对象
     *
     * @param t
     * @param <T>
     */
    public <T> void write(T t);

    /**
     * 对象反系列化
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T read(Class<T> clazz);

}
