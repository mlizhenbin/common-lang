package cn.lzb.common.lang;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: huxing(xing.hu@360hqb.com)
 * Date: 11-9-21
 * Time: 上午9:20
 */
public interface Constants {

    String[] EMPTY_ARRAY_STRING = new String[0];

    Integer[] EMPTY_ARRAY_INTEGER = new Integer[0];

    Long[] EMPTY_ARRAY_LONG = new Long[0];

    List<?> EMPTY_ARRAY_LIST = new ArrayList<Object>(0);

    /**
     * 26个大写字母数组
     */
    String[] ALL_ALPHABETS_UPPER = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};

    /**
     * 26个小写字母数组
     */
    String[] ALL_ALPHABETS_LOWER = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};

    /**
     * 字符串ID长度
     */
    int STRING_ID_LENGTH = 32;

    /**
     * 默认URLEncoding的值
     */
    String DEFAULT_CHARSET = "GBK";

    /**
     * utf8编码
     */
    String UTF8_CHARSET = "utf-8";

    /**
     * 签名方式，目前就是“DSA”
     */
    String DEFAULT_SIGN_TYPE = "DSA";

    /**
     * 超时设置(ms)
     */
    int TIME_OUT = 30*1000;

    /********************************************
     * 公共属性对应的Key常量
     ********************************************/
    String KEY_ID = "id";
    String KEY_IDS = "ids";
    String KEY_STATUS = "status";
    String KEY_GMT_CREATE = "gmtCreate";
    String KEY_GMT_MODIFY = "gmtModify";

    /********************************************
     * 在Context中存放ERROR_resultCode_message
     ********************************************/
    String KEY_ERROR = "error";
    String KEY_ERRORS = "errors";

    /********************************************
     * 常用的正则表达式
     ********************************************/
    String REGEXP_EMAIL = "^[_a-z0-9-]+(\\.[_a-z0-9-]*)*@[a-z0-9-]+([\\.][a-z0-9-]+)+$";
    String REGEXP_MOBILE = "(^1[358]{1}[0-9]{9}$)|(^[0-9]{8}$)";
    String REGEXP_PHONE = "^[0-9,-]{20}$";
    String REGEXP_IDCARD_15 = "^([0-9]){15}$";
    String REGEXP_IDCARD_18 = "^([0-9]){17}[0-9Xx]{1}$";

    /********************************************
     * 常用的分隔符
     ********************************************/
    String DELIMITER_SPACE = " ";
    String DELIMITER_COMMA = ",";
    String DELIMITER_SEMI = ";";
    String DELIMITER_AMP = "&";
    String DELIMITER_WELL = "#";
    String DELIMITER_UNDER = "_";
    String DELIMITER_SLASH = "-";
    String DELIMITER_EQUAL = "=";
    String DELIMITER_VERTICAL = "|";

    /**
     * login模块用于跳转的参数名称
     */
    String KEY_PARAM_REDIRECT_URL = "redirectUrl";

    /********************************************
     * 公司名称定义
     ********************************************/
    String COMPANY_NAME_TAOBAO = "taobao";
    String COMPANY_NAME_ALIPAY = "alipay";
    String COMPANY_NAME_ALISOFT = "alisoft";
    String COMPANY_NAME_ALIBABA = "alibaba";
    String COMPANY_NAME_KOUBEI = "koubei";
    String COMPANY_NAME_YAHOO = "yahoo";
    String COMPANY_NAME_YAHOO_CN = "yahoocn";

    /********************************************
     * component名称定义
     ********************************************/
    String COMPONENT_NAME_AUCTION = "auction";
    String COMPONENT_NAME_MEMBER = "member";
    String COMPONENT_NAME_MYTAOBAO = "mytaobao";
    String COMPONENT_NAME_COMMON = "common";
    String COMPONENT_NAME_SHOP = "shop";
    String COMPONENT_NAME_MESSAGE = "message";

    /********************************************
     * module名称定义
     ********************************************/
    String DEFAULT_MODULE_NAME = "webModule";
    String MODULE_NAME_SUFFIX = "Module";
    String MODULE_NAME_AUCTION = COMPONENT_NAME_AUCTION + MODULE_NAME_SUFFIX;
    String MODULE_NAME_MEMBER = COMPONENT_NAME_MEMBER + MODULE_NAME_SUFFIX;
    String MODULE_NAME_MYTAOBAO = COMPONENT_NAME_MYTAOBAO + MODULE_NAME_SUFFIX;
    String MODULE_NAME_COMMON = COMPONENT_NAME_COMMON + MODULE_NAME_SUFFIX;
    String MODULE_NAME_SHOP = COMPONENT_NAME_SHOP + MODULE_NAME_SUFFIX;
    String MODULE_NAME_MESSAGE = COMPONENT_NAME_MESSAGE + MODULE_NAME_SUFFIX;

    /********************************************
     * 页面名称定义
     ********************************************/
    String PAGE_NAME_INDEX_VM = "index.vm";
    String PAGE_NAME_INDEX_HTM = "index.htm";
    String PAGE_NAME_INDEX_HTML = "index.html";
    String PAGE_NAME_ERROR_VM = "error.vm";
    String PAGE_NAME_ERROR_HTM = "error.htm";
    String PAGE_NAME_ERROR_HTML = "error.html";
    String PAGE_NAME_404_VM = "404.vm";
    String PAGE_NAME_404_HTM = "404.htm";
    String PAGE_NAME_404_HTML = "404.html";
}
