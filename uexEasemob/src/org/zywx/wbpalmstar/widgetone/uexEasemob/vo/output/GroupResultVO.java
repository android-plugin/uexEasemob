package org.zywx.wbpalmstar.widgetone.uexEasemob.vo.output;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanlongtao on 2015/4/20.
 */
public class GroupResultVO implements Serializable {
    private static final long serialVersionUID = -3835632546422270073L;
    protected String groupSubject;
    protected String owner;
    protected List<String> members;
    protected boolean isPublic;
    protected boolean allowInvites;//是否允许群成员邀请人进群
    protected boolean membersOnly;//需要申请和验证才能加入
    protected int groupMaxUserCount = 0;
    protected boolean isBlock = false;
    private String groupId;

    public String getGroupSubject() {
        return groupSubject;
    }

    public void setGroupSubject(String groupSubject) {
        this.groupSubject = groupSubject;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public boolean isAllowInvites() {
        return allowInvites;
    }

    public void setAllowInvites(boolean allowInvites) {
        this.allowInvites = allowInvites;
    }

    public boolean isMembersOnly() {
        return membersOnly;
    }

    public void setMembersOnly(boolean membersOnly) {
        this.membersOnly = membersOnly;
    }

    public int getGroupMaxUserCount() {
        return groupMaxUserCount;
    }

    public void setGroupMaxUserCount(int groupMaxUserCount) {
        this.groupMaxUserCount = groupMaxUserCount;
    }

    public boolean isBlock() {
        return isBlock;
    }

    public void setIsBlock(boolean isBlock) {
        this.isBlock = isBlock;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
