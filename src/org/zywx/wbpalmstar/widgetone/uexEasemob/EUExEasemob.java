package org.zywx.wbpalmstar.widgetone.uexEasemob;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupInfo;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMLocationMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMNormalFileMessageBody;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chat.EMVideoMessageBody;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.hyphenate.exceptions.EMNoActiveCallException;
import com.hyphenate.exceptions.EMServiceNotReadyException;
import com.hyphenate.exceptions.HyphenateException;

import org.json.JSONException;
import org.json.JSONObject;
import org.zywx.wbpalmstar.base.BDebug;
import org.zywx.wbpalmstar.base.BUtility;
import org.zywx.wbpalmstar.base.cache.DiskCache;
import org.zywx.wbpalmstar.engine.DataHelper;
import org.zywx.wbpalmstar.engine.EBrowserView;
import org.zywx.wbpalmstar.engine.universalex.EUExBase;
import org.zywx.wbpalmstar.engine.universalex.EUExUtil;
import org.zywx.wbpalmstar.widgetone.uexEasemob.utils.CommonUtil;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.input.AddContactInputVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.input.CmdMsgInputVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.input.CreateGroupInputVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.input.GroupInfoVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.input.HistoryInputVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.input.ImportMsgInputVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.input.InitVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.input.NicknameVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.input.NotifySettingVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.input.PageVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.input.SendInputVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.input.UserInputVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.output.CallReceiveOutputVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.output.CallStateOutputVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.output.ChatterInfoVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.output.CmdMsgOutputVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.output.ConversationResultVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.output.GroupCreateResultVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.output.GroupInfosOutputVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.output.GroupOptVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.output.GroupResultVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.output.GroupsOutputVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.output.MessageHistoryVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.output.MessageVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.output.MsgResultVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.output.ResultVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.output.SendMsgResultVO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ylt on 15/3/13.
 */
public class EUExEasemob extends EUExBase implements ListenersRegister.ListenersCallback{
    private static final String TAG= "EUExEasemob";

    private static final String BUNDLE_DATA = "data";

    private static final int MSG_LOGIN = 1;
    private static final int MSG_LOGOUT = 2;
    private static final int MSG_REGISTER_User = 3;
    private static final int MSG_GETMESSAGEBYID=4;
    private static final int MSG_GET_CONVERSATION_BY_NAME=5;
    private static final int MSG_SEND_TEXT=6;
    private static final int MSG_SEND_VOICE=7;
    private static final int MSG_SEND_PICTURE=8;
    private static final int MSG_SEND_LOCATION=9;
    private static final int MSG_SEND_FILE=10;
    private static final int MSG_GET_MESSAGE_HISTORY=11;
    private static final int MSG_GET_UNREAD_MSG_COUNT=12;
    private static final int MSG_RESET_UNREAD_MSG_COUNT=13;
    private static final int MSG_RESET_ALL_UNREAD_MSG_COUNT=14;
    private static final int MSG_GET_MSG_COUNT=15;
    private static final int MSG_CLEAR_CONVERSATION=16;
    private static final int MSG_DELETE_CONVERSATION=17;
    private static final int MSG_REMOVE_MESSAGE=18;
    private static final int MSG_DELETE_ALL_CONVERSATION=19;
    private static final int MSG_SET_NOTIFY_BY_SOUND_AND_VIBRATE=20;
    private static final int MSG_GET_CONTACT_USER_NAMES=21;
    private static final int MSG_ADD_CONTACT=22;
    private static final int MSG_DELETE_CONTACT=23;
    private static final int MSG_ACCEPT_INVITATION=24;
    private static final int MSG_REFUSE_INVITATION=25;
    private static final int MSG_GET_BLACKLIST_USERNAMES=26;
    private static final int MSG_ADD_USER_TO_BLACKLIST=27;
    private static final int MSG_DELETE_USER_FROM_BLACKLIST=28;

    private static final int MSG_CREATEPRIVATEGROUP=29;
    private static final int MSG_CREATEPUBLICGROUP=30;
    private static final int MSG_ADDUSERSTOGROUP=31;
    private static final int MSG_REMOVEUSERFROMGROUP=32;
    private static final int MSG_JOINGROUP=33;
    private static final int MSG_EXITFROMGROUP=34;
    private static final int MSG_EXITANDDELETEGROUP=35;
    private static final int MSG_GETGROUPSFROMSERVER=36;
    private static final int MSG_GETALLPUBLICGROUPSFROMSERVER=37;
    private static final int MSG_GETGROUP=38;
    private static final int MSG_BLOCKGROUPMESSAGE=39;
    private static final int MSG_UNBLOCKGROUPMESSAGE=40;
    private static final int MSG_CHANGEGROUPNAME=41;
    private static final int MSG_SETRECEIVENOTNOIFYGROUP=42;
    private static final int MSG_BLOCKUSER=43;
    private static final int MSG_UNBLOCKUSER=44;
    private static final int MSG_GETBLOCKEDUSERS=45;

    private static final int MSG_IMPORTMESSAGE=46;
    private static final int MSG_MAKEVOICECALL=49;
    private static final int MSG_ANSWERCALL=50;
    private static final int MSG_REJECTCALL=51;
    private static final int MSG_ENDCALL=52;
    private static final int MSG_SENDCMDMESSAGE=53;

    private static final int MSG_UPDATECURRENTUSERNICK=54;

    private static final int MSG_GET_CHATTER_INFO=55;

    private static final int MSG_INIT=57;
    private static final int MSG_SEND_VIDEO=58;
    private static final int MSG_SEND_HAS_READ_RESPONSE_FOR_MESSAGE=59;
    private static final int MSG_GET_TOTAL_UNREAD_MSG_COUNT=60;

    private static final int MSG_GET_RECENT_CHATTERS=61;

    private List<String> tempContacts=new ArrayList<String>();

    private boolean debug=false;//用于同步调试

    private boolean mHasInit =false;

    private static final String TEMP_PATH = "temp";

    private boolean miPush=false;

    private static List<EBrowserView> callbackBrowserViews;//需要回调的EBrowserView

    ExecutorService mExecutorService;

    public EUExEasemob(Context context, EBrowserView eBrowserView) {
        super(context, eBrowserView);
        if ("root".equals(eBrowserView.getWindowName())){
            registerCallback(null);
        }
    }

    @Override
    protected boolean clean() {
        if (mExecutorService!=null){
            mExecutorService.shutdownNow();
            mExecutorService=null;
        }
        return false;
    }

    public void registerCallback(String[] params){
        if (callbackBrowserViews==null){
            callbackBrowserViews=new ArrayList<EBrowserView>();
        }
        if (!callbackBrowserViews.contains(mBrwView)){
            callbackBrowserViews.add(mBrwView);
        }
    }

    public void unRegisterCallback(String[] params){
        if (callbackBrowserViews!=null&&callbackBrowserViews.contains(mBrwView)){
            callbackBrowserViews.remove(mBrwView);
        }
        if (params!=null&&params.length>0&&"-1".equals(params[0])){
            callbackBrowserViews.clear();
            callbackBrowserViews=null;
        }
    }

    public void initEasemob(String[] param){
        initExecutorService();
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_INIT;
        Bundle bd = new Bundle();
        bd.putStringArray(BUNDLE_DATA, param);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    public void initEasemobMsg(String[] param){
        String funcId = null;
        if(null != param && param.length > 1) {
            funcId = param[1];
        }
        if (mHasInit){
            if (null != funcId) {
                callbackToJs(Integer.parseInt(funcId), false, "EaseMobSDK has already been initialized!");
            }
            return;
        }
        mHasInit =true;
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果app启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的app会在以包名为默认的process name下运行，如果查到的process name不是app的process name就立即返回

        if (processAppName == null ||!processAppName.equalsIgnoreCase(mContext.getPackageName())) {
            //"com.easemob.chatuidemo"为demo的包名，换到自己项目中要改成自己包名
            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }
        EMOptions options = new EMOptions();
        InitVO initVO= DataHelper.gson.fromJson(param[0],InitVO.class);
        String isAutoLogin = initVO.isAutoLoginEnabled;
        if ("1".equals(isAutoLogin)) {
            options.setAutoLogin(true);
        } else if ("2".equals(isAutoLogin)) {
            options.setAutoLogin(false);// 自动同意加群
        }
        if ("1".equals(initVO.debug)) {
            debug = true;
        }
        String isAutoAcceptGroupInvitation = initVO.isAutoAcceptGroupInvitation;
        if ("2".equals(isAutoAcceptGroupInvitation)) {
            options.setAutoAcceptGroupInvitation(false);
        } else {
            options.setAutoAcceptGroupInvitation(true);
        }
        String appKey = initVO.appKey;
        options.setAppKey(appKey);
        if (!TextUtils.isEmpty(initVO.miPushAppId)) {
            options.setMipushConfig(initVO.miPushAppId, initVO.miPushAppKey);
            miPush = true;
        }

        ListenersRegister register=new ListenersRegister();
        register.registerListeners(mContext.getApplicationContext(), options);
        register.setCallback(this);
        if (null != funcId) {
            callbackToJs(Integer.parseInt(funcId), false, "EaseMobSDK initialized successfully!");
        }
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) mContext.getSystemService(mContext.ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = mContext.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
                    // Log.d("Process", "Id: "+ info.pid +" ProcessName: "+
                    // info.processName +"  Label: "+c.toString());
                    // processName = c.toString();
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    public void login(final String[] params){
        initExecutorService();
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                loginOnThread(params);
            }
        });
    }

    private void loginOnThread(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        String funcId = null;
        if (params.length == 2) {
            funcId = params[1];
        }
        UserInputVO inputVO=DataHelper.gson.fromJson(params[0], new TypeToken<UserInputVO>() {
        }.getType());
        if (TextUtils.isEmpty(inputVO.getUsername())||TextUtils.isEmpty(inputVO.getPassword())){
            errorCallback(0, 0, "error params!");
            return;
        }
        final String callbackId = funcId;
        EMClient.getInstance().login(inputVO.getUsername(), inputVO.getPassword(), new EMCallBack() {
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                ResultVO resultVO = new ResultVO();
                resultVO.setResult(1);
                resultVO.setMsg("");
                if(null != callbackId) {
                    callbackToJs(Integer.parseInt(callbackId), false, DataHelper.gson.toJsonTree(resultVO));
                    return;
                }
                String js = SCRIPT_HEADER + "if(" + JSConst.CALLBACK_LOGIN + "){"
                        + JSConst.CALLBACK_LOGIN + "('" + DataHelper.gson.toJson(resultVO) + "');}";
                evaluateRootWindowScript(js);
            }

            @Override
            public void onError(int i, String s) {
                ResultVO resultVO = new ResultVO();
                resultVO.setResult(2);
                resultVO.setMsg(s);
                if(null != callbackId) {
                    callbackToJs(Integer.parseInt(callbackId), false, DataHelper.gson.toJsonTree(resultVO));
                    return;
                }
                String js = SCRIPT_HEADER + "if(" + JSConst.CALLBACK_LOGIN + "){"
                        + JSConst.CALLBACK_LOGIN + "('" + DataHelper.gson.toJson(resultVO) + "');}";
                evaluateRootWindowScript(js);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    public void logout(String[] params){
        int result = 0;

        if (!miPush) {
            result=EMClient.getInstance().logout(false);
        } else {
            EMClient.getInstance().logout(true, null);
        }
        Log.i(TAG, "logout result:" + result);
        if (null != params && params.length == 1) {
            String funcId = params[0];
            try {
                JSONObject jsonObject = new JSONObject();
                if (result == 0) { //退出成功
                    jsonObject.put("result", 1);
                    jsonObject.put("msg", "logout success");
                    callbackToJs(Integer.parseInt(funcId), false, jsonObject);
                } else {
                    jsonObject.put("result", 2);
                    jsonObject.put("msg", "logout fail");
                    callbackToJs(Integer.parseInt(funcId), false, jsonObject);
                }
            } catch (JSONException e ) {

            }
        }

    }


    public void registerUser(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        final UserInputVO inputVO=DataHelper.gson.fromJson(params[0],new TypeToken<UserInputVO>(){}.getType());
        if (TextUtils.isEmpty(inputVO.getUsername())||TextUtils.isEmpty(inputVO.getPassword())){
            errorCallback(0, 0, "error params!");
            return;
        }
        String funcId = null;
        if (params.length == 2) {
            funcId = params[1];
        }
        final String callbackId = funcId;
        new Thread(new Runnable() {
            public void run() {
                ResultVO resultVO=new ResultVO();
                try {
                    // 调用sdk注册方法
                    EMClient.getInstance().createAccount(inputVO.getUsername(), inputVO.getPassword());
                    resultVO.setResult(1);
                } catch (final HyphenateException e) {
                    //注册失败
                    resultVO.setResult(2);
                    int errorCode = e.getErrorCode();
                    if (errorCode == EMError.NETWORK_ERROR) {
                        resultVO.setMsg("网络异常，请检查网络！");
                    } else if (errorCode == EMError.USER_ALREADY_EXIST) {
                        resultVO.setMsg("用户已存在！");
                    } else if (errorCode == EMError.USER_AUTHENTICATION_FAILED) {
                        resultVO.setMsg("注册失败，无权限！");
                    } else {
                        resultVO.setMsg("注册失败:" + e.getMessage());
                    }
                    if (BDebug.DEBUG) {
                        e.printStackTrace();
                    }
                }finally {
                    if (null != callbackId) {
                        callbackToJs(Integer.parseInt(callbackId), false, DataHelper.gson.toJsonTree(resultVO));
                    } else {
                        String js = SCRIPT_HEADER + "if(" + JSConst.CALLBACK_REGISTER + "){"
                                + JSConst.CALLBACK_REGISTER + "('" + DataHelper.gson.toJson(resultVO) + "');}";
                        evaluateRootWindowScript(js);
                    }
                }
            }
        }).start();
    }

    public void onNewMessage(String result){
        String js = SCRIPT_HEADER + "if(" + JSConst.ON_NEW_MESSAGE + "){"
                + JSConst.ON_NEW_MESSAGE + "('" + result + "');}";
        evaluateRootWindowScript(js);
    }

    public void onAckMessage(MessageVO messageVO){
        String js = SCRIPT_HEADER + "if(" + JSConst.ON_ACK_MESSAGE + "){"
                + JSConst.ON_ACK_MESSAGE + "('" + DataHelper.gson.toJson(messageVO) + "');}";
        evaluateRootWindowScript(js);
    }

    public void onDeliveryMessage(MessageVO messageVO){
        String js = SCRIPT_HEADER + "if(" + JSConst.ON_DELIVERY_MESSAGE + "){"
                + JSConst.ON_DELIVERY_MESSAGE + "('" + DataHelper.gson.toJson(messageVO) + "');}";
        evaluateRootWindowScript(js);
    }

    @Override
    public void onContactAdded(String username) {
        if (username!=null){
            if (!tempContacts.contains(username)) {
                tempContacts.add(username);
            }
        }
        List<String> usernameList = new ArrayList<String>();
        usernameList.add(username);
        String js = SCRIPT_HEADER + "if(" + JSConst.ON_CONTACT_ADDED + "){"
                + JSConst.ON_CONTACT_ADDED + "('" + DataHelper.gson.toJson(usernameList) + "');}";
        evaluateRootWindowScript(js);
    }

    @Override
    public void onContactDeleted(String username) {
        if (username != null){
            tempContacts.remove(username);
        }
        List<String> usernameList = new ArrayList<String>();
        usernameList.add(username);
        String js = SCRIPT_HEADER + "if(" + JSConst.ON_CONTACT_DELETED + "){"
                + JSConst.ON_CONTACT_DELETED + "('" + DataHelper.gson.toJson(usernameList) + "');}";
        evaluateRootWindowScript(js);
    }

    @Override
    public void onContactInvited(GroupOptVO optVO) {
        String js = SCRIPT_HEADER + "if(" + JSConst.ON_CONTACT_INVITED + "){"
                + JSConst.ON_CONTACT_INVITED + "('" + DataHelper.gson.toJson(optVO) + "');}";
        evaluateRootWindowScript(js);
    }

    @Override
    public void onContactAgreed(GroupOptVO optVO) {
        String js = SCRIPT_HEADER + "if(" + JSConst.ON_CONTACT_AGREED + "){"
                + JSConst.ON_CONTACT_AGREED + "('" + DataHelper.gson.toJson(optVO) + "');}";
        evaluateRootWindowScript(js);
    }

    @Override
    public void onContactRefused(GroupOptVO optVO) {
        String js = SCRIPT_HEADER + "if(" + JSConst.ON_CONTACT_REFUSED + "){"
                + JSConst.ON_CONTACT_REFUSED + "('" + DataHelper.gson.toJson(optVO) + "');}";
        evaluateRootWindowScript(js);
    }

    @Override
    public void onConnected() {
        String js = SCRIPT_HEADER + "if(" + JSConst.ON_CONNECTED + "){"
                + JSConst.ON_CONNECTED + "();}";
        evaluateRootWindowScript(js);
    }

    @Override
    public void onDisconnected(int error) {
        JSONObject jsonData=new JSONObject();
        try {
            jsonData.put("error",error);
        } catch (JSONException e) {

        }
        String js = SCRIPT_HEADER + "if(" + JSConst.ON_DISCONNECTED + "){"
                + JSConst.ON_DISCONNECTED + "('" + jsonData.toString() + "');}";
        evaluateRootWindowScript(js);
    }

    @Override
    public void onInvitationAccpted(GroupOptVO groupOptVO) {
        String js = SCRIPT_HEADER + "if(" + JSConst.ON_INVITATION_ACCPTED + "){"
                + JSConst.ON_INVITATION_ACCPTED + "('" + DataHelper.gson.toJson(groupOptVO) + "');}";
        evaluateRootWindowScript(js);
    }

    @Override
    public void onInvitationDeclined(GroupOptVO groupOptVO) {
        String js = SCRIPT_HEADER + "if(" + JSConst.ON_INVITATION_DECLINED + "){"
                + JSConst.ON_INVITATION_DECLINED + "('" + DataHelper.gson.toJson(groupOptVO) + "');}";
        evaluateRootWindowScript(js);
    }

    @Override
    public void onUserRemoved(GroupOptVO groupOptVO) {
        String js = SCRIPT_HEADER + "if(" + JSConst.ON_USER_REMOVED + "){"
                + JSConst.ON_USER_REMOVED + "('" + DataHelper.gson.toJson(groupOptVO) + "');}";
        evaluateRootWindowScript(js);
    }

    @Override
    public void onGroupDestroy(GroupOptVO groupOptVO) {
        String js = SCRIPT_HEADER + "if(" + JSConst.ON_GROUP_DESTROY + "){"
                + JSConst.ON_GROUP_DESTROY + "('" + DataHelper.gson.toJson(groupOptVO) + "');}";
        evaluateRootWindowScript(js);
    }

    @Override
    public void onApplicationReceived(GroupOptVO groupOptVO) {
        String js = SCRIPT_HEADER + "if(" + JSConst.ON_APPLICATION_RECEIVED + "){"
                + JSConst.ON_APPLICATION_RECEIVED + "('" + DataHelper.gson.toJson(groupOptVO) + "');}";
        evaluateRootWindowScript(js);
    }

    @Override
    public void onApplicationAccept(GroupOptVO groupOptVO) {
        String js = SCRIPT_HEADER + "if(" + JSConst.ON_APPLICATION_ACCEPT + "){"
                + JSConst.ON_APPLICATION_ACCEPT + "('" + DataHelper.gson.toJson(groupOptVO) + "');}";
        evaluateRootWindowScript(js);
    }

    @Override
    public void onInvitationReceived(GroupOptVO groupOptVO) {
        String js = SCRIPT_HEADER + "if(" + JSConst.ON_INVITATION_RECEIVED + "){"
                + JSConst.ON_INVITATION_RECEIVED + "('" + DataHelper.gson.toJson(groupOptVO) + "');}";
        evaluateRootWindowScript(js);
    }

    @Override
    public void onApplicationDeclined(GroupOptVO groupOptVO) {
        String js = SCRIPT_HEADER + "if(" + JSConst.ON_APPLICATION_DECLINED + "){"
                + JSConst.ON_APPLICATION_DECLINED + "('" + DataHelper.gson.toJson(groupOptVO) + "');}";
        evaluateRootWindowScript(js);
    }

    @Override
    public void onCallReceive(CallReceiveOutputVO outputVO) {
        String js = SCRIPT_HEADER + "if(" + JSConst.ON_CALLRECEIVE + "){"
                + JSConst.ON_CALLRECEIVE + "('" + DataHelper.gson.toJson(outputVO) + "');}";
        evaluateRootWindowScript(js);
    }

    @Override
    public void onCallStateChanged(CallStateOutputVO outputVO) {
        String js = SCRIPT_HEADER + "if(" + JSConst.ON_CALLSTATECHANGED + "){"
                + JSConst.ON_CALLSTATECHANGED + "('" + DataHelper.gson.toJson(outputVO) + "');}";
        evaluateRootWindowScript(js);
    }

    @Override
    public void onCmdMessageReceive(CmdMsgOutputVO outputVO) {
        String js = SCRIPT_HEADER + "if(" + JSConst.ON_CMDMESSAGERECEIVE + "){"
                + JSConst.ON_CMDMESSAGERECEIVE + "('" + DataHelper.gson.toJson(outputVO) + "');}";
        evaluateRootWindowScript(js);
    }

    @Override
    public void onDidJoinedGroup(Map<String, String> data) {
        String js = SCRIPT_HEADER + "if(" + JSConst.ON_CMDMESSAGERECEIVE + "){"
                + JSConst.ON_DID_JOINED_GROUP + "('" + DataHelper.gson.toJson(data) + "');}";
        evaluateRootWindowScript(js);
    }

    public void getMessageById(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        MessageVO inputVO=DataHelper.gson.fromJson(params[0],new TypeToken<MessageVO>(){}.getType());
        if (TextUtils.isEmpty(inputVO.getMsgId())){
            errorCallback(0, 0, "error params!");
            return;
        }
        String funcId = null;
        if (params.length == 2) {
            funcId = params[1];
        }
        EMMessage message=EMClient.getInstance().chatManager().getMessage(inputVO.getMsgId());
        if (null != funcId) {
            callbackToJs(Integer.parseInt(funcId), false, DataHelper.gson.toJsonTree(ListenersRegister.convertEMMessage(message)));
        } else {
            String js = SCRIPT_HEADER + "if(" + JSConst.CALLBACK_GETMESSAGEBYID + "){"
                    + JSConst.CALLBACK_GETMESSAGEBYID + "('" + DataHelper.gson.toJson(ListenersRegister.convertEMMessage(message)) + "');}";
            evaluateRootWindowScript(js);
        }
    }

    public void getConversationByName(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        MessageVO inputVO=DataHelper.gson.fromJson(params[0], new TypeToken<MessageVO>() {
        }.getType());
        if (TextUtils.isEmpty(inputVO.getUsername())){
            errorCallback(0, 0, "error params!");
            return;
        }
        String funcId = null;
        if (params.length == 2) {
            funcId = params[1];
        }
        EMConversation conversation= EMClient.getInstance().chatManager().getConversation(inputVO.getUsername());
        ConversationResultVO resultVO= new ConversationResultVO();
        if (conversation != null) {
            resultVO.setIsGroup(conversation.isGroup() ? "1" : "0");
            resultVO.setChatType(getChatTypeValue(conversation.getType()));
            resultVO.setChatType(conversation.getType().toString());
            resultVO.setChatter(inputVO.getUsername());
            List<EMMessage> emMessages=conversation.getAllMessages();
            List<MsgResultVO> msgResultVOs=new ArrayList<MsgResultVO>();
            if (emMessages!=null){
                for (EMMessage emMessage : emMessages) {
                    msgResultVOs.add(ListenersRegister.convertEMMessage(emMessage));
                }
            }
            resultVO.setMessages(msgResultVOs);
        }
        if(null != funcId) {
            callbackToJs(Integer.parseInt(funcId), false, DataHelper.gson.toJsonTree(resultVO));
        } else {
            String js = SCRIPT_HEADER + "if(" + JSConst.CALLBACK_GETCONVERSATIONBYNAME + "){"
                    + JSConst.CALLBACK_GETCONVERSATIONBYNAME + "('" + DataHelper.gson.toJson(resultVO) + "');}";
            evaluateRootWindowScript(js);
        }
    }

    public static String getChatTypeValue(EMConversation.EMConversationType type){
        String chatType=null;
        if (type== EMConversation.EMConversationType.GroupChat){
            chatType="1";
        }else if (type== EMConversation.EMConversationType.Chat){
            chatType="0";
        }else if (type== EMConversation.EMConversationType.ChatRoom){
            chatType="2";
        }
        return chatType;
    }

    public static String getChatTypeValue(EMMessage.ChatType type){
        String chatType=null;
        if (type== EMMessage.ChatType.GroupChat){
            chatType="1";
        }else if (type== EMMessage.ChatType.Chat){
            chatType="0";
        }else if (type== EMMessage.ChatType.ChatRoom){
            chatType="2";
        }
        return chatType;
    }


    public void sendText(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        SendInputVO inputVO=DataHelper.gson.fromJson(params[0], new TypeToken<SendInputVO>() {
        }.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(params[0]);
            inputVO.setExtObj(jsonObject.optJSONObject("extObj"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_SEND_TEXT;
        Bundle bd = new Bundle();
        bd.putSerializable(BUNDLE_DATA, inputVO);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void sendTextMsg(SendInputVO sendInputVO) {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(sendInputVO.getUsername());
        //创建一条文本消息
        final EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
        //如果是群聊，设置chattype,默认是单聊
        if (String.valueOf(1).equals(sendInputVO.getChatType())) {
            message.setChatType(EMMessage.ChatType.GroupChat);
        }
        //设置消息body
        EMTextMessageBody txtBody = new EMTextMessageBody(sendInputVO.getContent());
        message.addBody(txtBody);
        if (sendInputVO.getExtObj() != null) {
            message.setAttribute("extObj", sendInputVO.getExtObj());
        } else if (!TextUtils.isEmpty(sendInputVO.getExt())) {
            message.setAttribute("ext", sendInputVO.getExt());
        }
        //设置接收人
        message.setTo(sendInputVO.getUsername());
        //把消息加入到此会话对象中
        //发送消息
        message.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onError(int arg0, String arg1) {
                callbackSendMsgResult(false, arg1, message);
            }

            @Override
            public void onProgress(int arg0, String arg1) {

            }

            @Override
            public void onSuccess() {
                callbackSendMsgResult(true, null, message);
            }
        });
        EMClient.getInstance().chatManager().sendMessage(message);
    }

    public void sendVoice(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        SendInputVO inputVO=DataHelper.gson.fromJson(params[0], new TypeToken<SendInputVO>() {
        }.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(params[0]);
            inputVO.setExtObj(jsonObject.optJSONObject("extObj"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_SEND_VOICE;
        Bundle bd = new Bundle();
        bd.putSerializable(BUNDLE_DATA, inputVO);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void sendVoiceMsg(SendInputVO sendInputVO) {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(sendInputVO.getUsername());
        //创建一条文本消息
        final EMMessage message = EMMessage.createSendMessage(EMMessage.Type.VOICE);
        //如果是群聊，设置chattype,默认是单聊
        if (String.valueOf(1).equals(sendInputVO.getChatType())) {
            message.setChatType(EMMessage.ChatType.GroupChat);
        }
        //设置消息body
        EMVoiceMessageBody body = new EMVoiceMessageBody(new File(getRealPath(sendInputVO.getFilePath())),
                sendInputVO.length);
        message.addBody(body);
        if (sendInputVO.getExtObj() != null) {
            message.setAttribute("extObj", sendInputVO.getExtObj());
        } else if (!TextUtils.isEmpty(sendInputVO.getExt())) {
            message.setAttribute("ext", sendInputVO.getExt());
        }
        message.setTo(sendInputVO.getUsername());
        message.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                callbackSendMsgResult(true, null, message);
            }

            @Override
            public void onError(int i, String s) {
                callbackSendMsgResult(false, s, message);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
        EMClient.getInstance().chatManager().sendMessage(message);
    }

    public String getRealPath(String path){
        String realPath=BUtility.makeRealPath(
                BUtility.makeUrl(mBrwView.getCurrentUrl(), path),
                mBrwView.getCurrentWidget().m_widgetPath,
                mBrwView.getCurrentWidget().m_wgtType);

        String fileName = path.substring(path.lastIndexOf("/") + 1, path.length());
        Log.i(TAG, "getRealPath:" + fileName);

        //先将assets文件写入到临时文件夹中
        if (path.startsWith(BUtility.F_Widget_RES_SCHEMA)) {
            //为res对应的文件生成一个临时文件到系统中
            File dir = new File(Environment.getExternalStorageDirectory(),
                    File.separator + TEMP_PATH);
            if (!dir.exists()) {
                dir.mkdirs();
            } else {
                //及时清理这个缓存文件夹中的数据
                for (File file: dir.listFiles()) {
                    file.delete();
                }
            }
            File destFile = new File(dir, fileName);
            try {
                destFile.deleteOnExit();
                destFile.createNewFile();
            } catch (IOException e) {
                Log.i(TAG, "[Create File]" +  e.getMessage());
                return null;
            }
            if (realPath.startsWith("/data")){
                CommonUtil.copyFile(new File(realPath), destFile);
                return destFile.getAbsolutePath();
            }else if( CommonUtil.saveFileFromAssetsToSystem(mContext, realPath, destFile)) {
                return destFile.getAbsolutePath();
            } else {
                Log.i(TAG, "[getRealPath error]");
                return null;
            }
        } else {
            return realPath;
        }
    }

    public void sendPicture(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        SendInputVO inputVO=DataHelper.gson.fromJson(params[0], new TypeToken<SendInputVO>() {
        }.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(params[0]);
            inputVO.setExtObj(jsonObject.optJSONObject("extObj"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_SEND_PICTURE;
        Bundle bd = new Bundle();
        bd.putSerializable(BUNDLE_DATA, inputVO);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void sendPictureMsg(SendInputVO sendInputVO) {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(sendInputVO.getUsername());
        final EMMessage message = EMMessage.createSendMessage(EMMessage.Type.IMAGE);
        //如果是群聊，设置chattype,默认是单聊
        if (String.valueOf(1).equals(sendInputVO.getChatType())) {
            message.setChatType(EMMessage.ChatType.GroupChat);
        }
        EMImageMessageBody body = new EMImageMessageBody(new File(getRealPath(sendInputVO.getFilePath())));
        // 默认超过100k的图片会压缩后发给对方，可以设置成发送原图
        // body.setSendOriginalImage(true);
        message.addBody(body);
        if (sendInputVO.getExtObj() != null) {
            message.setAttribute("extObj", sendInputVO.getExtObj());
        } else if (!TextUtils.isEmpty(sendInputVO.getExt())) {
            message.setAttribute("ext", sendInputVO.getExt());
        }
        message.setTo(sendInputVO.getUsername());
        message.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                callbackSendMsgResult(true, null, message);
            }

            @Override
            public void onError(int i, String s) {
                callbackSendMsgResult(false, s, message);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
        EMClient.getInstance().chatManager().sendMessage(message);
    }

    public void sendLocationMsg(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        SendInputVO inputVO=DataHelper.gson.fromJson(params[0], new TypeToken<SendInputVO>() {
        }.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(params[0]);
            inputVO.setExtObj(jsonObject.optJSONObject("extObj"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_SEND_LOCATION;
        Bundle bd = new Bundle();
        bd.putSerializable(BUNDLE_DATA, inputVO);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void sendLocationMsgResult(SendInputVO sendInputVO) {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(sendInputVO.getUsername());
        final EMMessage message = EMMessage.createSendMessage(EMMessage.Type.LOCATION);
        //如果是群聊，设置chattype,默认是单聊
        if (String.valueOf(1).equals(sendInputVO.getChatType())) {
            message.setChatType(EMMessage.ChatType.GroupChat);
        }
        EMLocationMessageBody locBody = new EMLocationMessageBody(sendInputVO.getLocationAddress(), Double.valueOf(sendInputVO.getLatitude()), Double.valueOf(sendInputVO.getLongitude()));
        message.addBody(locBody);
        if (sendInputVO.getExtObj() != null) {
            message.setAttribute("extObj", sendInputVO.getExtObj());
        } else if (!TextUtils.isEmpty(sendInputVO.getExt())) {
            message.setAttribute("ext", sendInputVO.getExt());
        }
        message.setTo(sendInputVO.getUsername());
        message.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                callbackSendMsgResult(true, null, message);
            }

            @Override
            public void onError(int i, String s) {
                callbackSendMsgResult(false, s, message);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
        EMClient.getInstance().chatManager().sendMessage(message);
    }

    public void sendFile(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        SendInputVO inputVO=DataHelper.gson.fromJson(params[0], new TypeToken<SendInputVO>() {
        }.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(params[0]);
            inputVO.setExtObj(jsonObject.optJSONObject("extObj"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_SEND_FILE;
        Bundle bd = new Bundle();
        bd.putSerializable(BUNDLE_DATA, inputVO);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void sendFileMsg(SendInputVO sendInputVO) {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(sendInputVO.getUsername());
        // 创建一个文件消息
        final EMMessage message = EMMessage.createSendMessage(EMMessage.Type.FILE);
        // 如果是群聊，设置chattype,默认是单聊
        if (String.valueOf(1).equals(sendInputVO.getChatType())) {
            message.setChatType(EMMessage.ChatType.GroupChat);
        }
        //设置接收人的username
        message.setTo(sendInputVO.getUsername());
        if (sendInputVO.getExtObj() != null) {
            message.setAttribute("extObj", sendInputVO.getExtObj());
        } else if (!TextUtils.isEmpty(sendInputVO.getExt())) {
            message.setAttribute("ext", sendInputVO.getExt());
        }
        // add message body
        EMNormalFileMessageBody body = new EMNormalFileMessageBody(new File(getRealPath(sendInputVO.getFilePath())));
        message.addBody(body);
        message.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                callbackSendMsgResult(true, null, message);
            }

            @Override
            public void onError(int i, String s) {
                callbackSendMsgResult(false, s, message);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
        EMClient.getInstance().chatManager().sendMessage(message);
    }

    public void sendVideo(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        SendInputVO inputVO=DataHelper.gson.fromJson(params[0], new TypeToken<SendInputVO>() {
        }.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(params[0]);
            inputVO.setExtObj(jsonObject.optJSONObject("extObj"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_SEND_VIDEO;
        Bundle bd = new Bundle();
        bd.putSerializable(BUNDLE_DATA, inputVO);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void sendVideoMsg(SendInputVO sendInputVO){
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(sendInputVO.getUsername());
        // 创建一个文件消息
        final EMMessage message = EMMessage.createSendMessage(EMMessage.Type.VIDEO);
        // 如果是群聊，设置chattype,默认是单聊
        if (String.valueOf(1).equals(sendInputVO.getChatType())) {
            message.setChatType(EMMessage.ChatType.GroupChat);
        }
        //设置接收人的username
        message.setTo(sendInputVO.getUsername());
        // add message body
        if (sendInputVO.getExtObj() != null) {
            message.setAttribute("extObj", (JSONObject)sendInputVO.getExtObj());
        } else if (!TextUtils.isEmpty(sendInputVO.getExt())) {
            message.setAttribute("ext", sendInputVO.getExt());
        }
        File videoFile = new File(getRealPath(sendInputVO.getFilePath()));
        EMVideoMessageBody body = new EMVideoMessageBody(videoFile.getAbsolutePath(), getThumbPath(sendInputVO
                .getFilePath()),Double.valueOf(sendInputVO.length).intValue(),videoFile.length());
        message.addBody(body);
        message.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                callbackSendMsgResult(true, null, message);
            }

            @Override
            public void onError(int i, String s) {
                callbackSendMsgResult(false, s, message);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
        EMClient.getInstance().chatManager().sendMessage(message);
    }

    private String getThumbPath(String videoPath){
        if (DiskCache.cacheFolder==null){
            DiskCache.initDiskCache(mContext.getApplicationContext());
        }
        File thumbFile=new File(DiskCache.cacheFolder,"thvideo"+System.currentTimeMillis());
        Bitmap bitmap = null;
        FileOutputStream fos = null;
        try {
            if (!thumbFile.getParentFile().exists()) {
                thumbFile.getParentFile().mkdirs();
            }
            bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, 3);
            if (bitmap == null) {
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), EUExUtil.getResDrawableID("plugin_easemob_app_panel_video_icon"));
            }
            fos = new FileOutputStream(thumbFile);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

        } catch (Exception e) {
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                }
                fos = null;
            }
            if (bitmap != null) {
                bitmap.recycle();
                bitmap = null;
            }

        }
        return thumbFile.getAbsolutePath();
    }


    public void getMessageHistory(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        HistoryInputVO inputVO=DataHelper.gson.fromJson(params[0], new TypeToken<HistoryInputVO>() {
        }.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
        }
        String funcId = null;
        if (params.length == 2) {
            funcId = params[1];
        }

        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(inputVO.getUsername());
        //获取此会话的所有消息
        List<EMMessage> messages = null;
        if (conversation != null) {
            if (inputVO.pagesize==0) {
                messages = conversation.getAllMessages();
            } else {
                if ("1".equals(inputVO.getChatType())) {
                    //是群聊
                    messages = conversation.loadMoreMsgFromDB(inputVO.getStartMsgId(), inputVO.pagesize);
                } else {
                    messages = conversation.loadMoreMsgFromDB(inputVO.getStartMsgId(), inputVO.pagesize);
                }
            }
        }
        List<MsgResultVO> resultVOs=new ArrayList<MsgResultVO>();
        if (messages!=null){
            for (EMMessage emMessage : messages) {
                resultVOs.add(ListenersRegister.convertEMMessage(emMessage));
            }
        }
        MessageHistoryVO historyVO=new MessageHistoryVO();
        historyVO.messages=resultVOs;
        if (null != funcId) {
            callbackToJs(Integer.parseInt(funcId),false, DataHelper.gson.toJsonTree(historyVO));
        } else {
            String js = SCRIPT_HEADER + "if(" + JSConst.CALLBACK_GET_MESSAGE_HISTORY + "){"
                    + JSConst.CALLBACK_GET_MESSAGE_HISTORY + "('" + DataHelper.gson.toJson(historyVO) + "');}";
            evaluateRootWindowScript(js);
        }
    }

    public void getUnreadMsgCount(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        HistoryInputVO inputVO=DataHelper.gson.fromJson(params[0], new TypeToken<HistoryInputVO>() {
        }.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
        }
        String funcId = null;
        if (params.length == 2) {
            funcId = params[1];
        }
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(inputVO.getUsername());
        int result = 0;
        if (conversation != null ) {
            result = conversation.getUnreadMsgCount();
        }
        JSONObject jsonResult=new JSONObject();
        try {
            jsonResult.put("count",result);
        } catch (JSONException e) {

        }
        if (null != funcId) {
            callbackToJs(Integer.parseInt(funcId), false, jsonResult);
        } else {
            String js = SCRIPT_HEADER + "if(" + JSConst.CALLBACK_GET_UNREAD_MSG_COUNT + "){"
                    + JSConst.CALLBACK_GET_UNREAD_MSG_COUNT + "('" + jsonResult.toString() + "');}";
            evaluateRootWindowScript(js);
        }

    }

    private void getUnreadMsgCountMsg(HistoryInputVO inputVO) {

    }

    public void resetUnreadMsgCount(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        HistoryInputVO inputVO=DataHelper.gson.fromJson(params[0], new TypeToken<HistoryInputVO>() {
        }.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_RESET_UNREAD_MSG_COUNT;
        Bundle bd = new Bundle();
        bd.putSerializable(BUNDLE_DATA,inputVO);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    /**
     * 未读消息数清零
     * @param inputVO
     */
    private void resetUnreadMsgCountMsg(HistoryInputVO inputVO) {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(inputVO.getUsername());
        if (conversation != null) {
            conversation.markAllMessagesAsRead();
        }
    }

    public void sendHasReadResponseForMessage(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        MessageVO inputVO=DataHelper.gson.fromJson(params[0], new TypeToken<MessageVO>() {
        }.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_SEND_HAS_READ_RESPONSE_FOR_MESSAGE;
        Bundle bd = new Bundle();
        bd.putSerializable(BUNDLE_DATA,inputVO);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void sendHasReadResponseForMessageMsg(final MessageVO messageVO){
        EMMessage emMessage = EMClient.getInstance().chatManager().getMessage(messageVO.getMsgId());
        if (emMessage == null) {
            return;
        }
        try {
            EMClient.getInstance().chatManager().ackMessageRead(emMessage.getFrom(), emMessage.getMsgId());
        } catch (HyphenateException e) {
            if (BDebug.DEBUG) {
                e.printStackTrace();
            }
        }
    }

    public void resetAllUnreadMsgCount(String[] params){
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_RESET_ALL_UNREAD_MSG_COUNT;
        mHandler.sendMessage(msg);
    }

    private void resetAllUnreadMsgCountMsg() {
        EMClient.getInstance().chatManager().markAllConversationsAsRead();
    }

    public void getMsgCount(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        HistoryInputVO inputVO=DataHelper.gson.fromJson(params[0],new TypeToken<HistoryInputVO>(){}.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
        }
        String funcId = null;
        if (params.length == 2) {
            funcId = params[1];
        }
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(inputVO.getUsername());
        int msgCount=conversation.getAllMessages().size();
        JSONObject jsonResult=new JSONObject();
        try {
            jsonResult.put("msgCount",msgCount);
        } catch (JSONException e) {

        }
        if (null != funcId) {
            callbackToJs(Integer.parseInt(funcId), false, jsonResult);
        } else {
            String js = SCRIPT_HEADER + "if(" + JSConst.CALLBACK_GET_MSG_COUNT + "){"
                    + JSConst.CALLBACK_GET_MSG_COUNT + "('" + jsonResult.toString() + "');}";
            evaluateRootWindowScript(js);
        }
    }

    public void clearConversation(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        HistoryInputVO inputVO=DataHelper.gson.fromJson(params[0],new TypeToken<HistoryInputVO>(){}.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_CLEAR_CONVERSATION;
        Bundle bd = new Bundle();
        bd.putSerializable(BUNDLE_DATA,inputVO);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void clearConversationMsg(HistoryInputVO inputVO) {
        //清空和某个user的聊天记录(包括本地)，不删除conversation这个会话对象
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(inputVO.getUsername());
        if (conversation != null) {
            conversation.clearAllMessages();//同时清除内存和数据库中的消息
        }
    }

    public void deleteConversation(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        HistoryInputVO inputVO=DataHelper.gson.fromJson(params[0],new TypeToken<HistoryInputVO>(){}.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_DELETE_CONVERSATION;
        Bundle bd = new Bundle();
        bd.putSerializable(BUNDLE_DATA,inputVO);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void deleteConversationMsg(HistoryInputVO inputVO) {
        //删除和某个user的整个的聊天记录(包括本地)
        EMClient.getInstance().chatManager().deleteConversation(inputVO.getUsername(), true);
    }

    public void removeMessage(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        HistoryInputVO inputVO=DataHelper.gson.fromJson(params[0],new TypeToken<HistoryInputVO>(){}.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_REMOVE_MESSAGE;
        Bundle bd = new Bundle();
        bd.putSerializable(BUNDLE_DATA,inputVO);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void removeMessageMsg(HistoryInputVO inputVO) {
        //删除当前会话的某条聊天记录
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(inputVO.getUsername());
        if (conversation!=null) {
            conversation.removeMessage(inputVO.getMsgId());
        }
    }

    public void deleteAllConversation(String[] params){
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_DELETE_ALL_CONVERSATION;
        mHandler.sendMessage(msg);
    }

    private void deleteAllConversationMsg() {
        //删除所有会话记录(包括本地)
        Map<String, EMConversation> map = EMClient.getInstance().chatManager().getAllConversations();
        for (String key : map.keySet()) {
            EMClient.getInstance().chatManager().deleteConversation(key, true);
        }
    }

    public void setNotifyBySoundAndVibrate(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        NotifySettingVO inputVO=DataHelper.gson.fromJson(params[0], new TypeToken<NotifySettingVO>() {
        }.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_SET_NOTIFY_BY_SOUND_AND_VIBRATE;
        Bundle bd = new Bundle();
        bd.putSerializable(BUNDLE_DATA, inputVO);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void setNotifyBySoundAndVibrateMsg(NotifySettingVO settingVO) {
        EMOptions chatOptions = EMClient.getInstance().getOptions();
        //setting里面字段不传时值为null
        if (!TextUtils.isEmpty(settingVO.getEnable())) {
            //默认为true 开启新消息提醒
            ListenersRegister.hxsdkHelper.hxModel.setSettingMsgNotification(!"0".equals(settingVO.getEnable()));
        }
        if (!TextUtils.isEmpty(settingVO.getSoundEnable())) {
            //默认为true 开启声音提醒
            ListenersRegister.hxsdkHelper.hxModel.setSettingMsgSound(!"0".equals(settingVO.getSoundEnable()));
        }
        if (!TextUtils.isEmpty(settingVO.getVibrateEnable())) {
            //默认为true 开启震动提醒
            ListenersRegister.hxsdkHelper.hxModel.setSettingMsgVibrate(!"0".equals(settingVO.getVibrateEnable()));

        }
        if (!TextUtils.isEmpty(settingVO.getUserSpeaker())) {
            ListenersRegister.hxsdkHelper.hxModel.setSettingMsgSpeaker(!"0".equals(settingVO.getUserSpeaker()));
            //默认为true 开启扬声器播放
        }
        if (!TextUtils.isEmpty(settingVO.getShowNotificationInBackgroud())) {
            // 已不支持, 环信SDK将其放入了UI框架中
            //chatOptions.setShowNotificationInBackgroud(!"0".equals(settingVO.getShowNotificationInBackgroud())); //默认为true
        }
        if (!TextUtils.isEmpty(settingVO.getAcceptInvitationAlways())) {
            chatOptions.setAcceptInvitationAlways(!"0".equals(settingVO.getAcceptInvitationAlways()));
        }
    }

    public void getContactUserNames(final String[] params){
        initExecutorService();
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                getContactUserNamesOnThread(params);
            }
        });
    }

    private void getContactUserNamesOnThread(String[] params){
        String funcId = null;
        if (null != params && params.length == 1) {
            funcId = params[0];
        }
        List<String> usernames = new ArrayList<String>();
        if (tempContacts!=null){
            usernames.addAll(tempContacts);
        }
        try {
            List<String> localNames= EMClient.getInstance().contactManager().getAllContactsFromServer();
            if (localNames!=null){
                for (String username:localNames) {
                    if (!usernames.contains(username)) {
                        usernames.add(username);
                    }
                }
            }
        } catch (HyphenateException e) {
            if (BDebug.DEBUG) {
                e.printStackTrace();
            }
        }
        if (null != funcId) {
            callbackToJs(Integer.parseInt(funcId), false, DataHelper.gson.toJsonTree(usernames));
        } else {
            String js = SCRIPT_HEADER + "if(" + JSConst.CALLBACK_GET_CONTACT_USERNAMES + "){"
                    + JSConst.CALLBACK_GET_CONTACT_USERNAMES + "('" + DataHelper.gson.toJson(usernames) + "');}";
            evaluateRootWindowScript(js);
        }
    }


    public void addContact(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        AddContactInputVO inputVO=DataHelper.gson.fromJson(params[0],new TypeToken<AddContactInputVO>(){}.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_ADD_CONTACT;
        Bundle bd = new Bundle();
        bd.putSerializable(BUNDLE_DATA,inputVO);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void addContactMsg(final AddContactInputVO inputVO) {

        //参数为要添加的好友的username和添加理由
        try {
            EMClient.getInstance().contactManager().addContact(inputVO.getToAddUsername(), inputVO.getReason());//需异步处理
        } catch (HyphenateException e) {
            if (BDebug.DEBUG) {
                e.printStackTrace();
            }
        }
    }

    public void deleteContact(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        UserInputVO inputVO=DataHelper.gson.fromJson(params[0],new TypeToken<UserInputVO>(){}.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_DELETE_CONTACT;
        Bundle bd = new Bundle();
        bd.putSerializable(BUNDLE_DATA,inputVO);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void deleteContactMsg(final UserInputVO inputVO) {
        try {
            EMClient.getInstance().contactManager().deleteContact(inputVO.getUsername());//需异步处理
        } catch (HyphenateException e) {
            if (BDebug.DEBUG) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 同意好友请求
     * @param params
     */
    public void acceptInvitation(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        UserInputVO inputVO=DataHelper.gson.fromJson(params[0],new TypeToken<UserInputVO>(){}.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_ACCEPT_INVITATION;
        Bundle bd = new Bundle();
        bd.putSerializable(BUNDLE_DATA,inputVO);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void acceptInvitationMsg(final UserInputVO inputVO) {
        try {
            EMClient.getInstance().contactManager().acceptInvitation(inputVO.getUsername());//需异步处理
        } catch (HyphenateException e) {
            if (BDebug.DEBUG) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 拒绝好友请求
     * @param params
     */
    public void refuseInvitation(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        UserInputVO inputVO=DataHelper.gson.fromJson(params[0],new TypeToken<UserInputVO>(){}.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_REFUSE_INVITATION;
        Bundle bd = new Bundle();
        bd.putSerializable(BUNDLE_DATA,inputVO);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void refuseInvitationMsg(final UserInputVO inputVO) {
        try {
            EMClient.getInstance().contactManager().declineInvitation(inputVO.getUsername());//需异步处理
        } catch (HyphenateException e) {
            if (BDebug.DEBUG) {
                e.printStackTrace();
            }
        }
    }

    public void getBlackListUsernames(String[] params){
        String funcId = null;
        if (params != null && params.length == 1) {
            funcId = params[0];
        }
        //获取黑名单用户的usernames
        List<String> usernames=EMClient.getInstance().contactManager().getBlackListUsernames();
        if (funcId != null) {
            callbackToJs(Integer.parseInt(funcId), false, DataHelper.gson.toJsonTree(usernames));
        } else {
            String js = SCRIPT_HEADER + "if(" + JSConst.CALLBACK_GET_BLACKLIST_USERNAMES + "){"
                    + JSConst.CALLBACK_GET_BLACKLIST_USERNAMES + "('" + DataHelper.gson.toJson(usernames) + "');}";
            evaluateRootWindowScript(js);
        }
    }

    public void addUserToBlackList(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        UserInputVO inputVO=DataHelper.gson.fromJson(params[0],new TypeToken<UserInputVO>(){}.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_ADD_USER_TO_BLACKLIST;
        Bundle bd = new Bundle();
        bd.putSerializable(BUNDLE_DATA,inputVO);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void addUserToBlackListMsg(UserInputVO inputVO) {
        //第二个参数如果为true，则把用户加入到黑名单后双方发消息时对方都收不到；false,则
//我能给黑名单的中用户发消息，但是对方发给我时我是收不到的
        try {
            EMClient.getInstance().contactManager().addUserToBlackList(inputVO.getUsername(), true);//需异步处理
        } catch (HyphenateException e) {
            if (BDebug.DEBUG) {
                e.printStackTrace();
            }
        }
    }

    public void deleteUserFromBlackList(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        UserInputVO inputVO=DataHelper.gson.fromJson(params[0],new TypeToken<UserInputVO>(){}.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_DELETE_USER_FROM_BLACKLIST;
        Bundle bd = new Bundle();
        bd.putSerializable(BUNDLE_DATA,inputVO);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void deleteUserFromBlackListMsg(final UserInputVO inputVO) {
        //第二个参数如果为true，则把用户加入到黑名单后双方发消息时对方都收不到；false,则
//我能给黑名单的中用户发消息，但是对方发给我时我是收不到的
        try {
            EMClient.getInstance().contactManager().removeUserFromBlackList(inputVO.getUsername());//需异步处理
        } catch (HyphenateException e) {
            if (BDebug.DEBUG) {
                e.printStackTrace();
            }
        }

    }

    public void createPrivateGroup(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        CreateGroupInputVO inputVO=DataHelper.gson.fromJson(params[0],new TypeToken<CreateGroupInputVO>(){}.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_CREATEPRIVATEGROUP;
        Bundle bd = new Bundle();
        bd.putSerializable(BUNDLE_DATA,inputVO);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void createPrivateGroupMsg(CreateGroupInputVO inputVO) {
        GroupCreateResultVO resultVO = new GroupCreateResultVO();
        if (!TextUtils.isEmpty(inputVO.getMaxUsers())) {
            //前一种方法创建的群聊默认最大群聊用户数为200，传入maxUsers后设置自定义的最大用户数，最大为2000
            try {
                EMGroupManager.EMGroupOptions options = new EMGroupManager.EMGroupOptions();
                if (Boolean.valueOf(inputVO.getAllowInvite())) {
                    options.style = EMGroupManager.EMGroupStyle.EMGroupStylePrivateMemberCanInvite;
                } else {
                    options.style = EMGroupManager.EMGroupStyle.EMGroupStylePrivateOnlyOwnerInvite;
                }
                options.maxUsers = Integer.parseInt(inputVO.getMaxUsers());
                EMGroup emGroup = EMClient.getInstance().groupManager().createGroup(inputVO.getGroupName(),inputVO.getDesc(), inputVO.getMembers(), inputVO.getInitialWelcomeMessage(), options);
                resultVO.setIsSuccess(true);
                resultVO.setGroup(convertEMGroup2VO(emGroup));
            } catch (HyphenateException e) {
                resultVO.setIsSuccess(false);
                resultVO.setErrorStr(String.valueOf(e.getErrorCode()));
                if (BDebug.DEBUG){
                    e.printStackTrace();
                }
            }
        } else {
            try {
                EMGroupManager.EMGroupOptions options = new EMGroupManager.EMGroupOptions();
                if (Boolean.valueOf(inputVO.getAllowInvite())) {
                    options.style = EMGroupManager.EMGroupStyle.EMGroupStylePrivateMemberCanInvite;
                } else {
                    options.style = EMGroupManager.EMGroupStyle.EMGroupStylePrivateOnlyOwnerInvite;
                }
                String[] members={};
                if (inputVO.getMembers()!=null){
                    members=inputVO.getMembers();
                }
                EMGroup emGroup = EMClient.getInstance().groupManager().createGroup(inputVO.getGroupName(), inputVO.getDesc(), members, null, options);
                resultVO.setGroup(convertEMGroup2VO(emGroup));
                resultVO.setIsSuccess(true);
            } catch (HyphenateException e) {
                resultVO.setIsSuccess(false);
                resultVO.setErrorStr(String.valueOf(e.getErrorCode()));
            }
        }
        String js = SCRIPT_HEADER + "if(" + JSConst.ON_GROUP_CREATED + "){"
                + JSConst.ON_GROUP_CREATED + "('" + DataHelper.gson.toJson(resultVO) + "');}";
        evaluateRootWindowScript(js);
    }

    public void createPublicGroup(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        CreateGroupInputVO inputVO=DataHelper.gson.fromJson(params[0],new TypeToken<CreateGroupInputVO>(){}.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_CREATEPUBLICGROUP;
        Bundle bd = new Bundle();
        bd.putSerializable(BUNDLE_DATA,inputVO);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void createPublicGroupMsg(CreateGroupInputVO inputVO) {
        EMGroupManager.EMGroupOptions options = new EMGroupManager.EMGroupOptions();
        options.style = EMGroupManager.EMGroupStyle.EMGroupStylePublicJoinNeedApproval;
        GroupCreateResultVO resultVO = new GroupCreateResultVO();
        if (!TextUtils.isEmpty(inputVO.getMaxUsers())){
            //前一种方法创建的群聊默认最大群聊用户数为200，传入maxUsers后设置自定义的最大用户数，最大为2000
            try {
                options.maxUsers = Integer.parseInt(inputVO.getMaxUsers());
                EMGroup emGroup = EMClient.getInstance().groupManager().createGroup(inputVO.getGroupName(), inputVO.getDesc(), inputVO.getMembers(), inputVO.getInitialWelcomeMessage(), options);
                resultVO.setIsSuccess(true);
                resultVO.setGroup(convertEMGroup2VO(emGroup));
            } catch (HyphenateException e) {
                resultVO.setIsSuccess(true);
                resultVO.setErrorStr(String.valueOf(e.getErrorCode()));
            }
        }else{
            try {
                String[] members={};
                if (inputVO.getMembers()!=null){
                    members=inputVO.getMembers();
                }
                EMGroup emGroup = EMClient.getInstance().groupManager().createGroup(inputVO.getGroupName(), inputVO.getDesc(), members, null, options);
                resultVO.setIsSuccess(true);
                resultVO.setGroup(convertEMGroup2VO(emGroup));
            } catch (HyphenateException e) {
                resultVO.setIsSuccess(true);
                resultVO.setErrorStr(String.valueOf(e.getErrorCode()));
            }
        }
        String js = SCRIPT_HEADER + "if(" + JSConst.ON_GROUP_CREATED + "){"
                + JSConst.ON_GROUP_CREATED + "('" + DataHelper.gson.toJson(resultVO) + "');}";
        evaluateRootWindowScript(js);
    }

    public void addUsersToGroup(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        GroupInfoVO inputVO=DataHelper.gson.fromJson(params[0],new TypeToken<GroupInfoVO>(){}.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_ADDUSERSTOGROUP;
        Bundle bd = new Bundle();
        bd.putSerializable(BUNDLE_DATA,inputVO);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void addUsersToGroupMsg(GroupInfoVO inputVO) {
        try {

            if (Boolean.valueOf(inputVO.getIsGroupOwner())) {
                //群主加人调用此方法
                EMClient.getInstance().groupManager().addUsersToGroup(inputVO.getGroupId(), inputVO.getNewmembers());//需异步处理
            }else{
                //私有群里，如果开放了群成员邀请，群成员邀请调用下面方法
                EMClient.getInstance().groupManager().inviteUser(inputVO.getGroupId(), inputVO.getNewmembers(), null);//需异步处理
            }
        } catch (HyphenateException e) {
            if (BDebug.DEBUG) {
                e.printStackTrace();
            }
        }
    }

    public void removeUserFromGroup(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        GroupInfoVO inputVO=DataHelper.gson.fromJson(params[0],new TypeToken<GroupInfoVO>(){}.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_REMOVEUSERFROMGROUP;
        Bundle bd = new Bundle();
        bd.putSerializable(BUNDLE_DATA,inputVO);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void removeUserFromGroupMsg(GroupInfoVO inputVO) {
        try {
            //把username从群聊里删除
            EMClient.getInstance().groupManager().removeUserFromGroup(inputVO.getGroupId(), inputVO.getUsername());//需异步处理
        } catch (HyphenateException e) {
            if (BDebug.DEBUG) {
                e.printStackTrace();
            }
        }
    }

    public void joinGroup(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        GroupInfoVO inputVO=DataHelper.gson.fromJson(params[0],new TypeToken<GroupInfoVO>(){}.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_JOINGROUP;
        Bundle bd = new Bundle();
        bd.putSerializable(BUNDLE_DATA,inputVO);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void joinGroupMsg(GroupInfoVO inputVO) {
        try {
            if (TextUtils.isEmpty(inputVO.getReason())) {
                EMClient.getInstance().groupManager().joinGroup(inputVO.getGroupId());//需异步处理
            }else{
                EMClient.getInstance().groupManager().applyJoinToGroup(inputVO.getGroupId(),inputVO.getReason());//需异步处理
            }
        } catch (HyphenateException e) {
            if (BDebug.DEBUG) {
                e.printStackTrace();
            }
        }
    }

    public void exitFromGroup(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        GroupInfoVO inputVO=DataHelper.gson.fromJson(params[0],new TypeToken<GroupInfoVO>(){}.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_EXITFROMGROUP;
        Bundle bd = new Bundle();
        bd.putSerializable(BUNDLE_DATA,inputVO);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void exitFromGroupMsg(GroupInfoVO inputVO) {
        try {
            EMClient.getInstance().groupManager().leaveGroup(inputVO.getGroupId());
        } catch (HyphenateException e) {
            if (BDebug.DEBUG) {
                e.printStackTrace();
            }
        }
    }

    public void exitAndDeleteGroup(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        GroupInfoVO inputVO=DataHelper.gson.fromJson(params[0],new TypeToken<GroupInfoVO>(){}.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_EXITANDDELETEGROUP;
        Bundle bd = new Bundle();
        bd.putSerializable(BUNDLE_DATA,inputVO);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void exitAndDeleteGroupMsg(GroupInfoVO inputVO) {
        try {
            EMClient.getInstance().groupManager().destroyGroup(inputVO.getGroupId());
        } catch (HyphenateException e) {
            if (BDebug.DEBUG) {
                e.printStackTrace();
            }
        }
    }

    public void getGroupsFromServer(final String[] params){
        new Thread(new Runnable() {
            @Override
            public void run() {
                getGroupsFromServerOnThread(params);
            }
        }).start();
    }

    public void getGroupsFromServerOnThread(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        GroupInfoVO inputVO=DataHelper.gson.fromJson(params[0],new TypeToken<GroupInfoVO>(){}.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
        }
        String funcId = null;
        if (params.length == 2) {
            funcId = params[1];
        }
        final GroupsOutputVO outputVO=new GroupsOutputVO();
        List<EMGroup> grouplist = null;
        if (Boolean.valueOf(inputVO.getLoadCache())){
            grouplist = EMClient.getInstance().groupManager().getAllGroups();
            if (grouplist==null){
                outputVO.setResult("1");
                outputVO.setErrorMsg("");
            }else {
                outputVO.setResult("0");
                List <GroupResultVO> groupResultVOList = new ArrayList<GroupResultVO>();
                for (EMGroup emGroup : grouplist) {
                    GroupResultVO vo = convertEMGroup2VO(emGroup);
                    groupResultVOList.add(vo);
                }
                outputVO.setGrouplist(groupResultVOList);
            }
            if(null != funcId) {
                callbackToJs(Integer.parseInt(funcId), false, DataHelper.gson.toJsonTree(outputVO));
            } else {
                String js = SCRIPT_HEADER + "if(" + JSConst.CALLBACK_GETGROUPSFROMSERVER + "){"
                        + JSConst.CALLBACK_GETGROUPSFROMSERVER + "('" + DataHelper.gson.toJson(outputVO) + "');}";
                evaluateRootWindowScript(js);
            }
            return;
        }
        try {
            List<EMGroup> groupList = EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
            outputVO.setResult("0");
            List <GroupResultVO> groupResultVOList = new ArrayList<GroupResultVO>();
            for (EMGroup emGroup : groupList) {
                GroupResultVO vo = convertEMGroup2VO(emGroup);
                groupResultVOList.add(vo);
            }
            outputVO.setGrouplist(groupResultVOList);
        } catch (HyphenateException e) {
            outputVO.setResult("1");
            outputVO.setErrorMsg(e.getMessage());
            if (BDebug.DEBUG) {
                e.printStackTrace();
            }
        }

        if (null != funcId) {
            callbackToJs(Integer.parseInt(funcId), false, DataHelper.gson.toJsonTree(outputVO));
        } else {
            String js = SCRIPT_HEADER + "if(" + JSConst.CALLBACK_GETGROUPSFROMSERVER + "){"
                    + JSConst.CALLBACK_GETGROUPSFROMSERVER + "('" + DataHelper.gson.toJson(outputVO) + "');}";
            evaluateRootWindowScript(js);
        }
    }

    public void getAllPublicGroupsFromServer(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        final PageVO inputVO=DataHelper.gson.fromJson(params[0],PageVO.class);
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
        }
        String funcId = null;
        if (params.length == 2) {
            funcId = params[1];
        }
        final String callbackId = funcId;
        new Thread(new Runnable() {
            @Override
            public void run() {
                GroupInfosOutputVO outputVO= new GroupInfosOutputVO();
                try {
                    String cursor = null;
                    if(!TextUtils.isEmpty(cursor)){
                        cursor = inputVO.getCursor();
                    }
                    final EMCursorResult<EMGroupInfo> result= EMClient.getInstance().groupManager()
                            .getPublicGroupsFromServer(inputVO.pageSize
                                    , cursor);
                    outputVO.setCursor(result.getCursor());
                    outputVO.setGrouplist(result.getData());
                    outputVO.setResult("0");
                } catch (HyphenateException e) {
                    outputVO.setResult("1");
                    if (BDebug.DEBUG) {
                        e.printStackTrace();
                    }
                }finally {
                    if (null != callbackId) {
                        callbackToJs(Integer.parseInt(callbackId), false, DataHelper.gson.toJsonTree(outputVO));
                    } else {
                        String js = SCRIPT_HEADER + "if(" + JSConst.CALLBACK_GETALLPUBLICGROUPSFROMSERVER + "){"
                                + JSConst.CALLBACK_GETALLPUBLICGROUPSFROMSERVER + "('" + DataHelper.gson.toJson(outputVO) + "');}";
                        evaluateRootWindowScript(js);
                    }
                }
            }
        }).start();
    }

    public void getGroup(final String[] params){
        initExecutorService();
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                getGroupOnThread(params);
            }
        });
    }

    private void getGroupOnThread(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        GroupInfoVO inputVO=DataHelper.gson.fromJson(params[0],new TypeToken<GroupInfoVO>(){}.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
        }
        String funcId = null;
        if (params.length == 2) {
            funcId = params[1];
        }

        EMGroup group = null;
        if (Boolean.valueOf(inputVO.getLoadCache())){
            group = EMClient.getInstance().groupManager().getGroup(inputVO.getGroupId());
        }else{
            try {
                group =EMClient.getInstance().groupManager().getGroupFromServer(inputVO.getGroupId());
            } catch (HyphenateException e) {
                if (BDebug.DEBUG) {
                    e.printStackTrace();
                }
            }
        }
        if (null != funcId) {
            callbackToJs(Integer.parseInt(funcId), false, DataHelper.gson.toJsonTree(convertEMGroup2VO(group)));
        } else {
            String js = SCRIPT_HEADER + "if(" + JSConst.CALLBACK_GETGROUP + "){"
                    + JSConst.CALLBACK_GETGROUP + "('" + DataHelper.gson.toJson(convertEMGroup2VO(group)) + "');}";
            evaluateRootWindowScript(js);
        }
    }

    public static GroupResultVO convertEMGroup2VO(EMGroup group){
        GroupResultVO resultVO=new GroupResultVO();
        if (group!=null){
            resultVO.setGroupId(group.getGroupId());
            resultVO.setIsBlock(group.isMsgBlocked());
            resultVO.setGroupSubject(group.getDescription());
            resultVO.setOwner(group.getOwner());
            resultVO.setMembers(group.getMembers());
            resultVO.setIsPublic(group.isPublic());
            resultVO.setAllowInvites(group.isAllowInvites());
            resultVO.setMembersOnly(group.isMembersOnly());
//            resultVO.setGroupMaxUserCount(group.getMaxUsers()); 该属性已废弃
            resultVO.setGroupName(group.getGroupName());
            resultVO.setGroupDescription(group.getDescription());
        }
        return resultVO;
    }

    public void blockGroupMessage(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        GroupInfoVO inputVO=DataHelper.gson.fromJson(params[0],new TypeToken<GroupInfoVO>(){}.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_BLOCKGROUPMESSAGE;
        Bundle bd = new Bundle();
        bd.putSerializable(BUNDLE_DATA,inputVO);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void blockGroupMessageMsg(GroupInfoVO infoVO) {
        //屏蔽群消息后，就不能接收到此群的消息 （群创建者不能屏蔽群消息）（还是群里面的成员，但不再接收消息）

        try {
            EMClient.getInstance().groupManager().blockGroupMessage(infoVO.getGroupId());//需异步处理
        } catch (HyphenateException e) {
            if (BDebug.DEBUG){
                e.printStackTrace();
            }
        }
    }


    public void unblockGroupMessage(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        GroupInfoVO inputVO=DataHelper.gson.fromJson(params[0],new TypeToken<GroupInfoVO>(){}.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_UNBLOCKGROUPMESSAGE;
        Bundle bd = new Bundle();
        bd.putSerializable(BUNDLE_DATA,inputVO);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void unblockGroupMessageMsg(GroupInfoVO infoVO) {
        //取消屏蔽群消息,就可以正常收到群的所有消息
        try {
            EMClient.getInstance().groupManager().unblockGroupMessage(infoVO.getGroupId());//需异步处理
        } catch (HyphenateException e) {
            if (BDebug.DEBUG) {
                e.printStackTrace();
            }
        }
    }

    public void changeGroupName(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        GroupInfoVO inputVO=DataHelper.gson.fromJson(params[0],new TypeToken<GroupInfoVO>(){}.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_CHANGEGROUPNAME;
        Bundle bd = new Bundle();
        bd.putSerializable(BUNDLE_DATA,inputVO);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void changeGroupNameMsg(GroupInfoVO infoVO) {
        //groupId 需要改变名称的群组的id
        //changedGroupName 改变后的群组名称
        try {
            EMClient.getInstance().groupManager().changeGroupName(infoVO.getGroupId(), infoVO.getChangedGroupName());//需异步处理
        } catch (HyphenateException e) {
            if (BDebug.DEBUG) {
                e.printStackTrace();
            }
        }
    }


    public void setReceiveNotNoifyGroup(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        GroupInfoVO inputVO=DataHelper.gson.fromJson(params[0],new TypeToken<GroupInfoVO>(){}.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_SETRECEIVENOTNOIFYGROUP;
        Bundle bd = new Bundle();
        bd.putSerializable(BUNDLE_DATA,inputVO);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void setReceiveNotNoifyGroupMsg(GroupInfoVO infoVO) {
        //如果群聊只是想提示数目，不响铃。可以通过此属性设置，此属性是本地属性
//        EMClient.getInstance().getOptions().setReceiveNotNoifyGroup(infoVO.getGroupIds());
    }

    public void blockUser(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        GroupInfoVO inputVO=DataHelper.gson.fromJson(params[0],new TypeToken<GroupInfoVO>(){}.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_BLOCKUSER;
        Bundle bd = new Bundle();
        bd.putSerializable(BUNDLE_DATA,inputVO);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void blockUserMsg(GroupInfoVO infoVO) {
        try {
            EMClient.getInstance().groupManager().blockUser(infoVO.getGroupId(), infoVO.getUsername());//需异步处理    }
        } catch (HyphenateException e) {
            if (BDebug.DEBUG) {
                e.printStackTrace();
            }
        }
    }

    public void unblockUser(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        GroupInfoVO inputVO=DataHelper.gson.fromJson(params[0],new TypeToken<GroupInfoVO>(){}.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_UNBLOCKUSER;
        Bundle bd = new Bundle();
        bd.putSerializable(BUNDLE_DATA,inputVO);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void unblockUserMsg(GroupInfoVO infoVO) {
        try {
            EMClient.getInstance().groupManager().unblockUser(infoVO.getGroupId(), infoVO.getUsername());
        } catch (HyphenateException e) {
            if (BDebug.DEBUG) {
                e.printStackTrace();
            }
        }
    }

    public void getBlockedUsers(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        final GroupInfoVO inputVO = DataHelper.gson.fromJson(params[0],new TypeToken<GroupInfoVO>(){}.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
        }
        String funcId = null;
        if (params.length == 2) {
            funcId = params[1];
        }
        final String callbackId = funcId;
        new Thread(new Runnable() {

            public void run() {
                try {
                    final List<String> usernames = EMClient.getInstance().groupManager().getBlockedUsers(inputVO.getGroupId());
                    ((Activity)mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (callbackId != null) {
                                callbackToJs(Integer.parseInt(callbackId), false, DataHelper.gson.toJsonTree(usernames));
                            } else {
                                String js = SCRIPT_HEADER + "if(" + JSConst.CALLBACK_GETBLOCKEDUSERS + "){"
                                        + JSConst.CALLBACK_GETBLOCKEDUSERS + "('" + DataHelper.gson.toJson(usernames) + "');}";
                                evaluateRootWindowScript(js);
                            }
                            System.out.println(new Gson().toJson(usernames));
                        }
                    });
                } catch (HyphenateException e) {
                    if (BDebug.DEBUG) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    if (BDebug.DEBUG) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void importMessage(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        ImportMsgInputVO inputVO=DataHelper.gson.fromJson(params[0],new TypeToken<ImportMsgInputVO>(){}.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_IMPORTMESSAGE;
        Bundle bd = new Bundle();
        bd.putSerializable(BUNDLE_DATA,inputVO);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void importMessageMsg(ImportMsgInputVO inputVO) {
        EMMessage msg=null;
        if ("1".equals(inputVO.getSendType())){
            //发送消息
            msg = EMMessage.createSendMessage(EMMessage.Type.TXT);
            EMTextMessageBody body = new EMTextMessageBody("send text msg " + System.currentTimeMillis());
            msg.addBody(body);
            msg.setTo(inputVO.getTo());
            msg.setFrom(inputVO.getFrom());
            msg.setMsgTime(System.currentTimeMillis());
        }else{
            //接收消息
            msg = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
            EMTextMessageBody body = new EMTextMessageBody("receive text msg " + System.currentTimeMillis());
            msg.addBody(body);
            msg.setFrom(inputVO.getFrom());
            msg.setTo(inputVO.getTo());
            msg.setMsgTime(System.currentTimeMillis());
        }
        if ("1".equals(inputVO.getChatType())){
            msg.setChatType(EMMessage.ChatType.GroupChat);
        }
        List<EMMessage> list = new ArrayList<EMMessage>();
        list.add(msg);
        EMClient.getInstance().chatManager().importMessages(list);
    }

    public void makeVoiceCall(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        UserInputVO inputVO=DataHelper.gson.fromJson(params[0],new TypeToken<UserInputVO>(){}.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_MAKEVOICECALL;
        Bundle bd = new Bundle();
        bd.putSerializable(BUNDLE_DATA,inputVO);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void makeVoiceCallMsg(UserInputVO inputVO) {
        try {
            EMClient.getInstance().callManager().makeVoiceCall(inputVO.getUsername());
        } catch (EMServiceNotReadyException e) {
            // TODO Auto-generated catch block

        }
    }

    public void answerCall(String[] params){
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_ANSWERCALL;
        mHandler.sendMessage(msg);
    }

    private void answerCallMsg() {
        try {
            EMClient.getInstance().callManager().answerCall();
        } catch (EMNoActiveCallException e) {
        }
    }

    public void rejectCall(String[] params){
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_REJECTCALL;
        mHandler.sendMessage(msg);
    }

    private void rejectCallMsg() {
        try {
            EMClient.getInstance().callManager().rejectCall();
        } catch (EMNoActiveCallException e) {
        }
    }

    public void endCall(String[] params){
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_ENDCALL;
        mHandler.sendMessage(msg);
    }

    private void endCallMsg() {
        try {
            EMClient.getInstance().callManager().endCall();
        } catch (EMNoActiveCallException e) {
            Log.i(TAG, "endCall exception:" + e.getMessage());
        }
    }

    public void sendCmdMessage(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        CmdMsgInputVO inputVO=DataHelper.gson.fromJson(params[0], new TypeToken<CmdMsgInputVO>() {
        }.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_SENDCMDMESSAGE;
        Bundle bd = new Bundle();
        bd.putSerializable(BUNDLE_DATA, inputVO);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void sendCmdMessageMsg(CmdMsgInputVO inputVO) {
        final EMMessage cmdMsg = EMMessage.createSendMessage(EMMessage.Type.CMD);

        //支持单聊和群聊，默认单聊，如果是群聊添加下面这行
        if ("1".equals(inputVO.getChatType())) {
            cmdMsg.setChatType(EMMessage.ChatType.GroupChat);
        }
        String action=inputVO.getAction();//action可以自定义，在广播接收时可以收到
        EMCmdMessageBody cmdBody=new EMCmdMessageBody(action);
        cmdMsg.setTo(inputVO.getToUsername());
        cmdMsg.addBody(cmdBody);
        cmdMsg.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                callbackSendMsgResult(true, null, cmdMsg);
            }

            @Override
            public void onError(int i, String s) {
                callbackSendMsgResult(false, null, cmdMsg);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
        EMClient.getInstance().chatManager().sendMessage(cmdMsg);
    }

    public void updateCurrentUserNickname(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        NicknameVO inputVO=DataHelper.gson.fromJson(params[0], new TypeToken<NicknameVO>() {
        }.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_UPDATECURRENTUSERNICK;
        Bundle bd = new Bundle();
        bd.putSerializable(BUNDLE_DATA, inputVO);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void updateCurrentUserNickMsg(final NicknameVO infoVO) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                EMClient.getInstance().updateCurrentUserNick(infoVO.getNickname());
            }
        }).start();
    }

    public void getChatterInfo(final String[] params){
        initExecutorService();
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                getChatterInfoOnThread(params);
            }
        });
    }

    private void getChatterInfoOnThread(String[] params){
        String funcId = null;
        if (null != params && params.length == 1) {
            funcId = params[0];
        }
        List<String> usernames = new ArrayList<String>();
        if (tempContacts!=null){
            usernames.addAll(tempContacts);
        }
        final String callbackId = funcId;
        final List<String> usernameList = usernames;

        try {
            List<String> tempList = EMClient.getInstance().contactManager().getAllContactsFromServer();
            if (tempList!=null){
                for (String username:tempList) {
                    if (!usernameList.contains(username)) {
                        usernameList.add(username);
                    }
                }
            }
        } catch (HyphenateException e) {
            if (BDebug.DEBUG) {
                e.printStackTrace();
            }
        }
        final List<ChatterInfoVO> chatterInfoVOs=new ArrayList<ChatterInfoVO>();
        if (usernameList.size() > 0){
            for (String username:usernameList){
                ChatterInfoVO infoVO=new ChatterInfoVO();
                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(username);
                infoVO.setChatter(username);
                infoVO.setIsGroup("0");
                infoVO.setChatType("0");
                if (conversation != null) {
                    if (conversation.getLastMessage()!=null) {
                        infoVO.setLastMsg(ListenersRegister.convertEMMessage(conversation.getLastMessage()));
                    }
                    infoVO.setUnreadMsgCount(String.valueOf(conversation.getUnreadMsgCount()));
                }
                chatterInfoVOs.add(infoVO);
            }
        }
        try {
            List<EMGroup> groupList = EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
            for (EMGroup emGroup : groupList) {
                ChatterInfoVO infoVO = new ChatterInfoVO();
                infoVO.setIsGroup("1");
                infoVO.setChatType("1");
                infoVO.setGroupName(emGroup.getGroupName());
                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(emGroup.getGroupId());
                if (conversation != null) {
                    infoVO.setUnreadMsgCount(String.valueOf(conversation.getUnreadMsgCount()));
                    if (conversation.getLastMessage() != null) {
                        infoVO.setLastMsg(ListenersRegister.convertEMMessage(conversation.getLastMessage()));
                    }
                }
                infoVO.setChatter(emGroup.getGroupId());
                chatterInfoVOs.add(infoVO);
            }

        } catch (HyphenateException e) {
            if (BDebug.DEBUG) {
                e.printStackTrace();
            }
        }
        if (null != callbackId) {
            callbackToJs(Integer.parseInt(callbackId), false, DataHelper.gson.toJsonTree(chatterInfoVOs));
        } else {
            String js = SCRIPT_HEADER + "if(" + JSConst.CALLBACK_GET_CHATTER_INFO + "){"
                    + JSConst.CALLBACK_GET_CHATTER_INFO + "('" + DataHelper.gson.toJson(chatterInfoVOs) + "');}";
            evaluateRootWindowScript(js);
        }
    }


    public void getRecentChatters(String[] params){
        String funcId = null;
        if (null != params && params.length == 1) {
            funcId = params[0];
        }
        List<EMConversation> conversations=HXHelper.loadConversationsWithRecentChat();
        List<ChatterInfoVO> chatterInfoVOs=new ArrayList<ChatterInfoVO>();
        if (conversations!=null){
            for (EMConversation conversation:conversations){
                ChatterInfoVO infoVO=new ChatterInfoVO();
                infoVO.setChatType(getChatTypeValue(conversation.getType()));
                infoVO.setChatter(conversation.getLastMessage().getUserName());
                infoVO.setIsGroup("0");
                if (conversation.getType()== EMConversation.EMConversationType.GroupChat) {
                    EMGroup emGroup=EMClient.getInstance().groupManager().getGroup(conversation.getLastMessage().getUserName());
                    if (emGroup!=null) {
                        infoVO.setGroupName(emGroup.getGroupName());
                        infoVO.setChatter(emGroup.getGroupId());
                    }
                    infoVO.setIsGroup("1");
                }
                if (conversation.getLastMessage()!=null){
                    infoVO.setLastMsg(ListenersRegister.convertEMMessage(conversation.getLastMessage()));
                }
                infoVO.setUnreadMsgCount(String.valueOf(conversation.getUnreadMsgCount()));
                chatterInfoVOs.add(infoVO);
            }
            if (null != funcId) {
                callbackToJs(Integer.parseInt(funcId), false, DataHelper.gson.toJsonTree(chatterInfoVOs));
            } else {
                String js = SCRIPT_HEADER + "if(" + JSConst.CALLBACK_GET_RECENT_CHATTERS + "){"
                        + JSConst.CALLBACK_GET_RECENT_CHATTERS + "('" + DataHelper.gson.toJson(chatterInfoVOs) + "');}";
                evaluateRootWindowScript(js);
            }
        }
    }


    public void getTotalUnreadMsgCount(String[] params){
        String funcId = null;
        if (null != params && params.length == 1) {
            funcId = params[0];
        }
        int unreadMsgCountTotal = 0;
        int chatroomUnreadMsgCount = 0;
        unreadMsgCountTotal = EMClient.getInstance().chatManager().getUnreadMsgsCount();
        Map<String, EMConversation> map = EMClient.getInstance().chatManager().getAllConversations();
        for (String key : map.keySet()) {
            EMConversation conversation = map.get(key);
            if (conversation.getType() == EMConversation.EMConversationType.ChatRoom) {
                chatroomUnreadMsgCount = chatroomUnreadMsgCount + conversation.getUnreadMsgCount();
            }
        }
        int totalCount=unreadMsgCountTotal - chatroomUnreadMsgCount;

        HashMap<String,String> resulMap = new HashMap<String, String>();
        resulMap.put("count", String.valueOf(totalCount));
        if (funcId != null) {
            callbackToJs(Integer.parseInt(funcId), false, DataHelper.gson.toJsonTree(resulMap));
        } else {
            String js = SCRIPT_HEADER + "if(" + JSConst.CALLBACK_GET_TOTAL_UNREAD_MSG_COUNT + "){"
                    + JSConst.CALLBACK_GET_TOTAL_UNREAD_MSG_COUNT + "('" + DataHelper.gson.toJson(resulMap) + "');}";
            evaluateRootWindowScript(js);
        }
    }

    /**
     * 接受申请者的入群申请
     * @param params
     */
    public void acceptJoinApplication(final String params[]) {
        initExecutorService();
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject(params[0]);
                    String groupId = jsonObject.optString("groupId","");
                    String username = jsonObject.optString("username", "");
                    if (TextUtils.isEmpty(groupId) || TextUtils.isEmpty(username)) {
                        BDebug.i(TAG, "acceptJoinApplication: invalid params");
                        return;
                    }
                    EMClient.getInstance().groupManager().acceptApplication(username, groupId);
                } catch (JSONException e) {
                    if (BDebug.DEBUG) {
                        e.printStackTrace();
                    }
                } catch (HyphenateException e) {
                    if (BDebug.DEBUG) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 拒绝申请者的入群申请
     * @param params
     */
    public void declineJoinApplication(final String params[]) {
        initExecutorService();
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                if (params.length < 0) {
                    BDebug.i(TAG, "acceptJoinApplication: invalid params");
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(params[0]);
                    String groupId = jsonObject.optString("groupId","");
                    String username = jsonObject.optString("username", "");
                    String reason = jsonObject.optString("reason", "");
                    if (TextUtils.isEmpty(groupId) || TextUtils.isEmpty(username)) {
                        BDebug.i(TAG, "declineJoinApplication: invalid params");
                        return;
                    }
                    EMClient.getInstance().groupManager().declineApplication(username, groupId, reason);
                } catch (JSONException e) {
                    if (BDebug.DEBUG) {
                        e.printStackTrace();
                    }
                } catch (HyphenateException e) {
                    if (BDebug.DEBUG) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    /**
     * 接收入群邀请
     * @param params
     */
    public void acceptInvitationFromGroup(final String params[]) {
        initExecutorService();
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                if (params.length < 0) {
                    BDebug.i(TAG, "acceptJoinApplication: invalid params");
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(params[0]);
                    String groupId = jsonObject.optString("groupId","");
                    String username = jsonObject.optString("username", ""); //inviter
                    if (TextUtils.isEmpty(groupId) || TextUtils.isEmpty(username)) {
                        BDebug.i(TAG, "acceptInvitationFromGroup: invalid params");
                        return;
                    }
                    EMClient.getInstance().groupManager().acceptInvitation(groupId, username);
                } catch (JSONException e) {
                    if (BDebug.DEBUG) {
                        e.printStackTrace();
                    }
                } catch (HyphenateException e) {
                    if (BDebug.DEBUG) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    /**
     * 拒绝入群邀请
     * @param params
     */
    public void declineInvitationFromGroup(final String params[]) {
        initExecutorService();
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                if (params.length < 0) {
                    BDebug.i(TAG, "acceptJoinApplication: invalid params");
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(params[0]);
                    String groupId = jsonObject.optString("groupId","");
                    String username = jsonObject.optString("username", ""); //inviter
                    String reason = jsonObject.optString("reason", "");
                    if (TextUtils.isEmpty(groupId) || TextUtils.isEmpty(username)) {
                        BDebug.i(TAG, "declineInvitationFromGroup: invalid params");
                        return;
                    }
                    EMClient.getInstance().groupManager().declineInvitation(groupId, username, reason);
                } catch (JSONException e) {
                    if (BDebug.DEBUG) {
                        e.printStackTrace();
                    }
                } catch (HyphenateException e) {
                    if (BDebug.DEBUG) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }




    private void callbackSendMsgResult(boolean result,String errorInfo,EMMessage emMessage){
        SendMsgResultVO resultVO=new SendMsgResultVO();
        resultVO.setIsSuccess(result);
        resultVO.setErrorStr(errorInfo);
        resultVO.setMessage(ListenersRegister.convertEMMessage(emMessage));
        String js = SCRIPT_HEADER + "if(" + JSConst.ON_MESSAGE_SENT + "){"
                + JSConst.ON_MESSAGE_SENT + "('" + DataHelper.gson.toJson(resultVO) + "');}";
        evaluateRootWindowScript(js);
    }

    /**
     * 执行Root Window脚本
     *
     * @param script
     */
    private void evaluateRootWindowScript(String script) {
            if (callbackBrowserViews!=null){
                for (EBrowserView eBrowserView:callbackBrowserViews){
                    eBrowserView.addUriTask(script);
                }
            }
    }

    private void handleMessageOnThread(Message message){
        if(message == null){
            return;
        }
        Bundle bundle=message.getData();
        switch (message.what) {
            case MSG_SEND_TEXT:
                sendTextMsg((SendInputVO) bundle.getSerializable(BUNDLE_DATA));
                break;
            case MSG_SEND_VOICE:
                sendVoiceMsg((SendInputVO) bundle.getSerializable(BUNDLE_DATA));
                break;
            case MSG_SEND_PICTURE:
                sendPictureMsg((SendInputVO) bundle.getSerializable(BUNDLE_DATA));
                break;
            case MSG_SEND_LOCATION:
                sendLocationMsgResult((SendInputVO) bundle.getSerializable(BUNDLE_DATA));
                break;
            case MSG_SEND_FILE:
                sendFileMsg((SendInputVO) bundle.getSerializable(BUNDLE_DATA));
                break;
            case MSG_GET_UNREAD_MSG_COUNT:
                getUnreadMsgCountMsg((HistoryInputVO) bundle.getSerializable(BUNDLE_DATA));
                break;
            case MSG_RESET_UNREAD_MSG_COUNT:
                resetUnreadMsgCountMsg((HistoryInputVO) bundle.getSerializable(BUNDLE_DATA));
                break;
            case MSG_RESET_ALL_UNREAD_MSG_COUNT:
                resetAllUnreadMsgCountMsg();
                break;
            case MSG_CLEAR_CONVERSATION:
                clearConversationMsg((HistoryInputVO) bundle.getSerializable(BUNDLE_DATA));
                break;
            case MSG_DELETE_CONVERSATION:
                deleteConversationMsg((HistoryInputVO) bundle.getSerializable(BUNDLE_DATA));
                break;
            case MSG_REMOVE_MESSAGE:
                removeMessageMsg((HistoryInputVO) bundle.getSerializable(BUNDLE_DATA));
                break;
            case MSG_DELETE_ALL_CONVERSATION:
                deleteAllConversationMsg();
                break;
            case MSG_SET_NOTIFY_BY_SOUND_AND_VIBRATE:
                setNotifyBySoundAndVibrateMsg((NotifySettingVO) bundle.getSerializable(BUNDLE_DATA));
                break;
            case MSG_ADD_CONTACT:
                addContactMsg((AddContactInputVO) bundle.getSerializable(BUNDLE_DATA));
                break;
            case MSG_DELETE_CONTACT:
                deleteContactMsg((UserInputVO) bundle.getSerializable(BUNDLE_DATA));
                break;
            case MSG_ACCEPT_INVITATION:
                acceptInvitationMsg((UserInputVO) bundle.getSerializable(BUNDLE_DATA));
                break;
            case MSG_REFUSE_INVITATION:
                refuseInvitationMsg((UserInputVO) bundle.getSerializable(BUNDLE_DATA));
                break;
            case MSG_ADD_USER_TO_BLACKLIST:
                addUserToBlackListMsg((UserInputVO) bundle.getSerializable(BUNDLE_DATA));
                break;
            case MSG_DELETE_USER_FROM_BLACKLIST:
                deleteUserFromBlackListMsg((UserInputVO) bundle.getSerializable(BUNDLE_DATA));
                break;
            case MSG_CREATEPRIVATEGROUP:
                createPrivateGroupMsg((CreateGroupInputVO) bundle.getSerializable(BUNDLE_DATA));
                break;
            case MSG_CREATEPUBLICGROUP:
                createPublicGroupMsg((CreateGroupInputVO) bundle.getSerializable(BUNDLE_DATA));
                break;
            case MSG_ADDUSERSTOGROUP:
                addUsersToGroupMsg((GroupInfoVO) bundle.getSerializable(BUNDLE_DATA));
                break;
            case MSG_REMOVEUSERFROMGROUP:
                removeUserFromGroupMsg((GroupInfoVO) bundle.getSerializable(BUNDLE_DATA));
                break;
            case MSG_JOINGROUP:
                joinGroupMsg((GroupInfoVO) bundle.getSerializable(BUNDLE_DATA));
                break;
            case MSG_EXITFROMGROUP:
                exitFromGroupMsg((GroupInfoVO) bundle.getSerializable(BUNDLE_DATA));
                break;
            case MSG_EXITANDDELETEGROUP:
                exitAndDeleteGroupMsg((GroupInfoVO) bundle.getSerializable(BUNDLE_DATA));
                break;
            case MSG_BLOCKGROUPMESSAGE:
                blockGroupMessageMsg((GroupInfoVO) bundle.getSerializable(BUNDLE_DATA));
                break;
            case MSG_UNBLOCKGROUPMESSAGE:
                unblockGroupMessageMsg((GroupInfoVO) bundle.getSerializable(BUNDLE_DATA));
                break;
            case MSG_CHANGEGROUPNAME:
                changeGroupNameMsg((GroupInfoVO) bundle.getSerializable(BUNDLE_DATA));
                break;
            case MSG_SETRECEIVENOTNOIFYGROUP:
                setReceiveNotNoifyGroupMsg((GroupInfoVO) bundle.getSerializable(BUNDLE_DATA));
                break;
            case MSG_BLOCKUSER:
                blockUserMsg((GroupInfoVO) bundle.getSerializable(BUNDLE_DATA));
                break;
            case MSG_UNBLOCKUSER:
                unblockUserMsg((GroupInfoVO) bundle.getSerializable(BUNDLE_DATA));
                break;
            case MSG_IMPORTMESSAGE:
                importMessageMsg((ImportMsgInputVO) bundle.getSerializable(BUNDLE_DATA));
                break;
            case MSG_MAKEVOICECALL:
                makeVoiceCallMsg((UserInputVO) bundle.getSerializable(BUNDLE_DATA));
                break;
            case MSG_ANSWERCALL:
                answerCallMsg();
                break;
            case MSG_REJECTCALL:
                rejectCallMsg();
                break;
            case MSG_ENDCALL:
                endCallMsg();
                break;
            case MSG_SENDCMDMESSAGE:
                sendCmdMessageMsg((CmdMsgInputVO) bundle.getSerializable(BUNDLE_DATA));
                break;
            case MSG_UPDATECURRENTUSERNICK:
                updateCurrentUserNickMsg((NicknameVO) bundle.getSerializable(BUNDLE_DATA));
                break;
            case MSG_INIT:
                initEasemobMsg(bundle.getStringArray(BUNDLE_DATA));
                break;
            case MSG_SEND_VIDEO:
                sendVideoMsg((SendInputVO) bundle.getSerializable(BUNDLE_DATA));
                break;
            case MSG_SEND_HAS_READ_RESPONSE_FOR_MESSAGE:
                sendHasReadResponseForMessageMsg((MessageVO) bundle.getSerializable(BUNDLE_DATA));
                break;
            default:
                super.onHandleMessage(message);
        }
    }


    private void initExecutorService(){
        if (mExecutorService==null){
            mExecutorService=Executors.newSingleThreadExecutor();
        }
    }

    @Override
    public void onHandleMessage( Message message) {
        final Message finalMessage=new Message();
        finalMessage.copyFrom(message);
        initExecutorService();
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                handleMessageOnThread(finalMessage);
            }
        });
     }


}
