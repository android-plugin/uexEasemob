package org.zywx.wbpalmstar.widgetone.uexEasemob.vo.output;

import java.io.Serializable;

/**
 * Created by ylt on 15/7/31.
 */
public class SendMsgResultVO implements Serializable {

    private static final long serialVersionUID = -6139276024106523092L;
    private boolean isSuccess=false;
    private String errorStr;
    private MsgResultVO message;


    public boolean isSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getErrorStr() {
        return errorStr;
    }

    public void setErrorStr(String errorStr) {
        this.errorStr = errorStr;
    }

    public MsgResultVO getMessage() {
        return message;
    }

    public void setMessage(MsgResultVO message) {
        this.message = message;
    }
}
