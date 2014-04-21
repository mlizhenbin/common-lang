package cn.lzb.common.excel.impl;

/**
 * 功能描述：生成Excel文件字体颜色，目前只支持红色与绿色
 *
 * @author: Zhenbin.Li
 * email： zhenbin.li@okhqb.com
 * company：华强北在线
 * Date: 13-12-4 Time：下午5:57
 */
public enum ExportFrontStyle {

    /** 红色 */
    RED("RED"),

    /** 绿色 */
    GREEN("GREEN"),

    /** 黑色 */
    BLACK("BLACK"),

    /** 标题样式 */
    TITLE("TITLE"),
    ;

    /** 颜色编码 */
    private String colorCode;

    private ExportFrontStyle(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getColorCode() {
        return colorCode;
    }
}
