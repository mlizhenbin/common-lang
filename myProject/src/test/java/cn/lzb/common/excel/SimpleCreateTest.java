package cn.lzb.common.excel;

import cn.lzb.common.excel.enity.SimpleEntity;
import cn.lzb.common.excel.factory.ExportFrontColor;
import cn.lzb.common.excel.impl.CommonExportExcelImpl;
import cn.lzb.common.excel.impl.DefaultExportExcelAdapter;
import cn.lzb.common.lang.CollectionUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * 功能描述：创建简单Excel文件测试类
 *
 * @author: Zhenbin.Li
 * email： zhenbin.li@okhqb.com
 * company：华强北在线
 * Date: 13-12-4 Time：下午3:36
 */
public class SimpleCreateTest {

    /**
     * 单例模式
     */
    private static SimpleCreateTest instance = new SimpleCreateTest();

    private SimpleCreateTest() {
    }

    ;

    public static SimpleCreateTest getInstance() {
        return instance;
    }

    /**
     * 导出Excel文件接口
     */
    private static ExportExcelFacade exportExcelFacade = new CommonExportExcelImpl();

    /**
     * 获取测试数据
     *
     * @return
     */
    private List<SimpleEntity> getSimpleEntities() {

        List<SimpleEntity> simpleEntities = Lists.newArrayList();
        for (int i = 1; i < 30000; i++) {
            SimpleEntity simpleEntity = new SimpleEntity();
            simpleEntity.setUserId(i);
            simpleEntity.setUserName("okhqeqwewqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqewqewqeb_" + i);
            simpleEntity.setAddress("www.okhqb.comeqweeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeewqewqeqw_" + i);
            simpleEntity.setTel("136351462eqwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwweqwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwewqee" + i);
            simpleEntity.setMemo("测试1232321321312321111111111111111111114214213123123213qwewqewqewqewqewqeqweqweqweqweqwewqeqweqweqeqweqweqweqweqwww数据_ + " + i);
            simpleEntity.setScore(80 + i);
            simpleEntities.add(simpleEntity);
        }
        return simpleEntities;
    }

    /**
     * 创建Excel文件
     */
    public void doCreate() {

        final List<SimpleEntity> simpleEntities = getSimpleEntities();

        exportExcelFacade.export(new DefaultExportExcelAdapter<SimpleEntity>() {

            @Override
            public void handleExportData(List<SimpleEntity> list, List<String[]> excelData) {

                if (CollectionUtil.isNotEmpty(list)) {
                    for (SimpleEntity simpleEntity : list) {
                        String[] excel = new String[getColumnSize()];
                        excel[0] = String.valueOf(simpleEntity.getUserId());
                        excel[1] = simpleEntity.getUserName();
                        excel[2] = simpleEntity.getAddress();
                        excel[3] = simpleEntity.getTel();
                        excel[4] = String.valueOf(simpleEntity.getScore());
                        excel[5] = simpleEntity.getMemo();
                        excelData.add(excel);
                    }
                }
            }

            @Override
            public void init(ExportExcelContext<SimpleEntity> exportContext) {
                exportContext.setExports(simpleEntities);
                String[][] colTitles = {{"学号", "姓名", "地址", "手机号码", "分数", "备注"}};
                exportContext.setColTitles(colTitles);
                exportContext.setFileName("测试创建生成Excel表格");
                exportContext.setFilePath("E:\\");
                exportContext.setSheetName("表格1");
                exportContext.setTitle("测试表格");
                int[] colWidths = {200, 200, 200, 200, 200, 200};
                exportContext.setColumnWidths(colWidths);
                Map<Integer, String> map = Maps.newHashMap();
                map.put(0, ExportFrontColor.RED.getColorCode());
                map.put(1, ExportFrontColor.GREEN.getColorCode());
                map.put(2, ExportFrontColor.GREEN.getColorCode());
                exportContext.setRowColors(map);
            }
        });
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        SimpleCreateTest.getInstance().doCreate();
    }
}
