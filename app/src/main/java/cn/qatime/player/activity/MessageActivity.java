package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.constant.TeamTypeEnum;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.TeamMember;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.bean.Container;
import cn.qatime.player.bean.InputPanel;
import cn.qatime.player.bean.MessageListPanel;
import cn.qatime.player.bean.ModuleProxy;
import cn.qatime.player.im.SimpleCallback;
import cn.qatime.player.im.cache.TeamDataCache;
import cn.qatime.player.utils.Constant;
import libraryextra.utils.StringUtils;

/**
 * @author luntify
 * @date 2016/8/30 12:25
 * @Description 聊天
 */
public class MessageActivity extends BaseActivity implements InputPanel.InputPanelListener, ModuleProxy {
    private String sessionId;//聊天对象id
    private SessionTypeEnum sessionType;
    private Team team;
    private TextView tipText;

    private int courseId;
    private boolean isMute = false;//当前用户 是否被禁言
    private View rootView;
    private InputPanel inputpanel;
    private MessageListPanel messageListPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = View.inflate(this, R.layout.activity_message, null);
        setContentView(rootView);
//        EventBus.getDefault().register(this);
        String name = getIntent().getStringExtra("name");
        if (!StringUtils.isNullOrBlanK(name)) {
            setTitles(name);
        } else {
            setTitles(getResources().getString(R.string.team_group));
        }
        sessionId = getIntent().getStringExtra("sessionId");
        final String type = getIntent().getStringExtra("type");//直播类型，正常/一对一
        sessionType = (SessionTypeEnum) getIntent().getSerializableExtra("sessionType");
        courseId = getIntent().getIntExtra("courseId", 0);

        if (StringUtils.isNullOrBlanK(type)) {
            findViewById(R.id.right).setVisibility(View.GONE);
        }
        setRightImage(R.mipmap.online_room, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("custom".equals(type)) {
                    Intent intent = new Intent(MessageActivity.this, NEVideoPlayerActivity.class);
                    intent.putExtra("id", courseId);
                    intent.putExtra("sessionId", sessionId);
                    startActivityForResult(intent, Constant.REQUEST);
                } else if ("interactive".equals(type)) {
                    Intent intent = new Intent(MessageActivity.this, InteractiveLiveActivity.class);
                    intent.putExtra("id", courseId);
                    intent.putExtra("teamId", sessionId);
                    startActivity(intent);
                }
            }
        });
        registerObservers(true);
        registerTeamUpdateObserver(true);
        initView();
    }

    private void initView() {
        TeamMember teamMember = TeamDataCache.getInstance().getTeamMember(sessionId, BaseApplication.getAccount());
        if (teamMember != null) {
            isMute = teamMember.isMute();
        }
        tipText = (TextView) findViewById(R.id.tip);
        Container container = new Container(this, sessionId, this);
        messageListPanel = new MessageListPanel(container, rootView);

        inputpanel = new InputPanel(this, this, rootView, true, sessionId);
        inputpanel.setMute(isMute);
        inputpanel.setOnInputShowListener(new InputPanel.OnInputShowListener() {
            @Override
            public void OnInputShow() {
                messageListPanel.scrollToBottom();
            }
        });
    }

    /**
     * 收起输入法等
     */
    @Override
    public void shouldCollapseInputPanel() {
        inputpanel.closeEmojiAndInput();
    }

    @Override
    public boolean isLongClickEnabled() {
        return !inputpanel.isRecording();
    }

    @Override
    public void ChatMessage(IMMessage message) {
////         创建文本消息
//        IMMessage message = MessageBuilder.createTextMessage(
//                sessionId, // 聊天对象的 ID，如果是单聊，为用户帐号，如果是群聊，为群组 ID
//                sessionType, // 聊天类型，单聊或群组
//                data // 文本内容
//        );
        sendMessage(message);
    }

    private void sendMessage(IMMessage message) {
        // 发送消息。如果需要关心发送结果，可设置回调函数。发送完成时，会收到回调。如果失败，会有具体的错误码。
        NIMClient.getService(MsgService.class).sendMessage(message, true);

        messageListPanel.onMsgSend(message);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        inputpanel.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * ****************** 观察者 **********************
     */

    private void registerObservers(boolean register) {
        MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
        service.observeReceiveMessage(receiveMessageObserver, register);
    }

    Observer<List<IMMessage>> receiveMessageObserver = new Observer<List<IMMessage>>() {
        @Override
        public void onEvent(List<IMMessage> messages) {
            if (messages == null || messages.isEmpty()) {
                return;
            }
            messageListPanel.onIncomingMessage(messages);
        }
    };


    /**
     * 请求群基本信息
     */

    private void requestTeamInfo() {
        Team team = TeamDataCache.getInstance().getTeamById(sessionId);
        if (team != null) {
            updateTeamInfo(team);
        } else {
            TeamDataCache.getInstance().fetchTeamById(sessionId, new SimpleCallback<Team>() {
                @Override
                public void onResult(boolean success, Team result) {
                    if (success && result != null) {
                        updateTeamInfo(result);
                    } else {
                        Toast.makeText(MessageActivity.this, getResourceString(R.string.failed_to_obtain_group_information), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            });
        }
    }

    /**
     * 更新群信息
     */
    private void updateTeamInfo(final Team d) {
        if (d == null) {
            return;
        }
        team = d;
        inputpanel.setTeam(team);
        tipText.setText(team.getType() == TeamTypeEnum.Normal ? getResourceString(R.string.you_have_quit_the_group) : getResourceString(R.string.you_have_quit_the_group));
        tipText.setVisibility(team.isMyTeam() ? View.GONE : View.VISIBLE);
    }


    /**
     * 注册群信息更新监听
     *
     * @param register
     */
    private void registerTeamUpdateObserver(boolean register) {
        if (register) {
            TeamDataCache.getInstance().registerTeamDataChangedObserver(teamDataChangedObserver);
            TeamDataCache.getInstance().registerTeamMemberDataChangedObserver(teamMemberDataChangedObserver);
        } else {
            TeamDataCache.getInstance().unregisterTeamDataChangedObserver(teamDataChangedObserver);
            TeamDataCache.getInstance().unregisterTeamMemberDataChangedObserver(teamMemberDataChangedObserver);
        }
    }

    /**
     * 群资料变动通知和移除群的通知（包括自己退群和群被解散）
     */
    TeamDataCache.TeamDataChangedObserver teamDataChangedObserver = new TeamDataCache.TeamDataChangedObserver() {
        @Override
        public void onUpdateTeams(List<Team> teams) {
            if (team == null) {
                return;
            }
            for (Team t : teams) {
                if (t.getId().equals(team.getId())) {
                    updateTeamInfo(t);
                    break;
                }
            }
        }

        @Override
        public void onRemoveTeam(Team team) {
            if (team == null) {
                return;
            }
            if (team.getId().equals(MessageActivity.this.team.getId())) {
                updateTeamInfo(team);
            }
        }
    };

    /**
     * 群成员资料变动通知和移除群成员通知
     */
    TeamDataCache.TeamMemberDataChangedObserver teamMemberDataChangedObserver = new TeamDataCache.TeamMemberDataChangedObserver() {

        @Override
        public void onUpdateTeamMember(List<TeamMember> members) {
            messageListPanel.refreshMessageList();
        }

        @Override
        public void onRemoveTeamMember(TeamMember member) {
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        requestTeamInfo();
        MobclickAgent.onResume(this);
        messageListPanel.onResume();
        NIMClient.getService(MsgService.class).setChattingAccount(sessionId, sessionType);
    }

    @Override
    public void onBackPressed() {
        if (inputpanel.isEmojiShow()) {
            inputpanel.closeEmojiAndInput();
            return;
        }
        if (messageListPanel.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        inputpanel.onPause();
        messageListPanel.onPause();
        MobclickAgent.onPause(this);
        NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_ALL, SessionTypeEnum.None);
    }

//    @Subscribe
//    public void onEvent(ChatVideoBean event) {
//        if (event != null) {
//            this.courseId = event.getCourseId();
//            if (messageListPanel != null) {
//                messageListPanel.setOwner(event.getChat_team_owner());
//            }
//            if (!StringUtils.isNullOrBlanK(event.getName())) {
//                setTitles(event.getName());
//            } else {
//                setTitles(getResources().getString(R.string.team_group));
//            }
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registerObservers(false);
        registerTeamUpdateObserver(false);
        messageListPanel.onDestroy();
//        EventBus.getDefault().unregister(this);
    }

}
