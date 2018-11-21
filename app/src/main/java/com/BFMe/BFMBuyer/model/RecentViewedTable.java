package com.BFMe.BFMBuyer.model;

/**
 * Description：建表的语句
 * Created by : 奚兆进
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/4/17 12:36
 */

public class RecentViewedTable {

    public static final String TAB_NAME = "tab_recent";//最近搜索表名
    public static final String VIEWED_CONTENT = "viewed_content";//内容字段
    public static final String CREATE_TABLE = "create table "
            +RecentViewedTable.TAB_NAME +"("
            +RecentViewedTable.VIEWED_CONTENT +");";

    public static final String TAB_NAME_BOUND = "tab_recent_bound";//历史搜索的表名
    public static final String VIEWED_CONTENT_BOUND = "viewed_content_bound";//内容字段
    public static final String CREATE_TABLE_BOUND = "create table "
            +RecentViewedTable.TAB_NAME_BOUND +"("
            +RecentViewedTable.VIEWED_CONTENT_BOUND +");";

}
