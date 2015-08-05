package cn.lzb.common.excel.export.impl;

import cn.lzb.common.lang.ArrayUtil;
import cn.lzb.common.lang.CollectionUtil;
import cn.lzb.common.lang.StringUtil;
import com.google.common.collect.Maps;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * 功能描述：生成Excel文件工厂
 *
 * @author: Zhenbin.Li
 * email： zhenbin.li@okhqb.com
 * company：华强北在线
 * Date: 13-11-19 Time：下午12:46
 */
public class ExportExcelFactory {

    /**
     * 工作薄
     */
    private SXSSFWorkbook workbook;

    /**
     * 工作表
     */
    private Sheet sheet;

    /**
     * 当前行数
     */
    private int currentRow = 0;

    /**
     * 总列数
     */
    private int totalCols;

    /**
     * Excel Sheet名称
     */
    private final String sheetName;

    /**
     * 表格颜色样式
     */
    private static Map<ExportFrontStyle, CellStyle> styles = Maps.newHashMap();

    /**
     * 表格默认长度
     */
    private static final int DEFAULT_WORK_BOOK_SIZE = 500;

    /**
     * 工作表默认名称
     */
    private static final String DEFAULT_SHEET_NAME = "sheet";

    /**
     * 默认构造方法
     */
    public ExportExcelFactory(String sheetName) {
        this.sheetName = StringUtil.isNotBlank(sheetName) ? sheetName : DEFAULT_SHEET_NAME;
        workbook = new SXSSFWorkbook(DEFAULT_WORK_BOOK_SIZE);
        sheet = workbook.createSheet(getSheetName());
        getColorCellStyles(workbook, styles);
    }

    /**
     * 创建合并Excel列列表的主体数据
     *
     * @param list List(String[][])列表数据
     */
    public void createMultipleBody(List<String[][]> list) {

        if (CollectionUtil.isEmpty(list)) {
            return;
        }

        CellStyle style = styles.get(ExportFrontStyle.BLACK);
        // 记住合并单元格的位置
        Map<Integer, Integer> mergeRegions = Maps.newHashMap();
        for (String[][] excels : list) {
            // 先写数组的第一条数据
            Row firstRow = sheet.createRow(currentRow);
            for (int j = 0; j < excels[0].length; j++) {
                createCell(firstRow, j, style, excels[0][j]);
            }
            currentRow++;

            // 验证二位数组长度是否大于1，如果大于，则合并不需要写的数据
            // 行合并的长度长度为Excel表格的纬度
            if (excels.length > 1) {
                for (int i = 1; i < excels.length; i++) {
                    Row row = sheet.createRow(currentRow);
                    for (int j = 0; j < excels[i].length; j++) {
                        createCell(row, j, style, excels[i][j]);
                        // 为空的都记住，KEY：col，value：数组col空的数量
                        if (excels[i][j] == null) {
                            mergeRegions.put(j, mergeRegions.get(j) == null ? 1 : mergeRegions.get(j) + 1);
                        }
                    }
                    currentRow++;
                }
                doMergeRegions(mergeRegions, excels);
            }
        }
    }

    /**
     * 创建合并Excel列列表的主体数据
     *
     * @param list
     * @param color
     */
    public void createMultipleBody(List<String[][]> list, Map<Integer, String> color) {

        if (CollectionUtil.isEmpty(list)) {
            return;
        }

        // 合并单元格位置放置在map中
        Map<Integer, Integer> mergeRegions = Maps.newHashMap();
        for (int index = 0; index < list.size(); index++) {
            String rowColor = color.get(index);
            String[][] excels = list.get(index);
            Row firstRow = sheet.createRow(currentRow);
            for (int j = 0; j < excels[0].length; j++) {
                if (StringUtil.isNotBlank(rowColor)) {
                    if (rowColor.equals(ExportFrontStyle.RED.getColorCode())) {
                        createCell(firstRow, j, styles.get(ExportFrontStyle.RED), excels[0][j]);
                    } else if (rowColor.equals(ExportFrontStyle.GREEN.getColorCode())) {
                        createCell(firstRow, j, styles.get(ExportFrontStyle.GREEN), excels[0][j]);
                    } else {
                        createCell(firstRow, j, styles.get(ExportFrontStyle.BLACK), excels[0][j]);
                    }
                } else {
                    createCell(firstRow, j, styles.get(ExportFrontStyle.BLACK), excels[0][j]);
                }
            }
            currentRow++;

            // 开始第二条数据
            if (excels.length > 1) {
                for (int i = 1; i < excels.length; i++) {
                    Row row = sheet.createRow(currentRow);
                    for (int j = 0; j < excels[i].length; j++) {
                        if (rowColor != null) {
                            if (rowColor.equals(ExportFrontStyle.RED.getColorCode())) {
                                createCell(row, j, styles.get(ExportFrontStyle.RED), excels[i][j]);
                            } else if (rowColor.equals(ExportFrontStyle.GREEN.getColorCode())) {
                                createCell(row, j, styles.get(ExportFrontStyle.GREEN), excels[i][j]);
                            } else {
                                createCell(row, j, styles.get(ExportFrontStyle.BLACK), excels[i][j]);
                            }
                        } else {
                            createCell(row, j, styles.get(ExportFrontStyle.BLACK), excels[i][j]);
                        }
                        // 合并单元格数量
                        if (excels[i][j] == null) {
                            mergeRegions.put(j, mergeRegions.get(j) == null ? 1 : mergeRegions.get(j) + 1);
                        }
                    }
                    currentRow++;
                }
                doMergeRegions(mergeRegions, excels);
            }
        }
    }

    /**
     * 创建列表的主体数据
     *
     * @param list List(String[])列表数据
     */
    public void createBody(List<String[]> list) {

        if (CollectionUtil.isEmpty(list)) {
            return;
        }

        CellStyle style = styles.get(ExportFrontStyle.BLACK);
        for (int index = 0; index < list.size(); index++) {
            Row row = sheet.createRow(currentRow);
            String[] stringArray = list.get(index);
            for (int j = 0; j < stringArray.length; j++) {
                createCell(row, j, style, stringArray[j]);
            }
            currentRow++;
        }
    }

    /**
     * 创建列表的主体数据，包含单元格颜色样式
     *
     * @param list  List(String[])列表数据
     * @param color 单元格颜色样式
     */
    public void createBodyColor(List<String[]> list, Map<Integer, String> color) {

        if (CollectionUtil.isEmpty(list)) {
            return;
        }

        for (int index = 0; index < list.size(); index++) {
            String rowColor = color.get(index);
            Row row = sheet.createRow(currentRow);
            String[] stringArray = list.get(index);
            for (int j = 0; j < stringArray.length; j++) {
                if (rowColor != null) {
                    if (rowColor.equals(ExportFrontStyle.RED.getColorCode())) {
                        createCell(row, j, styles.get(ExportFrontStyle.RED), stringArray[j]);
                    } else if (rowColor.equals(ExportFrontStyle.GREEN.getColorCode())) {
                        createCell(row, j, styles.get(ExportFrontStyle.GREEN), stringArray[j]);
                    } else {
                        createCell(row, j, styles.get(ExportFrontStyle.BLACK), stringArray[j]);
                    }
                } else {
                    createCell(row, j, styles.get(ExportFrontStyle.BLACK), stringArray[j]);
                }
            }
            currentRow++;
        }
    }

    /**
     * 设置列宽
     *
     * @param columnWidth 列宽信息
     */
    public void setColumnWidth(int[] columnWidth) {

        if (ArrayUtil.isEmpty(columnWidth)) {
            return;
        }

        for (int i = 0; i < columnWidth.length; i++) {
            if (columnWidth[i] != 0) {
                if (sheet.getColumnWidth((short) i) != (columnWidth[i] * 37.5)) {
                    sheet.setColumnWidth((short) i, (short) (columnWidth[i] * 37.5));
                }
            }
        }
    }

    /**
     * 设置列数
     *
     * @param cols 列数
     */
    public void setCols(int cols) {
        this.totalCols = cols;
    }

    /**
     * 创建标题
     *
     * @param caption 标题
     */
    public void createCaption(String caption) {
        CellStyle style = styles.get(ExportFrontStyle.TITLE);
        Row row = sheet.createRow(currentRow);
        createCell(row, 0, style, caption);
        for (int i = 1; i < totalCols; i++) {
            createCell(row, i, style, "");
        }
        mergeCol(sheet, 0, totalCols - 1, currentRow);
        currentRow++;
    }

    /**
     * 创建一行列标题
     *
     * @param colCaption 一行列标题
     */
    public void createColCaption(String[] colCaption) {
        CellStyle style = styles.get(ExportFrontStyle.TITLE);
        Row row = sheet.createRow(currentRow);
        for (int i = 0; i < colCaption.length; i++) {
            createCell(row, i, style, colCaption[i]);
        }
        currentRow++;
    }

    /**
     * 创建多行列标题
     *
     * @param colCaptions 多行列标题
     */
    public void createColCaption(String[][] colCaptions) {

        if (ArrayUtil.isEmpty(colCaptions)) {
            return;
        }

        CellStyle style = styles.get(ExportFrontStyle.TITLE);
        // 先写第一行数据
        Row firstRow = sheet.createRow(currentRow);
        for (int i = 0; i < colCaptions[0].length; i++) {
            createCell(firstRow, i, style, colCaptions[0][i]);
        }
        currentRow++;

        // 开始写第二行数据
        if (colCaptions.length > 1) {
            // 记住合并单元格的位置
            Map<Integer, Integer> mergeRegions = Maps.newHashMap();
            for (int i = 1; i < colCaptions.length; i++) {
                Row row = sheet.createRow(currentRow);
                for (int j = 0; j < colCaptions[i].length; j++) {
                    createCell(row, j, style, colCaptions[i][j]);
                    // 为空的都记住，KEY：col，value：数组col空的数量
                    if (colCaptions[i][j] == null) {
                        mergeRegions.put(j, mergeRegions.get(j) == null ? 1 : mergeRegions.get(j) + 1);
                    }
                }
                currentRow++;
            }
            doMergeRegions(mergeRegions, colCaptions);
        }
    }

    /**
     * 创建备注信息
     *
     * @param remarks 备注信息
     */
    public void createRemarks(String[] remarks) {
        CellStyle style = styles.get(ExportFrontStyle.BLACK);
        for (int i = 0; i < remarks.length; i++) {
            Row row = sheet.createRow(currentRow);
            createCell(row, 0, style, remarks[i]);

            for (int j = 1; j < totalCols; j++) {
                createCell(row, j, style, "");
            }

            mergeRow(sheet, currentRow, 0, totalCols - 1);
            currentRow++;
        }
    }

    /**
     * 创建本地路径的EXCEL文件
     *
     * @param filePath EXCEL文件路径
     */
    public void createFile(String filePath) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filePath);
            workbook.write(out);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("无法创建文件：" + filePath);
        } catch (IOException e) {
            throw new RuntimeException("无法写入文件：" + filePath);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * 把EXCEL文件写入到输出流中
     *
     * @param out
     * @throws java.io.IOException
     */
    public void createFile(OutputStream out) throws IOException {
        workbook.write(out);
    }

    /**
     * 获取Excel Sheet名称
     *
     * @return
     */
    public String getSheetName() {
        return sheetName;
    }

    /**
     * 清除硬盘上在导出时写入的文件
     */
    public void dispose() {
        workbook.dispose();
    }

    /**
     * 合并单元格
     *
     * @param mergeRegions 单元格合并每一行合并数量
     * @param excels       目标二维数组
     */
    protected void doMergeRegions(Map<Integer, Integer> mergeRegions, String[][] excels) {
        // 可以合并单元格
        if (CollectionUtil.isNotEmpty(mergeRegions)) {
            int mergeSize = excels.length - 1;
            for (int j = 0; j < excels[0].length; j++) {
                if (mergeRegions.containsKey(j) && (mergeRegions.get(j) == mergeSize)) {
                    mergeRegion(sheet, currentRow - mergeRegions.get(j) - 1, j, currentRow - 1, j);
                }
            }
            //合并完成之后清掉map的值
            mergeRegions.clear();
        }
    }

    /**
     * 创建单元格
     *
     * @param row   行对象
     * @param index 在行中创建单元格的位置，从0开始。
     * @param style 单元格样式
     * @param value 单元格的值
     */
    protected void createCell(Row row, int index, CellStyle style, String value) {
        Cell cell = row.createCell((short) index);
        cell.setCellStyle(style);
        cell.setCellValue(value);
    }

    /**
     * 合并行
     *
     * @param sheet
     * @param row
     * @param firstCol
     * @param lastCol
     */
    protected void mergeRow(Sheet sheet, int row, int firstCol, int lastCol) {
        sheet.addMergedRegion(new CellRangeAddress(row, row, firstCol, lastCol));
    }

    /**
     * 合并列
     *
     * @param sheet
     * @param startCol 起始列（从0开始）
     * @param endCol   结束列（从0开始）
     * @param row      行位置（从0开始）
     */
    protected void mergeCol(Sheet sheet, int startCol, int endCol, int row) {
        sheet.addMergedRegion(new CellRangeAddress(row, startCol, row, endCol));
    }

    /**
     * 合并区域
     *
     * @param sheet
     * @param startRow 起始行（从0开始）
     * @param startCol 起始列（从0开始）
     * @param endRow   结束行（从0开始）
     * @param endCol   结束列（从0开始）
     */
    protected void mergeRegion(Sheet sheet, int startRow, int startCol, int endRow, int endCol) {
        sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, startCol, endCol));
    }

    /**
     * 获取系统支持对应颜色对应的样式
     *
     * @param workbook
     * @param map
     */
    protected void getColorCellStyles(SXSSFWorkbook workbook, Map<ExportFrontStyle, CellStyle> map) {
        map.put(ExportFrontStyle.BLACK, getStyle(workbook,
                getFont(workbook, 10, Font.BOLDWEIGHT_NORMAL, IndexedColors.BLACK.getIndex()), CellStyle.ALIGN_CENTER));
        map.put(ExportFrontStyle.RED, getStyle(workbook,
                getFont(workbook, 10, Font.BOLDWEIGHT_NORMAL, IndexedColors.RED.getIndex()), CellStyle.ALIGN_CENTER));
        map.put(ExportFrontStyle.GREEN, getStyle(workbook,
                getFont(workbook, 10, Font.BOLDWEIGHT_NORMAL, IndexedColors.GREEN.getIndex()), CellStyle.ALIGN_CENTER));
        map.put(ExportFrontStyle.TITLE, getStyle(workbook,
                getFont(workbook, 10, Font.BOLDWEIGHT_BOLD, IndexedColors.BLACK.getIndex()), CellStyle.ALIGN_CENTER));
    }

    /**
     * 根据字体高度和字体类型获得字体对象
     *
     * @param fontHeight 字体高度
     * @param boldWeight 字体类型
     * @param color      字体颜色
     * @return 字体对象
     */
    protected Font getFont(Workbook workbook, int fontHeight, short boldWeight, short color) {
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) fontHeight);
        font.setFontName("新宋体");
        font.setColor(color);
        font.setBoldweight(boldWeight);
        return font;
    }

    /**
     * 根据字体和对其方式获得单元格样式
     *
     * @param font  字体
     * @param align 对其方式
     * @return 单元格样式
     */
    protected CellStyle getStyle(SXSSFWorkbook workbook, Font font, short align) {
        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        //边框
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        //对齐方式
        style.setAlignment(align);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        return style;
    }
}
