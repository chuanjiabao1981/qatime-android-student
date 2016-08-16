package cn.qatime.player.bean;

import java.io.Serializable;

/**
 * @author luntify
 * @date 2016/8/16 16:05
 * @Description 跳转到支付页面所需的bean
 */
public class OrderPayBean implements Serializable {
    public String image;
    public String name;

    public String subject;
    public String grade;
    public int classnumber;
    public String teacher;
    public String classstarttime;
    public String classendtime;
    public String status;
    public int price;
}
