package cn.lzb.common.excel.factory;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 功能描述：ExportExcelFactory工具类
 *
 * @author: Zhenbin.Li
 * email： zhenbin.li@okhqb.com
 * company：华强北在线
 * Date: 13-11-19 Time：下午12:48
 */
public class ExportExcelUtil {

    /**
     * 根据多行报表标题获得一个合并的行列信息
     *
     * @param colTitles 列标题
     * @return 报表标题合并的行列信息：第一维对应标题的行，第二维对应标题的列，
     *         第三维对应标题的合并行和列，已经被合并的行或列则对应的第三维为null。
     */
    public static int[][][] getSpans(String[][] colTitles) {

        int[][][] spans = new int[colTitles.length][][];

        for (int i = 0; i < colTitles.length; i++) {
            spans[i] = new int[colTitles[i].length][];
            for (int j = 0; j < colTitles[i].length; j++) {
                if (colTitles[i][j] != null) {
                    spans[i][j] = new int[2];
                    spans[i][j][0] = ExportExcelUtil.getRowSpan(colTitles, i, j);
                    spans[i][j][1] = ExportExcelUtil.getColSpan(colTitles, i, j);
                }
            }
        }

        return spans;
    }


    /**
     * 创建单元格
     *
     * @param row   行对象
     * @param index 在行中创建单元格的位置，从0开始。
     * @param style 单元格样式
     * @param value 单元格的值
     */
    public static void createCell(XSSFRow row, int index, XSSFCellStyle style, String value) {
        XSSFCell cell = row.createCell((short) index);
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
    public static void mergeRow(XSSFSheet sheet, int row, int firstCol, int lastCol) {
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
    public static void mergeCol(XSSFSheet sheet, int startCol, int endCol, int row) {
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
    public static void mergeRegion(XSSFSheet sheet, int startRow, int startCol, int endRow, int endCol) {
        sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, startCol,  endCol));
    }

    /**
     * 关闭输出流
     *
     * @param out 输出流
     */
    public static void closeOutputStream(OutputStream out) {
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {

            }
        }
    }

    /**
     * 根据字体高度和字体类型获得字体对象
     *
     * @param fontHeight 字体高度
     * @param boldweight 字体类型
     * @param color      字体颜色
     * @return 字体对象
     */
    public static XSSFFont getFont(XSSFWorkbook workbook, int fontHeight, short boldweight, short color) {
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) fontHeight);
        font.setFontName("新宋体");
        font.setColor(color);
        font.setBoldweight(boldweight);
        return font;
    }

    /**
     * 根据字体和对其方式获得单元格样式
     *
     * @param font  字体
     * @param align 对其方式
     * @return 单元格样式
     */
    public static XSSFCellStyle getStyle(XSSFWorkbook workbook, XSSFFont font, short align) {
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);

        //边框
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        //对齐方式
        style.setAlignment(align);
        style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        return style;
    }

    /**
     * 获得报表标题在某行某列的行跨度
     *
     * @param colTitles 报表列标题
     * @param row       行
     * @param col       列
     * @return 行跨度
     */
    public static int getRowSpan(String[][] colTitles, int row, int col) {
        int rowSpan = 1;
        if (colTitles[row][col] != null) {
            for (int i = row + 1; i < colTitles.length; i++) {
                if (colTitles[i][col] == null) {
                    rowSpan++;
                } else {
                    break;
                }
            }
        }
        return rowSpan;
    }

    /**
     * 获得报表标题在某行某列的列跨度
     *
     * @param colTitles 报表列标题
     * @param row       行
     * @param col       列
     * @return 列跨度
     */
    public static int getColSpan(String[][] colTitles, int row, int col) {
        int colSpan = 1;
        if (colTitles[row][col] != null) {
            for (int i = col; i < colTitles[row].length; i++) {
                if (colTitles[row][i] == null) {
                    if (row == 0) {
                        colSpan++;
                    } else if (colTitles[row - 1][i] == null) {
                        colSpan++;
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        return colSpan;
    }
}
