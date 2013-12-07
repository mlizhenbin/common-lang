package cn.lzb.common.excel.impl;

import java.util.List;
import java.util.Map;

import cn.lzb.common.lang.CollectionUtil;

import com.google.common.collect.Lists;

/**
 * 功能描述：创建Excel抽象实现
 *
 * @author: Zhenbin.Li
 * email： zhenbin.li@okhqb.com
 * company：华强北在线
 * Date: 13-11-19 Time：下午12:44
 */
public abstract class DefaultExportExcelAdapter<T> extends BaseExportExcelAdapter<T> {

    /**
     * 生成Excel文件主体数据集合
     */
    protected List<String[]> excelData = Lists.newArrayList();

    /**
     * 构造方法
     */
    public DefaultExportExcelAdapter() {
        // 处理导出数据
        handleExportData(exportExcelContext.getExports(), excelData);
        // 创建
        create();
    }

    /**
     * 业务对象集合转换为String[]集合，数据转换为Excel导出数据
     *
     * @param list      业务对象集合
     * @param excelData 生成Excel文件数据数组集合
     */
    public abstract void handleExportData(List<T> list, List<String[]> excelData);

    /**
     * 创建生成Excel文件
     */
    public void create() {
        // 处理表格名称及其标题
        handleColTitle();
        // 处理导出文件数据
        handleBodyData();
        // 处理生成单元格宽度
        handleColumnWidth();
        // 生成备注
        handleRemark();
        // 写文件
        write();
    }

    @Override
    public void handleBodyData() {
        super.handleBodyData();
        Map<Integer, String> rowColors = exportExcelContext.getRowColors();
        if (CollectionUtil.isEmpty(rowColors)) {
            exportExcelFactory.createBody(excelData);
        } else {
            exportExcelFactory.createBodyColor(excelData, rowColors);
        }
    }
}
