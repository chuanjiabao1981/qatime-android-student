package cn.qatime.player.bean;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.model.Team;

import java.lang.reflect.Field;
import java.util.ArrayList;

import cn.qatime.player.R;
import cn.qatime.player.activity.NEVideoPlayerActivity;
import cn.qatime.player.activity.PictureSelectActivity;
import cn.qatime.player.adapter.BiaoqingAdapter;
import cn.qatime.player.utils.Constant;
import libraryextra.utils.DensityUtils;
import libraryextra.utils.StringUtils;
import libraryextra.view.TagViewPager;

import static android.view.View.GONE;

/**
 * @author lungtify
 * @Time 2016/12/9 21:02
 * @Describe 输入框编辑栏
 */

public class InputPanel implements View.OnClickListener {
    private final Activity context;
    private final View rootView;
    private final InputPanelListener listener;
    private final String sessionId;
    private String[][] biaoqingTags = new String[3][28];
    private LinearLayout inputEmojiLayout;
    private EditText content;
    private ImageView emoji;
    private Button send;
    private ImageView imageSelect;
    private TagViewPager tagViewPager;
    private Runnable runnable;
    private Handler hd = new Handler();
    private Team team;
    private boolean isMute = false;
    private OnInputShowListener onInputShowListener;

    public interface InputPanelListener {
        void ChatMessage(IMMessage message);
    }

    public interface OnInputShowListener {
        void OnInputShow();
    }

    public void setOnInputShowListener(OnInputShowListener onTouchListener) {
        this.onInputShowListener = onTouchListener;
    }

    /**
     * @param context
     * @param rootView
     * @param showInput 初始化时,是否显示输入框
     * @param sessionId 群组id
     */
    public InputPanel(final Activity context, InputPanelListener listener, View rootView, boolean showInput, String sessionId) {
        this.context = context;
        this.rootView = rootView;
        this.listener = listener;
        this.sessionId = sessionId;

        assignViews(showInput);
        for (int i = 0; i < 3; i++) {
            for (int j = 27 * i; j < 27 * (i + 1); j++) {
                biaoqingTags[i][j - 27 * i] = "em_" + String.valueOf(j + 1);
            }
            biaoqingTags[i][27] = "emoji_delete";
        }

        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, DensityUtils.dp2px(context, 180));
        tagViewPager.setLayoutParams(params);
        tagViewPager.setId(R.id.viewPager);
        tagViewPager.setVisibility(GONE);
        tagViewPager.init(R.drawable.shape_biaoqing_tag_select, R.drawable.shape_biaoqing_tag_nomal, 16, 8, 2, 40);
        tagViewPager.setAutoNext(false, 0);
        tagViewPager.setOnGetView(new TagViewPager.OnGetView() {
            @Override
            public View getView(ViewGroup container, int position) {
                GridView mGridView = new GridView(context);
                mGridView.setLayoutParams(params);
                mGridView.setNumColumns(7);
                mGridView.setBackgroundColor(Color.WHITE);
                mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
                mGridView.setAdapter(new BiaoqingAdapter(context, getList(position)));
                mGridView.setOnItemClickListener(new ItemClickListener(position));
                container.addView(mGridView);
                return mGridView;
            }
        });
        tagViewPager.setAdapter(3);
    }

    /**
     * @param position 第几项
     * @return 链表
     */
    private ArrayList<BiaoQingData> getList(int position) {
        ArrayList<BiaoQingData> list = new ArrayList<>();
        for (int i = 0; i < biaoqingTags[position].length; i++) {
            BiaoQingData data = new BiaoQingData();
            data.setTag(biaoqingTags[position][i]);
            int resId = 0;
            try {
                Field field = R.mipmap.class.getDeclaredField(biaoqingTags[position][i]);
                resId = Integer.parseInt(field.get(null).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            data.setResourceId(resId);
            list.add(data);
        }
        return list;
    }

    private void assignViews(boolean showInput) {
        inputEmojiLayout = (LinearLayout) rootView.findViewById(R.id.input_emoji_layout);
        content = (EditText) rootView.findViewById(R.id.content);
        emoji = (ImageView) rootView.findViewById(R.id.emoji);
        send = (Button) rootView.findViewById(R.id.send);
        imageSelect = (ImageView) rootView.findViewById(R.id.image_select);
        tagViewPager = (TagViewPager) rootView.findViewById(R.id.tagViewPager);
        inputEmojiLayout.setVisibility(showInput ? View.VISIBLE : GONE);

        runnable = new Runnable() {
            @Override
            public void run() {
                tagViewPager.setVisibility(View.VISIBLE);
                emoji.setImageResource(R.mipmap.keybord);
            }
        };
        emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onInputShowListener != null) {
                    onInputShowListener.OnInputShow();
                }
                if (tagViewPager.getVisibility() == GONE) {
                    hd.postDelayed(runnable, 50);
                    closeInput();
                } else {
                    tagViewPager.setVisibility(GONE);
                    emoji.setImageResource(R.mipmap.biaoqing);
                    content.requestFocus();
                    openInput();
                }
            }
        });
        content.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (onInputShowListener != null) {
                    onInputShowListener.OnInputShow();
                }
                content.requestFocus();
                openInput();
                emoji.setImageResource(R.mipmap.biaoqing);
                tagViewPager.setVisibility(GONE);
                return false;
            }
        });
        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                changeSendStatus();
            }
        });
        send.setOnClickListener(this);
        imageSelect.setOnClickListener(this);
    }

    /**
     * 刷新发送按钮状态
     */
    private void changeSendStatus() {
        if (StringUtils.isNullOrBlanK(content.getText().toString().trim())) {
            send.setVisibility(GONE);
            imageSelect.setVisibility(View.VISIBLE);
            imageSelect.setEnabled(true);
        } else {
            send.setVisibility(View.VISIBLE);
            imageSelect.setVisibility(View.INVISIBLE);
            imageSelect.setEnabled(false);
        }
    }


    /**
     * 关闭输入法
     */
    private void closeInput() {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(content.getWindowToken(), 0);
    }

    /**
     * 打开输入发
     */

    private void openInput() {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        // 接受软键盘输入的编辑文本或其它视图
        inputMethodManager.showSoftInput(content, 0);
    }

    public void closeEmojiAndInput() {
        emoji.setImageResource(R.mipmap.biaoqing);
        if (tagViewPager.getVisibility() == View.VISIBLE) {
            tagViewPager.setVisibility(GONE);
        }
        closeInput();
    }

    public boolean isEmojiShow() {
        return tagViewPager != null && tagViewPager.getVisibility() == View.VISIBLE;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send://发送按钮
                if (!isAllowSendMessage()) {
                    return;
                }
                if (StringUtils.isNullOrBlanK(content.getText().toString())) {
                    Toast.makeText(context, context.getResources().getString(R.string.message_can_not_null), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (checkMute()) {
                    return;
                }
                // 创建文本消息
                IMMessage message = MessageBuilder.createTextMessage(
                        sessionId, // 聊天对象的 ID，如果是单聊，为用户帐号，如果是群聊，为群组 ID
                        SessionTypeEnum.Team, // 聊天类型，单聊或群组
                        content.getText().toString().trim() // 文本内容
                );

                if (context.getClass().equals(NEVideoPlayerActivity.class)) {
                    if (((NEVideoPlayerActivity) context).limitMessage.size() >= 1) {
                        if (message.getTime() - ((NEVideoPlayerActivity) context).limitMessage.get(0).getTime() < 2000) {
                            Toast.makeText(context, context.getResources().getString(R.string.please_talk_later), Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            ((NEVideoPlayerActivity) context).limitMessage.add(message);
                            if (((NEVideoPlayerActivity) context).limitMessage.size() > 1) {
                                ((NEVideoPlayerActivity) context).limitMessage.remove(0);
                            }
                        }
                    } else {
                        ((NEVideoPlayerActivity) context).limitMessage.add(message);
                    }
                }
                if (listener != null) {
                    listener.ChatMessage(message);
                }
//                Logger.e(content.getText().toString().trim());
                content.setText("");
                changeSendStatus();
                break;
            case R.id.image_select://选择照片
                closeEmojiAndInput();
                Intent intent = new Intent(context, PictureSelectActivity.class);
//                intent.putExtra("gonecamera", true);
                context.startActivityForResult(intent, Constant.REQUEST);
                break;
        }
    }

    /**
     * 检查是否被禁言
     *
     * @return true 被禁言
     */
    public boolean checkMute() {
        if (isMute) {
            Toast.makeText(context, context.getResources().getString(R.string.have_muted), Toast.LENGTH_SHORT).show();
            content.setText("");
            changeSendStatus();
            return true;
        }
        return false;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setMute(boolean mute) {
        this.isMute = mute;
        if (isMute) {
            content.setHint(R.string.have_muted);
        } else {
            content.setHint("");
        }
    }

    public void clearInputValue() {
        content.setText("");
        changeSendStatus();
    }

    /**
     * 关闭表情输入法等  & 不显示输入框
     */
    public void goneInput() {
        closeEmojiAndInput();
        inputEmojiLayout.setVisibility(GONE);
    }

    /**
     * 显示输入框
     */
    public void visibilityInput() {
        inputEmojiLayout.setVisibility(View.VISIBLE);
    }

    private class ItemClickListener implements AdapterView.OnItemClickListener {

        private final int position;

        public ItemClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position == ((BiaoqingAdapter) (parent.getAdapter())).getList().size() - 1) {
                //动作按下
                int action = KeyEvent.ACTION_DOWN;
                //code:删除，其他code也可以，例如 code = 0
                int code = KeyEvent.KEYCODE_DEL;
                KeyEvent event = new KeyEvent(action, code);
                content.onKeyDown(KeyEvent.KEYCODE_DEL, event); //抛给系统处理了
                return;
            }
            if (this.position != 2 || position < 21) {
                BiaoQingData item = ((BiaoqingAdapter) (parent.getAdapter())).getList().get(position);
                ImageSpan is;
                SpannableString sp;
                is = new ImageSpan(content.getContext(), item.getResourceId());
                sp = new SpannableString(item.getCode());
                sp.setSpan(is, 0, item.getCode().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                content.append(sp);
            }
        }
    }

    public boolean isAllowSendMessage() {
        if (team == null || !team.isMyTeam()) {
            Toast.makeText(context, R.string.team_send_message_not_allow, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
