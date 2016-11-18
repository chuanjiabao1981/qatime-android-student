package cn.qatime.player.bean;

import java.io.Serializable;

/**
 * @author lungtify
 * @Time 2016/9/21 21:27
 * @Describe
 */
public class ChatVideoBean implements Serializable {
    private int courseId;
    private String camera;
    private String  board;
    private String name;

    public ChatVideoBean(int courseId, String camera,String board, String name) {
        this.camera=camera;
        this.board=board;
        this.courseId = courseId;
        this.name = name;
    }

    public String getCamera() {
        return camera;
    }

    public String getBoard() {
        return board;
    }

    public int getCourseId() {
        return courseId;
    }


    public String getName() {
        return name;
    }
}
