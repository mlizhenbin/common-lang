package cn.lzb.common.common;

/**
 * 功能描述：系列化对象对应的path
 *
 * @author: Zhenbin.Li
 * email： zhenbin.li@okhqb.com
 * company：华强北在线
 * Date: 14-1-24 Time：上午11:26
 */
public interface SerializePath {

    /**
     * 获取文件path
     * eg：/usr/test.txt
     *
     * @return
     */
    public String getPath();
}
