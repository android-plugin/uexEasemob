package org.zywx.wbpalmstar.widgetone.uexEasemob.vo.output;

import java.io.Serializable;

/**
 * Created by ylt on 15/8/4.
 */
public class GroupCreateResultVO implements Serializable {

    private static final long serialVersionUID = -2338019654627316233L;

    private boolean isSuccess;
    private String errorStr;
    private GroupResultVO group;

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

    public GroupResultVO getGroup() {
        return group;
    }

    public void setGroup(GroupResultVO group) {
        this.group = group;
    }
}
