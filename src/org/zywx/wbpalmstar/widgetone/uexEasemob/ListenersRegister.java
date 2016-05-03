package org.zywx.wbpalmstar.widgetone.uexEasemob;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMError;
import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMCallStateChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMFileMessageBody;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMLocationMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chat.EMVideoMessageBody;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.NetUtils;

import org.zywx.wbpalmstar.widgetone.uexEasemob.model.HXSDKModel;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.output.CallReceiveOutputVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.output.CallStateOutputVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.output.CmdMsgOutputVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.output.GroupOptVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.output.MessageVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.output.MsgBodyVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.output.MsgResultVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by ylt on 15/3/13.
 */
public class ListenersRegister {

    private Context mContext;

    private ListenersCallback callback;

    private Gson mGson;

    public static HXSDKHelper hxsdkHelper=new HXSDKHelper() {
        @Override
        protected HXSDKModel createModel() {
            return null;
        }
    };

    public void registerListeners(Context context, EMOptions options, Gson gson){
        mContext=context;
        mGson=gson;
        hxsdkHelper.onInit(context, options);
        EMClient.getInstance().chatManager().addMessageListener(new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> list) {
                for (EMMessage message : list) {
                    callbackNewMessage(message);
                    if (isRunBackground()){
                        HXSDKHelper.getInstance().getNotifier().onNewMsg(message);
                    }
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> list) {
                for (EMMessage message : list) {
                    callbackNewCMDMessage(message);
                }
            }

            @Override
            public void onMessageReadAckReceived(List<EMMessage> list) {
                for (EMMessage message : list) {
                    MessageVO messageVO = new MessageVO();
                    messageVO.setMsgId(message.getMsgId());
                    messageVO.setUsername(message.getFrom());
                    if (callback != null) {
                        callback.onAckMessage(messageVO);
                    }
                }
            }

            @Override
            public void onMessageDeliveryAckReceived(List<EMMessage> list) {
                for (EMMessage message : list) {
                    MessageVO messageVO = new MessageVO();
                    messageVO.setMsgId(message.getMsgId());
                    messageVO.setUsername(message.getFrom());
                    if (callback != null) {
                        callback.onDeliveryMessage(messageVO);
                    }
                    message.setDelivered(true);
                }
            }

            @Override
            public void onMessageChanged(EMMessage emMessage, Object o) {

            }
        });

        //只有注册了广播才能接收到新消息，目前离线消息，在线消息都是走接收消息的广播（离线消息目前无法监听，在登录以后，接收消息广播会执行一次拿到所有的离线消息）
        EMClient.getInstance().getOptions().setRequireAck(true);
        //如果用到已读的回执需要把这个flag设置成true

        EMClient.getInstance().getOptions().setRequireDeliveryAck(true);
        //如果用到已发送的回执需要把这个flag设置成true

        EMClient.getInstance().contactManager().setContactListener(new MyContactListener());

        //注册一个监听连接状态的listener
        EMClient.getInstance().addConnectionListener(new MyConnectionListener());

        EMClient.getInstance().groupManager().addGroupChangeListener(new MyGroupChangeListener());

        //注册实时语音监听
        IntentFilter callFilter = new IntentFilter(EMClient.getInstance().callManager().getIncomingCallBroadcastAction());
        context.registerReceiver(new CallReceiver(), callFilter);
        //设置通话状态监听
        EMClient.getInstance().callManager().addCallStateChangeListener(new EMCallStateChangeListener() {
            @Override
            public void onCallStateChanged(CallState callState, CallError callError) {
                CallStateOutputVO outputVO = new CallStateOutputVO();
                switch (callState) {
                    case CONNECTING: // 正在连接对方
                        outputVO.setState("1");
                        break;
                    case CONNECTED: // 双方已经建立连接
                        outputVO.setState("2");
                        break;

                    case ACCEPTED: // 电话接通成功
                        outputVO.setState("3");
                        break;
                    case DISCONNNECTED: // 电话断了
                        outputVO.setState("4");
                        break;

                    default:
                        break;
                }
                callback.onCallStateChanged(outputVO);
            }
        });
    }

    public boolean isRunBackground(){
        return true;
    }

    private class CallReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // 拨打方username
            String from = intent.getStringExtra("from");
            // call type
            String type = intent.getStringExtra("type");
            CallReceiveOutputVO outputVO=new CallReceiveOutputVO();
            outputVO.setCallType(type);
            outputVO.setFrom(from);
            callback.onCallReceive(outputVO);
        }
    }

    private class MyGroupChangeListener implements EMGroupChangeListener {

        @Override
        public void onInvitationReceived(String groupId, String groupName,String inviter, String reason) {
            //收到加入群聊的邀请
            EMMessage msg = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
            msg.setChatType(EMMessage.ChatType.GroupChat);
            msg.setFrom(inviter);
            msg.setTo(groupId);
            msg.setMsgId(UUID.randomUUID().toString());
            msg.addBody(new EMTextMessageBody(inviter + "邀请你加入了群聊"));
            // 保存邀请消息
            EMClient.getInstance().chatManager().saveMessage(msg);
            // 提醒新消息
            GroupOptVO groupOptVO=new GroupOptVO();
            groupOptVO.setGroupId(groupId);
//            groupOptVO.setGroupName(groupName);
            groupOptVO.setInviter(inviter);
            groupOptVO.setReason(reason);
            callback.onInvitationReceived(groupOptVO);

        }

        @Override
        public void onInvitationAccpted(String groupId, String inviter,
                                        String reason) {
            //群聊邀请被接受
            GroupOptVO groupOptVO=new GroupOptVO();
            groupOptVO.setGroupId(groupId);
            groupOptVO.setInviter(inviter);
            groupOptVO.setReason(reason);
            callback.onInvitationAccpted(groupOptVO);
        }

        @Override
        public void onInvitationDeclined(String groupId, String invitee,
                                         String reason) {
            //群聊邀请被拒绝
            GroupOptVO groupOptVO=new GroupOptVO();
            groupOptVO.setGroupId(groupId);
            groupOptVO.setInvitee(invitee);
            groupOptVO.setReason(reason);
            callback.onInvitationDeclined(groupOptVO);
        }

        @Override
        public void onUserRemoved(String groupId, String groupName) {
            //当前用户被管理员移除出群聊
            GroupOptVO groupOptVO=new GroupOptVO();
            groupOptVO.setGroupId(groupId);
            groupOptVO.setGroupName(groupName);
            callback.onUserRemoved(groupOptVO);
        }

        @Override
        public void onGroupDestroy(String groupId, String groupName) {
            //群聊被创建者解散
            // 提示用户群被解散
            GroupOptVO groupOptVO=new GroupOptVO();
            groupOptVO.setGroupId(groupId);
            groupOptVO.setGroupName(groupName);
            callback.onGroupDestroy(groupOptVO);
        }

        @Override
        public void onApplicationReceived(String groupId, String groupName,String applyer, String reason) {
            // 用户申请加入群聊，收到加群申请
            GroupOptVO groupOptVO=new GroupOptVO();
            groupOptVO.setGroupId(groupId);
            groupOptVO.setGroupName(groupName);
            groupOptVO.setApplyer(applyer);
            groupOptVO.setReason(reason);
            callback.onApplicationReceived(groupOptVO);
        }

        @Override
        public void onApplicationAccept(String groupId, String groupName,String accepter) {
            // 加群申请被同意
            EMMessage msg = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
            msg.setChatType(EMMessage.ChatType.GroupChat);
            msg.setFrom(accepter);
            msg.setTo(groupId);
            msg.setMsgId(UUID.randomUUID().toString());
            msg.addBody(new EMTextMessageBody(accepter + "同意了你的群聊申请"));
            // 保存同意消息
            EMClient.getInstance().chatManager().saveMessage(msg);
            // 提醒新消息
            //EMNotifier.getInstance(mContext).notifyOnNewMsg();
            GroupOptVO groupOptVO=new GroupOptVO();
            groupOptVO.setGroupId(groupId);
            groupOptVO.setGroupName(groupName);
            groupOptVO.setAccepter(accepter);
            callback.onApplicationAccept(groupOptVO);
        }

        @Override
        public void onApplicationDeclined(String groupId, String groupName,String decliner, String reason) {
            // 加群申请被拒绝
            GroupOptVO groupOptVO=new GroupOptVO();
            groupOptVO.setGroupId(groupId);
            groupOptVO.setGroupName(groupName);
            groupOptVO.setDecliner(decliner);
            callback.onApplicationDeclined(groupOptVO);
        }

        @Override
        public void onAutoAcceptInvitationFromGroup(String groupId, String inviter, String inviteMessage ) {
            Map<String, String> result  = new HashMap<String, String>();
            result.put("groupId", groupId);
            result.put("username", inviter);
            result.put("message", inviteMessage);
            callback.onDidJoinedGroup(result);

        }
    }

    private void callbackNewMessage(EMMessage message){
        if (callback!=null) {
            callback.onNewMessage(mGson.toJson(convertEMMessage(message)));
        }
    }

    private void callbackNewCMDMessage(EMMessage message){
        //获取消息body
        EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
        String aciton = cmdMsgBody.action();//获取自定义action
        CmdMsgOutputVO outputVO=new CmdMsgOutputVO();
        outputVO.setAction(aciton);
        outputVO.setMessage(convertEMMessage(message));
        outputVO.setMsgId(message.getMsgId());
        callback.onCmdMessageReceive(outputVO);
    }

    public static MsgResultVO convertEMMessage(EMMessage message){
        MsgResultVO resultVO=new MsgResultVO();
        resultVO.setFrom(message.getFrom());
        resultVO.setTo(message.getTo());
        resultVO.setMessageId(message.getMsgId());
        resultVO.setMessageType(getMsgType(message.getType()));
        resultVO.setMessageTime(String.valueOf(message.getMsgTime()));
        resultVO.setIsRead(message.isUnread() ? "0" : "1");
        resultVO.setIsGroup(message.getChatType() == EMMessage.ChatType.GroupChat ? "1" : "0");
        resultVO.setChatType(EUExEasemob.getChatTypeValue(message.getChatType()));
        resultVO.setIsAcked(message.isAcked() ? "1" : "0");
        resultVO.setMessageBody(getMessageBody(message, message.getType()));
        try {
            resultVO.setExt(message.getStringAttribute("ext"));
        } catch (HyphenateException e) {
        }
        return resultVO;
    }

    private static MsgBodyVO getMessageBody(EMMessage message, EMMessage.Type type){
        MsgBodyVO msgBodyVO=new MsgBodyVO();
        if (type== EMMessage.Type.TXT){
            EMTextMessageBody messageBody= (EMTextMessageBody) message.getBody();
            msgBodyVO.setText(messageBody.getMessage());
        }else if (type== EMMessage.Type.CMD){
            EMCmdMessageBody messageBody= (EMCmdMessageBody) message.getBody();
            msgBodyVO.setAction(messageBody.action());
        }else if (type== EMMessage.Type.LOCATION){
            EMLocationMessageBody messageBody= (EMLocationMessageBody) message.getBody();
            msgBodyVO.setLatitude(String.valueOf(messageBody.getLatitude()));
            msgBodyVO.setLongitude(String.valueOf(messageBody.getLongitude()));
            msgBodyVO.setAddress(messageBody.getAddress());
        }else if (type== EMMessage.Type.FILE){
            EMFileMessageBody messageBody= (EMFileMessageBody) message.getBody();
            msgBodyVO.setDisplayName(messageBody.getFileName());
            msgBodyVO.setRemotePath(isEmpty(messageBody.getRemoteUrl())? messageBody.getLocalUrl() :
                    messageBody.getRemoteUrl());
            msgBodyVO.setSecretKey(messageBody.getSecret());
        }else if (type== EMMessage.Type.IMAGE){
            EMImageMessageBody messageBody= (EMImageMessageBody) message.getBody();
            msgBodyVO.setDisplayName(messageBody.getFileName());
            msgBodyVO.setRemotePath(isEmpty(messageBody.getRemoteUrl())?messageBody.getLocalUrl():messageBody
                    .getRemoteUrl());
            msgBodyVO.setSecretKey(messageBody.getSecret());
            msgBodyVO.setThumbnailRemotePath(messageBody.getThumbnailUrl());
            msgBodyVO.setThumbnailSecretKey(messageBody.getThumbnailSecret());
        }else if (type== EMMessage.Type.VIDEO){
            EMVideoMessageBody messageBody= (EMVideoMessageBody) message.getBody();
            msgBodyVO.setDisplayName(messageBody.getFileName());
            msgBodyVO.setRemotePath(isEmpty(messageBody.getRemoteUrl())?messageBody.getLocalUrl():messageBody
                    .getRemoteUrl());
            msgBodyVO.setSecretKey(messageBody.getSecret());
            msgBodyVO.setLength(String.valueOf(messageBody.getVideoFileLength()));
            msgBodyVO.setThumbnailRemotePath(messageBody.getThumbnailUrl());
            msgBodyVO.setThumbnailSecretKey(messageBody.getThumbnailSecret());
        }else if (type== EMMessage.Type.VOICE){
            EMVoiceMessageBody messageBody= (EMVoiceMessageBody) message.getBody();
            msgBodyVO.setDisplayName(messageBody.getFileName());
            msgBodyVO.setLength(String.valueOf(messageBody.getLength()));
            msgBodyVO.setRemotePath(isEmpty(messageBody.getRemoteUrl())?messageBody.getLocalUrl():messageBody
                    .getRemoteUrl());
            msgBodyVO.setSecretKey(messageBody.getSecret());
        }
        return msgBodyVO;
    }

    public static boolean isEmpty(String value){
        if (TextUtils.isEmpty(value)||"null".equals(value)){
            return true;
        }
        return false;
    }

    private static String getMsgType(EMMessage.Type type){
        String result=null;
        switch (type){
            case CMD:
                result="cmd";
                break;
            case FILE:
                result="file";
                break;
            case IMAGE:
                result="image";
                break;
            case LOCATION:
                result="location";
                break;
            case VOICE:
                result="audio";
                break;
            case TXT:
                result="text";
                break;
            case VIDEO:
                result="video";
                break;
        }
        return result;
    }

    //实现ConnectionListener接口
    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
            callback.onConnected();
        }
        @Override
        public void onDisconnected(final int error) {
            int result=4;
            if(error == EMError.USER_REMOVED){
                // 显示帐号已经被移除
                result=1;
            }else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                // 显示帐号在其他设备登陆
                result=2;
            } else if (NetUtils.hasNetwork(mContext)){
                result=3;
            }
            if (callback != null) {
                callback.onDisconnected(result);
            }

        }
    }
    private class MyContactListener implements EMContactListener {
        @Override
        public void onContactAdded(String username) {
            callback.onContactAdded(username);
        }

        @Override
        public void onContactDeleted(String username) {
            callback.onContactDeleted(username);
        }

        @Override
        public void onContactInvited(String username, String reason) {
            // 接到邀请的消息，如果不处理(同意或拒绝)，掉线后，服务器会自动再发过来，所以客户端不要重复提醒
            GroupOptVO optVO=new GroupOptVO();
            optVO.setReason(reason);
            optVO.setUsername(username);
            callback.onContactInvited(optVO);
        }

        @Override
        public void onContactAgreed(String username) {
            //同意好友请求
            GroupOptVO optVO=new GroupOptVO();
            optVO.setUsername(username);
            callback.onContactAgreed(optVO);
        }

        @Override
        public void onContactRefused(String username) {
            // 拒绝好友请求
            GroupOptVO optVO=new GroupOptVO();
            optVO.setUsername(username);
            callback.onContactRefused(optVO);
        }
    }



    public void setCallback(ListenersCallback callback) {
        this.callback = callback;
    }

    public interface ListenersCallback{
        void onNewMessage(String result);
        void onAckMessage(MessageVO messageVO);
        void onDeliveryMessage(MessageVO messageVO);
        void onContactAdded(String username);
        void onContactDeleted(String username);
        void onContactInvited(GroupOptVO optVO);
        void onContactAgreed(GroupOptVO optVO);
        void onContactRefused(GroupOptVO optVO);
        void onConnected();
        void onDisconnected(final int error);
        void onInvitationAccpted(GroupOptVO groupOptVO);
        void onInvitationDeclined(GroupOptVO groupOptVO);
        void onUserRemoved(GroupOptVO groupOptVO);
        void onGroupDestroy(GroupOptVO groupOptVO);
        void onApplicationReceived(GroupOptVO groupOptVO);
        void onApplicationAccept(GroupOptVO groupOptVO);
        void onInvitationReceived(GroupOptVO groupOptVO);
        void onApplicationDeclined(GroupOptVO groupOptVO);
        void onCallReceive(CallReceiveOutputVO outputVO);
        void onCallStateChanged(CallStateOutputVO outputVO);
        void onCmdMessageReceive(CmdMsgOutputVO outputVO);
        void onDidJoinedGroup(Map<String, String> data);
    }
}
