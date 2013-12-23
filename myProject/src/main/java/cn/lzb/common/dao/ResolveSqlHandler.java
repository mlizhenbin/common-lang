package cn.lzb.common.dao;

import cn.lzb.common.common.ClassMethodHeadType;
import cn.lzb.common.lang.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 功能描述：读取XML文件配置SQL，并且自动赋值到SQL中处理器
 *
 * @author: Zhenbin.Li
 * email： zhenbin.li@okhqb.com
 * company：华强北在线
 * Date: 13-12-6 Time：下午3:06
 */
public class ResolveSqlHandler {

    /**
     * 读取配置XML文件
     */
    private static SqlXmlLoader xmlLoader = new SqlXmlLoader();

    /**
     * 初始化获取XML文件SQL配置信息
     */
    private final Map<String, String> xmlSQLs;

    /**
     * 初始化
     *
     * @param xmlPath XML文件路径
     */
    public ResolveSqlHandler(String xmlPath) {
        xmlSQLs = xmlLoader.resolve(xmlPath);
    }

    /**
     * 通过ID直接获取SQL语句
     *
     * @param id 配置XML文件ID
     * @return
     */
    public String getSql(String id) {
        if (StringUtil.isBlank(id)) {
            return null;
        }
        return xmlSQLs.get(id);
    }

    /**
     * 通过属性值获取属性的值
     *
     * @param id    XML文件ID
     * @param value XML配置属性值
     * @return
     */
    public String getSqlByProperty(String id, Object value) {

        if (StringUtil.isBlank(id) || value == null) {
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

        Map<String, Object> propertyValues = new HashMap<String, Object>(1);
        int index = 0;
        while (index < splitIndexList.size() && splitIndexList.get(index + 1) != null) {

            int begin = splitIndexList.get(index);
            int end = splitIndexList.get(index + 1);

            // 配置属性名称
            String configurePropertyName = sql.substring(begin, end + 1);
            propertyValues.put(configurePropertyName, value);
            index = index + 2;
        }

        return assembly(sql, propertyValues);
    }

    /**
     * 通过XML ID与Entity获取SQL
     *
     * @param id  XML ID
     * @param t   Entity对象
     * @param <T> 对象类型
     * @return
     */
    public <T> String getSql(String id, T t) {

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
        while (index < splitIndexList.size() && splitIndexList.get(index + 1) != null) {

            int begin = splitIndexList.get(index);
            int end = splitIndexList.get(index + 1);

            // 配置属性名称
            String configurePropertyName = sql.substring(begin, end + 1);
            // 属性名称
            String propertyName = null;
            if (configurePropertyName.contains(SignConstants.PERCENT_SIGN)) {
                propertyName = sql.substring(begin + 2, end - 1);
            } else {
                propertyName = sql.substring(begin + 1, end);
            }

            propertyValues.put(configurePropertyName, getReflectValue(propertyName, t));
            index = index + 2;
        }

        return assembly(sql, propertyValues);
    }

    /**
     * 组装XML文件，赋值到配置的属性中
     *
     * @param xmlSql         配置XML
     * @param propertyValues 解析配置与值MAP
     * @return
     */
    protected String assembly(String xmlSql, Map<String, Object> propertyValues) {

        if (StringUtil.isBlank(xmlSql) || CollectionUtil.isEmpty(propertyValues)) {
            return null;
        }

        for (String property : propertyValues.keySet()) {

            StringBuffer valueStr = new StringBuffer();
            if (xmlSql.contains(property)) {
                if (property.contains(SignConstants.PERCENT_SIGN)) {
                    valueStr.append(SignConstants.QUOTE).append(SignConstants.PERCENT_SIGN)
                            .append(propertyValues.get(property))
                            .append(SignConstants.PERCENT_SIGN).append(SignConstants.QUOTE);
                } else {
                    valueStr.append(SignConstants.QUOTE)
                            .append(propertyValues.get(property)).append(SignConstants.QUOTE);
                }
            } else {
                valueStr.append(SignConstants.QUOTE).append(SignConstants.QUOTE);
            }
            xmlSql = StringUtil.replace(xmlSql, property, valueStr.toString());
        }

        return xmlSql;
    }

    /**
     * 通过属性名称获取属性的值
     *
     * @param propertyName 属性名称
     * @param v            对象
     * @return
     */
    protected <T> Object getReflectValue(String propertyName, T v) {

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
        nameBuffer.append(ClassMethodHeadType.GET.getMethodHeadCode()).append(propertyName);

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
