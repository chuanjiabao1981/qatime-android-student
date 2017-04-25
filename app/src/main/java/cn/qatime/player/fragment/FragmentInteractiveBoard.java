package cn.qatime.player.fragment;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netease.nimlib.sdk.chatroom.model.ChatRoomInfo;
import com.orhanobut.logger.Logger;

import java.util.HashMap;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.im.cache.ChatRoomMemberCache;
import cn.qatime.player.im.doodle.ActionTypeEnum;
import cn.qatime.player.im.doodle.DoodleView;
import cn.qatime.player.im.doodle.OnlineStatusObserver;
import cn.qatime.player.im.doodle.SupportActionType;
import cn.qatime.player.im.doodle.Transaction;
import cn.qatime.player.im.doodle.TransactionCenter;
import cn.qatime.player.im.doodle.action.MyPath;
import libraryextra.utils.DensityUtils;

import static android.os.Looper.getMainLooper;

/**
 * @author lungtify
 * @Time 2017/3/28 11:17
 * @Describe 白板
 */
public class FragmentInteractiveBoard extends BaseFragment implements View.OnClickListener, OnlineStatusObserver, DoodleView.FlipListener {
    private DoodleView doodleView;
    private TextView chooseColor;
    private ImageView playBack;
    private TextView joinTipText;
    private LinearLayout paletteLayout;
    private ImageView blackColorImage;
    private ImageView redColorImage;
    private ImageView yellowColorImage;
    private ImageView greenColorImage;
    private ImageView blueColorImage;
    private ImageView purpleColorImage;

    private HashMap<Integer, Integer> colorChooseMap = new HashMap<>();
    private HashMap<Integer, Integer> colorMap = new HashMap<>();
    private int choosedColor;

    private String sessionId; // 白板sessionId


    private void assignViews() {
        doodleView = findViewById(R.id.doodleView);
        chooseColor = findViewById(R.id.choose_color);
        playBack = findViewById(R.id.play_back);
        joinTipText = findViewById(R.id.join_tip_text);
        paletteLayout = findViewById(R.id.palette_layout);
        blackColorImage = findViewById(R.id.black_color_image);
        redColorImage = findViewById(R.id.red_color_image);
        yellowColorImage = findViewById(R.id.yellow_color_image);
        greenColorImage = findViewById(R.id.green_color_image);
        blueColorImage = findViewById(R.id.blue_color_image);
        purpleColorImage = findViewById(R.id.purple_color_image);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return View.inflate(getActivity(), R.layout.fragment_interactive_board, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        assignViews();
        initView();
        setListeners();
    }

    public void initRTSView(String roomId) {
        this.sessionId = roomId;
        initView();
        initDoodleView(null);
        registerObservers(true);
    }

    private void initDoodleView(String account) {
        // add support ActionType
        SupportActionType.getInstance().addSupportActionType(ActionTypeEnum.Path.getValue(), MyPath.class);
        doodleView.init(sessionId, account, DoodleView.Mode.BOTH, Color.WHITE, colorMap.get(R.id.blue_color_image), getContext(), this);
        doodleView.setPaintSize(3);
        doodleView.setPaintType(ActionTypeEnum.Path.getValue());
// adjust paint offset
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Rect frame = new Rect();
                getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
                int statusBarHeight = frame.top;
//                Logger.e("Doodle", "statusBarHeight =" + statusBarHeight);
                int marginTop = doodleView.getTop();
//                Logger.e("Doodle", "doodleView marginTop =" + marginTop);
                int marginLeft = doodleView.getLeft();
//                Logger.e("Doodle", "doodleView marginLeft =" + marginLeft);
                float offsetY = statusBarHeight + marginTop + DensityUtils.dip2px(getActivity(), 220 + 45);

                doodleView.setPaintOffset((float) marginLeft, offsetY);
                Logger.e("Doodle", "client1 offsetX = " + (float) marginLeft + ", offsetY = " + offsetY);
            }
        }, 50);
    }

    private void initData() {
        colorChooseMap.put(R.id.black_color_image, R.drawable.choose_black_circle_shape);
        colorChooseMap.put(R.id.red_color_image, R.drawable.choose_red_circle_shape);
        colorChooseMap.put(R.id.yellow_color_image, R.drawable.choose_yellow_circle_shape);
        colorChooseMap.put(R.id.green_color_image, R.drawable.choose_green_circle_shape);
        colorChooseMap.put(R.id.blue_color_image, R.drawable.choose_blue_circle_shape);
        colorChooseMap.put(R.id.purple_color_image, R.drawable.choose_purple_circle_shape);

        colorMap.put(R.id.black_color_image, Color.BLACK);
        colorMap.put(R.id.red_color_image, getResources().getColor(R.color.color_red_d1021c));
        colorMap.put(R.id.yellow_color_image, getResources().getColor(R.color.color_yellow_fddc01));
        colorMap.put(R.id.green_color_image, getResources().getColor(R.color.color_green_7dd21f));
        colorMap.put(R.id.blue_color_image, getResources().getColor(R.color.color_blue_228bf7));
        colorMap.put(R.id.purple_color_image, getResources().getColor(R.color.color_purple_9b0df5));
    }

    private void setListeners() {
        chooseColor.setOnClickListener(this);
        playBack.setOnClickListener(this);

        blackColorImage.setOnClickListener(colorClickListener);
        redColorImage.setOnClickListener(colorClickListener);
        yellowColorImage.setOnClickListener(colorClickListener);
        greenColorImage.setOnClickListener(colorClickListener);
        blueColorImage.setOnClickListener(colorClickListener);
        purpleColorImage.setOnClickListener(colorClickListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose_color:
                paletteLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.play_back:
                doodleView.paintBack();
                break;
        }
    }

    View.OnClickListener colorClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            paletteLayout.setVisibility(View.GONE);
            choosedColor = colorChooseMap.get(v.getId());
            chooseColor.setBackgroundResource(choosedColor);
            doodleView.setPaintColor(colorMap.get(v.getId()));
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        doodleView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (doodleView != null) {
            doodleView.end();
        }
        registerObservers(false);
    }

    private void registerObservers(boolean register) {
//        ChatRoomMemberCache.getInstance().registerMeetingControlObserver(meetingControlObserver, register);
        TransactionCenter.getInstance().registerOnlineStatusObserver(sessionId, this);
    }

    @Override
    public boolean onNetWorkChange(boolean isCreator) {
        initView();
        doodleView.clearAll();
        return true;
    }

    // 初始化是否开启白板
    public void initView() {
        if (ChatRoomMemberCache.getInstance().isRTSOpen()) {
            playBack.setBackgroundResource(R.mipmap.rts_back);
            playBack.setEnabled(true);
            doodleView.setEnableView(true);
            this.choosedColor = R.drawable.choose_blue_circle_shape;
            chooseColor.setBackgroundResource(R.drawable.choose_blue_circle_shape);
            chooseColor.setEnabled(true);
            joinTipText.setVisibility(View.GONE);
        } else {
            playBack.setBackgroundResource(R.mipmap.play_back_disable);
            chooseColor.setBackgroundResource(R.mipmap.choose_color_disable);
            chooseColor.setEnabled(false);
            playBack.setEnabled(false);
            doodleView.setEnableView(false);
            joinTipText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFlipPage(Transaction transaction) {

    }
}
