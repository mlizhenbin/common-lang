package cn.lzb.common.common.impl;

import cn.lzb.common.common.SerializePath;
import org.springframework.stereotype.Service;

/**
 * 功能描述：系列化对象对应的path实现
 *
 * @author: Zhenbin.Li
 * email： zhenbin.li@okhqb.com
 * company：华强北在线
 * Date: 14-1-24 Time：上午11:29
 */
@Service("serializePath")
public class SerializePathImpl implements SerializePath {

    /**
     * 配置文件路径
     */
    private String serializePath;

    @Override
    public String getPath() {
        return serializePath;
    }

    public void setSerializePath(String serializePath) {
        this.serializePath = serializePath;
    }
}
