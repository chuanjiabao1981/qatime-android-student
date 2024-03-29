package cn.qatime.player.barrage.model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;

/**
 * Created by ch on 15-7-16.
 */
public abstract class BaseCacheStuffer {


    public static abstract class Proxy {
        /**
         * 在弹幕显示前使用新的text,使用新的text
         * @param danmaku
         * @param fromWorkerThread 是否在工作(非UI)线程,在true的情况下可以做一些耗时操作(例如更新Span的drawblae或者其他IO操作)
         * @return 如果不需重置，直接返回danmaku.text
         */
        public abstract void prepareDrawing(BaseDanmaku danmaku, boolean fromWorkerThread);

        public abstract void releaseResource(BaseDanmaku danmaku);

    }

    protected Proxy mProxy;

    /**
     * set paintWidth, paintHeight to danmaku
     * @param danmaku
     * @param fromWorkerThread
     */
    public abstract void measure(BaseDanmaku danmaku, TextPaint paint, boolean fromWorkerThread);

    /**
     * clear caches which created by this stuffer
     */
    public abstract void clearCaches();

    public abstract void drawDanmaku(BaseDanmaku danmaku, Canvas canvas, float left, float top, boolean fromWorkerThread, AndroidDisplayer.DisplayerConfig displayerConfig);

    public abstract boolean drawCache(BaseDanmaku danmaku, Canvas canvas, float left, float top, Paint alphaPaint, TextPaint paint);

    public void clearCache(BaseDanmaku danmaku) {

    }

    public void setProxy(Proxy adapter) {
        mProxy = adapter;
    }

    public void releaseResource(BaseDanmaku danmaku) {
        if (mProxy != null) {
            mProxy.releaseResource(danmaku);
        }
    }

}
