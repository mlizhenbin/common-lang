package cn.lzb.common.excel.imported.impl;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 功能描述：导入Excel工具类
 *
 * @author: Zhenbin.Li
 * email： zhenbin.li@okhqb.com
 * company：华强北在线
 * Date: 2015/1/10 Time：13:08
 */
public class ImportedExcelUtils {

    /**
     * Don't let anyone instantiate this class.
     */
    private ImportedExcelUtils() {
    }

    /**
     * 时间格式化格式
     */
    private static final String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取通过流的Excel Workbook
     *
     * @param inputStream
     * @return
     */
    public static Workbook getStreamWorkbook(InputStream inputStream) {
        try {
            return new XSSFWorkbook(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("通过流读取Excel获取Workbook异常.", e);
        }
    }

    /**
     * 通过文件路径读取Excel，获取Workbook
     *
     * @param excelFilePath
     * @return
     */
    public static Workbook getPathWorkbook(String excelFilePath) {
        File excelFile = new File(excelFilePath);
        try {
            return WorkbookFactory.create(excelFile);
        } catch (IOException e) {
            throw new RuntimeException("通过流读取Excel获取Workbook异常.", e);
        } catch (InvalidFormatException e) {
            throw new RuntimeException("通过流读取Excel获取Workbook异常.", e);
        }
    }

    /**
     * 获取单元格数据信息
     *
     * @param cell
     * @param object
     * @param field
     */
    public static void handlerCellValue(Cell cell, Object object, Field field) {
        try {
            DateFormat dateFormat = new SimpleDateFormat(FORMAT_DATE_TIME);
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_BLANK:
                    field.set(object, cell.getStringCellValue());
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    field.setBoolean(object, cell.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_ERROR:
                    field.setByte(object, cell.getErrorCellValue());
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    field.set(object, cell.getCellFormula());
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        if (field.getType().getName().equals(Date.class.getName())) {
                            field.set(object, cell.getDateCellValue());
                        } else {
                            field.set(object, dateFormat.format(cell.getDateCellValue()));
                        }
                    } else {
                        // 处理整型
                        if (field.getType().isAssignableFrom(Integer.class)) {
                            Integer integer = new Integer(new BigDecimal(cell.getNumericCellValue()).intValue());
                            field.set(object, integer);
                        } else if (field.getType().getName().equals("int")) {
                            field.setInt(object, (int) cell.getNumericCellValue());
                        }
                        // 处理short
                        else if (field.getType().isAssignableFrom(Short.class)) {
                            Short aShort = new Short(new BigDecimal(cell.getNumericCellValue()).shortValue());
                            field.set(object, aShort);
                        } else if (field.getType().getName().equals("short")) {
                            field.setShort(object, (short) cell.getNumericCellValue());
                        }
                        // 处理float
                        else if (field.getType().isAssignableFrom(Float.class)) {
                            Float aFloat = new Float(new BigDecimal(cell.getNumericCellValue()).shortValue());
                            field.set(object, aFloat);
                        } else if (field.getType().getName().equals("float")) {
                            field.setFloat(object, (float) cell.getNumericCellValue());
                        }
                        // 处理Byte
                        else if (field.getType().isAssignableFrom(Byte.class)) {
                            Byte aByte = new Byte(new BigDecimal(cell.getNumericCellValue()).byteValue());
                            field.set(object, aByte);
                        } else if (field.getType().getName().equals("byte")) {
                            field.setByte(object, (byte) cell.getNumericCellValue());
                        }
                        // 处理double
                        else if (field.getType().isAssignableFrom(Double.class)) {
                            Double aDouble = new Double(new BigDecimal(cell.getNumericCellValue()).doubleValue());
                            field.set(object, aDouble);
                        } else if (field.getType().getName().equals("double")) {
                            field.setDouble(object, cell.getNumericCellValue());
                        }
                        // String类型
                        else if (field.getType().isAssignableFrom(String.class)) {
                            String s = String.valueOf(cell.getNumericCellValue());
                            if (s.contains("E")) {
                                s = s.trim();
                                BigDecimal bigDecimal = new BigDecimal(s);
                                s = bigDecimal.toPlainString();
                            }
                            field.set(object, s);
                        } else {
                            field.set(object, cell.getNumericCellValue());
                        }
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    if (field.getType().getName().equals(Date.class.getName())) {
                        field.set(object, dateFormat.parse(cell.getRichStringCellValue().getString()));
                    } else {
                        field.set(object, cell.getRichStringCellValue().getString());
                    }
                    break;
                default:
                    field.set(object, cell.getStringCellValue());
                    break;
            }
        } catch (ParseException ex) {
            throw new RuntimeException("获取Excel单元格数据异常.", ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException("获取Excel单元格数据异常.", ex);
        }
    }
}
