package org.zywx.wbpalmstar.widgetone.uexEasemob.vo.output;

import java.io.Serializable;

/**
 * Created by yanlongtao on 2015/4/20.
 */
public class MsgResultVO implements Serializable {

    private static final long serialVersionUID = -111624905854203501L;

    private String from;
    private String to;
    private String messageId;
    private String isGroup;
    private String isRead;
    private String isOfflineMessage;
    private String messageType;
    private MsgBodyVO messageBody;
    private String messageTime;
    private String isAcked;
    private String isDelivered;
    private String length;
    private String ext;


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

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(String isGroup) {
        this.isGroup = isGroup;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public String getIsOfflineMessage() {
        return isOfflineMessage;
    }

    public void setIsOfflineMessage(String isOfflineMessage) {
        this.isOfflineMessage = isOfflineMessage;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public MsgBodyVO getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(MsgBodyVO messageBody) {
        this.messageBody = messageBody;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public String getIsAcked() {
        return isAcked;
    }

    public void setIsAcked(String isAcked) {
        this.isAcked = isAcked;
    }

    public String getIsDelivered() {
        return isDelivered;
    }

    public void setIsDelivered(String isDelivered) {
        this.isDelivered = isDelivered;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }
}
