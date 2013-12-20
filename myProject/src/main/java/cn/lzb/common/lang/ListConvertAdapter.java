package cn.lzb.common.lang;

import cn.lzb.common.common.ElementsMethod;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 功能描述：List转换获取参数值适配器
 *
 * @author: Zhenbin.Li
 * email： zhenbin.li@okhqb.com
 * company：华强北在线
 * Date: 13-11-3 Time：下午12:29
 */
public class ListConvertAdapter<T, V> {

    /**
     * 生成类属性对象List集合
     */
    private List<V> objects;

    /**
     * 需要生成List的类属性名称
     */
    private String propertyName;

    /**
     * 构造方法： 适配器在创建时，必须传List参数和生成属性名称
     *
     * @param objects      List集合
     * @param propertyName 需要生成List的类属性名称
     */
    public ListConvertAdapter(List<V> objects, String propertyName) {

        this.objects = objects;
        this.propertyName = propertyName;

        if (!validConvertParams()) {
            throw new RuntimeException("传入参数为空，objects=" + objects + ", propertyName=" + propertyName);
        }
    }

    /**
     * 获取某一个属性的集合
     *
     * eg：主订单下有多个子订单，通过主订单获取主订单集合
     * <p>Key：值ID， Value：值ID对应的List集合</p>
     * @return Map
     */
    @SuppressWarnings("unchecked")
	public Map<T, List<V>> getKeyElements() {

        Set<T> keys = getUnRepeatElements();
        if (CollectionUtil.isEmpty(keys)) {
            return Maps.newHashMap();
        }

        Map<T, List<V>> props = Maps.newLinkedHashMap();
        for (V v : objects) {

            List<V> vs;
            Object key = getReflectValue(v);
            if (key == null) {
                return Maps.newLinkedHashMap();
            }

            if (props.get(key) == null) {
                vs = Lists.newArrayList();
            } else {
                vs = props.get(key);
            }
            vs.add(v);
            props.put((T) key, vs);
        }
        return props;
    }

    /**
     * List集合对象的某一个属性转换为Object[]集合
     * <p/>
     * <p>获取属性list去重复，返回数组</p>
     *
     * @return
     */
    public Object[] getUnRepeatElementsArray() {

        Set<T> objectPropertyElements = getUnRepeatElements();
        if (CollectionUtil.isEmpty(objectPropertyElements)) {
            return null;
        }

        return objectPropertyElements.toArray();
    }

    /**
     * List集合对象的某一个属性转换为Set集合
     * <p/>
     * <p>获取属性list去重复，返回HashSet</p>
     *
     * @return
     */
    public Set<T> getUnRepeatElements() {

        List<T> objectPropertyElements = getElements();
        if (CollectionUtil.isEmpty(objectPropertyElements)) {
            return new HashSet<T>(0);
        }

        Set<T> objectPropertyElementSet = Sets.newHashSet();
        for (T objectPropertyElement : objectPropertyElements) {
            objectPropertyElementSet.add(objectPropertyElement);
        }

        return objectPropertyElementSet;
    }

    /**
     * List集合对象的某一个属性转换为Object[]集合
     *
     * @return
     */
    public Object[] getElementsArray() {

        List<T> objectPropertyElements = getElements();
        if (CollectionUtil.isEmpty(objectPropertyElements)) {
            return null;
        }

        return objectPropertyElements.toArray();
    }

    /**
     * List集合对象的某一个属性转换为List集合
     * <p/>
     * <p>获取属性list不去重复，返回ArrayList</p>
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<T> getElements() {

        List<T> objectPropertyElements = Lists.newArrayList();
        for (V v : objects) {
            Object obj = getReflectValue(v);
            if (obj == null) {
                return getElements();
            }
            objectPropertyElements.add((T) obj);
        }
        return objectPropertyElements;
    }

    /**
     * 获取属性的传入关键属性值
     *
     * @param v
     * @return
     */
    protected Object getReflectValue(V v) {

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
            return getEmptyValues();
        }

        try {
            return getPropertyNameMethod.invoke(v);
        } catch (IllegalAccessException ex) {
            return null;
        } catch (InvocationTargetException ex) {
            return null;
        }
    }


    /**
     * 验证需要转换参数是否符合转换逻辑
     *
     * @return
     */
    protected boolean validConvertParams() {

        // 属性名称为空
        if (StringUtil.isBlank(propertyName)) {
            return false;
        }

        // 传入参数集合为空直接返回空list
        if (CollectionUtil.isEmpty(objects)) {
            return false;
        }

        for (V object : objects) {
            if (object == null) {
                return false;
            }
        }

        return true;
    }

    /**
     * 验证不符合逻辑时，返回空List
     *
     * @return
     */
    private List<T> getEmptyValues() {
        return new ArrayList<T>(0);
    }

    /**
     * 常用获取数组常量，参数取值可以扩展
     */
    protected interface PropertiesAware {
        public static final String id = "id";
        public static final String cid = "cid";
        public static final String spuId = "spuId";
        public static final String skuId = "skuId";
        public static final String skuName = "skuName";
        public static final String orderNo = "orderNo";
    }
}

