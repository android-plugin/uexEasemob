package org.zywx.wbpalmstar.widgetone.uexEasemob.vo.output;


import java.io.Serializable;

/**
 * Created by ylt on 15/3/18.
 */
public class CmdMsgOutputVO implements Serializable {
    private static final long serialVersionUID = 5593033161174598040L;

    private String msgId;
    private MsgResultVO message;
    private String action;

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public MsgResultVO getMessage() {
        return message;
    }

    public void setMessage(MsgResultVO message) {
        this.message = message;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
