package org.zywx.wbpalmstar.widgetone.uexEasemob.vo.input;

import java.io.Serializable;

/**
 * Created by ylt on 15/3/18.
 */
public class ImportMsgInputVO implements Serializable {

    private static final long serialVersionUID = 6345340858757707116L;
    private String chatType;
    private String sendType;
    private String textContent;
    private String from;
    private String to;
    private String msgTime;

    public String getChatType() {
        return chatType;
    }

    public void setChatType(String chatType) {
        this.chatType = chatType;
    }

    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(String msgTime) {
        this.msgTime = msgTime;
    }
}
