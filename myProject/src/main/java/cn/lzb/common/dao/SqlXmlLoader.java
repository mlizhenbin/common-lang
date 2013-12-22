package cn.lzb.common.dao;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.util.IOUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import cn.lzb.common.lang.CollectionUtil;
import cn.lzb.common.lang.StringUtil;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * 功能描述：读取XML文件，解析文件内容
 *
 * @author: Zhenbin.Li
 * email： zhenbin.li@okhqb.com
 * company：华强北在线
 * Date: 13-12-6 Time：下午2:25
 */
public class SqlXmlLoader {

    /**
     * 解析XML文件，返回sql集合
     * <p/>
     * <p>KEY:xml文件中ID， VALUE:SQL</p>
     *
     * @param xmlPath xml文件路径
     * @return MAP
     */
    public Map<String, String> resolve(String xmlPath) {

        // 使用SAX方式解析
        SAXReader reader = new SAXReader();

        // 读取XML文件
        URL url = SqlXmlLoader.class.getClassLoader().getResource(xmlPath);

        // 获取XML对象
        Document doc;
        InputStream in = null;
        try {
            in = new FileInputStream(url.getFile());
            doc = reader.read(in);
        } catch (Exception e) {
            throw new RuntimeException("加载SqlTest.xml配置文件出现异常", e);
        } finally {
            IOUtils.closeQuietly(in);
        }
        Element root = doc.getRootElement();

        // 获取XML所有节点
        Map<String, List<String>> xmlAddNodes = getXmlAddNodes(root);
        Map<String, String> xmlSQLs = Maps.newHashMap();
        if (CollectionUtil.isNotEmpty(xmlAddNodes)) {
            for (String nodeKey : xmlAddNodes.keySet()) {

                List<String> nodeValues = xmlAddNodes.get(nodeKey);
                if (CollectionUtil.isNotEmpty(nodeValues)) {
                    for (String nodeValue : nodeValues) {
                        if (StringUtil.isNotBlank(nodeKey) && StringUtil.isNotBlank(nodeValue)) {
                            StringBuffer nodeSqlBuffer = new StringBuffer();
                            if (nodeKey.equals(SQLOperatorType.SELECT.getCode())) {
                                nodeSqlBuffer.append("//").append(SQLOperatorType.SELECT.getCode())
                                        .append("[@id='").append(nodeValue).append("']");
                            } else if (nodeKey.equals(SQLOperatorType.INSERT.getCode())) {
                                nodeSqlBuffer.append("//").append(SQLOperatorType.INSERT.getCode())
                                        .append("[@id='").append(nodeValue).append("']");
                            } else if (nodeKey.equals(SQLOperatorType.UPDATE.getCode())) {
                                nodeSqlBuffer.append("//").append(SQLOperatorType.UPDATE.getCode())
                                        .append("[@id='").append(nodeValue).append("']");
                            } else if (nodeKey.equals(SQLOperatorType.DELETE.getCode())) {
                                nodeSqlBuffer.append("//").append(SQLOperatorType.DELETE.getCode())
                                        .append("[@id='").append(nodeValue).append("']");
                            }
                            Node node = root.selectSingleNode(nodeSqlBuffer.toString());
                            xmlSQLs.put(nodeValue, node.getText());
                        }
                    }
                }
            }
        }

        return xmlSQLs;
    }

    /**
     * 从XML的根节点解析读取所有的节点信息内容
     *
     * @param root XML最外层节点
     * @return
     */
    @SuppressWarnings("unchecked")
    protected Map<String, List<String>> getXmlAddNodes(Element root) {

        if (root == null) {
            return Maps.newHashMap();
        }

        // 获取他的子节点
        List<Element> childNodes = root.elements();
        if (CollectionUtil.isEmpty(childNodes)) {
            return Maps.newHashMap();
        }

        Set<String> operateKeys = Sets.newLinkedHashSet();
        for (Element childNode : childNodes) {
            operateKeys.add(childNode.getName());
        }

        // 获取配置在XML对应的每一个操作ID
        Map<String, List<String>> nodes = Maps.newLinkedHashMap();
        for (String operateKey : operateKeys) {
            List<String> keyValues = Lists.newArrayList();
            for (Element element : childNodes) {
                if (element != null) {
                    if (operateKey.equals(element.getName())) {
                        List<Attribute> attributes = element.attributes();
                        if (CollectionUtil.isNotEmpty(attributes)) {
                            for (Attribute attribute : attributes) {
                                keyValues.add(attribute.getValue());
                            }
                        }
                    }
                }
            }
            nodes.put(operateKey, keyValues);
        }

        return nodes;
    }

    /**
     * SQL操作枚举
     */
    protected enum SQLOperatorType {

        /**
         * 查询
         */
        SELECT("select"),

        /**
         * 增加
         */
        INSERT("insert"),

        /**
         * 修改
         */
        UPDATE("update"),

        /**
         * 删除
         */
        DELETE("delete");

        /**
         * XML文件中SQL操作
         */
        private String code;

        private SQLOperatorType(String code) {
            this.code = code;
        }

        /**
         * 获取SQL操作类型编码
         *
         * @return
         */
        private String getCode() {
            return code;
        }
    }
}
