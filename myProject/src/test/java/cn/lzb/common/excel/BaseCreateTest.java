package cn.lzb.common.excel;

import cn.lzb.common.excel.export.ExportExcelContext;
import cn.lzb.common.excel.export.ExportExcelFacade;
import cn.lzb.common.excel.export.impl.BasicExportExcelAdapter;
import cn.lzb.common.excel.export.impl.CommonExportExcelImpl;

/**
 * 功能描述：导出生成Excel文件基本Adapter测试类
 *
 * @author: Zhenbin.Li
 * email： zhenbin.li@okhqb.com
 * company：华强北在线
 * Date: 13-12-6 Time：上午12:14
 */
public class BaseCreateTest {

    /**
     * 导出Excel文件接口
     */
    private static ExportExcelFacade exportExcelFacade = new CommonExportExcelImpl();

    public static void main(String[] args) {

        exportExcelFacade.export(new BasicExportExcelAdapter<Object>() {
            @Override
            public void init(ExportExcelContext<Object> exportContext) {
                // 初始化上下文
            }

            @Override
            public void create() {
                // 创建Excel文件
            }
        });
    }
}
