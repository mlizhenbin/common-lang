package cn.lzb.common.common;

import java.io.Serializable;

/**
 * 功能描述：java系列化与反系列的测试对象
 *
 * @author: Zhenbin.Li
 * email： zhenbin.li@okhqb.com
 * company：华强北在线
 * Date: 14-1-24 Time：下午1:03
 */
public class SerializeDomain implements Serializable {

    private static final long serialVersionUID = -2918914381936799564L;

    public SerializeDomain() {

        System.out.println("call constructor.");
    }

    private String userNo;

    private String userName;

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
