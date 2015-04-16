package org.zywx.wbpalmstar.widgetone.uexEasemob.vo.input;

import java.io.Serializable;

/**
 * Created by ylt on 15/3/17.
 */
public class HistoryInputVO implements Serializable {

    private static final long serialVersionUID = 2560136159108323344L;
    private String username;//单聊时聊天人的userName或者群聊时groupid
    private String chatType;//1-单聊，2-群聊
    private String startMsgId;//获取startMsgId之前的pagesize条消息
    private String pagesize;//分页大小，为0时获取所有消息
    private String msgId;//单个消息的id
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getChatType() {
        return chatType;
    }

    public void setChatType(String chatType) {
        this.chatType = chatType;
    }

    public String getStartMsgId() {
        return startMsgId;
    }

    public void setStartMsgId(String startMsgId) {
        this.startMsgId = startMsgId;
    }

    public String getPagesize() {
        return pagesize;
    }

    public void setPagesize(String pagesize) {
        this.pagesize = pagesize;
    }


    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
}
