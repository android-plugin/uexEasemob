package org.zywx.wbpalmstar.widgetone.uexEasemob.vo.output;

import java.io.Serializable;
import java.util.List;

/**
 */
public class GroupsOutputVO implements Serializable {
    private static final long serialVersionUID = -1305826581588016313L;

    private String result;

    private String errorMsg;

    private  List <GroupResultVO> grouplist;

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

    public List<GroupResultVO> getGrouplist() {
        return grouplist;
    }

    public void setGrouplist(List<GroupResultVO> grouplist) {
        this.grouplist = grouplist;
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }
}
