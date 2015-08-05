package cn.lzb.common.excel.imported.impl;

import cn.lzb.common.excel.imported.annotation.CellMapper;
import cn.lzb.common.excel.imported.context.ExcelCommonContext;
import com.google.common.collect.Maps;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 功能描述：读取Excel文件工厂
 *
 * @author: Zhenbin.Li
 * email： zhenbin.li@okhqb.com
 * company：华强北在线
 * Date: 2014/12/31 Time：10:56
 */
public class ImportedExcelFactory {

    /**
     * Excel Sheet
     */
    private final Sheet sheet;

    /**
     * Excel Sheet名称
     */
    private String sheetName;

    /**
     * 读取数据的开始行数
     */
    private int startRow;

    /**
     * 默认的Excel Sheet名称
     */
    private static final String DEFAULT_SHEET_NAME = "Sheet1";

    /**
     * 读取Excel文件默认开始行数，0表示Excel标题
     */
    private static final int DEFAULT_START_ROW = 0;

    /**
     * 默认构造方法
     *
     * @param workbook
     */
    public ImportedExcelFactory(Workbook workbook) {
        this.startRow = DEFAULT_START_ROW;
        this.sheetName = DEFAULT_SHEET_NAME;
        this.sheet = workbook.getSheet(this.sheetName);
        if (this.sheet == null) {
            throw new RuntimeException("读取Excel文件，获取Excel sheet为空.");
        }
    }

    /**
     * 构造方法，可选择传递Excel的sheet名称，读取数据行索引
     *
     * @param workbook
     * @param context
     */
    public ImportedExcelFactory(Workbook workbook, ExcelCommonContext context) {
        if (context.getSheetName() != null && context.getSheetName().length() > 0) {
            this.sheetName = context.getSheetName();
        }
        if (context.getStartRow() != null && context.getStartRow() > 0) {
            this.startRow = context.getStartRow();
        }
        this.sheet = workbook.getSheet(this.sheetName);
        if (this.sheet == null) {
            throw new RuntimeException("读取Excel文件，获取Excel sheet为空.");
        }
    }

    /**
     * 解析读取Excel文件
     *
     * @param clazz 解析Excel映射对象class
     * @param <T>   映射对象类型
     * @return 映射对象集合
     */
    public <T> List<T> resolve(Class<T> clazz) {
        try {
            List<T> excels = new ArrayList<T>(sheet.getLastRowNum() - 1);
            Row row = sheet.getRow(this.startRow);
            // 读取Excel配置注解类的MAP，KEY：注解名称，VALUE：配置类属性名称
            Map<String, Field> configClassFieldMap = Maps.newHashMap();
            // Excel文件对应的属性标题名称, KEY:Excel列属性序号（列序号），VALUE：Excel列属性值（标题）
            Map<String, String> excelTitleMap = Maps.newHashMap();

            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(CellMapper.class)) {
                    CellMapper cellMapper = field.getAnnotation(CellMapper.class);
                    configClassFieldMap.put(cellMapper.name(), field);
                }
            }

            for (Cell excelTitle : row) {
                CellReference cellRef = new CellReference(excelTitle);
                excelTitle.getRichStringCellValue();
                excelTitleMap.put(cellRef.getCellRefParts()[2], excelTitle.getRichStringCellValue().getString());
            }

            for (int i = this.startRow + 1; i <= sheet.getLastRowNum(); i++) {
                T instance = clazz.newInstance();
                Row sheetRow = sheet.getRow(i);
                for (Cell cell : sheetRow) {
                    CellReference cellRef = new CellReference(cell);
                    String cellValue = cellRef.getCellRefParts()[2];
                    String cellName = excelTitleMap.get(cellValue);
                    Field field = configClassFieldMap.get(cellName);
                    if (null != field) {
                        field.setAccessible(true);
                        ImportedExcelUtils.handlerCellValue(cell, instance, field);
                    }
                }
                excels.add(instance);
            }

            return excels;
        } catch (InstantiationException ex) {
            throw new RuntimeException("读取Excel文件，解析读取Excel文件.", ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException("读取Excel文件，解析读取Excel文件.", ex);
        }
    }
}
