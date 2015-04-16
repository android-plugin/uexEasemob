package org.zywx.wbpalmstar.widgetone.uexEasemob.vo.output;

import com.easemob.chat.EMGroup;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ylt on 15/3/17.
 */
public class GroupsOutputVO implements Serializable {

    private static final long serialVersionUID = 2239805697002416405L;

    private String result;

    private String errorMsg;

    private String grouplist;

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

    public String getGrouplist() {
        return grouplist;
    }

    public void setGrouplist(String grouplist) {
        this.grouplist = grouplist;
    }
}
