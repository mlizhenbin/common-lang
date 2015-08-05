package cn.lzb.common.excel.imported.context;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.InputStream;

/**
 * 功能描述：从流中获取Excel文件内容上下文
 *
 * @author: Zhenbin.Li
 * email： zhenbin.li@okhqb.com
 * company：华强北在线
 * Date: 2015/1/10 Time：13:01
 */
public class ExcelInputStreamContext<T> extends ExcelCommonContext<T> {

    /**
     * 输入流
     */
    private InputStream inputStream;

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public ExcelInputStreamContext(Class<T> clazz, InputStream inputStream) {
        super(clazz);
        this.inputStream = inputStream;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
