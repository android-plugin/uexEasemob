package org.zywx.wbpalmstar.widgetone.uexEasemob;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.easemob.EMCallBack;
import com.easemob.EMError;
import com.easemob.EMValueCallBack;
import com.easemob.chat.CmdMessageBody;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupInfo;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.LocationMessageBody;
import com.easemob.chat.NormalFileMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.VoiceMessageBody;
import com.easemob.exceptions.EMNetworkUnconnectedException;
import com.easemob.exceptions.EMNoActiveCallException;
import com.easemob.exceptions.EMServiceNotReadyException;
import com.easemob.exceptions.EaseMobException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import org.zywx.wbpalmstar.base.BDebug;
import org.zywx.wbpalmstar.engine.EBrowserView;
import org.zywx.wbpalmstar.engine.universalex.EUExBase;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.input.AddContactInputVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.input.CmdMsgInputVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.input.CreateGroupInputVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.input.GroupInfoVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.input.HistoryInputVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.input.ImportMsgInputVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.input.NicknameVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.input.UserInputVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.input.NotifySettingVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.input.SendInputVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.output.CallReceiveOutputVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.output.CallStateOutputVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.output.ChatterInfoVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.output.CmdMsgOutputVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.output.ConversationResultVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.output.GroupOptVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.output.GroupResultVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.output.GroupsOutputVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.output.MessageVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.output.MsgResultVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.output.ResultVO;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ylt on 15/3/13.
 */
public class EUExEasemob extends EUExBase implements ListenersRegister.ListenersCallback{

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

    private Gson mGson;

    public EUExEasemob(Context context, EBrowserView eBrowserView) {
        super(context, eBrowserView);
        mGson =new Gson();
    }

    @Override
    protected boolean clean() {
        return false;
    }

    public void initEasemob(String[] param){
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_INIT;
        Bundle bd = new Bundle();
        bd.putStringArray(BUNDLE_DATA, param);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    public void initEasemobMsg(String[] param){

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

        try {
            JSONObject jsonObject=new JSONObject(param[0]);
            String isAutoLogin=jsonObject.optString("isAutoLoginEnabled");
            if ("1".equals(isAutoLogin)){
                EMChat.getInstance().setAutoLogin(true);
            }else if ("2".equals(isAutoLogin)){
                EMChat.getInstance().setAutoLogin(false);
            }
        } catch (JSONException e) {
        }

        EMChat.getInstance().init(mContext.getApplicationContext());
        ListenersRegister register=new ListenersRegister();
        register.registerListeners(mContext.getApplicationContext(),mGson);
        register.setCallback(this);

        //如果使用环信的好友体系需要先设置
        EMChatManager.getInstance().getChatOptions().setUseRoster(true);
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

    public void login(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        UserInputVO inputVO=mGson.fromJson(params[0], new TypeToken<UserInputVO>() {
        }.getType());
        if (TextUtils.isEmpty(inputVO.getUsername())||TextUtils.isEmpty(inputVO.getPassword())){
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_LOGIN;
        Bundle bd = new Bundle();
        bd.putSerializable(BUNDLE_DATA,inputVO);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    public void loginMsg(UserInputVO inputVO){
        EMChatManager.getInstance().login(inputVO.getUsername(), inputVO.getPassword(), new EMCallBack() {
            @Override
            public void onSuccess() {
                EMGroupManager.getInstance().loadAllGroups();
                EMChatManager.getInstance().loadAllConversations();
                ResultVO resultVO = new ResultVO();
                resultVO.setResult("1");
                resultVO.setMsg("");
                String js = SCRIPT_HEADER + "if(" + JSConst.CALLBACK_LOGIN + "){"
                        + JSConst.CALLBACK_LOGIN + "('" + mGson.toJson(resultVO) + "');}";
                onCallback(js);
            }

            @Override
            public void onError(int i, String s) {
                ResultVO resultVO = new ResultVO();
                resultVO.setResult("2");
                resultVO.setMsg(s);
                String js = SCRIPT_HEADER + "if(" + JSConst.CALLBACK_LOGIN + "){"
                        + JSConst.CALLBACK_LOGIN + "('" + mGson.toJson(resultVO) + "');}";
                onCallback(js);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    public void logout(String[] params){
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_LOGOUT;
        mHandler.sendMessage(msg);
    }

    private void logoutMsg() {
        EMChatManager.getInstance().logout();
    }

    public void registerUser(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        UserInputVO inputVO=mGson.fromJson(params[0],new TypeToken<UserInputVO>(){}.getType());
        if (TextUtils.isEmpty(inputVO.getUsername())||TextUtils.isEmpty(inputVO.getPassword())){
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_REGISTER_User;
        Bundle bd = new Bundle();
        bd.putSerializable(BUNDLE_DATA,inputVO);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    public void registerUserMsg(final UserInputVO inputVO){
        new Thread(new Runnable() {
            public void run() {
                ResultVO resultVO=new ResultVO();
                try {
                    // 调用sdk注册方法
                    EMChatManager.getInstance().createAccountOnServer(inputVO.getUsername(), inputVO.getPassword());
                    resultVO.setResult("1");
                } catch (final EaseMobException e) {
                    //注册失败
                    resultVO.setResult("2");
                    int errorCode = e.getErrorCode();
                    if (errorCode == EMError.NONETWORK_ERROR) {
                        resultVO.setMsg("网络异常，请检查网络！");
                    } else if (errorCode == EMError.USER_ALREADY_EXISTS) {
                        resultVO.setMsg("用户已存在！");
                    } else if (errorCode == EMError.UNAUTHORIZED) {
                        resultVO.setMsg("注册失败，无权限！");
                    } else {
                        resultVO.setMsg("注册失败:" + e.getMessage());
                    }
                }finally {
                    String js = SCRIPT_HEADER + "if(" + JSConst.CALLBACK_REGISTER + "){"
                            + JSConst.CALLBACK_REGISTER + "('" + mGson.toJson(resultVO) + "');}";
                    onCallback(js);
                }
            }
        }).start();
    }


    public void onNewMessage(String result){
        String js = SCRIPT_HEADER + "if(" + JSConst.ON_NEW_MESSAGE + "){"
                + JSConst.ON_NEW_MESSAGE + "('" + result + "');}";
        onCallback(js);
    }

    public void onAckMessage(MessageVO messageVO){
        String js = SCRIPT_HEADER + "if(" + JSConst.ON_ACK_MESSAGE + "){"
                + JSConst.ON_ACK_MESSAGE + "('" + mGson.toJson(messageVO) + "');}";
        onCallback(js);
    }

    public void onDeliveryMessage(MessageVO messageVO){
        String js = SCRIPT_HEADER + "if(" + JSConst.ON_DELIVERY_MESSAGE + "){"
                + JSConst.ON_DELIVERY_MESSAGE + "('" + mGson.toJson(messageVO) + "');}";
        onCallback(js);
    }

    @Override
    public void onContactAdded(List<String> usernameList) {
        String js = SCRIPT_HEADER + "if(" + JSConst.ON_CONTACT_ADDED + "){"
                + JSConst.ON_CONTACT_ADDED + "('" + mGson.toJson(usernameList) + "');}";
        onCallback(js);
    }

    @Override
    public void onContactDeleted(List<String> usernameList) {
        String js = SCRIPT_HEADER + "if(" + JSConst.ON_CONTACT_DELETED + "){"
                + JSConst.ON_CONTACT_DELETED + "('" + mGson.toJson(usernameList) + "');}";
        onCallback(js);
    }

    @Override
    public void onContactInvited(GroupOptVO optVO) {
        String js = SCRIPT_HEADER + "if(" + JSConst.ON_CONTACT_INVITED + "){"
                + JSConst.ON_CONTACT_INVITED + "('" + mGson.toJson(optVO) + "');}";
        onCallback(js);
    }

    @Override
    public void onContactAgreed(GroupOptVO optVO) {
        String js = SCRIPT_HEADER + "if(" + JSConst.ON_CONTACT_AGREED + "){"
                + JSConst.ON_CONTACT_AGREED + "('" + mGson.toJson(optVO) + "');}";
        onCallback(js);
    }

    @Override
    public void onContactRefused(GroupOptVO optVO) {
        String js = SCRIPT_HEADER + "if(" + JSConst.ON_CONTACT_REFUSED + "){"
                + JSConst.ON_CONTACT_REFUSED + "('" + mGson.toJson(optVO) + "');}";
        onCallback(js);
    }

    @Override
    public void onConnected() {
        String js = SCRIPT_HEADER + "if(" + JSConst.ON_CONNECTED + "){"
                + JSConst.ON_CONNECTED + "();}";
        onCallback(js);
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
        onCallback(js);
    }

    @Override
    public void onInvitationAccpted(GroupOptVO groupOptVO) {
        String js = SCRIPT_HEADER + "if(" + JSConst.ON_INVITATION_ACCPTED + "){"
                + JSConst.ON_INVITATION_ACCPTED + "('" + mGson.toJson(groupOptVO) + "');}";
        onCallback(js);
    }

    @Override
    public void onInvitationDeclined(GroupOptVO groupOptVO) {
        String js = SCRIPT_HEADER + "if(" + JSConst.ON_INVITATION_DECLINED + "){"
                + JSConst.ON_INVITATION_DECLINED + "('" + mGson.toJson(groupOptVO) + "');}";
        onCallback(js);
    }

    @Override
    public void onUserRemoved(GroupOptVO groupOptVO) {
        String js = SCRIPT_HEADER + "if(" + JSConst.ON_USER_REMOVED + "){"
                + JSConst.ON_USER_REMOVED + "('" + mGson.toJson(groupOptVO) + "');}";
        onCallback(js);
    }

    @Override
    public void onGroupDestroy(GroupOptVO groupOptVO) {
        String js = SCRIPT_HEADER + "if(" + JSConst.ON_GROUP_DESTROY + "){"
                + JSConst.ON_GROUP_DESTROY + "('" + mGson.toJson(groupOptVO) + "');}";
        onCallback(js);
    }

    @Override
    public void onApplicationReceived(GroupOptVO groupOptVO) {
        String js = SCRIPT_HEADER + "if(" + JSConst.ON_APPLICATION_RECEIVED + "){"
                + JSConst.ON_APPLICATION_RECEIVED + "('" + mGson.toJson(groupOptVO) + "');}";
        onCallback(js);
    }

    @Override
    public void onApplicationAccept(GroupOptVO groupOptVO) {
        String js = SCRIPT_HEADER + "if(" + JSConst.ON_APPLICATION_ACCEPT + "){"
                + JSConst.ON_APPLICATION_ACCEPT + "('" + mGson.toJson(groupOptVO) + "');}";
        onCallback(js);
    }

    @Override
    public void onInvitationReceived(GroupOptVO groupOptVO) {
        String js = SCRIPT_HEADER + "if(" + JSConst.ON_INVITATION_RECEIVED + "){"
                + JSConst.ON_INVITATION_RECEIVED + "('" + mGson.toJson(groupOptVO) + "');}";
        onCallback(js);
    }

    @Override
    public void onApplicationDeclined(GroupOptVO groupOptVO) {
        String js = SCRIPT_HEADER + "if(" + JSConst.ON_APPLICATION_DECLINED + "){"
                + JSConst.ON_APPLICATION_DECLINED + "('" + mGson.toJson(groupOptVO) + "');}";
        onCallback(js);
    }

    @Override
    public void onCallReceive(CallReceiveOutputVO outputVO) {
        String js = SCRIPT_HEADER + "if(" + JSConst.ON_CALLRECEIVE + "){"
                + JSConst.ON_CALLRECEIVE + "('" + mGson.toJson(outputVO) + "');}";
        onCallback(js);
    }

    @Override
    public void onCallStateChanged(CallStateOutputVO outputVO) {
        String js = SCRIPT_HEADER + "if(" + JSConst.ON_CALLSTATECHANGED + "){"
                + JSConst.ON_CALLSTATECHANGED + "('" + mGson.toJson(outputVO) + "');}";
        onCallback(js);
    }

    @Override
    public void onCmdMessageReceive(CmdMsgOutputVO outputVO) {
        String js = SCRIPT_HEADER + "if(" + JSConst.ON_CMDMESSAGERECEIVE + "){"
                + JSConst.ON_CMDMESSAGERECEIVE + "('" + mGson.toJson(outputVO) + "');}";
        onCallback(js);
    }

    public void getMessageById(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        MessageVO inputVO=mGson.fromJson(params[0],new TypeToken<MessageVO>(){}.getType());
        if (TextUtils.isEmpty(inputVO.getMsgId())){
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_GETMESSAGEBYID;
        Bundle bd = new Bundle();
        bd.putSerializable(BUNDLE_DATA,inputVO);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    public void getMessageByIdMsg(MessageVO messageVO){
        EMMessage message=EMChatManager.getInstance().getMessage(messageVO.getMsgId());
        String js = SCRIPT_HEADER + "if(" + JSConst.CALLBACK_GETMESSAGEBYID + "){"
                + JSConst.CALLBACK_GETMESSAGEBYID + "('" + mGson.toJson(message) + "');}";
        onCallback(js);
    }

    public void getConversationByName(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        MessageVO inputVO=mGson.fromJson(params[0], new TypeToken<MessageVO>() {
        }.getType());
        if (TextUtils.isEmpty(inputVO.getUsername())){
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_GET_CONVERSATION_BY_NAME;
        Bundle bd = new Bundle();
        bd.putSerializable(BUNDLE_DATA,inputVO);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    public void getConversationByNameMSG(MessageVO messageVO){
        EMConversation conversation=EMChatManager.getInstance().getConversation(messageVO.getUsername());
        ConversationResultVO resultVO=new ConversationResultVO();
        resultVO.setIsGroup(conversation.getIsGroup() ? "1" : "0");
        resultVO.setChatter(conversation.getUserName());
        List<EMMessage> emMessages=conversation.getAllMessages();
        List<MsgResultVO> msgResultVOs=new ArrayList<MsgResultVO>();
        if (emMessages!=null){
            for (EMMessage emMessage : emMessages) {
                msgResultVOs.add(ListenersRegister.convertEMMessage(emMessage));
            }
        }
        resultVO.setMessages(msgResultVOs);
        String js = SCRIPT_HEADER + "if(" + JSConst.CALLBACK_GETCONVERSATIONBYNAME + "){"
                + JSConst.CALLBACK_GETCONVERSATIONBYNAME + "('" + mGson.toJson(resultVO) + "');}";
        onCallback(js);
    }

    public void sendText(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        SendInputVO inputVO=mGson.fromJson(params[0], new TypeToken<SendInputVO>() {
        }.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
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
        EMConversation conversation = EMChatManager.getInstance().getConversation(sendInputVO.getUsername());
        //创建一条文本消息
        EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
        //如果是群聊，设置chattype,默认是单聊
        if (String.valueOf(2).equals(sendInputVO.getChatType())) {
            message.setChatType(EMMessage.ChatType.GroupChat);
        }
        //设置消息body
        TextMessageBody txtBody = new TextMessageBody(sendInputVO.getContent());
        message.addBody(txtBody);
        //设置接收人
        message.setReceipt(sendInputVO.getUsername());
        //把消息加入到此会话对象中
        conversation.addMessage(message);
      //发送消息
        EMChatManager.getInstance().sendMessage(message, new EMCallBack() {

            @Override
            public void onError(int arg0, String arg1) {
            }

            @Override
            public void onProgress(int arg0, String arg1) {

            }

            @Override
            public void onSuccess() {

            }
        });
    }

    public void sendVoice(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        SendInputVO inputVO=mGson.fromJson(params[0], new TypeToken<SendInputVO>() {
        }.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
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
        EMConversation conversation = EMChatManager.getInstance().getConversation(sendInputVO.getUsername());
        //创建一条文本消息
        EMMessage message = EMMessage.createSendMessage(EMMessage.Type.VOICE);
        //如果是群聊，设置chattype,默认是单聊
        if (String.valueOf(2).equals(sendInputVO.getChatType())) {
            message.setChatType(EMMessage.ChatType.GroupChat);
        }
        //设置消息body
        VoiceMessageBody body = new VoiceMessageBody(new File(sendInputVO.getFilePath()), Integer.valueOf(sendInputVO.getLength()));
        message.addBody(body);
        message.setReceipt(sendInputVO.getUsername());
        conversation.addMessage(message);
        EMChatManager.getInstance().sendMessage(message, new EMCallBack() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    public void sendPicture(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        SendInputVO inputVO=mGson.fromJson(params[0], new TypeToken<SendInputVO>() {
        }.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
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
        EMConversation conversation = EMChatManager.getInstance().getConversation(sendInputVO.getUsername());
        EMMessage message = EMMessage.createSendMessage(EMMessage.Type.IMAGE);
        //如果是群聊，设置chattype,默认是单聊
        if (String.valueOf(2).equals(sendInputVO.getChatType())) {
            message.setChatType(EMMessage.ChatType.GroupChat);
        }
        ImageMessageBody body = new ImageMessageBody(new File(sendInputVO.getFilePath()));
        // 默认超过100k的图片会压缩后发给对方，可以设置成发送原图
        // body.setSendOriginalImage(true);
        message.addBody(body);
        message.setReceipt(sendInputVO.getUsername());
        conversation.addMessage(message);
        EMChatManager.getInstance().sendMessage(message, new EMCallBack() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    public void sendLocationMsg(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        SendInputVO inputVO=mGson.fromJson(params[0], new TypeToken<SendInputVO>() {
        }.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
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
        EMConversation conversation = EMChatManager.getInstance().getConversation(sendInputVO.getUsername());
        EMMessage message = EMMessage.createSendMessage(EMMessage.Type.LOCATION);
//如果是群聊，设置chattype,默认是单聊
        if (String.valueOf(2).equals(sendInputVO.getChatType())) {
            message.setChatType(EMMessage.ChatType.GroupChat);
        }
        LocationMessageBody locBody = new LocationMessageBody(sendInputVO.getLocationAddress(), Double.valueOf(sendInputVO.getLatitude()), Double.valueOf(sendInputVO.getLongitude()));
        message.addBody(locBody);
        message.setReceipt(sendInputVO.getUsername());
        conversation.addMessage(message);
        EMChatManager.getInstance().sendMessage(message, new EMCallBack() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    public void sendFile(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        SendInputVO inputVO=mGson.fromJson(params[0], new TypeToken<SendInputVO>() {
        }.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
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
        EMConversation conversation = EMChatManager.getInstance().getConversation(sendInputVO.getUsername());
// 创建一个文件消息
        EMMessage message = EMMessage.createSendMessage(EMMessage.Type.FILE);
// 如果是群聊，设置chattype,默认是单聊
        if (String.valueOf(2).equals(sendInputVO.getChatType())) {
            message.setChatType(EMMessage.ChatType.GroupChat);
        }
//设置接收人的username
        message.setReceipt(sendInputVO.getUsername());
// add message body
        NormalFileMessageBody body = new NormalFileMessageBody(new File(sendInputVO.getFilePath()));
        message.addBody(body);
        conversation.addMessage(message);
        EMChatManager.getInstance().sendMessage(message, new EMCallBack() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    public void getMessageHistory(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        HistoryInputVO inputVO=mGson.fromJson(params[0], new TypeToken<HistoryInputVO>() {
        }.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_GET_MESSAGE_HISTORY;
        Bundle bd = new Bundle();
        bd.putSerializable(BUNDLE_DATA, inputVO);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void getMessageHistoryMsg(HistoryInputVO inputVO) {
        EMConversation conversation = EMChatManager.getInstance().getConversation(inputVO.getUsername());
//获取此会话的所有消息
        List<EMMessage> messages;
        if (String.valueOf("0").equals(inputVO.getPagesize())||TextUtils.isEmpty(inputVO.getPagesize())){
            messages = conversation.getAllMessages();
        }else{
            if ("2".equals(inputVO.getChatType())){
                //是群聊
                messages=conversation.loadMoreGroupMsgFromDB(inputVO.getStartMsgId(), Integer.parseInt(inputVO.getPagesize()));
            }else{
                messages=conversation.loadMoreMsgFromDB(inputVO.getStartMsgId(), Integer.parseInt(inputVO.getPagesize()));
            }
        }
        List<MsgResultVO> resultVOs=new ArrayList<MsgResultVO>();
        if (messages!=null){
            for (EMMessage emMessage : messages) {
                resultVOs.add(ListenersRegister.convertEMMessage(emMessage));
            }
        }
        String js = SCRIPT_HEADER + "if(" + JSConst.CALLBACK_GET_MESSAGE_HISTORY + "){"
                + JSConst.CALLBACK_GET_MESSAGE_HISTORY + "('" + mGson.toJson(resultVOs) + "');}";
        onCallback(js);
    }

    public void getUnreadMsgCount(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        HistoryInputVO inputVO=mGson.fromJson(params[0], new TypeToken<HistoryInputVO>() {
        }.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_GET_UNREAD_MSG_COUNT;
        Bundle bd = new Bundle();
        bd.putSerializable(BUNDLE_DATA,inputVO);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void getUnreadMsgCountMsg(HistoryInputVO inputVO) {
        EMConversation conversation = EMChatManager.getInstance().getConversation(inputVO.getUsername());
        int result=conversation.getUnreadMsgCount();
        JSONObject jsonResult=new JSONObject();
        try {
            jsonResult.put("count",result);
        } catch (JSONException e) {

        }
        String js = SCRIPT_HEADER + "if(" + JSConst.CALLBACK_GET_UNREAD_MSG_COUNT + "){"
                + JSConst.CALLBACK_GET_UNREAD_MSG_COUNT + "('" + jsonResult.toString() + "');}";
        onCallback(js);
    }

    public void resetUnreadMsgCount(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        HistoryInputVO inputVO=mGson.fromJson(params[0],new TypeToken<HistoryInputVO>(){}.getType());
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

    private void resetUnreadMsgCountMsg(HistoryInputVO inputVO) {
        EMConversation conversation = EMChatManager.getInstance().getConversation(inputVO.getUsername());
        conversation.resetUnreadMsgCount();
    }

    public void resetAllUnreadMsgCount(String[] params){
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_RESET_ALL_UNREAD_MSG_COUNT;
        mHandler.sendMessage(msg);
    }

    private void resetAllUnreadMsgCountMsg() {
        EMChatManager.getInstance().resetAllUnreadMsgCount();
    }

    public void getMsgCount(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        HistoryInputVO inputVO=mGson.fromJson(params[0],new TypeToken<HistoryInputVO>(){}.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_GET_MSG_COUNT;
        Bundle bd = new Bundle();
        bd.putSerializable(BUNDLE_DATA,inputVO);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void getMsgCountMsg(HistoryInputVO inputVO) {
        EMConversation conversation = EMChatManager.getInstance().getConversation(inputVO.getUsername());
        int msgCount=conversation.getMsgCount();
        JSONObject jsonResult=new JSONObject();
        try {
            jsonResult.put("msgCount",msgCount);
        } catch (JSONException e) {

        }
        String js = SCRIPT_HEADER + "if(" + JSConst.CALLBACK_GET_MSG_COUNT+ "){"
                + JSConst.CALLBACK_GET_MSG_COUNT + "('" + jsonResult.toString() + "');}";
        onCallback(js);
    }

    public void clearConversation(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        HistoryInputVO inputVO=mGson.fromJson(params[0],new TypeToken<HistoryInputVO>(){}.getType());
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
        EMChatManager.getInstance().clearConversation(inputVO.getUsername());
    }

    public void deleteConversation(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        HistoryInputVO inputVO=mGson.fromJson(params[0],new TypeToken<HistoryInputVO>(){}.getType());
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
        EMChatManager.getInstance().deleteConversation(inputVO.getUsername());
    }

    public void removeMessage(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        HistoryInputVO inputVO=mGson.fromJson(params[0],new TypeToken<HistoryInputVO>(){}.getType());
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
        EMConversation conversation = EMChatManager.getInstance().getConversation(inputVO.getUsername());
        conversation.removeMessage(inputVO.getMsgId());
    }

    public void deleteAllConversation(String[] params){
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_DELETE_ALL_CONVERSATION;
        mHandler.sendMessage(msg);
    }

    private void deleteAllConversationMsg() {
        //删除所有会话记录(包括本地)
        EMChatManager.getInstance().deleteAllConversation();
    }

    public void setNotifyBySoundAndVibrate(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        NotifySettingVO inputVO=mGson.fromJson(params[0], new TypeToken<NotifySettingVO>() {
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
        EMChatOptions chatOptions=EMChatManager.getInstance().getChatOptions();
        //setting里面字段不传时值为null
        chatOptions.setNotifyBySoundAndVibrate(!"0".equals(settingVO.getEnable())); //默认为true 开启新消息提醒
        chatOptions.setNoticeBySound(!"0".equals(settingVO.getSoundEnable())); //默认为true 开启声音提醒
        chatOptions.setNoticedByVibrate(!"0".equals(settingVO.getVibrateEnable())); //默认为true 开启震动提醒
        chatOptions.setUseSpeaker(!"0".equals(settingVO.getUserSpeaker())); //默认为true 开启扬声器播放
        chatOptions.setShowNotificationInBackgroud(!"0".equals(settingVO.getShowNotificationInBackgroud())); //默认为true
        chatOptions.setAcceptInvitationAlways(!"0".equals(settingVO.getAcceptInvitationAlways()));
    }

    public void getContactUserNames(String[] params){
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_GET_CONTACT_USER_NAMES;
        mHandler.sendMessage(msg);
    }

    private void getContactUserNamesMsg() {
        List<String> usernames = null;
        try {
            usernames = EMContactManager.getInstance().getContactUserNames();
        } catch (EaseMobException e) {

        }
        String js = SCRIPT_HEADER + "if(" + JSConst.CALLBACK_GET_CONTACT_USERNAMES+ "){"
                + JSConst.CALLBACK_GET_CONTACT_USERNAMES + "('" + mGson.toJson(usernames) + "');}";
        onCallback(js);
    }

    public void addContact(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        AddContactInputVO inputVO=mGson.fromJson(params[0],new TypeToken<AddContactInputVO>(){}.getType());
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

    private void addContactMsg(AddContactInputVO inputVO) {
        //参数为要添加的好友的username和添加理由
        try {
            EMContactManager.getInstance().addContact(inputVO.getToAddUsername(), inputVO.getReason());//需异步处理
        } catch (EaseMobException e) {

        }
    }

    public void deleteContact(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        UserInputVO inputVO=mGson.fromJson(params[0],new TypeToken<UserInputVO>(){}.getType());
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

    private void deleteContactMsg(UserInputVO inputVO) {
        try {
            EMContactManager.getInstance().deleteContact(inputVO.getUsername());//需异步处理
        } catch (EaseMobException e) {

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
        UserInputVO inputVO=mGson.fromJson(params[0],new TypeToken<UserInputVO>(){}.getType());
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

    private void acceptInvitationMsg(UserInputVO inputVO) {
        try {
            EMChatManager.getInstance().acceptInvitation(inputVO.getUsername());//需异步处理
             } catch (EaseMobException e) {

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
        UserInputVO inputVO=mGson.fromJson(params[0],new TypeToken<UserInputVO>(){}.getType());
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

    private void refuseInvitationMsg(UserInputVO inputVO) {
        try {
            EMChatManager.getInstance().refuseInvitation(inputVO.getUsername());//需异步处理
        } catch (EaseMobException e) {

        }
    }

    public void getBlackListUsernames(String[] params){
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_GET_BLACKLIST_USERNAMES;
        mHandler.sendMessage(msg);
    }

    private void getBlackListUsernamesMsg() {
        //获取黑名单用户的usernames
        List<String> usernames=EMContactManager.getInstance().getBlackListUsernames();
        String js = SCRIPT_HEADER + "if(" + JSConst.CALLBACK_GET_BLACKLIST_USERNAMES+ "){"
                + JSConst.CALLBACK_GET_BLACKLIST_USERNAMES + "('" + mGson.toJson(usernames) + "');}";
        onCallback(js);
    }

    public void addUserToBlackList(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        UserInputVO inputVO=mGson.fromJson(params[0],new TypeToken<UserInputVO>(){}.getType());
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
            EMContactManager.getInstance().addUserToBlackList(inputVO.getUsername(),true);//需异步处理
        } catch (EaseMobException e) {

        }
    }

    public void deleteUserFromBlackList(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        UserInputVO inputVO=mGson.fromJson(params[0],new TypeToken<UserInputVO>(){}.getType());
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

    private void deleteUserFromBlackListMsg(UserInputVO inputVO) {
        //第二个参数如果为true，则把用户加入到黑名单后双方发消息时对方都收不到；false,则
//我能给黑名单的中用户发消息，但是对方发给我时我是收不到的
        try {
            EMContactManager.getInstance().deleteUserFromBlackList(inputVO.getUsername());//需异步处理
        } catch (EaseMobException e) {

        }
    }

    public void createPrivateGroup(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        CreateGroupInputVO inputVO=mGson.fromJson(params[0],new TypeToken<CreateGroupInputVO>(){}.getType());
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
       if (!TextUtils.isEmpty(inputVO.getMaxUsers())){
           //前一种方法创建的群聊默认最大群聊用户数为200，传入maxUsers后设置自定义的最大用户数，最大为2000
           try {
               EMGroupManager.getInstance().createPrivateGroup(inputVO.getGroupName(), inputVO.getDesc(),
                       inputVO.getMembers(),Boolean.valueOf(inputVO.getAllowInvite()), Integer.parseInt(inputVO.getMaxUsers()));//需异步处理
           } catch (EaseMobException e) {

           }
       }else{
           try {
               EMGroupManager.getInstance().createPrivateGroup(inputVO.getGroupName(), inputVO.getDesc(),
                       inputVO.getMembers(),Boolean.valueOf(inputVO.getAllowInvite()));//需异步处理
           } catch (EaseMobException e) {

           }
       }
    }

    public void createPublicGroup(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        CreateGroupInputVO inputVO=mGson.fromJson(params[0],new TypeToken<CreateGroupInputVO>(){}.getType());
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
        if (!TextUtils.isEmpty(inputVO.getMaxUsers())){
            //前一种方法创建的群聊默认最大群聊用户数为200，传入maxUsers后设置自定义的最大用户数，最大为2000
            try {
                EMGroupManager.getInstance().createPublicGroup(inputVO.getGroupName(),
                        inputVO.getDesc(), inputVO.getMembers(), Boolean.valueOf(inputVO.getNeedApprovalRequired()),
                        Integer.valueOf(inputVO.getMaxUsers()));//需异步处理
            } catch (EaseMobException e) {

            }
        }else{
            try {
                EMGroupManager.getInstance().createPublicGroup(inputVO.getGroupName(),
                        inputVO.getDesc(), inputVO.getMembers(), Boolean.valueOf(inputVO.getNeedApprovalRequired()));//需异步处理
            } catch (EaseMobException e) {

            }
        }
    }

    public void addUsersToGroup(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        GroupInfoVO inputVO=mGson.fromJson(params[0],new TypeToken<GroupInfoVO>(){}.getType());
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

                EMGroupManager.getInstance().addUsersToGroup(inputVO.getGroupId(), inputVO.getNewmembers());//需异步处理
            }else{
                //私有群里，如果开放了群成员邀请，群成员邀请调用下面方法
                EMGroupManager.getInstance().inviteUser(inputVO.getGroupId(), inputVO.getNewmembers(), null);//需异步处理
            }
        } catch (EaseMobException e) {

        }
    }

    public void removeUserFromGroup(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        GroupInfoVO inputVO=mGson.fromJson(params[0],new TypeToken<GroupInfoVO>(){}.getType());
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
            EMGroupManager.getInstance().removeUserFromGroup(inputVO.getGroupId(), inputVO.getUsername());//需异步处理
        } catch (EaseMobException e) {

        }
    }

    public void joinGroup(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        GroupInfoVO inputVO=mGson.fromJson(params[0],new TypeToken<GroupInfoVO>(){}.getType());
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

                EMGroupManager.getInstance().joinGroup(inputVO.getGroupId());//需异步处理
            }else{
                EMGroupManager.getInstance().applyJoinToGroup(inputVO.getGroupId(),inputVO.getReason());//需异步处理
            }
        } catch (EaseMobException e) {

        }
    }

    public void exitFromGroup(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        GroupInfoVO inputVO=mGson.fromJson(params[0],new TypeToken<GroupInfoVO>(){}.getType());
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
            EMGroupManager.getInstance().exitFromGroup(inputVO.getGroupId());
        } catch (EaseMobException e) {

        }
    }

    public void exitAndDeleteGroup(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        GroupInfoVO inputVO=mGson.fromJson(params[0],new TypeToken<GroupInfoVO>(){}.getType());
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
            EMGroupManager.getInstance().exitAndDeleteGroup(inputVO.getGroupId());
        } catch (EaseMobException e) {

        }
    }

    public void getGroupsFromServer(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        GroupInfoVO inputVO=mGson.fromJson(params[0],new TypeToken<GroupInfoVO>(){}.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_GETGROUPSFROMSERVER;
        Bundle bd = new Bundle();
        bd.putSerializable(BUNDLE_DATA,inputVO);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void getGroupsFromServerMsg(GroupInfoVO infoVO) {
        final GroupsOutputVO outputVO=new GroupsOutputVO();
        List<EMGroup> grouplist = null;
        if (Boolean.valueOf(infoVO.getLoadCache())){
            grouplist = EMGroupManager.getInstance().getAllGroups();
            if (grouplist==null){
                outputVO.setResult("1");
                outputVO.setErrorMsg("");
            }else {
                outputVO.setResult("0");
                outputVO.setGrouplist(mGson.toJson(grouplist));
            }
            String js = SCRIPT_HEADER + "if(" + JSConst.CALLBACK_GETGROUPSFROMSERVER + "){"
                    + JSConst.CALLBACK_GETGROUPSFROMSERVER + "('" + mGson.toJson(outputVO) + "');}";
            onCallback(js);
            return;
        }

        EMGroupManager.getInstance().asyncGetGroupsFromServer(new EMValueCallBack<List<EMGroup>>() {

            @Override
            public void onSuccess(List<EMGroup> value) {
                outputVO.setResult("0");
                outputVO.setGrouplist(mGson.toJson(value));
                String js = SCRIPT_HEADER + "if(" + JSConst.CALLBACK_GETGROUPSFROMSERVER + "){"
                        + JSConst.CALLBACK_GETGROUPSFROMSERVER + "('" + mGson.toJson(outputVO) + "');}";
                onCallback(js);
            }

            @Override
            public void onError(int error, String errorMsg) {
                outputVO.setResult("1");
                outputVO.setErrorMsg(errorMsg);
                String js = SCRIPT_HEADER + "if(" + JSConst.CALLBACK_GETGROUPSFROMSERVER + "){"
                        + JSConst.CALLBACK_GETGROUPSFROMSERVER + "('" + mGson.toJson(outputVO) + "');}";
                onCallback(js);
            }
        });


    }

    public void getAllPublicGroupsFromServer(String[] params){
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_GETALLPUBLICGROUPSFROMSERVER;
        mHandler.sendMessage(msg);
    }

    private void getAllPublicGroupsFromServerMsg() {
        EMGroupManager.getInstance().asyncGetAllPublicGroupsFromServer(new EMValueCallBack<List<EMGroupInfo>>() {

            @Override
            public void onSuccess(List<EMGroupInfo> value) {
                GroupsOutputVO outputVO = new GroupsOutputVO();
                outputVO.setGrouplist(mGson.toJson(value));
                outputVO.setResult("0");
                String js = SCRIPT_HEADER + "if(" + JSConst.CALLBACK_GETALLPUBLICGROUPSFROMSERVER + "){"
                        + JSConst.CALLBACK_GETALLPUBLICGROUPSFROMSERVER + "('" + mGson.toJson(outputVO) + "');}";
                onCallback(js);
            }

            @Override
            public void onError(int error, String errorMsg) {
                GroupsOutputVO outputVO = new GroupsOutputVO();
                outputVO.setResult("1");
                outputVO.setErrorMsg(errorMsg);
                String js = SCRIPT_HEADER + "if(" + JSConst.CALLBACK_GETALLPUBLICGROUPSFROMSERVER + "){"
                        + JSConst.CALLBACK_GETALLPUBLICGROUPSFROMSERVER + "('" + mGson.toJson(outputVO) + "');}";
                onCallback(js);
            }
        });
    }

    public void getGroup(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        GroupInfoVO inputVO=mGson.fromJson(params[0],new TypeToken<GroupInfoVO>(){}.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_GETGROUP;
        Bundle bd = new Bundle();
        bd.putSerializable(BUNDLE_DATA,inputVO);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void getGroupMsg(GroupInfoVO infoVO) {
        EMGroup group = null;
        if (Boolean.valueOf(infoVO.getLoadCache())){
            group = EMGroupManager.getInstance().getGroup(infoVO.getGroupId());
        }else{
            try {
                group =EMGroupManager.getInstance().getGroupFromServer(infoVO.getGroupId());
                EMGroupManager.getInstance().createOrUpdateLocalGroup(group);
            } catch (EaseMobException e) {

            }
        }
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
            resultVO.setGroupMaxUserCount(group.getMaxUsers());
        }
        String js = SCRIPT_HEADER + "if(" + JSConst.CALLBACK_GETGROUP + "){"
                + JSConst.CALLBACK_GETGROUP + "('" + mGson.toJson(resultVO) + "');}";
        onCallback(js);
    }

    public void blockGroupMessage(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        GroupInfoVO inputVO=mGson.fromJson(params[0],new TypeToken<GroupInfoVO>(){}.getType());
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
            EMGroupManager.getInstance().blockGroupMessage(infoVO.getGroupId());//需异步处理
        } catch (EaseMobException e) {

        }
    }


    public void unblockGroupMessage(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        GroupInfoVO inputVO=mGson.fromJson(params[0],new TypeToken<GroupInfoVO>(){}.getType());
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
            EMGroupManager.getInstance().unblockGroupMessage(infoVO.getGroupId());//需异步处理
        } catch (EaseMobException e) {

        }
    }

    public void changeGroupName(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        GroupInfoVO inputVO=mGson.fromJson(params[0],new TypeToken<GroupInfoVO>(){}.getType());
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
            EMGroupManager.getInstance().changeGroupName(infoVO.getGroupId(), infoVO.getChangedGroupName());//需异步处理
        } catch (EaseMobException e) {

        }
    }


    public void setReceiveNotNoifyGroup(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        GroupInfoVO inputVO=mGson.fromJson(params[0],new TypeToken<GroupInfoVO>(){}.getType());
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
        EMChatManager.getInstance().getChatOptions().setReceiveNotNoifyGroup(infoVO.getGroupIds());
    }

    public void blockUser(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        GroupInfoVO inputVO=mGson.fromJson(params[0],new TypeToken<GroupInfoVO>(){}.getType());
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
            EMGroupManager.getInstance().blockUser(infoVO.getGroupId(), infoVO.getUsername());//需异步处理    }
        } catch (EaseMobException e) {

        }
    }

    public void unblockUser(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        GroupInfoVO inputVO=mGson.fromJson(params[0],new TypeToken<GroupInfoVO>(){}.getType());
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
            EMGroupManager.getInstance().unblockUser(infoVO.getGroupId(), infoVO.getUsername());
        } catch (EaseMobException e) {

        }
    }

    public void getBlockedUsers(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        GroupInfoVO inputVO=mGson.fromJson(params[0],new TypeToken<GroupInfoVO>(){}.getType());
        if (inputVO==null){
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_GETBLOCKEDUSERS;
        Bundle bd = new Bundle();
        bd.putSerializable(BUNDLE_DATA,inputVO);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void getBlockedUsersMsg(GroupInfoVO infoVO) {
        try {
            List<String> ususernames=EMGroupManager.getInstance().getBlockedUsers(infoVO.getGroupId());
            String js = SCRIPT_HEADER + "if(" + JSConst.CALLBACK_GETBLOCKEDUSERS + "){"
                    + JSConst.CALLBACK_GETBLOCKEDUSERS + "('" + mGson.toJson(ususernames) + "');}";
            onCallback(js);
        } catch (EaseMobException e) {

        }

    }

    public void importMessage(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        ImportMsgInputVO inputVO=mGson.fromJson(params[0],new TypeToken<ImportMsgInputVO>(){}.getType());
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
            TextMessageBody body = new TextMessageBody("send text msg " + System.currentTimeMillis());
            msg.addBody(body);
            msg.setTo(inputVO.getTo());
            msg.setFrom(inputVO.getFrom());
            msg.setMsgTime(System.currentTimeMillis());
        }else{
            //接收消息
            msg = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
            TextMessageBody body = new TextMessageBody("receive text msg " + System.currentTimeMillis());
            msg.addBody(body);
            msg.setFrom(inputVO.getFrom());
            msg.setTo(inputVO.getTo());
            msg.setMsgTime(System.currentTimeMillis());
        }
        if ("2".equals(inputVO.getChatType())){
            msg.setChatType(EMMessage.ChatType.GroupChat);
        }
        EMChatManager.getInstance().importMessage(msg, false);
    }

    public void makeVoiceCall(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        UserInputVO inputVO=mGson.fromJson(params[0],new TypeToken<UserInputVO>(){}.getType());
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
            EMChatManager.getInstance().makeVoiceCall(inputVO.getUsername());
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
            EMChatManager.getInstance().answerCall();
        } catch (EMNoActiveCallException e) {
        } catch (EMNetworkUnconnectedException e) {
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
            EMChatManager.getInstance().rejectCall();
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
        EMChatManager.getInstance().endCall();
    }

    public void sendCmdMessage(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        CmdMsgInputVO inputVO=mGson.fromJson(params[0],new TypeToken<CmdMsgInputVO>(){}.getType());
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
        EMMessage cmdMsg = EMMessage.createSendMessage(EMMessage.Type.CMD);

        //支持单聊和群聊，默认单聊，如果是群聊添加下面这行
        if ("2".equals(inputVO.getChatType())) {
            cmdMsg.setChatType(EMMessage.ChatType.GroupChat);
        }
        String action=inputVO.getAction();//action可以自定义，在广播接收时可以收到
        CmdMessageBody cmdBody=new CmdMessageBody(action);
        cmdMsg.setReceipt(inputVO.getToUsername());
        cmdMsg.addBody(cmdBody);
        EMChatManager.getInstance().sendMessage(cmdMsg, new EMCallBack() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    public void updateCurrentUserNick(String[] params){
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        NicknameVO inputVO=mGson.fromJson(params[0],new TypeToken<NicknameVO>(){}.getType());
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

    private void updateCurrentUserNickMsg(NicknameVO infoVO) {
        EMChatManager.getInstance().updateCurrentUserNick(infoVO.getNickname());
    }

    public void getChatterInfo(String[] params){
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_GET_CHATTER_INFO;
        mHandler.sendMessage(msg);
    }

    public void getChatterInfoMsg(){
        List<String> usernames = new ArrayList<String>();
        try {
            List<String> tempList = EMContactManager.getInstance().getContactUserNames();
            if (tempList!=null){
                usernames.addAll(tempList);
            }
        } catch (EaseMobException e) {
        }
        final List<ChatterInfoVO> chatterInfoVOs=new ArrayList<ChatterInfoVO>();
        if (usernames!=null&&usernames.size()>0){
            for (String username:usernames){
                ChatterInfoVO infoVO=new ChatterInfoVO();
                EMConversation conversation=EMChatManager.getInstance().getConversation(username);
                infoVO.setChatter(username);
                infoVO.setIsGroup("2");
                if (conversation.getLastMessage()!=null) {
                    infoVO.setLastMsg(ListenersRegister.convertEMMessage(conversation.getLastMessage()));
                }
                infoVO.setUnreadMsgCount(String.valueOf(conversation.getUnreadMsgCount()));
                chatterInfoVOs.add(infoVO);
            }
        }

        EMGroupManager.getInstance().asyncGetGroupsFromServer(new EMValueCallBack<List<EMGroup>>() {

            @Override
            public void onSuccess(List<EMGroup> value) {
                for (EMGroup emGroup : value) {
                    ChatterInfoVO infoVO = new ChatterInfoVO();
                    String groupName = emGroup.getGroupName();
                    infoVO.setIsGroup("1");
                    infoVO.setGroupName(emGroup.getNick());
                    EMConversation conversation = EMChatManager.getInstance().getConversation(emGroup.getUsername());
                    infoVO.setUnreadMsgCount(String.valueOf(conversation.getUnreadMsgCount()));
                    if (conversation.getLastMessage()!=null) {
                        infoVO.setLastMsg(ListenersRegister.convertEMMessage(conversation.getLastMessage()));
                    }
                    infoVO.setChatter(emGroup.getUsername());
                    chatterInfoVOs.add(infoVO);
                }
                String js = SCRIPT_HEADER + "if(" + JSConst.CALLBACK_GET_CHATTER_INFO + "){"
                        + JSConst.CALLBACK_GET_CHATTER_INFO + "('" + mGson.toJson(chatterInfoVOs) + "');}";
                onCallback(js);
            }

            @Override
            public void onError(int error, String errorMsg) {
                String js = SCRIPT_HEADER + "if(" + JSConst.CALLBACK_GET_CHATTER_INFO + "){"
                        + JSConst.CALLBACK_GET_CHATTER_INFO + "('" + mGson.toJson(chatterInfoVOs) + "');}";
                onCallback(js);
            }
        });


    }


    @Override
    public void onHandleMessage(Message message) {
        if(message == null){
            return;
        }
        Bundle bundle=message.getData();
        switch (message.what) {
            case MSG_LOGIN:
                loginMsg((UserInputVO)bundle.getSerializable(BUNDLE_DATA));
                break;
            case MSG_LOGOUT:
                logoutMsg();
                break;
            case MSG_REGISTER_User:
                registerUserMsg((UserInputVO) bundle.getSerializable(BUNDLE_DATA));
                break;
            case MSG_GETMESSAGEBYID:
                getMessageByIdMsg((MessageVO) bundle.getSerializable(BUNDLE_DATA));
                break;
            case MSG_GET_CONVERSATION_BY_NAME:
                getConversationByNameMSG((MessageVO) bundle.getSerializable(BUNDLE_DATA));
                break;
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
            case MSG_GET_MESSAGE_HISTORY:
                getMessageHistoryMsg((HistoryInputVO) bundle.getSerializable(BUNDLE_DATA));
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
            case MSG_GET_MSG_COUNT:
                getMsgCountMsg((HistoryInputVO) bundle.getSerializable(BUNDLE_DATA));
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
            case MSG_GET_CONTACT_USER_NAMES:
                getContactUserNamesMsg();
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
            case MSG_GET_BLACKLIST_USERNAMES:
                getBlackListUsernamesMsg();
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
            case MSG_GETGROUPSFROMSERVER:
                getGroupsFromServerMsg((GroupInfoVO) bundle.getSerializable(BUNDLE_DATA));
                break;
            case MSG_GETALLPUBLICGROUPSFROMSERVER:
                getAllPublicGroupsFromServerMsg();
                break;
            case MSG_GETGROUP:
                getGroupMsg((GroupInfoVO) bundle.getSerializable(BUNDLE_DATA));
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
            case MSG_GETBLOCKEDUSERS:
                getBlockedUsersMsg((GroupInfoVO) bundle.getSerializable(BUNDLE_DATA));
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
            case MSG_GET_CHATTER_INFO:
                getChatterInfoMsg();
                break;
            case MSG_INIT:
                initEasemobMsg(bundle.getStringArray(BUNDLE_DATA));
                break;
            default:
                super.onHandleMessage(message);
        }
     }


}
