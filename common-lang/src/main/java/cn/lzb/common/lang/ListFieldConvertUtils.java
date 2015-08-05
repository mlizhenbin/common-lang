package cn.lzb.common.lang;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * 功能描述：List转换获取参数值适配器，获取对象某个属性的集合信息
 * <p/>
 * <p/>
 * 使用方式：ListFieldConvertUtils.getFieldXXXX(list集合, 对象属性名称);
 * eg:
 * 常见获取List集合对象某个字段的集合方式：
 * List<TransferOutDTO> transferOutDTOs = this.selectAll("TransferOutstorage.getUnPushedTransferOutList");
 * Set<String> inStorageSet = new HashSet<String>();
 * for (TransferOutDTO transferOutDTO : transferOutDTOList) {
 * inStorageSet.add(transferOutDTO.getInStorage);
 * }
 * 可用来替换代码实现方式：
 * Set<String> inStorageSet = ListFieldConvertUtils.getFieldElementSet(transferOutDTOList, "inStorage");
 * List<String> inStorages = ListFieldConvertUtils.getFieldElements(transferOutDTOList, "inStorage");
 * Object[] inStorageArray = ListFieldConvertUtils.getFieldElementArray(transferOutDTOList, "inStorage");
 *
 * @author: Zhenbin.Li
 * email： lizhenbin@oneplus.cn
 * company：一加科技
 * Date: 15-07-03 Time：下午12:29
 */
public class ListFieldConvertUtils {

    /**
     * Don't let anyone instantiate this class.
     */
    private ListFieldConvertUtils() {
    }

    /**
     * List集合对象的某一个属性转换为List集合
     * <p/>
     * 获取属性list不去重复，返回ArrayList
     *
     * @param objects
     * @param fieldName
     * @param <T>
     * @param <E>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T, E> List<T> getFieldElements(List<E> objects, String fieldName) {
        List<T> elements = Lists.newArrayList();
        for (E e : objects) {
            Object reflectValue = getReflectValue(e, fieldName);
            if (reflectValue == null) {
                continue;
            }
            elements.add((T) reflectValue);
        }
        return elements;
    }

    /**
     * List集合对象的某一个属性转换为Set集合
     *
     * @param objects
     * @param fieldName
     * @param <T>
     * @param <E>
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T, E> Set<T> getFieldElementSet(List<E> objects, String fieldName) {
        List<T> elements = getFieldElements(objects, fieldName);
        if (CollectionUtil.isEmpty(elements)) {
            return new HashSet(0);
        }

        Set<T> objectPropertyElementSet = Sets.newHashSet();
        for (T objectPropertyElement : elements) {
            objectPropertyElementSet.add(objectPropertyElement);
        }

        return objectPropertyElementSet;
    }

    /**
     * List集合对象的某一个属性转换为T[]集合
     *
     * @param objects
     * @param fieldName
     * @param <T>
     * @param <E>
     * @return
     */
    public static <T, E> Object[] getFieldElementArray(List<E> objects, String fieldName) {
        List<T> elements = getFieldElements(objects, fieldName);
        if (CollectionUtil.isEmpty(elements)) {
            return null;
        }

        if (CollectionUtil.isEmpty(elements)) {
            return null;
        }


        return elements.toArray();
    }

    /**
     * List集合对象的某一个属性转换为Object[]集合
     *
     * @param objects
     * @param fieldName
     * @param <T>
     * @param <E>
     * @return
     */
    public static <T, E> Object[] getUnRepeatFieldElementArray(List<E> objects, String fieldName) {
        Set<T> elements = getFieldElementSet(objects, fieldName);
        if (CollectionUtil.isEmpty(elements)) {
            return null;
        }
        return elements.toArray();
    }


    /**
     * 获取某一个属性的集合
     * <p/>
     * eg：主订单下有多个子订单，通过主订单获取主订单集合
     * <p>Key：值ID， Value：值ID对应的List集合</p>
     *
     * @param objects
     * @param fieldName
     * @param <T>
     * @param <E>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T, E> Map<T, List<E>> getKeyFieldElements(List<E> objects, String fieldName) {
        Set<T> keys = getFieldElementSet(objects, fieldName);
        if (CollectionUtil.isEmpty(keys)) {
            return Maps.newHashMap();
        }

        Map<T, List<E>> props = Maps.newLinkedHashMap();
        for (E e : objects) {

            List<E> es;
            Object key = getReflectValue(e, fieldName);
            if (key == null) {
                return Maps.newLinkedHashMap();
            }

            if (props.get(key) == null) {
                es = Lists.newArrayList();
            } else {
                es = props.get(key);
            }
            es.add(e);
            props.put((T) key, es);
        }
        return props;
    }

    /**
     * 获取某一个属性作为主键的对象集合
     * <p/>
     * eg：主订单下有多个子订单，通过主订单获取主订单集合
     * <p>Key：值ID， Value：值ID对应的List集合</p>
     *
     * @param objects
     * @param fieldName
     * @param <T>
     * @param <E>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T, E> Map<T, E> getObjectMap(List<E> objects, String fieldName) {
        Map<T, E> map = Maps.newLinkedHashMap();
        if (CollectionUtil.isEmpty(objects) || StringUtil.isBlank(fieldName)) {
            return map;
        }

        for (E e : objects) {
            T key = getReflectValue(e, fieldName);
            if (key == null) {
                continue;
            }

            map.put(key, e);
        }

        return map;
    }


    /**
     * 获取属性的传入关键属性值
     *
     * @param e
     * @param fieldName
     * @param <T>
     * @param <E>
     * @return
     */
    @SuppressWarnings("unchecked")
    private static <T, E> T getReflectValue(E e, String fieldName) {
        Class<?> clazz = e.getClass();
        // 查询属性在类中存不存在
        // private方法查询
        Field field = null;
        try {
            field = clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException ex) {
        }
        // 查询不到找public方法
        if (field == null) {
            try {
                field = clazz.getField(fieldName);
            } catch (NoSuchFieldException ex) {
            }
        }
        // 还是为空直接返回
        if (field == null) {
            return null;
        }

        // 获取方法名称
        StringBuffer nameBuffer = new StringBuffer();
        nameBuffer.append(ClassMethodHeadType.GET.getMethodHeadCode()).append(fieldName);

        // 找出对应方法
        Method getPropertyNameMethod = null;
        Method[] methods = clazz.getMethods();
        if (ArrayUtils.isEmpty(methods)) {
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
            return (T) getEmptyValues();
        }

        try {
            return (T) getPropertyNameMethod.invoke(e);
        } catch (IllegalAccessException ex) {
            return null;
        } catch (InvocationTargetException ex) {
            return null;
        }
    }

    /**
     * 验证需要转换参数是否符合转换逻辑
     *
     * @param propertyName
     * @param objects
     * @param <E>
     * @return
     */
    protected static <E> boolean validConvertParams(String propertyName, List<E> objects) {

        if (StringUtils.isBlank(propertyName)) {
            return false;
        }

        // 传入参数集合为空直接返回空list
        if (CollectionUtil.isEmpty(objects)) {
            return false;
        }

        for (E object : objects) {
            if (object == null) {
                return false;
            }
        }

        return true;
    }

    /**
     * 验证不符合逻辑时，返回空List
     *
     * @param <T>
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static <T> List<T> getEmptyValues() {
        return new ArrayList(0);
    }

    /**
     * 常用获取数组常量，参数取值可以扩展
     */
    public interface Props {
        public static final String ID = "id";
        public static final String SKU_CODE = "skuCode";
        public static final String SKU_NAME = "skuName";
        public static final String ORDER_NO = "orderNo";
    }

    /**
     * J2EE生成get/set方法的方法头枚举
     */
    private enum ClassMethodHeadType {

        /**
         * get方法
         */
        GET("get"),

        /**
         * boolean方法
         */
        IS("is"),

        /**
         * set方法
         */
        SET("set");

        /**
         * 方法头参数
         */
        private String methodHeadCode;

        /**
         * 构造方法
         *
         * @param methodHeadCode
         */
        private ClassMethodHeadType(String methodHeadCode) {
            this.methodHeadCode = methodHeadCode;
        }

        /**
         * 获取方法Head枚举
         *
         * @return
         */
        public String getMethodHeadCode() {
            return methodHeadCode;
        }
    }
}

