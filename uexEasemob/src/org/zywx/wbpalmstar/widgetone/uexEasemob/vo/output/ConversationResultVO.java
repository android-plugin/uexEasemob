package org.zywx.wbpalmstar.widgetone.uexEasemob.vo.output;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yanlongtao on 2015/4/20.
 */
public class ConversationResultVO implements Serializable {
    private static final long serialVersionUID = 6801248916468826327L;
    private String chatter;
    private String isGroup;
    private List<MsgResultVO> messages;

    public String getChatter() {
        return chatter;
    }

    public void setChatter(String chatter) {
        this.chatter = chatter;
    }

    public String getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(String isGroup) {
        this.isGroup = isGroup;
    }

    public List<MsgResultVO> getMessages() {
        return messages;
    }

    public void setMessages(List<MsgResultVO> messages) {
        this.messages = messages;
    }
}
