package com.BFMe.BFMBuyer.javaBean;

/**
 * Description：
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/7/24 12:35
 */
public class JpushBean {

    /**
     * MessageType//消息类型  1：官方推荐，2：关注精选，3：物流通知，4：新的评论
     RelationId//关联Id 比如话题Id，订单Id
     Remark//其他备注说明
     */
    private int MessageType;
    private long RelationId;
    private String Remark;

    public int getMessageType() {
        return MessageType;
    }

    public void setMessageType(int messageType) {
        MessageType = messageType;
    }

    public long getRelationId() {
        return RelationId;
    }

    public void setRelationId(long relationId) {
        RelationId = relationId;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }
}
