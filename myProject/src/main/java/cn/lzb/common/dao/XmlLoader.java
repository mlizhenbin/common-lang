package cn.lzb.common.dao;

import cn.lzb.common.lang.CollectionUtil;
import cn.lzb.common.lang.StringUtil;

import com.google.common.collect.Maps;

import org.apache.poi.util.IOUtils;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能描述：
 *
 * @author: Zhenbin.Li
 * email： zhenbin.li@okhqb.com
 * company：华强北在线
 * Date: 13-12-6 Time：下午2:25
 */
public class XmlLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(XmlLoader.class);

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
        URL url = XmlLoader.class.getClassLoader().getResource("SqlTest.xml");

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
        Map<String, String> xmlAddNodes = getXmlAddNodes(root);
        Map<String, String> xmlSQLs = Maps.newHashMap();
        if (CollectionUtil.isNotEmpty(xmlAddNodes)) {
            for (String nodeKey : xmlAddNodes.keySet()) {

                String nodeValue = xmlAddNodes.get(nodeKey);
                if (StringUtil.isNotBlank(nodeKey) && StringUtil.isNotBlank(nodeValue)) {

                    Node node;
                    StringBuffer nodeSqlBuffer = new StringBuffer();
                    if (nodeKey.equals(SQLOperatorEnum.SELECT.getCode())) {
                        nodeSqlBuffer.append("//").append(SQLOperatorEnum.SELECT.getCode())
                                .append("[@id='").append(nodeValue).append("']");
                    } else if (nodeKey.equals(SQLOperatorEnum.INSERT.getCode())) {
                        nodeSqlBuffer.append("//").append(SQLOperatorEnum.INSERT.getCode())
                                .append("[@id='").append(nodeValue).append("']");
                    } else if (nodeKey.equals(SQLOperatorEnum.UPDATE.getCode())) {
                        nodeSqlBuffer.append("//").append(SQLOperatorEnum.UPDATE.getCode())
                                .append("[@id='").append(nodeValue).append("']");
                    } else if (nodeKey.equals(SQLOperatorEnum.DELETE.getCode())) {
                        nodeSqlBuffer.append("//").append(SQLOperatorEnum.DELETE.getCode())
                                .append("[@id='").append(nodeValue).append("']");
                    }
                    node = root.selectSingleNode(nodeSqlBuffer.toString());
                    xmlSQLs.put(nodeValue, node.getText());
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
	protected Map<String, String> getXmlAddNodes(Element root) {

        if (root == null) {
            return new HashMap<String, String>(0);
        }

        // 获取他的子节点
        List<Element> childNodes = root.elements();
        if (CollectionUtil.isEmpty(childNodes)) {
            return new HashMap<String, String>(0);
        }

        Map<String, String> nodes = Maps.newHashMap();
        for (Element element : childNodes) {
            if (element != null) {
                List<Attribute> attributes = element.attributes();
                if (CollectionUtil.isNotEmpty(attributes)) {
                    for (Attribute attribute : attributes) {
                        nodes.put(element.getName(), attribute.getValue());
                    }
                }
            }
        }
        return nodes;
    }

    /**
     * SQL操作枚举
     */
    protected enum SQLOperatorEnum {

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

        private SQLOperatorEnum(String code) {
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
