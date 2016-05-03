package org.zywx.wbpalmstar.widgetone.uexEasemob.vo.output;

import com.hyphenate.chat.EMGroupInfo;

import java.io.Serializable;
import java.util.List;

/**
 */
public class GroupInfosOutputVO implements Serializable {

    private static final long serialVersionUID = 2239805697002416405L;

    private String result;

    private String errorMsg;

    private List<EMGroupInfo> grouplist;

    private String cursor;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public List<EMGroupInfo> getGrouplist() {
        return grouplist;
    }

    public void setGrouplist(List<EMGroupInfo> grouplist) {
        this.grouplist = grouplist;
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }
}
