package org.zywx.wbpalmstar.widgetone.uexEasemob.vo.input;

import java.io.Serializable;

/**
 * Created by ylt on 15/3/17.
 */
public class CreateGroupInputVO implements Serializable {
    private static final long serialVersionUID = -4705024033323969227L;

    private String groupName;
    private String desc;
    private String[] members;
    private String allowInvite;
    private String maxUsers;
    private boolean needApprovalRequired;
    private String initialWelcomeMessage;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String[] getMembers() {
        return members;
    }

    public void setMembers(String[] members) {
        this.members = members;
    }

    public String getAllowInvite() {
        return allowInvite;
    }

    public void setAllowInvite(String allowInvite) {
        this.allowInvite = allowInvite;
    }

    public String getMaxUsers() {
        return maxUsers;
    }

    public void setMaxUsers(String maxUsers) {
        this.maxUsers = maxUsers;
    }

    public boolean getNeedApprovalRequired() {
        return needApprovalRequired;
    }

    public void setNeedApprovalRequired(boolean needApprovalRequired) {
        this.needApprovalRequired = needApprovalRequired;
    }

    public String getInitialWelcomeMessage() {
        return initialWelcomeMessage;
    }

    public void setInitialWelcomeMessage(String initialWelcomeMessage) {
        this.initialWelcomeMessage = initialWelcomeMessage;
    }
}
