package org.zywx.wbpalmstar.widgetone.uexEasemob.vo.output;

import java.io.Serializable;

/**
 * Created by ylt on 15/3/18.
 */
public class CallReceiveOutputVO implements Serializable {

    private static final long serialVersionUID = 127862243459131255L;

    private String from;
    private String callType;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }
}
