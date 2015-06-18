package org.zywx.wbpalmstar.widgetone.uexEasemob.vo.output;

import java.io.Serializable;

/**
 * Created by ylt on 2015/5/4.
 */
public class ChatterInfoVO implements Serializable {
    private static final long serialVersionUID = 2441922867733150893L;

    private String chatter;
    private String groupName;
    private String isGroup;
    private String chatType;//0-单聊 1-群聊 2-聊天室
    private String unreadMsgCount;
    private MsgResultVO lastMsg;

    public String getChatter() {
        return chatter;
    }

    public void setChatter(String chatter) {
        this.chatter = chatter;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(String isGroup) {
        this.isGroup = isGroup;
    }

    public String getUnreadMsgCount() {
        return unreadMsgCount;
    }

    public void setUnreadMsgCount(String unreadMsgCount) {
        this.unreadMsgCount = unreadMsgCount;
    }

    public MsgResultVO getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(MsgResultVO lastMsg) {
        this.lastMsg = lastMsg;
    }

    public String getChatType() {
        return chatType;
    }

    public void setChatType(String chatType) {
        this.chatType = chatType;
    }
}
