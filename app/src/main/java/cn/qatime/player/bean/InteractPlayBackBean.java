package cn.qatime.player.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author lungtify
 * @Time 2017/1/17 18:22
 * @Describe
 */

public class InteractPlayBackBean implements Serializable {

    private int status;

    private Data data;

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return this.data;
    }

    public class Data implements Serializable {
        private int id;

        private String name;

        private String duration;

        private boolean replayable;

        private boolean user_playable;

        private int user_play_times;

        private int left_replay_times;

        private List<Replay> replay;

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return this.id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getDuration() {
            return this.duration;
        }

        public void setReplayable(boolean replayable) {
            this.replayable = replayable;
        }

        public boolean getReplayable() {
            return this.replayable;
        }

        public void setUser_playable(boolean user_playable) {
            this.user_playable = user_playable;
        }

        public boolean getUser_playable() {
            return this.user_playable;
        }

        public void setUser_play_times(int user_play_times) {
            this.user_play_times = user_play_times;
        }

        public int getUser_play_times() {
            return this.user_play_times;
        }

        public void setLeft_replay_times(int left_replay_times) {
            this.left_replay_times = left_replay_times;
        }

        public int getLeft_replay_times() {
            return this.left_replay_times;
        }

        public void setReplay(List<Replay> replay) {
            this.replay = replay;
        }

        public List<Replay> getReplay() {
            return this.replay;
        }

    }

    public class Replay implements Serializable {
        private int id;

        private String duration;

        private String format;

        private String url;

        private String hd_url;

        private String shd_url;

        private String orig_url;

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return this.id;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getDuration() {
            return this.duration;
        }

        public void setFormat(String format) {
            this.format = format;
        }

        public String getFormat() {
            return this.format;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUrl() {
            return this.url;
        }

        public void setHd_url(String hd_url) {
            this.hd_url = hd_url;
        }

        public String getHd_url() {
            return this.hd_url;
        }

        public void setShd_url(String shd_url) {
            this.shd_url = shd_url;
        }

        public String getShd_url() {
            return this.shd_url;
        }

        public void setOrig_url(String orig_url) {
            this.orig_url = orig_url;
        }

        public String getOrig_url() {
            return this.orig_url;
        }

    }
}
