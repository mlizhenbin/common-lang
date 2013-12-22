package cn.lzb.common.common;

/**
 * 功能描述：类get/set方法Head名称
 *
 * @author: Zhenbin.Li
 * email： zhenbin.li@okhqb.com
 * company：华强北在线
 * Date: 13-12-8 Time：下午1:21
 */
public enum ClassPropertyMethodHead {

    /**
     * get方法
     */
    GET("get"),

    /**
     * boolean方法
     */
    IS("is"),

    /**
     * set方法
     */
    SET("set");

    /**
     * 方法头参数
     */
    private String methodHeadCode;

    /**
     * 构造方法
     *
     * @param methodHeadCode
     */
    private ClassPropertyMethodHead(String methodHeadCode) {
        this.methodHeadCode = methodHeadCode;
    }

    /**
     * 获取方法Head枚举
     *
     * @return
     */
    public String getMethodHeadCode() {
        return methodHeadCode;
    }
}
