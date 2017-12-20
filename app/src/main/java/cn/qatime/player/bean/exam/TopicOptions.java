package cn.qatime.player.bean.exam;

import java.io.Serializable;

/**
 * @author luntify
 * @date 2017/12/14 16:56
 * @Description:
 */

public class TopicOptions implements Serializable {

    /**
     * id : 4
     * topic_id : 3
     * name : A
     * title : AAAAA
     * correct : false
     * created_at : 2017-12-13T16:25:22.914+08:00
     * updated_at : 2017-12-13T18:01:14.331+08:00
     */

    private int id;
    private int topic_id;
    private String name;
    private String title;
    private boolean correct;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(int topic_id) {
        this.topic_id = topic_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}
