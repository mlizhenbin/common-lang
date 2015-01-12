package cn.lzb.common.excel.imported.context;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 功能描述：从文件路径中读取Excel文件上下文
 *
 * @author: Zhenbin.Li
 * email： zhenbin.li@okhqb.com
 * company：华强北在线
 * Date: 2015/1/10 Time：10:16
 */
public class ExcelPathContext<T> extends ExcelCommonContext<T> {

    /**
     * 文件路径
     */
    private String excelFilePath;

    public String getExcelFilePath() {
        return excelFilePath;
    }

    public void setExcelFilePath(String excelFilePath) {
        this.excelFilePath = excelFilePath;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
