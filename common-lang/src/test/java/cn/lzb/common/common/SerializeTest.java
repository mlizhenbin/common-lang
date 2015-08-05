package cn.lzb.common.common;

import cn.lzb.common.common.impl.DefaultSerialize;

import java.util.Random;

/**
 * 功能描述：
 *
 * @author: Zhenbin.Li
 * email： zhenbin.li@okhqb.com
 * company：华强北在线
 * Date: 14-1-24 Time：下午1:03
 */
public class SerializeTest {

    private SerializeHandler serializeHandler = new DefaultSerialize();

    public static void main(String[] args) {

        int j = 0;
        for(int i=0; i<10; i++) {

            j = j++;
            System.out.println(j);
        }
        System.out.println(j);
    }
//
//
//    public SerializeDomain init() {
//
//        SerializeDomain domain = new SerializeDomain();
//        domain.setUserNo(String.valueOf(new Random(10).nextInt()));
//        domain.setUserName("text serial.");
//
//        serializeHandler.write(domain);
//
//    }
}
