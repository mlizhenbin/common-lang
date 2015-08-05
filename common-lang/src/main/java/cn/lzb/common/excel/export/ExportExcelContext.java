package cn.lzb.common.excel.export;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 功能描述：创建生成Excel文件上下文
 *
 * @author: Zhenbin.Li
 * email： zhenbin.li@okhqb.com
 * company：华强北在线
 * Date: 13-12-3 Time：下午4:10
 */
public class ExportExcelContext<T> {

    /**
     * 生成Excel文件对象集合
     */
    private List<T> exports;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 表格标题
     */
    private String title;

    /**
     * 列标题， 初始化上下文时，必填
     * <p/>
     * 1、如果是一行列头标题，则可以通过如下代码实现：
     * String[][] colTitles = { { "列1", "列2", "列3" } }
     * <p/>
     * 2、如果是二行列头标题，则可以通过如下代码实现：
     * String[][] colTitles = {
     * { "行1列1", "行1列2", null, "行1列4" },
     * { null, "行2列2", "行2列3", "行2列4" }};
     * 将显示的列标题实际效果为：
     * <table border="1">
     * <tr align="center">
     * <td rowspan="2">行1列1</td>
     * <td colspan="2">行1列2</td>
     * <td>行1列4</td>
     * </tr>
     * <tr align="center">
     * <td>行2列2</td>
     * <td>行2列3</td>
     * <td>行2列4</td>
     * </tr>
     * </table>
     */
    private String[][] colTitles;

    /**
     * 单元格的宽度
     */
    private int[] columnWidths;

    /**
     * Excel Sheet名称
     */
    private String sheetName;

    /**
     * 备注信息
     */
    private String[] remarks;

    /**
     * 单元格颜色样式
     */
    private Map<Integer, String> rowColors;

    /**
     * 生成文件地址：eg: C:\\Users\\Administrator\\Documents\\
     */
    private String filePath;

    /**
     * 页面生成Excel HttpServletResponse
     */
    private HttpServletResponse httpServletResponse;

    public List<T> getExports() {
        return exports;
    }

    public void setExports(List<T> exports) {
        this.exports = exports;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[][] getColTitles() {
        return colTitles;
    }

    public void setColTitles(String[][] colTitles) {
        this.colTitles = colTitles;
    }

    public int[] getColumnWidths() {
        return columnWidths;
    }

    public void setColumnWidths(int[] columnWidths) {
        this.columnWidths = columnWidths;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public String[] getRemarks() {
        return remarks;
    }

    public void setRemarks(String[] remarks) {
        this.remarks = remarks;
    }

    public Map<Integer, String> getRowColors() {
        return rowColors;
    }

    public void setRowColors(Map<Integer, String> rowColors) {
        this.rowColors = rowColors;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public HttpServletResponse getHttpServletResponse() {
        return httpServletResponse;
    }

    public void setHttpServletResponse(HttpServletResponse httpServletResponse) {
        this.httpServletResponse = httpServletResponse;
    }
}
