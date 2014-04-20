package cn.lzb.common.excel;

import cn.lzb.common.excel.enity.MultipleEntity;
import cn.lzb.common.excel.enity.Subject;
import cn.lzb.common.excel.factory.ExportFrontColor;
import cn.lzb.common.excel.impl.CommonExportExcelImpl;
import cn.lzb.common.excel.impl.MultipartExportExcelAdapter;
import cn.lzb.common.lang.CollectionUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 功能描述：复杂表格创建测试类，主要包含列合并单元格
 *
 * @author: Zhenbin.Li
 * email： zhenbin.li@okhqb.com
 * company：华强北在线
 * Date: 13-12-5 Time：上午10:35
 */
public class MultipleCreateTest {

    private static MultipleCreateTest instance = null;

    private MultipleCreateTest() {
    }

    public synchronized static MultipleCreateTest getInstance() {
        if (instance == null) {
            instance = new MultipleCreateTest();
        }
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
    private List<MultipleEntity> getMultipleEntities() {

        List<MultipleEntity> multipleEntities = Lists.newArrayList();
        for (int i = 1; i < 7000; i++) {
            MultipleEntity multipleEntity = new MultipleEntity();
            multipleEntity.setUserId(i);
            multipleEntity.setUserName("okhqb_" + i);
            multipleEntity.setAddress("www.okhqb.com_" + i);
            multipleEntity.setTel("136351462" + i);
            multipleEntity.setMemo("测试数据_ + " + i);
            multipleEntity.setScore(80 + i);

            List<Subject> subjects = Lists.newArrayList();
            for (int j = 0; j < 5; j++) {
                Subject subject = new Subject();
                subject.setCode("00000" + j);
                subject.setName("subject_" + j);
                subject.setScore(new BigDecimal(80 + j));
                subjects.add(subject);
            }
            multipleEntity.setSubjects(subjects);

            multipleEntities.add(multipleEntity);
        }
        return multipleEntities;
    }

    public void doCreate() {

        final List<MultipleEntity> multipleEntities = getMultipleEntities();

        exportExcelFacade.export(new MultipartExportExcelAdapter<MultipleEntity>() {
            @Override
            public void handleExportData(List<MultipleEntity> list, List<String[][]> excelData) {

                if (CollectionUtil.isNotEmpty(list)) {

                    for (MultipleEntity multipleEntity : list) {

                        int size = multipleEntity.getSubjects().size();
                        String[][] excel = new String[size][getColumnSize()];
                        int index = 0;
                        for (int i = 0; i < size; i++) {

                            Subject subject = multipleEntity.getSubjects().get(i);
                            if (index == 0) {
                                excel[i][0] = String.valueOf(multipleEntity.getUserId());
                                excel[i][1] = multipleEntity.getUserName();
                                excel[i][2] = multipleEntity.getAddress();
                                excel[i][3] = multipleEntity.getTel();
                                excel[i][6] = String.valueOf(multipleEntity.getScore());
                                excel[i][8] = multipleEntity.getMemo();
                            }
                            excel[i][4] = subject.getCode();
                            excel[i][5] = subject.getName();
                            excel[i][7] = subject.getScore().toString();
                            index++;
                        }

                        excelData.add(excel);
                    }
                }
            }

            @Override
            public void init(ExportExcelContext<MultipleEntity> exportContext) {

                exportContext.setExports(multipleEntities);
                String[][] colTitles = {{"学号", "姓名", "地址", "手机号码", "课程编号", "课程名称", "综合评分", "分数", "备注"}};
                exportContext.setColTitles(colTitles);
                exportContext.setFileName("测试创建生成Excel合并单元格表格");
                exportContext.setFilePath("E:\\");
                exportContext.setSheetName("表格2");
                exportContext.setTitle("测试合并列表格");
                int[] colWidths = {100, 100, 100, 100, 100, 100, 100, 100, 100};
                exportContext.setColumnWidths(colWidths);

                Map<Integer, String> map = Maps.newHashMap();
                map.put(0, ExportFrontColor.RED.getColorCode());
                map.put(1, ExportFrontColor.GREEN.getColorCode());
                map.put(2, ExportFrontColor.RED.getColorCode());
                exportContext.setRowColors(map);
            }
        });
    }

    public static void main(String[] args) {
        MultipleCreateTest.getInstance().doCreate();
    }
}
