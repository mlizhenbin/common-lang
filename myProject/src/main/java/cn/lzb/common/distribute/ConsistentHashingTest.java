package cn.lzb.common.distribute;

import cn.lzb.common.lang.CollectionUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 功能描述：一致性Hash测试
 *
 * @author: Zhenbin.Li
 * email： zhenbin.li@okhqb.com
 * company：华强北在线
 * Date: 14-5-18 Time：下午11:25
 */
public class ConsistentHashingTest {

    /**
     * 打印cn.lzb.common.distribute.ConsistentHashingTest.java日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsistentHashingTest.class);

    /**
     * 虚拟节点个数
     */
    private Map<Long, Object> attributes = Maps.newHashMap();

    /**
     * cache个数
     */
    private final List<Object> caches;

    /**
     * 每个cache节点关联的虚拟节点个数
     */
    private final int CACHE_NUM = 10;

    /**
     * 在构造时候设置cache的数量
     *
     * @param caches
     */
    public ConsistentHashingTest(List<Object> caches) {
        this.caches = caches;
        if (CollectionUtil.isEmpty(caches)) {
            throw new RuntimeException("初始化cache为空");
        }

        int cacheIndex = 0;
        for (Object cache : caches) {
            for (int i = 0; i < CACHE_NUM; i++) {
                attributes.put(hash("cache-" + cacheIndex + "-attribute-" + i), cache);
            }
            cacheIndex++;
        }
    }

    protected void printAttributeNums() {
        Set<Object> values = Sets.newHashSet();
        for (Object cache : attributes.values()) {
            values.add(cache);
        }
        for (Object cache : values) {
            int keyCount = 0;
            for (Long key : attributes.keySet()) {
                if (attributes.get(key).equals(cache)) {
                    keyCount++;
                }
            }
            System.out.println("cache=" + cache + ", key num=" + keyCount);
        }
    }

    protected void printAttributes() {
        for (Long attributeKey : attributes.keySet()) {
            System.out.println("KEY=" + attributes + ", VALUE=" + attributes.get(attributeKey));
        }
    }

    public static void main(String[] args) {
        List<Object> caches = Lists.newArrayList();
        caches.add("A");
        caches.add("B");
        caches.add("C");
        caches.add("D");
        ConsistentHashingTest test = new ConsistentHashingTest(caches);
        test.printAttributeNums();
    }


    /**
     * MurMurHash算法，是非加密HASH算法，性能很高，
     * 比传统的CRC32,MD5，SHA-1（这两个算法都是加密HASH算法，复杂度本身就很高，带来的性能上的损害也不可避免）
     * 等HASH算法要快很多，而且据说这个算法的碰撞率很低.
     * http://murmurhash.googlepages.com/
     */
    private Long hash(String key) {

        ByteBuffer buf = ByteBuffer.wrap(key.getBytes());
        int seed = 0x1234ABCD;

        ByteOrder byteOrder = buf.order();
        buf.order(ByteOrder.LITTLE_ENDIAN);

        long m = 0xc6a4a7935bd1e995L;
        int r = 47;

        long h = seed ^ (buf.remaining() * m);

        long k;
        while (buf.remaining() >= 8) {
            k = buf.getLong();

            k *= m;
            k ^= k >>> r;
            k *= m;

            h ^= k;
            h *= m;
        }

        if (buf.remaining() > 0) {
            ByteBuffer finish = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
            finish.put(buf).rewind();
            h ^= finish.getLong();
            h *= m;
        }

        h ^= h >>> r;
        h *= m;
        h ^= h >>> r;

        buf.order(byteOrder);
        return h;
    }
}
