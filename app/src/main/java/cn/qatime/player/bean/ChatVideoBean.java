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
    private String chat_team_owner;

    public ChatVideoBean(int courseId, String camera, String board, String name,String chat_team_owner) {
        this.courseId = courseId;
        this.camera = camera;
        this.board = board;
        this.name = name;
        this.chat_team_owner = chat_team_owner;
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

    public String getChat_team_owner() {
        return chat_team_owner;
    }

    public void setChat_team_owner(String chat_team_owner) {
        this.chat_team_owner = chat_team_owner;
    }
}
