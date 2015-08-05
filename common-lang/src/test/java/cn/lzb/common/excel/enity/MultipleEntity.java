package cn.lzb.common.excel.enity;

import java.util.List;

/**
 * 功能描述：合并单元格测试类
 *
 * @author: Zhenbin.Li
 * email： zhenbin.li@okhqb.com
 * company：华强北在线
 * Date: 13-12-5 Time：上午10:31
 */
public class MultipleEntity extends SimpleEntity {

    private List<Subject> subjects;

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

}
