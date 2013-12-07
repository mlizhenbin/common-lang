package cn.lzb.common.excel;

import cn.lzb.common.excel.impl.BaseExportExcelAdapter;
import cn.lzb.common.excel.impl.DefaultExportExcelAdapter;
import cn.lzb.common.excel.impl.MultipartExportExcelAdapter;

/**
 * 功能描述：导出Excel文件facade
 * <p/>
 * <p>解决页面请求生成Excel文件，后台生成Excel文件</p>
 *
 * @author: Zhenbin.Li
 * email： zhenbin.li@okhqb.com
 * company：华强北在线
 * Date: 13-12-3 Time：下午3:10
 */
public interface ExportExcelFacade {

    /**
     * 导出简单的Excel文件，Excel文件无单元格合并等复杂样式
     * <p/>
     * <p>方法默认返回空，需要实现DefaultExportExcelAdapter</p>
     * <p>简单的生成Excel方式，无单元格合并，单行显示</p>
     *
     * @param adapter 创建Excel文件抽象实现
     * @param <T>     Excel文件对应对象
     */
    public <T> void export(DefaultExportExcelAdapter<? super T> adapter);

    /**
     * 导出复杂的Excel文件，Excel可以实现行与列之间的单元格合并等
     * <p/>
     * <p>方法默认返回空，需要实现DefaultExportExcelAdapter</p>
     * <p>MultipartExportExcelAdapter需要计算合并单元格，业务较为复杂，比较耗时</p>
     * <p>简单的生成Excel方式，无单元格合并，单行显示</p>
     *
     * @param adapter	创建Excel文件抽象实现
     * @param <T>		Excel文件对应对象
     */
    public <T> void export(MultipartExportExcelAdapter<? super T> adapter);

    /**
     * 基本创建导出Excel， 需要实现上下文与创建方法
     * <p/>
     * <p>在上述两种方法不满足情况下调用此方法，可以直接调用Excel工厂的方法</p>
     *
     * @param adapter 创建Excel文件抽象实现
     * @param <T>     Excel文件对应对象
     */
    public <T> void export(BaseExportExcelAdapter<? super T> adapter);

}
