package cn.lzb.common.excel.impl;

import cn.lzb.common.excel.ExportExcelFacade;

/**
 * 功能描述： 基本的导出Excel文件实现类
 *
 * @author: Zhenbin.Li
 * email： zhenbin.li@okhqb.com
 * company：华强北在线
 * Date: 13-12-5 Time：下午7:36
 */
public class CommonExportExcelImpl implements ExportExcelFacade {

    @Override
    public <T> void export(DefaultExportExcelAdapter<? super T> adapter) {
    }

    @Override
    public <T> void export(MultipartExportExcelAdapter<? super T> adapter) {
    }

    @Override
    public <T> void export(BaseExportExcelAdapter<? super T> adapter) {
    }
}
