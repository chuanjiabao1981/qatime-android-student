package cn.qatime.player.bean;

import java.io.Serializable;

/**
 * @author lungtify
 * @Time 2016/9/21 21:27
 * @Describe
 */
public class ChatVideoBean implements Serializable {
    private int courseId;
    private String board;
    private String camera;
    private String name;

    public ChatVideoBean(int courseId, String camera, String board, String name) {
        this.courseId = courseId;
        this.camera = camera;
        this.board = board;
        this.name = name;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getBoard() {
        return board;
    }

    public String getCamera() {
        return camera;
    }

    public String getName() {
        return name;
    }
}
