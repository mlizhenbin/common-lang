package cn.lzb.common.excel.imported.context;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 功能描述：导入读取Excel文件上下文
 *
 * @author: Zhenbin.Li
 * email： zhenbin.li@okhqb.com
 * company：华强北在线
 * Date: 2015/1/10 Time：12:59
 */
public class ExcelCommonContext<T> {

    /**
     * Excel文件Sheet名称
     */
    private String sheetName;

    /**
     * 读取文件的开始行数
     */
    private Integer startRow;

    /**
     * 读取Excel文件映射的对象class
     */
    private Class<T> clazz;

    public Integer getStartRow() {
        return startRow;
    }

    public void setStartRow(Integer startRow) {
        this.startRow = startRow;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
