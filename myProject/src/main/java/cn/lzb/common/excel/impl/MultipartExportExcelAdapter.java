package cn.lzb.common.excel.impl;

import java.util.List;
import java.util.Map;

import cn.lzb.common.lang.CollectionUtil;

import com.google.common.collect.Lists;

/**
 * 功能描述：复杂Excel文件生成Adapter
 *
 * @author: Zhenbin.Li
 * email： zhenbin.li@okhqb.com
 * company：华强北在线
 * Date: 13-12-4 Time：上午10:30
 */
public abstract class MultipartExportExcelAdapter<T> extends BasicExportExcelAdapter<T> {

    /**
     * 生成Excel文件主体数据集合
     */
    protected List<String[][]> excelData = Lists.newArrayList();

    /**
     * 构造方法
     */
    public MultipartExportExcelAdapter() {
    	// 处理导出数据
        handleExportData(exportExcelContext.getExports(), excelData);
        // 创建
        create();
    }

    /**
     * 业务对象集合转换为String[][]集合，数据转换为Excel导出数据
     * 需要合并时数组格式为
     * <p/>
     * <p/>
     * <p>二维数组格式：
     * [00001][2][3][0004][5]
     * [null][7][8][null][5]
     * [null][7][8][null][5]
     * </p>
     *
     * @param list      业务对象集合
     * @param excelData 生成Excel文件数据数组集合
     */
    public abstract void handleExportData(List<T> list, List<String[][]> excelData);

    /**
     * 创建Excel文件
     */
    @Override
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
            exportExcelFactory.createMultipleBody(excelData);
        } else {
            exportExcelFactory.createMultipleBody(excelData, rowColors);
        }
    }
}
