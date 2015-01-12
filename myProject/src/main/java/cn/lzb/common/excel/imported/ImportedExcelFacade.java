package cn.lzb.common.excel.imported;


import cn.lzb.common.excel.imported.context.ExcelInputStreamContext;
import cn.lzb.common.excel.imported.context.ExcelPathContext;

import java.util.List;

/**
 * 功能描述：读取Excel文件接口
 *
 * @author: Zhenbin.Li
 * email： zhenbin.li@okhqb.com
 * company：华强北在线
 * Date: 2014/12/31 Time：10:52
 */
public interface ImportedExcelFacade {

    /**
     * 从流读取Excel文件
     *
     * @param context
     * @param <T>
     * @return
     */
    public <T> List<T> read(ExcelInputStreamContext<T> context);

    /**
     * 从文件路径Excel文件
     *
     * @param context
     * @param <T>
     * @return
     */
    public <T> List<T> read(ExcelPathContext<T> context);


}
