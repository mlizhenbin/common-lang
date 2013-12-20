package cn.lzb.common.dao;

import cn.lzb.common.common.ElementsMethod;
import cn.lzb.common.lang.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

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
        for (int i = 0; i < sql.length(); i++) {
            String replaceSymbol = String.valueOf(sql.charAt(i));
            if (replaceSymbol.equals(SignConstants.SHARP)) {
                splitIndexList.add(i);
            }
        }

        if (CollectionUtil.isEmpty(splitIndexList)) {
            return null;
        }

        Map<String, Object> propertyValues = Maps.newHashMap();
        int index = 0;
        while (splitIndexList.get(index + 1) != null) {

            int begin = splitIndexList.get(index);
            int end = splitIndexList.get(index + 1);

            String propertyName = sql.substring(begin, end + 1);
            propertyValues.put(propertyName, getReflectValue(propertyName, t));
            index = index + 2;
        }

        // 赋值到对应的变量中
        String sqlStr = assembly(sql, propertyValues);
        return sqlStr;
    }

    protected String assembly(String xmlSql, Map<String, Object> propertyValues) {

//        for (String property : propertyValues.keySet()) {
//
//            StringBuffer sb = new StringBuffer();
//            StringBuffer newStr = new StringBuffer();
//            Set keySet = map.keySet();
//            String key = null;
//            for (Iterator itor = keySet.iterator(); itor.hasNext(); ) {
//                key = (String) itor.next();
//            }
//            sb.append(key);
//            if (map.get(key) != null) {
//                /**
//                 * 一般赋值#property#
//                 */
//                if (key.contains(this.SPLIT_XMLSTR) && !key.contains(this.SPLIT_LIKE_XMLSTR))
//                    newStr.append(this.SPLIT_SQLSTR).append(map.get(key)).append(this.SPLIT_SQLSTR);
//                /**
//                 * 模糊查询赋值#%property%#
//                 */
//                else if (key.contains(this.SPLIT_LIKE_XMLSTR) && key.contains(this.SPLIT_LIKE_XMLSTR))
//                    newStr.append(this.SPLIT_SQLSTR).append(this.SPLIT_LIKE_XMLSTR).append(map.get(key))
//                            .append(this.SPLIT_LIKE_XMLSTR).append(this.SPLIT_SQLSTR);
//                /**
//                 * 不符合规范赋''空串
//                 */
//                else
//                    newStr.append(this.SPLIT_SQLSTR + this.SPLIT_SQLSTR);
//            } else
//                // 空值默认是空串插入
//                newStr.append(this.SPLIT_SQLSTR + this.SPLIT_SQLSTR);
//            boolean flag = xmlSql.contains(sb.toString());
//            if (flag) {
//                tempStr = tempStr.replace(sb.toString(), newStr.toString());
//            }
//        }
//        return tempStr;
        return null;
    }

    /**
     * 通过属性名称获取属性的值
     *
     * @param propertyName 属性名称
     * @param v            对象
     * @return
     */
    protected Object getReflectValue(String propertyName, T v) {

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

}
