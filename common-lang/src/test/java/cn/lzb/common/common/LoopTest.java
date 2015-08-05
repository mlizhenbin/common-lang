package cn.lzb.common.common;

/**
 * 功能描述：
 *
 * @author: Zhenbin.Li
 * email： zhenbin.li@okhqb.com
 * company：华强北在线
 * Date: 14-2-24 Time：上午10:35
 */
public class LoopTest {

    public static void test1() {

        int i = 0;

        for (int j = 0; j < 10; j++) {
            i = i++;
        }
        System.out.println("i=" + i);
    }


    public static void test2() {

        int i = 0;

        for (int j = 0; j < 10; j++) {
            i++;
        }
        System.out.println("i=" + i);
    }

    public static void main(String[] args) {
        test1();
        test2();
    }
}
