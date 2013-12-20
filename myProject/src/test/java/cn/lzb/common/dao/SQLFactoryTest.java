package cn.lzb.common.dao;

/**
 * 功能描述：
 *
 * @author: Zhenbin.Li
 * email： zhenbin.li@okhqb.com
 * company：华强北在线
 * Date: 13-12-8 Time：下午1:59
 */
public class SQLFactoryTest {


    public static void main(String[] args) {
        SQLFactory sqlFactory = new SQLFactory("SqlTest.xml");
        sqlFactory.getSQL("findAllSalesOrders", null);
    }
}
