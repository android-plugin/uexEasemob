package org.zywx.wbpalmstar.widgetone.uexEasemob.vo.input;

import java.io.Serializable;


/**
 * Created by ylt on 15/3/17.
 */
public class NotifySettingVO implements Serializable {
    private static final long serialVersionUID = -5211040927801119094L;
    private String enable;
    private String soundEnable;
    private String vibrateEnable;
    private String userSpeaker;
    private String showNotificationInBackgroud;
    private String acceptInvitationAlways;

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public String getSoundEnable() {
        return soundEnable;
    }

    public void setSoundEnable(String soundEnable) {
        this.soundEnable = soundEnable;
    }

    public String getVibrateEnable() {
        return vibrateEnable;
    }

    public void setVibrateEnable(String vibrateEnable) {
        this.vibrateEnable = vibrateEnable;
    }

    public String getUserSpeaker() {
        return userSpeaker;
    }

    public void setUserSpeaker(String userSpeaker) {
        this.userSpeaker = userSpeaker;
    }

    public String getShowNotificationInBackgroud() {
        return showNotificationInBackgroud;
    }

    public void setShowNotificationInBackgroud(String showNotificationInBackgroud) {
        this.showNotificationInBackgroud = showNotificationInBackgroud;
    }

    public String getAcceptInvitationAlways() {
        return acceptInvitationAlways;
    }

    public void setAcceptInvitationAlways(String acceptInvitationAlways) {
        this.acceptInvitationAlways = acceptInvitationAlways;
    }
}
