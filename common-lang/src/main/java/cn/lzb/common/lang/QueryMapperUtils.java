package cn.lzb.common.lang;

import com.google.common.collect.Maps;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 功能描述：对象转化为HashMap
 *
 * @author: Zhenbin.Li
 * email： lizhenbin08@sina.com
 * company：一加科技
 * Date: 15/6/12 Time：23:41
 */
public class QueryMapperUtils {

    private QueryMapperUtils() {

    }

    /**
     * 将查询对接直接转化为HashMap
     *
     * @param <T> 查询对象类型
     * @return HashMap
     */
    public static <T> Map<String, Object> toQueryMap() {
        return toQueryMap(null);
    }

    /**
     * 将查询对接直接转化为HashMap
     *
     * @param obj 查询对象
     * @param <T> 查询对象类型
     * @return HashMap
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> Map<String, Object> toQueryMap(T obj) {
        if (obj == null) {
            return Maps.newHashMap();
        }

        Class type = obj.getClass();
        Map map = Maps.newHashMap();
        BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(type);
            if (beanInfo == null) {
                return map;
            }
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }

        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (!propertyName.equals("class")) {
                Method method = descriptor.getReadMethod();
                try {
                    Object result = method.invoke(obj, new Object[0]);
                    if (result != null) {
                        map.put(propertyName, result);
                    } else {
                        map.put(propertyName, null);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return map;
    }
}
