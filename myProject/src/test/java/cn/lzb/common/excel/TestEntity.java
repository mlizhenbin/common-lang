package cn.lzb.common.excel;


import cn.lzb.common.excel.imported.annotation.CellMapper;

/**
 * 功能描述：
 *
 * @author: Zhenbin.Li
 * email： zhenbin.li@okhqb.com
 * company：华强北在线
 * Date: 2015/1/10 Time：12:04
 */
public class TestEntity {
    @CellMapper(name = "ID", order = 0)
    private Integer id;

    @CellMapper(name = "名称", order = 1)
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "id=" + id +", name=" + name;
    }
}
