package org.zywx.wbpalmstar.widgetone.uexEasemob.vo.input;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ylt on 15/3/17.
 */
public class GroupInfoVO implements Serializable {

    private static final long serialVersionUID = 1168638319725750432L;

    private String isGroupOwner;//是否群主
    private String groupId;
    private String[] newmembers;
    private String reason;
    private String loadCache;
    private String username;
    private String changedGroupName;

    private List<String> groupIds;
    public String getIsGroupOwner() {
        return isGroupOwner;
    }

    public void setIsGroupOwner(String isGroupOwner) {
        this.isGroupOwner = isGroupOwner;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String[] getNewmembers() {
        return newmembers;
    }

    public void setNewmembers(String[] newmembers) {
        this.newmembers = newmembers;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getLoadCache() {
        return loadCache;
    }

    public void setLoadCache(String loadCache) {
        this.loadCache = loadCache;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getChangedGroupName() {
        return changedGroupName;
    }

    public void setChangedGroupName(String changedGroupName) {
        this.changedGroupName = changedGroupName;
    }

    public List<String> getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(List<String> groupIds) {
        this.groupIds = groupIds;
    }
}
