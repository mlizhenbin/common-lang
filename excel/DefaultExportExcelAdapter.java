package com.hqb360.common.excel;

import com.google.common.collect.Maps;
import com.hqb360.common.lang.ArrayUtil;
import com.hqb360.common.lang.DateUtil;
import com.hqb360.common.lang.StringUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * 功能描述：创建Excel抽象实现盘哦啊
 *
 * @author: Zhenbin.Li
 * email： zhenbin.li@okhqb.com
 * company：华强北在线
 * Date: 13-11-19 Time：下午12:44
 */
public abstract class DefaultExportExcelAdapter<T> implements ExportExcelAware<T> {

    /**
     * 文件下载的header key
     */
    private static final String RESOURCE_HEADER_KEY = "Content-Disposition";

    /**
     * 文件下载的contentType
     */
    private static final String RESOURCE_CONTENT_TYPE = "application/octet-stream;charset=UTF-8";

    /**
     * Excel Sheet默认名称
     */
    private static final String DEFAULT_SHEET_NAME = "sheet";

    /**
     * 生成Excel文件Session
     */
    private Map<SessionKey, Object> exportSession = Maps.newHashMap();

    @Override
    public synchronized void create(HttpServletResponse response, List<T> exportList) {

        // 初始化Session
        initSession(response, exportList);

        // 验证必要参数
        validExportSession();

        String sheetName = exportSession.get(SessionKey.SHEET_NAME) == null ?
                DEFAULT_SHEET_NAME : (String) exportSession.get(SessionKey.SHEET_NAME);
        ExportExcelFactory exportExcelFactory = new ExportExcelFactory(sheetName);

        //列头标题
        String[][] colTitles = (String[][]) exportSession.get(SessionKey.COL_TITLES);
        exportExcelFactory.setCols(colTitles[0].length);

        if (exportSession.get(SessionKey.TITLE) != null
                && StringUtil.isNotBlank((String) exportSession.get(SessionKey.TITLE))) {
            // 表格名称
            StringBuffer titleBuffer = new StringBuffer();
            titleBuffer.append(exportSession.get(SessionKey.TITLE)).append("(导出时间：")
                    .append(DateUtil.formatDateTime()).append(")");
            exportExcelFactory.createCaption(titleBuffer.toString());
        }
        // 列
        exportExcelFactory.createColCaption(colTitles);

        Object bodyData = exportSession.get(SessionKey.BODY_DATA);
        if (bodyData != null) {
            Object rowColor = exportSession.get(SessionKey.ROW_COLOR);
            if (rowColor == null) {
                exportExcelFactory.createBody(bodyData);
            } else {
                exportExcelFactory.createBody(bodyData, rowColor);
            }
        }

        if (exportSession.get(SessionKey.COLUMN_WIDTH) != null) {
            int[] columnWidths = (int[]) exportSession.get(SessionKey.COLUMN_WIDTH);
            if (ArrayUtil.isNotEmpty(columnWidths)) {
                exportExcelFactory.setColumnWidth(columnWidths);
            }
        }

        if (exportSession.get(SessionKey.REMARKS) != null) {
            String[] remarks = (String[]) exportSession.get(SessionKey.REMARKS);
            if (ArrayUtil.isEmpty(remarks)) {
                exportExcelFactory.createRemarks(remarks);
            }
        }

        String excelFileName = initFileName() + ".xls";
        try {
            excelFileName = new String(excelFileName.getBytes("GBK"),"ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("导出Excel业务， encode文件名称异常, excelFileName=" + excelFileName, e);
        }

        // 写文件
        OutputStream os = null;
        response.reset();

        // 写入下载响应头信息
        writeDownloadResHeader(excelFileName, response);
        try {
            os = response.getOutputStream();
            exportExcelFactory.createFile(response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException("导出Excel业务， 生成Excel文件异常", e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * Excel标题，继承时必须要实现
     *
     * @return
     */
    @Override
    public String initTitle() {
        return null;
    };

    /**
     * 获取导出Excel文件名称
     *
     * @return
     */
    @Override
    public abstract String initFileName();

    @Override
    public String initSheetName() {
        return "sheet";
    }

    /**
     * Excel列属性值，继承时必须要实现
     *
     * @return
     */
    @Override
    public abstract String[][] initColTitles();

    /**
     * 必须要传入值
     *
     * @return
     */
    @Override
    public abstract Object handleBodyData(List<T> exportList);

    /**
     * 设置单元格样式
     *
     * @return
     */
    @Override
    public Object initRowStyle() {
        return null;
    }

    @Override
    public int[] initColumnWidth() {
        return null;
    }

    /**
     * 导出Excel有无备注信息
     *
     * @return
     */
    @Override
    public String[] initRemark() {
        return null;
    }

    /**
     * 写入下载响应头信息
     * <p>声明响应类型和内容类型</p>
     *
     * @param fileName 文件名
     * @param response HTTP响应
     */
    protected static void writeDownloadResHeader(String fileName, HttpServletResponse response) {

        response.addHeader(RESOURCE_HEADER_KEY, "attachment; filename=" + fileName);
        response.setContentType(RESOURCE_CONTENT_TYPE);
    }

    /**
     * Excel文件Session属性值枚举
     */
    protected enum SessionKey {

        RESPONSE,
        BODY_DATA,
        TITLE,
        FILE_NAME,
        COL_TITLES,
        ROW_COLOR,
        REMARKS,
        COLUMN_WIDTH,
        SHEET_NAME
    }

    /**
     * 初始化生成Excel SessionKey
     *
     * @param response
     * @param exportList
     */
    protected void initSession(HttpServletResponse response, List exportList) {

        exportSession.put(SessionKey.RESPONSE, response);
        //报表数据
        exportSession.put(SessionKey.BODY_DATA, handleBodyData(exportList));
        // 报表标题
        exportSession.put(SessionKey.TITLE, initTitle());
        // 文件名称
        exportSession.put(SessionKey.FILE_NAME, initFileName());
        // 列名称
        exportSession.put(SessionKey.COL_TITLES, initColTitles());
        // 行颜色
        exportSession.put(SessionKey.ROW_COLOR, initRowStyle());
        // 备注
        exportSession.put(SessionKey.REMARKS, initRemark());
        // 列宽度
        exportSession.put(SessionKey.COLUMN_WIDTH, initColumnWidth());
        // Sheet名称
        exportSession.put(SessionKey.SHEET_NAME, initSheetName());
    }

    /**
     * 验证Session参数
     */
    protected void validExportSession() {

        if (exportSession.get(SessionKey.FILE_NAME) == null
                || StringUtil.isBlank(String.valueOf(exportSession.get(SessionKey.FILE_NAME)))) {
            throw new RuntimeException("导出Excel业务， Excel文件名称为空");
        }

        if (exportSession.get(SessionKey.COL_TITLES) == null
                || ArrayUtil.isEmpty((String[][]) exportSession.get(SessionKey.COL_TITLES))) {
            throw new RuntimeException("导出Excel业务， Excel列属性名称为空");
        }

    }
}
