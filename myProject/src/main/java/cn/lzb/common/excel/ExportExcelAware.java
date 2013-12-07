package cn.lzb.common.excel;


/**
 * 功能描述： 创建Excel接口
 *
 * @author: Zhenbin.Li
 * email： zhenbin.li@okhqb.com
 * company：华强北在线
 * Date: 13-11-19 Time：下午12:42
 */
public interface ExportExcelAware<T> {

    /**
     * 初始化导出生成Excel文件上下文
     *
     * @param exportContext 创建生成Excel文件上下文
     */
    public void init(ExportExcelContext<T> exportContext);

    /**
     * 创建生成Excel文件
     */
    public void create();
}

