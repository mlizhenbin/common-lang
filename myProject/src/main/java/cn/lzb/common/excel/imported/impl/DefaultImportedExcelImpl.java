package cn.lzb.common.excel.imported.impl;

import cn.lzb.common.excel.imported.ImportedExcelFacade;
import cn.lzb.common.excel.imported.context.ExcelCommonContext;
import cn.lzb.common.excel.imported.context.ExcelInputStreamContext;
import cn.lzb.common.excel.imported.context.ExcelPathContext;
import cn.lzb.common.lang.StringUtil;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

/**
 * 功能描述：读取Excel文件接口实现
 *
 * @author: Zhenbin.Li
 * email： zhenbin.li@okhqb.com
 * company：华强北在线
 * Date: 2015/1/10 Time：10:33
 */
public class DefaultImportedExcelImpl implements ImportedExcelFacade {

    @Override
    public <T> List<T> read(ExcelInputStreamContext<T> context) {
        if (context == null) {
            throw new RuntimeException("获取读取Excel文件, context为空。");
        }
        if (context.getClazz() == null) {
            throw new RuntimeException("获取读取Excel文件, context配置对象class为空，context=" + context);
        }
        if (context.getInputStream() == null) {
            throw new RuntimeException("获取读取Excel文件, context的InputStream为空，context=" + context);
        }

        Workbook streamWorkbook = ImportedExcelUtils.getStreamWorkbook(context.getInputStream());
        ExcelCommonContext commonContext = new ExcelCommonContext();
        commonContext.setSheetName(context.getSheetName());
        commonContext.setStartRow(context.getStartRow());
        return getFactory(commonContext, streamWorkbook).resolve(context.getClazz());
    }

    @Override
    public <T> List<T> read(ExcelPathContext<T> context) {
        if (context == null) {
            throw new RuntimeException("获取读取Excel文件, context为空。");
        }
        if (context.getClazz() == null) {
            throw new RuntimeException("获取读取Excel文件, context配置对象class为空，context=" + context);
        }
        if (StringUtil.isBlank(context.getExcelFilePath())) {
            throw new RuntimeException("获取读取Excel文件, context的filePath文件路径为空，context=" + context);
        }

        Workbook pathWorkbook = ImportedExcelUtils.getPathWorkbook(context.getExcelFilePath());
        ExcelCommonContext commonContext = new ExcelCommonContext();
        commonContext.setSheetName(context.getSheetName());
        commonContext.setStartRow(context.getStartRow());
        return getFactory(commonContext, pathWorkbook).resolve(context.getClazz());
    }

    /**
     * 传递上下文参数到Factory
     *
     * @param context
     * @param workbook
     * @return
     */
    protected ImportedExcelFactory getFactory(ExcelCommonContext context, Workbook workbook) {
        ImportedExcelFactory factory;
        if (context.getSheetName() != null && context.getSheetName().length() > 0) {
            factory = new ImportedExcelFactory(workbook, context);
        } else if (context.getStartRow() != null && context.getStartRow() > 0) {
            factory = new ImportedExcelFactory(workbook, context);
        } else {
            factory = new ImportedExcelFactory(workbook);
        }
        return factory;
    }
}
