/*
 * Copyright (C) 2013 Chen Hui <calmer91@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.qatime.player.barrage.controller;


import cn.qatime.player.barrage.model.AbsDisplayer;
import cn.qatime.player.barrage.model.BaseDanmaku;
import cn.qatime.player.barrage.model.IDanmakus;
import cn.qatime.player.barrage.parser.BaseDanmakuParser;
import cn.qatime.player.barrage.renderer.IRenderer;

public interface IDrawTask {
    public static final int PLAY_STATE_PLAYING = 1;
    public static final int PLAY_STATE_PAUSE = 2;

    public void addDanmaku(BaseDanmaku item);

    public void removeAllDanmakus(boolean isClearDanmakusOnScreen);

    public void removeAllLiveDanmakus();

    public void clearDanmakusOnScreen(long currMillis);

	public IDanmakus getVisibleDanmakusOnTime(long time);

    public IRenderer.RenderingState draw(AbsDisplayer displayer);

    public void reset();

    public void seek(long mills);

    public void start();

    public void quit();

    public void prepare();

    public void onPlayStateChanged(int state);

    public void requestClear();

    void requestClearRetainer();

    public void setParser(BaseDanmakuParser parser);

    void invalidateDanmaku(BaseDanmaku item, boolean remeasure);

    public interface TaskListener {
        public void ready();

        public void onDanmakuAdd(BaseDanmaku danmaku);

        public void onDanmakuShown(BaseDanmaku danmaku);

        public void onDanmakuConfigChanged();

        public void onDanmakusDrawingFinished();
    }

    public void requestHide();

}
