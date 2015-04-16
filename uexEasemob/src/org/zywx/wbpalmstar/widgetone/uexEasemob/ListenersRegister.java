package org.zywx.wbpalmstar.widgetone.uexEasemob;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.chat.CmdMessageBody;
import com.easemob.chat.EMCallStateChangeListener;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactListener;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMNotifier;
import com.easemob.chat.GroupChangeListener;
import com.easemob.chat.TextMessageBody;
import com.easemob.util.NetUtils;
import com.google.gson.Gson;

import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.output.CallReceiveOutputVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.output.CallStateOutputVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.output.CmdMsgOutputVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.output.GroupOptVO;
import org.zywx.wbpalmstar.widgetone.uexEasemob.vo.output.MessageVO;

import java.util.List;
import java.util.UUID;

/**
 * Created by ylt on 15/3/13.
 */
public class ListenersRegister {

    private Context mContext;

    private ListenersCallback callback;

    public void registerListeners(Context context){
        mContext=context;
        //只有注册了广播才能接收到新消息，目前离线消息，在线消息都是走接收消息的广播（离线消息目前无法监听，在登录以后，接收消息广播会执行一次拿到所有的离线消息）
        NewMessageBroadcastReceiver msgReceiver = new NewMessageBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
        intentFilter.setPriority(3);
        context.registerReceiver(msgReceiver, intentFilter);

        EMChatManager.getInstance().getChatOptions().setRequireAck(true);
        //如果用到已读的回执需要把这个flag设置成true

        IntentFilter ackMessageIntentFilter = new IntentFilter(EMChatManager.getInstance().getAckMessageBroadcastAction());
        ackMessageIntentFilter.setPriority(3);
        context.registerReceiver(ackMessageReceiver, ackMessageIntentFilter);

        EMChatManager.getInstance().getChatOptions().setRequireDeliveryAck(true);
        //如果用到已发送的回执需要把这个flag设置成true

        IntentFilter deliveryAckMessageIntentFilter = new IntentFilter(EMChatManager.getInstance().getDeliveryAckMessageBroadcastAction());
        deliveryAckMessageIntentFilter.setPriority(5);
        context.registerReceiver(deliveryAckMessageReceiver, deliveryAckMessageIntentFilter);

        EMContactManager.getInstance().setContactListener(new MyContactListener());

        //注册一个监听连接状态的listener
        EMChatManager.getInstance().addConnectionListener(new MyConnectionListener());

        EMGroupManager.getInstance().addGroupChangeListener(new MyGroupChangeListener());

        EMChat.getInstance().setAppInited();

        //注册实时语音监听
        IntentFilter callFilter = new IntentFilter(EMChatManager.getInstance().getIncomingCallBroadcastAction());
        context.registerReceiver(new CallReceiver(), callFilter);

        //设置通话状态监听
        EMChatManager.getInstance().addVoiceCallStateChangeListener(new EMCallStateChangeListener() {
            @Override
            public void onCallStateChanged(CallState callState, EMCallStateChangeListener.CallError error) {
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

        // 注册一个cmd消息的BroadcastReceiver
        IntentFilter cmdIntentFilter = new IntentFilter(EMChatManager.getInstance().getCmdMessageBroadcastAction());
        mContext.registerReceiver(cmdMessageReceiver, cmdIntentFilter);

    }

    /**
     * cmd消息BroadcastReceiver
     */
    private BroadcastReceiver cmdMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            //获取cmd message对象
            String msgId = intent.getStringExtra("msgid");
            EMMessage message = intent.getParcelableExtra("message");
            //获取消息body
            CmdMessageBody cmdMsgBody = (CmdMessageBody) message.getBody();
            String aciton = cmdMsgBody.action;//获取自定义action
            CmdMsgOutputVO outputVO=new CmdMsgOutputVO();
            outputVO.setAction(aciton);
            outputVO.setMessage(new Gson().toJson(message));
            outputVO.setMsgId(msgId);
            callback.onCmdMessageReceive(outputVO);
        }
    };

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

    private class MyGroupChangeListener implements GroupChangeListener {

        @Override
        public void onInvitationReceived(String groupId, String groupName,String inviter, String reason) {

            //收到加入群聊的邀请

            boolean hasGroup = false;
            for (EMGroup group : EMGroupManager.getInstance().getAllGroups()) {
                if (group.getGroupId().equals(groupId)) {
                    hasGroup = true;
                    break;
                }
            }
            if (!hasGroup)
                return;

            // 被邀请
            EMMessage msg = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
            msg.setChatType(EMMessage.ChatType.GroupChat);
            msg.setFrom(inviter);
            msg.setTo(groupId);
            msg.setMsgId(UUID.randomUUID().toString());
            msg.addBody(new TextMessageBody(inviter + "邀请你加入了群聊"));
            // 保存邀请消息
            EMChatManager.getInstance().saveMessage(msg);
            // 提醒新消息
            EMNotifier.getInstance(mContext).notifyOnNewMsg();
            GroupOptVO groupOptVO=new GroupOptVO();
            groupOptVO.setGroupId(groupId);
            groupOptVO.setGroupName(groupName);
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
            msg.addBody(new TextMessageBody(accepter + "同意了你的群聊申请"));
            // 保存同意消息
            EMChatManager.getInstance().saveMessage(msg);
            // 提醒新消息
            EMNotifier.getInstance(mContext).notifyOnNewMsg();
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

    }

    private class NewMessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 注销广播
            abortBroadcast();

            // 消息id（每条消息都会生成唯一的一个id，目前是SDK生成）
            String msgId = intent.getStringExtra("msgid");
            //发送方
            String username = intent.getStringExtra("from");

            MessageVO messageVO=new MessageVO();
            messageVO.setMsgId(msgId);
            messageVO.setUsername(username);
            if (callback!=null) {
                callback.onNewMessage(messageVO);
            }
        }
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
            }else if (error == EMError.CONNECTION_CONFLICT) {
                // 显示帐号在其他设备登陆
                result=2;
            } else if (NetUtils.hasNetwork(mContext)){
                result=3;
            }
            callback.onDisconnected(result);
        }
    }

    private class MyContactListener implements EMContactListener {

        @Override
        public void onContactAdded(List<String> usernameList) {
            // 保存增加的联系人
            callback.onContactAdded(usernameList);
        }

        @Override
        public void onContactDeleted(final List<String> usernameList) {
            // 被删除
            callback.onContactDeleted(usernameList);
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

    private BroadcastReceiver ackMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            abortBroadcast();
            String msgid = intent.getStringExtra("msgid");
            String from = intent.getStringExtra("from");

            MessageVO messageVO=new MessageVO();
            messageVO.setMsgId(msgid);
            messageVO.setUsername(from);
            if (callback!=null){
                callback.onAckMessage(messageVO);
            }
            EMConversation conversation = EMChatManager.getInstance().getConversation(from);
            if (conversation != null) {
                // 把message设为已读
                EMMessage msg = conversation.getMessage(msgid);
                if (msg != null) {
                    msg.isAcked = true;
                }
            }

        }
    };

    /**
     * 消息送达BroadcastReceiver
     */
    private BroadcastReceiver deliveryAckMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            abortBroadcast();

            String msgid = intent.getStringExtra("msgid");
            String from = intent.getStringExtra("from");
            MessageVO messageVO=new MessageVO();
            messageVO.setMsgId(msgid);
            messageVO.setUsername(from);
            if (callback!=null){
                callback.onDeliveryMessage(messageVO);
            }
            EMConversation conversation = EMChatManager.getInstance().getConversation(from);
            if (conversation != null) {
                // 把message设为已读
                EMMessage msg = conversation.getMessage(msgid);
                if (msg != null) {
                    msg.isDelivered = true;
                }
            }
        }
    };

    public void setCallback(ListenersCallback callback) {
        this.callback = callback;
    }

    public interface ListenersCallback{
        void onNewMessage(MessageVO messageVO);
        void onAckMessage(MessageVO messageVO);
        void onDeliveryMessage(MessageVO messageVO);
        void onContactAdded(List<String> usernameList);
        void onContactDeleted(List<String> usernameList);
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
    }
}
