package cn.lzb.common.dao;

import cn.lzb.common.lang.*;
import com.google.common.collect.Lists;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 功能描述：
 *
 * @author: Zhenbin.Li
 * email： zhenbin.li@okhqb.com
 * company：华强北在线
 * Date: 13-12-6 Time：下午3:06
 */
public class SQLFactory<T> {

    private static XmlLoader xmlLoader = new XmlLoader();

    private final Map<String, String> xmlSQLs;
    
    private final String xmlPath;
    
    public SQLFactory(String xmlPath) {
    	this.xmlPath = xmlPath;
        xmlSQLs = xmlLoader.resolve(xmlPath);
    }

    public String getSQL(String id, T t) {

        if (StringUtil.isBlank(id)) {
            return null;
        }

        String sql = xmlSQLs.get(id);
        if (StringUtil.isBlank()) {
            return null;
        }

        List<Integer> splitIndexList = Lists.newArrayList();
        for(int i = 0; i < sql.length(); i++) {
            String replaceSymbol = String.valueOf(sql.charAt(i));
            if(replaceSymbol.equals(SignConstants.SHARP)) {
                splitIndexList.add(i);
            }
        }

        if (CollectionUtil.isEmpty(splitIndexList)) {
            return null;
        }

        Class<?> clazz = t.getClass();

        int index = 0;
        while(splitIndexList.get(index + 1) != null) {

            int begin = splitIndexList.get(index);
            int end = splitIndexList.get(index + 1);

            String str = sql.substring(begin, end + 1);

            try {

                Object value = new ListConvertAdapter<T, Object>(null, null).getReflectValue(t);
                Map<Object, Object> map = new HashMap<Object, Object>();
                map.put(str, value);
                valuesList.add(map);
            } catch (Exception e) {
                e.printStackTrace();
            }
            fieldlist.add(str);
            index = index + 2;
        }

        /**
         * 把获取的值替换掉字符串中的属性值
         */
        String sqlStr = this.replaceStr(sql.toString(), valuesList);
        return sqlStr;
    }

    /**
     * 获取属性的传入关键属性值
     *
     * @param v
     * @return
     */
    public Object getReflectValue(V v) {

        Class<?> clazz = v.getClass();
        // 查询属性在类中存不存在
        // private方法查询
        Field field = null;
        try {
            field = clazz.getDeclaredField(propertyName);
        } catch (NoSuchFieldException e) {
        }
        // 查询不到找public方法
        if (field == null) {
            try {
                field = clazz.getField(propertyName);
            } catch (NoSuchFieldException e) {
            }
        }
        // 还是为空直接返回
        if (field == null) {
            return null;
        }

        // 获取方法名称
        StringBuffer nameBuffer = new StringBuffer();
        nameBuffer.append(ElementsMethod.GET.getMethodHeadCode()).append(propertyName);

        // 找出对应方法
        Method getPropertyNameMethod = null;
        Method[] methods = clazz.getMethods();
        if (ArrayUtil.isEmpty(methods)) {
            return null;
        }
        for (Method method : methods) {
            if (method.getName().toUpperCase().equals(nameBuffer.toString().toUpperCase())) {
                getPropertyNameMethod = method;
                break;
            }
        }

        // 找不到对应属性的GET方法
        if (getPropertyNameMethod == null) {
            return null;
        }

        try {
            return getPropertyNameMethod.invoke(v);
        } catch (IllegalAccessException ex) {
            return null;
        } catch (InvocationTargetException ex) {
            return null;
        }
    }

//
//    /**
//     * 动态生成sql语句
//     * @param xmlSql
//     * @param valuesList
//     * @return
//     */
//    private String replaceStr(String xmlSql, List<Map<Object, Object>> valuesList) {
//
//        String tempStr = xmlSql;
//        for(Map<Object, Object> map : valuesList) {
//            StringBuffer sb = new StringBuffer();
//            StringBuffer newStr = new StringBuffer();
//            Set keySet = map.keySet();
//            String key = null;
//            for(Iterator itor = keySet.iterator(); itor.hasNext();) {
//                key = (String) itor.next();
//            }
//            sb.append(key);
//            if(map.get(key) != null) {
//                /**
//                 * 一般赋值#property#
//                 */
//                if(key.contains(this.SPLIT_XMLSTR) && !key.contains(this.SPLIT_LIKE_XMLSTR))
//                    newStr.append(this.SPLIT_SQLSTR).append(map.get(key)).append(this.SPLIT_SQLSTR);
//                /**
//                 * 模糊查询赋值#%property%#
//                 */
//                else if(key.contains(this.SPLIT_LIKE_XMLSTR) && key.contains(this.SPLIT_LIKE_XMLSTR))
//                    newStr.append(this.SPLIT_SQLSTR).append(this.SPLIT_LIKE_XMLSTR).append(map.get(key))
//                            .append(this.SPLIT_LIKE_XMLSTR).append(this.SPLIT_SQLSTR);
//                /**
//                 * 不符合规范赋''空串
//                 */
//                else
//                    newStr.append(this.SPLIT_SQLSTR + this.SPLIT_SQLSTR);
//            }
//            else
//                // 空值默认是空串插入
//                newStr.append(this.SPLIT_SQLSTR + this.SPLIT_SQLSTR);
//            boolean flag = xmlSql.contains(sb.toString());
//            if(flag) {
//                tempStr = tempStr.replace(sb.toString(), newStr.toString());
//            }
//        }
//        return tempStr;
//    }
//
//    /**
//     * 获取配置参数名称，对应POJO属性参数
//     *
//     * @param splitStr
//     *          XML #Property#配置属性参数
//     * @return
//     */
//    private String getMethodName(String splitStr) {
//
//        StringBuilder sb = new StringBuilder();
//        if(splitStr != null && !splitStr.equals("")) {
//            for(int i = 0; i < splitStr.length(); i++) {
//                char symbol = splitStr.charAt(i);
//                if(symbol != this.SPLIT_XMLCHAR && symbol != this.SPLIT_LIKE_XMLCHAR)
//                    sb.append(symbol);
//            }
//            return sb.toString();
//        }
//        return null;
//    }
//
//    /**
//     * 通过SQL ID获取SQL语句
//     * @param sqlId
//     * @return
//     */
//    private String getXMLSql(String sqlId) {
//
//        String sql = null;
//        if(sqlId == null || sqlId.equals(""))
//            return null;
//        if(this.sqlMapList != null && this.sqlMapList.size() > 0) {
//            for(Map<String, String> sqlmap : this.sqlMapList) {
//                if(sqlmap.get(sqlId) != null) {
//                    sql = sqlmap.get(sqlId);
//                    break;
//                }
//            }
//        }
//        return sql;
//    }

}
