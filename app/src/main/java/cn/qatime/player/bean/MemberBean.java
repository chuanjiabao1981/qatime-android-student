package cn.qatime.player.bean;

import java.io.Serializable;

/**
 * @author luntify
 * @date 2016/8/9 17:22
 * @Description 成员列表单个item数据
 */
public class MemberBean implements Serializable {
    private String name;
    private String firstLetter;

    public MemberBean(String name, String firstLetter) {
        this.name = name;
        this.firstLetter = firstLetter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }
}
