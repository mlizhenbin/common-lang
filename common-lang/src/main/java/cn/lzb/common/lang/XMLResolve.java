package cn.lzb.common.lang;

import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
/**
 * 功能描述： 返回XML字符串解析器
 *
 * @author: Zhenbin.Li
 * email： zhenbin.li@okhqb.com
 * company：华强北在线
 * Date: 13-12-21 Time：上午11:46
 */
public class XMLResolve {

    /**
     * 打印XMLResolve.java日志信息
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(XMLResolve.class);

    /**
     * 解析创建保险业务接口XML数据
	     <?xml version="1.0" encoding="UTF-8"?>
		 <TransData>
		    <Head>
		        <TRAN_CODE>000265</TRAN_CODE>
		        <BankDate>20140115</BankDate>
		        <BankTime>09:58:40</BankTime>
		        <SaleChannel>1</SaleChannel>
		        <SerialNumber>2014011531043</SerialNumber>
		    </Head>
		    <Body>
		        <ResponseCode>999999</ResponseCode>
		        <Message>成功</Message>
		        <Order>
		            <totalActualPremium>199.00</totalActualPremium>
		            <applyPolicyNO>50515001900147626232</applyPolicyNO>
		            <totalInsuredAmount>199.00</totalInsuredAmount>
		            <insuranceEndTime>2015-01-15 23:59:59</insuranceEndTime>
		            <insuranceBeginTime>2014-01-15 00:00:00</insuranceBeginTime>
		            <OrderNo>E1401150958300003</OrderNo>
		            <Policy>10515001900113153207</Policy>
		        </Order>
		    </Body>
		 </TransData>
     * @param xml xml字符串
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static Map<String, String> resolveCreateResponse(String xml) {

        if (StringUtil.isBlank(xml)) {
            return null;
        }

        Map<String, String> resolves = Maps.newLinkedHashMap();
        try {

            Document doc = DocumentHelper.parseText(xml);
            Element rootElement = doc.getRootElement();

            // 获取根节点下的子节点Head
            Iterator headIterator = rootElement.elementIterator("Head");
            // 遍历head节点
            while (headIterator.hasNext()) {
                Element headElement = (Element) headIterator.next();
                resolves.put("tradeCode", headElement.elementTextTrim("TRAN_CODE"));
                resolves.put("BankDate", headElement.elementTextTrim("BankDate"));
                resolves.put("BankTime", headElement.elementTextTrim("BankTime"));
                resolves.put("SaleChannel", headElement.elementTextTrim("SaleChannel"));
                resolves.put("SerialNumber", headElement.elementTextTrim("SerialNumber"));
            }

            //获取根节点下的子节点body
            Iterator bodyIterator = rootElement.elementIterator("Body");
            // 遍历body节点
            while (bodyIterator.hasNext()) {

                Element bodyElement = (Element) bodyIterator.next();
                resolves.put("ResponseCode", bodyElement.elementTextTrim("ResponseCode"));
                resolves.put("Message", bodyElement.elementTextTrim("Message"));

                Iterator orderIterator = bodyElement.elementIterator("Order");
                while (orderIterator.hasNext()) {
                    Element orderElement = (Element) orderIterator.next();
                    resolves.put("totalActualPremium", orderElement.elementTextTrim("totalActualPremium"));
                    resolves.put("applyPolicyNO", orderElement.elementTextTrim("applyPolicyNO"));
                    resolves.put("totalInsuredAmount", orderElement.elementTextTrim("totalInsuredAmount"));
                    resolves.put("insuranceEndTime", orderElement.elementTextTrim("insuranceEndTime"));
                    resolves.put("insuranceBeginTime", orderElement.elementTextTrim("insuranceBeginTime"));
                    resolves.put("OrderNo", orderElement.elementTextTrim("OrderNo"));
                    resolves.put("Policy", orderElement.elementTextTrim("Policy"));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("解析XML数据异常， xml=" + xml, e);
        }

        return resolves;
    }
}
