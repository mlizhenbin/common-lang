package cn.lzb.common.lang;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Collection相关的工具方法
 * @author afei
 * @version 2007-7-5
 */
public class CollectionUtil {
    private static final Logger logger = LoggerFactory.getLogger(CollectionUtil.class);

    /**
     * 判断Collection是否null或者size为0
     * @param c
     * @return
     */
    @SuppressWarnings("rawtypes")
	public static final boolean isEmpty(Collection c) {
        return null == c || 0 == c.size()? true : false;
    }
    
    /**
     * 使用logger.debug()打印里面的对象
     * 一般用于单元测试
     * @param c
     */
    @SuppressWarnings("rawtypes")
	public static final void print(Collection c) {
        if(isEmpty(c)) {
            return;
        }
        for (Object object : c) {
            logger.debug(object.toString());
        }
    }
    
    /**
     * 从List的每个对象中抽取id值, List中对象必须有getId()方法
     * @param list
     * @return String[] ids
     */
    @SuppressWarnings("rawtypes")
	public static final String[] getIdsFromListToStringArray(List list) {
        if(null == list || 0 == list.size()) {
            return ArrayUtil.EMPTY_STRING_ARRAY;
        }
        
        List<String> ids = new ArrayList<String>();
        for (Object object : list) {
            try {
                Object id = BeanUtils.getProperty(object, "id");
                if(null != id) {
                    ids.add(String.valueOf(id));
                }
            } catch (Exception ex) {
                logger.info("[CollectionUtil.getIdsFromListToStringArray] cant get ID from " + object);
                continue;
            }
        }
        
        return ids.toArray(new String[ids.size()]);
    }
    
    /**
     * 从List的每个对象中抽取id值, List中对象必须有getId()方法
     * @param list
     * @return Integer[] ids
     */
    @SuppressWarnings("rawtypes")
	public static final Integer[] getIdsFromListToIntegerArray(List list) {
        if(null == list || 0 == list.size()) {
            return ArrayUtil.EMPTY_INTEGER_OBJECT_ARRAY;
        }
        
        List<Integer> ids = new ArrayList<Integer>();
        for (Object object : list) {
            try {
                Object id = BeanUtils.getProperty(object, Constants.KEY_ID);
                if(null != id) {
                    ids.add(Integer.valueOf(String.valueOf(id)));
                }
            } catch (Exception ex) {
                logger.info("[CollectionUtil.getIdsFromListToIntegerArray] cant get ID from " + object);
                continue;
            }
        }
        
        return ids.toArray(new Integer[ids.size()]);
    }
    
    /**
     * 从List的每个对象中抽取id值, List中对象必须有getId()方法
     * @param list
     * @return Long[] ids
     */
    @SuppressWarnings("rawtypes")
	public static final Long[] getIdsFromListToLongArray(List list) {
        if(null == list || 0 == list.size()) {
            return ArrayUtil.EMPTY_LONG_OBJECT_ARRAY;
        }
        
        List<Integer> ids = new ArrayList<Integer>();
        for (Object object : list) {
            try {
                Object id = BeanUtils.getProperty(object, Constants.KEY_ID);
                if(null != id) {
                    ids.add(Integer.valueOf(String.valueOf(id)));
                }
            } catch (Exception ex) {
                logger.info("[CollectionUtil.getIdsFromListToLongArray] cant get ID from " + object);
                continue;
            }
        }
        
        return ids.toArray(new Long[ids.size()]);
    }

    /**
     * 将list转化成String[]
     * 简化String[] groupids = (String[])list.toArray(new String[list.size()]);
     * @param list
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static final String[] toStringArray(List list) {
        if(CollectionUtil.isEmpty(list)) {
            return Constants.EMPTY_ARRAY_STRING;
        }
        return (String[])list.toArray(new String[list.size()]);
    }

    /**
     * 将list转化成Integer[]
     * 简化Integer[] groupids = (Integer[])list.toArray(new Integer[list.size()]);
     * @param list
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static final Integer[] toIntegerArray(List list) {
        if(CollectionUtil.isEmpty(list)) {
            return Constants.EMPTY_ARRAY_INTEGER;
        }
        return (Integer[])list.toArray(new Integer[list.size()]);
    }

    /**
     * 将list转化成Long[]
     * 简化Long[] groupids = (Long[])list.toArray(new Long[list.size()]);
     * @param list
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static final Long[] toLongArray(List list) {
        if(CollectionUtil.isEmpty(list)) {
            return Constants.EMPTY_ARRAY_LONG;
        }
        return (Long[])list.toArray(new Long[list.size()]);
    }

    /**
     * 将set转化成String[]
     * 简化String[] groupids = (String[])set.toArray(new String[set.size()]);
     * @param set
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static final String[] toStringArray(Set set) {
        if(CollectionUtil.isEmpty(set)) {
            return Constants.EMPTY_ARRAY_STRING;
        }
        return (String[])set.toArray(new String[set.size()]);
    }

    /**
     * 将set转化成Integer[]
     * 简化Integer[] groupids = (Integer[])set.toArray(new Integer[set.size()]);
     * @param set
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static final Integer[] toIntegerArray(Set set) {
        if(CollectionUtil.isEmpty(set)) {
            return Constants.EMPTY_ARRAY_INTEGER;
        }
        return (Integer[])set.toArray(new Integer[set.size()]);
    }

    /**
     * 将set转化成Long[]
     * 简化Long[] groupids = (Long[])set.toArray(new Long[set.size()]);
     * @param set
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static final Long[] toLongArray(Set set) {
        if(CollectionUtil.isEmpty(set)) {
            return Constants.EMPTY_ARRAY_LONG;
        }
        return (Long[])set.toArray(new Long[set.size()]);
    }

    /**
     * 将单个Str转化成String[]
     * @param str
     * @return
     */
    public static final String[] toStringArray(String str) {
        if(StringUtil.isBlank(str)) {
            return Constants.EMPTY_ARRAY_STRING;
        }
        return new String[] {str};
    }

    /**
     * 将单个int转化成String[]
     * @param i
     * @return
     */
    public static final String[] toStringArray(int i) {
        return new String[] {String.valueOf(i)};
    }

    /**
     * 将单个long转化成String[]
     * @param i
     * @return
     */
    public static final String[] toStringArray(long i) {
        return new String[] {String.valueOf(i)};
    }

    /**
     * 将int[]转化成String[]
     * @param is
     * @return
     */
    public static final String[] toStringArray(int[] is) {
        if(null == is || 0 == is.length) {
            return Constants.EMPTY_ARRAY_STRING;
        }
        
        int length = is.length;
        String[] strings = new String[length];
        for (int i = 0; i < length; i++) {
            String s = String.valueOf(is[i]);
            strings[i] = s;
        }
        return strings;
    }

    /**
     * 将long[]转化成String[]
     * @param is
     * @return
     */
    public static final String[] toStringArray(long[] is) {
        if(null == is || 0 == is.length) {
            return Constants.EMPTY_ARRAY_STRING;
        }
        
        int length = is.length;
        String[] strings = new String[length];
        for (int i = 0; i < length; i++) {
            String s = String.valueOf(is[i]);
            strings[i] = s;
        }
        return strings;
    }

    /**
     * 将List中为null的元素删除掉
     */
    @SuppressWarnings("rawtypes")
    public static final List chopList(List list) {
        if(null == list) {
            return null;
        } else {
            for (int i = 0; i < list.size(); i++) {
                if(null == list.get(i)) {
                    list.remove(i);
                    i--;
                }
            }
            return list;
        }
    }
    
    /**
	 * 判断指定的一个或多个集合, 是否存在空集合
	 * 
	 * @param colls 参数集合, 一个或多个
	 * @return 是否存在空集合
	 * @author xiaofeng
	 */
	public static boolean isEmpty(Collection<?>... colls) {
		boolean result = false;
		
		for (Collection<?> coll : colls) {
			if (null == coll || coll.isEmpty()) {
				result = true;
				break;
			}
		}
		
		return result;
	}
	
	/**
	 * 判断指定的一个或多个集合, 是否存在空集合
	 * 
	 * @param maps 参数集合, 一个或多个
	 * @return 是否存在空集合
	 * @author xiaofeng
	 */
	public static boolean isEmpty(Map<?, ?>... maps) {
		boolean result = false;
		
		for (Map<?, ?> map : maps) {
			if (null == map || map.isEmpty()) {
				result = true;
				break;
			}
		}
		
		return result;
	}
	
	/**
	 * 判断指定的一个或多个集合, 是否都是非空集合
	 * 
	 * @param colls 参数集合, 一个或多个
	 * @return 是否都是非空集合
	 * @author xiaofeng
	 */
	public static boolean isNotEmpty(Collection<?>... colls) {
		return !isEmpty(colls);
	}
	
	/**
	 * 判断指定的一个或多个集合, 是否都是非空集合
	 * 
	 * @param maps 参数集合, 一个或多个
	 * @return 是否都是非空集合
	 * @author xiaofeng
	 */
	public static boolean isNotEmpty(Map<?, ?>... maps) {
		return !isEmpty(maps);
	}
	
	/**
     * 判断Collection是否不为空
     * @param c 集合
     * @return 是否不为空
     * @author xiaofeng
     */
    public static final boolean isNotEmpty(Collection<?> c) {
        return null != c && !c.isEmpty();
    }
    
    /**
     * 两个list之间merge，暂时不支持对象作为key
     * @param toList 目标list 
     * @param toKeyProperty 源list中对象的映射的唯一 property
     * @param setProperty   源list中对象设置dest中对象的 property
     * @param fromList 源list
     * @param fromKeyProperty 目标的映射的相对应的 property
     */
    public static final void merge(List<? extends Object> toList, String toKeyProperty, String setProperty,  List<? extends Object> fromList, String fromKeyProperty)
    {
        //不为空
        if (!CollectionUtil.isEmpty(toList) && !CollectionUtil.isEmpty(fromList)){
            try{
                //建立 from 的Map
                Map<String, Object> m = new HashMap<String, Object>();
                for (Object from : fromList){
                    m.put(BeanUtils.getProperty(from, fromKeyProperty), from);
                }
                
                //遍历
                for (Object to : toList){
                    String v = BeanUtils.getProperty(to, toKeyProperty);
                    Object from = m.get(v);
                    if (from != null){
                        BeanUtils.setProperty(to, setProperty, from);
                    }
                }
            }
            catch (Exception e){
                logger.error(e.getMessage(), e);
            }
        }
    }
    
    /**
     * 把list中的对象的 property 组装成 类似 "1,2,3,4"的形式
     * @param source
     * @param sourceKeyProperty
     * @return String
     */
    public static final String list2String(List<? extends Object> source, String sourceKeyProperty){
        return list2String(source, sourceKeyProperty, Constants.DELIMITER_COMMA, false);
    }
    
    
    /**
     * 把list中的对象的 property 组装成 类似 "1,2,3,4"的形式
     * @param source
     * @param sourceKeyProperty
     * @param omitDuplicate
     * @return String
     */
    public static final String list2String(List<? extends Object> source, String sourceKeyProperty, boolean omitDuplicate){
        return list2String(source, sourceKeyProperty, Constants.DELIMITER_COMMA, omitDuplicate);
    }
    
    /**
     * 把list中的对象的 property 组装成 类似 "1,2,3,4"的形式
     * @param source 源list 
     * @param sourceKeyProperty 源list中对象的 property
     * @param separator 分隔符
     * @param omitDuplicate 是否去除 重复的id
     * @return  组装好的 String
     */
    public static final String list2String(List<? extends Object> source, 
            String sourceKeyProperty, String separator, boolean omitDuplicate) {
        return list2String(source, sourceKeyProperty, separator, omitDuplicate, -1);
    }
    
    /**
     * 把list中的对象的 property 组装成 类似 "1,2,3,4"的形式
     * @param source 源list 
     * @param sourceKeyProperty 源list中对象的 property
     * @param separator 分隔符
     * @param omitDuplicate 是否去除 重复的id
     * @param size 返回的id值数量 如果size小于等于0表示返回所有 如果size大于id总数则返回所有
     * @return  组装好的 String
     */
    public static final String list2String(List<? extends Object> source, 
            String sourceKeyProperty, String separator, boolean omitDuplicate, int size)
    {
        if (separator == null){
            separator = Constants.DELIMITER_COMMA;
        }
        String s = null;
        if (!CollectionUtil.isEmpty(source)){
            Collection<Object> c = getObjectCollection(source, sourceKeyProperty, omitDuplicate);
            StringBuilder sBuilder = new StringBuilder();
            if (c != null){
                int count = 0;
                for (Object value : c){
                    if (0 < size && count >= size) {
                        break;
                    }
                    count ++;
                    sBuilder.append(value).append(separator);
                }
                s = sBuilder.toString();
                if (s.endsWith(separator)){
                    s = s.substring(0, s.length() - separator.length());
                }
            }
        }
        return s;
    }
    
    /**
     * @param source
     * @param sourceKeyProperty
     * @param filterDuplicate
     * @return Collection<Object>
     */
    public static final Collection<Object> getObjectCollection(List<? extends Object> source, String sourceKeyProperty, boolean filterDuplicate){
        Collection<Object> c = null;
        if (filterDuplicate){
            c = new HashSet<Object>();
        }else{
            c = new ArrayList<Object>();
        }
        
        for (Object o : source){
            try{
                c.add(BeanUtils.getProperty(o,sourceKeyProperty));
            }
            catch (Exception e){
                logger.error(e.getMessage(), e);
            }
        }
        return c;
    }
    
    /**
     * 把Collection转换成string
     * @param collection
     * @return String
     */
    public static String collection2String(Collection<? extends Object> collection){
        return collection2String(collection, Constants.DELIMITER_COMMA);
    }
    
    /**
     * 返回 string
     * @param separator
     * @return String
     */
    public static String collection2String(Collection<? extends Object> collection, String separator){
        if (separator == null){
            separator = Constants.DELIMITER_COMMA;
        }
        StringBuilder sBuilder = new StringBuilder();
        for (Object o : collection){
            sBuilder.append(String.valueOf(o)).append(separator);
        }
        String ret = sBuilder.toString();
        if (ret.endsWith(separator)){
            ret = ret.substring(0, ret.lastIndexOf(separator));
        }
        return ret;
    }
    
    /**
     * 返回long 数组
     * @param source
     * @param sourceKeyProperty
     * @return list2longArray
     */
    public static final long[] list2longArray(List<? extends Object> source, String sourceKeyProperty){
        return list2longArray(source, sourceKeyProperty, false);
    }
    
    /**
     * 把list中的对象的property 组装成 long 数组
     * @param source 源list
     * @param sourceKeyProperty 源list中对象的 property
     * @return long[]
     */
    public static final long[] list2longArray(List<? extends Object> source, String sourceKeyProperty, boolean filterDuplicate)
    {
        long[] array = null;
        if (!CollectionUtil.isEmpty(source)){
            Collection<Object> c = getObjectCollection(source, sourceKeyProperty, filterDuplicate);
            if (c != null){
                array = collection2longArray(c);
            }
        }
        return array;
    }
    
    public static final long[] collection2longArray(Collection<Object> collection){
        long[] array =  new long[collection.size()];
        int index = 0;
        for (Object value : collection){
            try{
                array[index] = Long.parseLong(String.valueOf(value));
                index ++;
            }
            catch (Exception e){
                logger.error(e.getMessage(), e);
            }
        }
        return array;
    }
}
