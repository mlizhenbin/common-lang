package cn.lzb.common.lang;

import cn.lzb.common.excel.enity.Subject;
import com.beust.jcommander.internal.Lists;

import java.util.List;
import java.util.Map;

/**
 * 功能描述：自动化生成代码启动入口
 *
 * @author: Zhenbin.Li
 * email： lizhenbin08@sina.com
 * company：一加科技
 * Date: 15/8/5 Time: 15:33
 */
public class ListFieldTest {

    public static void main(String[] args) {

        List<Subject> subjectList = Lists.newArrayList();
        for (int i = 0; i < 5; i++) {
            Subject subject = new Subject();
            subject.setCode(i + "");
            subject.setName("a" + i);
//            subject.setScore(new BigDecimal(i));

            subjectList.add(subject);
        }

        Map<String, Subject> map = ListFieldConvertUtils.getObjectMap(subjectList, "code");

        System.out.println(map);

    }
}
