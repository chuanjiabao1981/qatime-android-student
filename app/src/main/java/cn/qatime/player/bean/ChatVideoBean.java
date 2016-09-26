package cn.qatime.player.bean;

import java.io.Serializable;

/**
 * @author lungtify
 * @Time 2016/9/21 21:27
 * @Describe
 */
public class ChatVideoBean implements Serializable {
    private int courseId;
    private String pull_address;
    private String name;

    public ChatVideoBean(int courseId, String pull_address, String name) {
        this.courseId = courseId;
        this.pull_address = pull_address;
        this.name = name;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getPull_address() {
        return pull_address;
    }

    public String getName() {
        return name;
    }
}
