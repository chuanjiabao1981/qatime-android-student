package cn.qatime.player.barrage;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.Spanned;
import android.text.TextPaint;

import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.List;

import cn.qatime.player.barrage.controller.DrawHandler;
import cn.qatime.player.barrage.controller.IDanmakuView;
import cn.qatime.player.barrage.model.BaseCacheStuffer;
import cn.qatime.player.barrage.model.BaseDanmaku;
import cn.qatime.player.barrage.model.DanmakuContext;
import cn.qatime.player.barrage.model.DanmakuTimer;
import cn.qatime.player.barrage.model.Danmakus;
import cn.qatime.player.barrage.model.IDisplayer;
import cn.qatime.player.barrage.model.SpannedCacheStuffer;
import cn.qatime.player.barrage.model.Status;
import cn.qatime.player.barrage.parser.BaseDanmakuParser;
import cn.qatime.player.utils.ExpressionUtil;
import libraryextra.utils.DensityUtils;
import libraryextra.utils.StringUtils;

public class DanmuControl {

    //弹幕显示的时间(如果是list的话，会 * i)，记得加上mDanmakuView.getCurrentTime()
    private static final long ADD_DANMU_TIME = 2000;

    private float DANMU_TEXT_SIZE = 12f;//弹幕字体的大小

    //这两个用来控制两行弹幕之间的间距
    private int DANMU_PADDING = 8;
    private int DANMU_PADDING_INNER = 6;
    private int DANMU_RADIUS = 10;//圆角半径

    private IDanmakuView mDanmakuView;
    private DanmakuContext mDanmakuContext;
    private Status status = Status.SHOW;

    public DanmuControl(Context context) {
        setSize(context);
        initDanmuConfig();
    }

    /**
     * 对数值进行转换，适配手机，必须在初始化之前，否则有些数据不会起作用
     */
    private void setSize(Context context) {
        DANMU_PADDING = DensityUtils.dp2px(context, DANMU_PADDING);
        DANMU_PADDING_INNER = DensityUtils.dp2px(context, DANMU_PADDING_INNER);
        DANMU_RADIUS = DensityUtils.dp2px(context, DANMU_RADIUS);
        DANMU_TEXT_SIZE = DensityUtils.sp2px(context, DANMU_TEXT_SIZE);
    }

    /**
     * 初始化配置
     */
    private void initDanmuConfig() {
        // 设置最大显示行数
        HashMap<Integer, Integer> maxLinesPair = new HashMap<Integer, Integer>();
        maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 6); // 滚动弹幕最大显示2行
        // 设置是否禁止重叠
        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<Integer, Boolean>();
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);

        mDanmakuContext = DanmakuContext.create();
        mDanmakuContext
                .setDanmakuStyle(IDisplayer.DANMAKU_STYLE_NONE)
                .setDuplicateMergingEnabled(false)
                .setScrollSpeedFactor(1.2f)//越大速度越慢
                .setScaleTextSize(1.2f)
                .setCacheStuffer(new SpannedCacheStuffer(), mCacheStufferAdapter) // 图文混排使用SpannedCacheStuffer
//                .setCacheStuffer(new BackgroundCacheStuffer(), mCacheStufferAdapter)
                .setMaximumLines(maxLinesPair)
                .preventOverlapping(overlappingEnablePair);
    }

    /**
     * 绘制背景(自定义弹幕样式)
     */
    private class BackgroundCacheStuffer extends SpannedCacheStuffer {
        // 通过扩展SimpleTextCacheStuffer或SpannedCacheStuffer个性化你的弹幕样式
        final Paint paint = new Paint();

        @Override
        public void measure(BaseDanmaku danmaku, TextPaint paint, boolean fromWorkerThread) {
            super.measure(danmaku, paint, fromWorkerThread);
        }

        @Override
        public void drawBackground(BaseDanmaku danmaku, Canvas canvas, float left, float top) {
            paint.setAntiAlias(true);
            canvas.drawRoundRect(new RectF(left + DANMU_PADDING_INNER, top + DANMU_PADDING_INNER
                            , left + danmaku.paintWidth - DANMU_PADDING_INNER + 6,
                            top + danmaku.paintHeight - DANMU_PADDING_INNER + 6),//+6 主要是底部被截得太厉害了，+6是增加padding的效果
                    DANMU_RADIUS, DANMU_RADIUS, paint);
        }

        @Override
        public void drawStroke(BaseDanmaku danmaku, String lineText, Canvas canvas, float left, float top, Paint paint) {
            // 禁用描边绘制
        }
    }

    private BaseCacheStuffer.Proxy mCacheStufferAdapter = new BaseCacheStuffer.Proxy() {

        @Override
        public void prepareDrawing(final BaseDanmaku danmaku, boolean fromWorkerThread) {
//            if (danmaku.text instanceof Spanned) { // 根据你的条件检查是否需要需要更新弹幕
//            }
        }

        @Override
        public void releaseResource(BaseDanmaku danmaku) {
            //  重要:清理含有ImageSpan的text中的一些占用内存的资源 例如drawable
            if (danmaku.text instanceof Spanned) {
                danmaku.text = "";
            }
        }
    };

    public void setDanmakuView(IDanmakuView danmakuView) {
        this.mDanmakuView = danmakuView;
        initDanmuView();
    }

    private void initDanmuView() {
        if (mDanmakuView != null) {
            mDanmakuView.setCallback(new DrawHandler.Callback() {
                @Override
                public void prepared() {
                    mDanmakuView.start();
                }

                @Override
                public void updateTimer(DanmakuTimer timer) {

                }

                @Override
                public void danmakuShown(BaseDanmaku danmaku) {

                }

                @Override
                public void drawingFinished() {

                }
            });
        }

        mDanmakuView.prepare(new BaseDanmakuParser() {

            @Override
            protected Danmakus parse() {
                return new Danmakus();
            }
        }, mDanmakuContext);
        mDanmakuView.enableDanmakuDrawingCache(true);
    }

    public void pause() {
        if (mDanmakuView != null && mDanmakuView.isPrepared()) {
            mDanmakuView.pause();
        }
    }

    public void hide() {
        if (mDanmakuView != null) {
            mDanmakuView.hide();
            status = Status.HIDE;
        }
    }

    public void show() {
        if (mDanmakuView != null) {
            mDanmakuView.show();
            status = Status.SHOW;
        }
    }

    public void resume() {
        if (mDanmakuView != null && mDanmakuView.isPrepared() && mDanmakuView.isPaused()) {
            mDanmakuView.resume();
        }
    }

    public void destroy() {
        if (mDanmakuView != null) {
            mDanmakuView.release();
            mDanmakuView = null;
        }
    }

    public void addDanmuList(final List<IMMessage> danmuLists) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < danmuLists.size(); i++) {
                    addDanmu(danmuLists.get(i), i);
                }
            }
        }).start();
    }

    public void addDanmu(IMMessage danmu, int i) {
        String result = ExpressionUtil.getExpressionString(danmu.getContent(), ExpressionUtil.emoji);
        if (!StringUtils.isNullOrBlanK(result)) {
            BaseDanmaku danmaku = mDanmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);

            danmaku.text = result;

            danmaku.padding = DANMU_PADDING;
            danmaku.priority = 0;  // 1:一定会显示, 一般用于本机发送的弹幕,但会导致行数的限制失效
            danmaku.isLive = false;
            danmaku.time = mDanmakuView.getCurrentTime() + (i * ADD_DANMU_TIME);
            danmaku.textSize = DANMU_TEXT_SIZE/* * (mDanmakuContext.getDisplayer().getDensity() - 0.6f)*/;
            danmaku.textColor = 0xff999999;
            danmaku.textShadowColor = 0; // 重要：如果有图文混排，最好不要设置描边(设textShadowColor=0)，否则会进行两次复杂的绘制导致运行效率降低
            mDanmakuView.addDanmaku(danmaku);
             Logger.e("弹幕收到消息");
        }
    }

    public Status getStatus() {
        return status;
    }
}
