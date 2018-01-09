package cn.qatime.player.bean.exam;

import java.io.Serializable;

/**
 * @author luntify
 * @date 2017/12/28 15:14
 * @Description:
 */

public class ExamResult implements Serializable {

    private int id;
    private Paper paper;

    public Paper getPaper() {
        return paper;
    }

    public void setPaper(Paper paper) {
        this.paper = paper;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

