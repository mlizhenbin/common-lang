package cn.lzb.common.excel.impl;

import cn.lzb.common.excel.ExportExcelAware;
import cn.lzb.common.excel.ExportExcelContext;
import cn.lzb.common.lang.ArrayUtil;
import cn.lzb.common.lang.DateUtil;
import cn.lzb.common.lang.StringUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * 功能描述：导出生成Excel文件基本Adapter
 * <p/>
 * <p>实现基本的ExportExcelAware接口方法</p>
 *
 * @author: Zhenbin.Li
 * email： zhenbin.li@okhqb.com
 * company：华强北在线
 * Date: 13-12-4 Time：上午11:05
 */
public abstract class BasicExportExcelAdapter<T> implements ExportExcelAware<T> {

    /**
     * 文件下载的header key
     */
    protected static final String RESOURCE_HEADER_KEY = "Content-Disposition";

    /**
     * 文件下载的contentType
     */
    protected static final String RESOURCE_CONTENT_TYPE = "application/octet-stream;charset=UTF-8";

    /**
     * 文件扩展名
     */
    protected static final String FILE_EXTENSION = ".xlsx";

    /**
     * 生成Excel文件上下文
     */
    protected ExportExcelContext<T> context = new ExportExcelContext<T>();

    /**
     * 创建Excel工厂
     */
    protected final ExportExcelFactory factory;

    /**
     * 构造方法，初始化导出文件上线问
     */
    public BasicExportExcelAdapter() {
        // 初始化上下文参数
        init(context);
        // 验证上下文参数
        doCheckContext(context);
        // 创建生产Excel文件工厂
        this.factory = getFactory();
    }

    /**
     * 初始化导出文件上下文
     *
     * @param exportContext
     */
    public abstract void init(ExportExcelContext<T> exportContext);

    /**
     * 创建生成文件
     */
    public abstract void create();

    /**
     * 处理Excel文件数据
     */
    protected void handleBodyData() {
    }

    /**
     * 处理列标题列头标题
     */
    protected void handleColTitle() {
        // 列头标长度
        factory.setCols(getColumnSize());
        // 表格标题
        String title = context.getTitle();
        if (StringUtil.isNotBlank(title)) {
            // 表格名称
            StringBuffer titleBuffer = new StringBuffer();
            titleBuffer.append(title).append("(导出时间：").append(DateUtil.formatDateTime()).append(")");
            factory.createCaption(titleBuffer.toString());
        }
        // 列
        factory.createColCaption(context.getColTitles());
    }

    /**
     * 处理单元格的列宽度
     */
    protected void handleColumnWidth() {
        int[] columnWidths = context.getColumnWidths();
        if (ArrayUtil.isNotEmpty(columnWidths)) {
            factory.setColumnWidth(columnWidths);
        }
    }

    /**
     * 处理备注信息
     */
    protected void handleRemark() {
        String[] remarks = context.getRemarks();
        if (ArrayUtil.isNotEmpty(remarks)) {
            factory.createRemarks(remarks);
        }
    }

    /**
     * 获取Excel表格宽度
     *
     * @return
     */
    protected int getColumnSize() {
        return context.getColTitles()[0].length;
    }

    /**
     * 执行创建Excel文件
     * <p/>
     * <p>HttpServletResponse为空使用FileOutputStream创建文件</p>
     */
    protected void write() {
        // 生成文件名称
        StringBuffer fileName = new StringBuffer();
        // 写文件
        if (context.getHttpServletResponse() != null) {

            try {
                fileName.append(new String(context.getFileName().getBytes("GBK"), "ISO-8859-1"))
                        .append(FILE_EXTENSION);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Encoding创建Excel文件名称异常, fileName=" + fileName, e);
            }

            HttpServletResponse response = context.getHttpServletResponse();
            response.reset();

            // 写入下载响应头信息
            OutputStream os = null;
            writeDownloadResHeader(fileName.toString(), response);
            try {
                os = response.getOutputStream();
                factory.createFile(response.getOutputStream());
                factory.dispose();
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
        } else {
            // 直接创建生成文件
            fileName.append(context.getFilePath()).append(context.getFileName()).append(FILE_EXTENSION);
            factory.createFile(fileName.toString());
        }
    }

    /**
     * 校验创建生成Excel文件上下文参数
     *
     * @param exportContext`创建生成Excel文件上下文
     */
    protected void doCheckContext(ExportExcelContext<T> exportContext) {
        if (StringUtil.isBlank(exportContext.getFileName())) {
            throw new RuntimeException("生成Excel文件名称为空");
        }
        if (ArrayUtil.isEmpty(exportContext.getColTitles())) {
            throw new RuntimeException("生成Excel文件名称为空");
        }
        if (exportContext.getHttpServletResponse() == null && StringUtil.isBlank(context.getFilePath())) {
            throw new RuntimeException("HttpServletResponse与filePath不能同时为空！");
        }
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
     * 获取创建Excel工厂
     *
     * @return
     */
    protected ExportExcelFactory getFactory() {
        return new ExportExcelFactory(context.getSheetName());
    }
}
