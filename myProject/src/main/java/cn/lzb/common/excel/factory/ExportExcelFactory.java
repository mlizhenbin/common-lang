package cn.lzb.common.excel.factory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cn.lzb.common.lang.CollectionUtil;
import cn.lzb.common.lang.StringUtil;

import com.google.common.collect.Maps;

/**
 * 功能描述：创建生成Excel文件工厂
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
    private XSSFWorkbook workbook;

    /**
     * 工作表
     */
    private XSSFSheet sheet;

    /**
     * 当前行数
     */
    private int currentRow;

    /**
     * 总列数
     */
    private int totalCols;

    /**
     * Excel Sheet名称
     */
    private final String sheetName;

    /**
     * 默认构造方法
     */
    public ExportExcelFactory(String sheetName) {

        if (StringUtil.isNotBlank(sheetName)) {
            this.sheetName = sheetName;
        } else {
            this.sheetName = "sheet";
        }
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet(getSheetName());
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

        XSSFFont font = ExportExcelUtil.getFont(workbook, 10, XSSFFont.BOLDWEIGHT_NORMAL, IndexedColors.BLACK.getIndex());
        XSSFCellStyle style = ExportExcelUtil.getStyle(workbook, font, XSSFCellStyle.ALIGN_CENTER);

        // 记住合并单元格的位置
        Map<Integer, Integer> mergeRegions = Maps.newHashMap();
        for (String[][] excel : list) {
            // 先写数组的第一条数据
            XSSFRow firstRow = sheet.createRow(currentRow);
            for (int j = 0; j < excel[0].length; j++) {
                ExportExcelUtil.createCell(firstRow, j, style, excel[0][j]);
            }
            currentRow++;

            // 验证二位数组长度是否大于2，如果大于，则合并不需要写的数据
            // 行合并的长度长度为Excel表格的纬度
            if (excel.length > 1) {
                for (int i = 1; i < excel.length; i++) {
                    XSSFRow row = sheet.createRow(currentRow);
                    for (int j = 0; j < excel[i].length; j++) {
                        ExportExcelUtil.createCell(row, j, style, excel[i][j]);
                        // 为空的都记住，KEY：col，value：数组col空的数量
                        if (excel[i][j] == null) {
                            if (mergeRegions.get(j) == null) {
                                mergeRegions.put(j, 1);
                            } else {
                                int num = mergeRegions.get(j) + 1;
                                mergeRegions.put(j, num);
                            }
                        }
                    }
                    currentRow++;
                }

                // 可以合并单元格
                if (CollectionUtil.isNotEmpty(mergeRegions)) {
                    int mergeSize = excel.length - 1;
                    for (int j = 0; j < excel[0].length; j++) {
                        if (mergeRegions.containsKey(j) && (mergeRegions.get(j) == mergeSize)) {
                            ExportExcelUtil.mergeRegion(sheet, currentRow - mergeRegions.get(j) - 1, j, currentRow - 1, j);
                        }
                    }
                    //合并完成之后清掉map的值
                    mergeRegions.clear();
                }
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

        XSSFFont font_black = ExportExcelUtil
                .getFont(workbook, 10, XSSFFont.BOLDWEIGHT_NORMAL, IndexedColors.BLACK.getIndex());
        XSSFCellStyle style_black = ExportExcelUtil
                .getStyle(workbook, font_black, XSSFCellStyle.ALIGN_CENTER);
        XSSFFont font_red = ExportExcelUtil
                .getFont(workbook, 10, XSSFFont.BOLDWEIGHT_NORMAL, IndexedColors.RED.getIndex());
        XSSFCellStyle style_red = ExportExcelUtil
                .getStyle(workbook, font_red, XSSFCellStyle.ALIGN_CENTER);
        XSSFFont font_green = ExportExcelUtil
                .getFont(workbook, 10, XSSFFont.BOLDWEIGHT_NORMAL, IndexedColors.GREEN.getIndex());
        XSSFCellStyle style_green = ExportExcelUtil
                .getStyle(workbook, font_green, XSSFCellStyle.ALIGN_CENTER);

        // 记住合并单元格的位置
        Map<Integer, Integer> mergeRegions = Maps.newHashMap();
        for (int index = 0; index < list.size(); index++) {
            String rowColor = color.get(index);
            String[][] excel = list.get(index);
            // 先写数组的第一条数据
            XSSFRow firstRow = sheet.createRow(currentRow);
            for (int j = 0; j < excel[0].length; j++) {
                if (rowColor != null) {
                    if (rowColor.equals(ExportFrontColor.RED.getColorCode())) {
                        ExportExcelUtil.createCell(firstRow, j, style_red, excel[0][j]);
                    } else if (rowColor.equals(ExportFrontColor.GREEN.getColorCode())) {
                        ExportExcelUtil.createCell(firstRow, j, style_green, excel[0][j]);
                    } else {
                        ExportExcelUtil.createCell(firstRow, j, style_black, excel[0][j]);
                    }
                } else {
                    ExportExcelUtil.createCell(firstRow, j, style_black, excel[0][j]);
                }
            }
            currentRow++;

            // 验证二位数组长度是否大于2，如果大于，则合并不需要写的数据
            // 行合并的长度长度为Excel表格的纬度
            if (excel.length > 1) {
                for (int i = 1; i < excel.length; i++) {
                    XSSFRow row = sheet.createRow(currentRow);
                    for (int j = 0; j < excel[i].length; j++) {
                        if (rowColor != null) {
                            if (rowColor.equals(ExportFrontColor.RED.getColorCode())) {
                                ExportExcelUtil.createCell(row, j, style_red, excel[i][j]);
                            } else if (rowColor.equals(ExportFrontColor.GREEN.getColorCode())) {
                                ExportExcelUtil.createCell(row, j, style_green, excel[i][j]);
                            } else {
                                ExportExcelUtil.createCell(row, j, style_black, excel[i][j]);
                            }
                        } else {
                            ExportExcelUtil.createCell(row, j, style_black, excel[i][j]);
                        }

                        // 为空的都记住，KEY：col，value：数组col空的数量
                        if (excel[i][j] == null) {
                            if (mergeRegions.get(j) == null) {
                                mergeRegions.put(j, 1);
                            } else {
                                int num = mergeRegions.get(j) + 1;
                                mergeRegions.put(j, num);
                            }
                        }
                    }
                    currentRow++;
                }

                // 可以合并单元格
                if (CollectionUtil.isNotEmpty(mergeRegions)) {
                    int mergeSize = excel.length - 1;
                    for (int j = 0; j < excel[0].length; j++) {
                        if (mergeRegions.containsKey(j) && (mergeRegions.get(j) == mergeSize)) {
                            ExportExcelUtil.mergeRegion(sheet, currentRow - mergeRegions.get(j) - 1, j, currentRow - 1, j);
                        }
                    }
                    //合并完成之后清掉map的值
                    mergeRegions.clear();
                }
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

        XSSFFont font = ExportExcelUtil.getFont(workbook, 10, XSSFFont.BOLDWEIGHT_NORMAL, IndexedColors.BLACK.getIndex());
        XSSFCellStyle style = ExportExcelUtil.getStyle(workbook, font, XSSFCellStyle.ALIGN_CENTER);

        for (int index = 0; index < list.size(); index++) {
            XSSFRow row = sheet.createRow(currentRow);
            String[] stringArray = list.get(index);
            for (int j = 0; j < stringArray.length; j++) {
                ExportExcelUtil.createCell(row, j, style, stringArray[j]);
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

        XSSFFont font_black = ExportExcelUtil
                .getFont(workbook, 10, XSSFFont.BOLDWEIGHT_NORMAL, IndexedColors.BLACK.getIndex());
        XSSFCellStyle style_black = ExportExcelUtil
                .getStyle(workbook, font_black, XSSFCellStyle.ALIGN_CENTER);
        XSSFFont font_red = ExportExcelUtil
                .getFont(workbook, 10, XSSFFont.BOLDWEIGHT_NORMAL, IndexedColors.RED.getIndex());
        XSSFCellStyle style_red = ExportExcelUtil
                .getStyle(workbook, font_red, XSSFCellStyle.ALIGN_CENTER);
        XSSFFont font_green = ExportExcelUtil
                .getFont(workbook, 10, XSSFFont.BOLDWEIGHT_NORMAL, IndexedColors.GREEN.getIndex());
        XSSFCellStyle style_green = ExportExcelUtil
                .getStyle(workbook, font_green, XSSFCellStyle.ALIGN_CENTER);

        for (int index = 0; index < list.size(); index++) {
            String rowColor = color.get(index);
            XSSFRow row = sheet.createRow(currentRow);
            String[] stringArray = list.get(index);
            for (int j = 0; j < stringArray.length; j++) {
                if (rowColor != null) {
                    if (rowColor.equals(ExportFrontColor.RED.getColorCode())) {
                        ExportExcelUtil.createCell(row, j, style_red, stringArray[j]);
                    } else if (rowColor.equals(ExportFrontColor.GREEN.getColorCode())) {
                        ExportExcelUtil.createCell(row, j, style_green, stringArray[j]);
                    } else {
                        ExportExcelUtil.createCell(row, j, style_black, stringArray[j]);
                    }
                } else {
                    ExportExcelUtil.createCell(row, j, style_black, stringArray[j]);
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

        XSSFFont font = ExportExcelUtil.getFont(workbook, 10, XSSFFont.BOLDWEIGHT_BOLD, IndexedColors.BLACK.getIndex());
        XSSFCellStyle style = ExportExcelUtil.getStyle(workbook, font, XSSFCellStyle.ALIGN_CENTER);

        XSSFRow row = sheet.createRow(currentRow);
        ExportExcelUtil.createCell(row, 0, style, caption);
        for (int i = 1; i < totalCols; i++) {
            ExportExcelUtil.createCell(row, i, style, "");
        }

        ExportExcelUtil.mergeCol(sheet, 0, totalCols - 1, currentRow);
        currentRow++;
    }

    /**
     * 创建一行列标题
     *
     * @param colCaption 一行列标题
     */
    public void createColCaption(String[] colCaption) {

        XSSFFont font = ExportExcelUtil.getFont(workbook, 0, XSSFFont.BOLDWEIGHT_BOLD, IndexedColors.BLACK.getIndex());
        XSSFCellStyle style = ExportExcelUtil.getStyle(workbook, font, XSSFCellStyle.ALIGN_CENTER);

        XSSFRow row = sheet.createRow(currentRow);
        for (int i = 0; i < colCaption.length; i++) {
            ExportExcelUtil.createCell(row, i, style, colCaption[i]);
        }
        currentRow++;
    }

    /**
     * 创建多行列标题
     *
     * @param colCaption 多行列标题
     */
    public void createColCaption(String[][] colCaption) {

        XSSFFont font = ExportExcelUtil.getFont(workbook, 10, XSSFFont.BOLDWEIGHT_BOLD, IndexedColors.BLACK.getIndex());
        XSSFCellStyle style = ExportExcelUtil.getStyle(workbook, font, XSSFCellStyle.ALIGN_CENTER);
        int[][][] spans = ExportExcelUtil.getSpans(colCaption);

        for (int i = 0; i < colCaption.length; i++) {
            XSSFRow row = sheet.createRow(currentRow);
            for (int j = 0; j < colCaption[i].length; j++) {
                ExportExcelUtil.createCell(row, j, style, colCaption[i][j]);
                if (colCaption[i][j] != null) {
                    ExportExcelUtil
                            .mergeRegion(sheet, currentRow, j, currentRow + spans[i][j][0] - 1, j + spans[i][j][1] - 1);
                }
            }
            currentRow++;
        }
    }

    /**
     * 创建备注信息
     *
     * @param remarks 备注信息
     */
    public void createRemarks(String[] remarks) {

        XSSFFont font = ExportExcelUtil.getFont(workbook, 10, XSSFFont.BOLDWEIGHT_NORMAL, IndexedColors.RED.getIndex());
        XSSFCellStyle style = ExportExcelUtil.getStyle(workbook, font, XSSFCellStyle.ALIGN_LEFT);

        for (int i = 0; i < remarks.length; i++) {
            XSSFRow row = sheet.createRow(currentRow);
            ExportExcelUtil.createCell(row, 0, style, remarks[i]);

            for (int j = 1; j < totalCols; j++) {
                ExportExcelUtil.createCell(row, j, style, "");
            }

            ExportExcelUtil.mergeRow(sheet, currentRow, 0, totalCols - 1);
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
            ExportExcelUtil.closeOutputStream(out);
        }
    }

    /**
     * 把EXCEL文件写入到输出流中
     *
     * @param out 输出流
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
}
