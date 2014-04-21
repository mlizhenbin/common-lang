package cn.lzb.common.common;

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Sets;

import java.util.List;
import java.util.Set;

/**
 * 功能描述：
 *
 * @author: Zhenbin.Li
 * email： zhenbin.li@okhqb.com
 * company：华强北在线
 * Date: 14-2-25 Time：下午4:16
 */
public class CollectionTest {

    public static void main(String[] args) {

        List<String> list = Lists.newArrayList();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");

        Set<String> set1 = Sets.newHashSet();
        for (String s : list) {
            set1.add(s);
        }

        Set<String> set2 = Sets.newHashSet();
        set2.add("3");
        set2.add("4");
        set2.add("5");
        set2.add("6");

        //set1.retainAll(set2);
        Set<String> sets = Sets.newHashSet();
        for (String s : list) {
            sets.add(s);
        }
        sets.retainAll(set2);

        System.out.println(set1);
        System.out.println(set2);
        System.out.println(sets);

    }
}
