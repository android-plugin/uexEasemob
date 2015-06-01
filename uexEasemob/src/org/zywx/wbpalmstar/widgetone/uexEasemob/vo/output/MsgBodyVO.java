package org.zywx.wbpalmstar.widgetone.uexEasemob.vo.output;

import java.io.Serializable;

/**
 * Created by yanlongtao on 2015/4/20.
 */
public class MsgBodyVO implements Serializable {

    private static final long serialVersionUID = 7695568683425626421L;

    private String text;
    private String action;
    private String longitute;
    private String latitude;
    private String address;
    private String displayName;
    private String remotePath;
    private String secretKey;
    private String thumbnailRemotePath;
    private String length;
    private String thumbnailSecretKey;
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getLongitute() {
        return longitute;
    }

    public void setLongitute(String longitute) {
        this.longitute = longitute;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getRemotePath() {
        return remotePath;
    }

    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getThumbnailRemotePath() {
        return thumbnailRemotePath;
    }

    public void setThumbnailRemotePath(String thumbnailRemotePath) {
        this.thumbnailRemotePath = thumbnailRemotePath;
    }

    public String getThumbnailSecretKey() {
        return thumbnailSecretKey;
    }

    public void setThumbnailSecretKey(String thumbnailSecretKey) {
        this.thumbnailSecretKey = thumbnailSecretKey;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }
}
