package org.zywx.wbpalmstar.widgetone.uexEasemob.vo.input;

import java.io.Serializable;

/**
 * Created by ylt on 15/3/17.
 */
public class AddContactInputVO implements Serializable {
    private static final long serialVersionUID = -5054249167645567585L;
    private String toAddUsername;
    private String reason;

    public String getToAddUsername() {
        return toAddUsername;
    }

    public void setToAddUsername(String toAddUsername) {
        this.toAddUsername = toAddUsername;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
