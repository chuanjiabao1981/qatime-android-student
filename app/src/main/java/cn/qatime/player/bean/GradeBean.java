package cn.qatime.player.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author luntify
 * @date 2016/8/12 14:16
 * @Description 基本数据   班级
 */
public class GradeBean implements Serializable {
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

    public class Data {
        private List<String> grades;

        public void setString(List<String> grades) {
            this.grades = grades;
        }

        public List<String> getGrades() {
            return this.grades;
        }

    }
}
