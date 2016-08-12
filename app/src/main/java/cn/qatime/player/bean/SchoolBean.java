package cn.qatime.player.bean;

import java.util.List;

/**
 * @author luntify
 * @date 2016/8/12 14:41
 * @Description 基础数据  学校
 */
public class SchoolBean {

    private int status;

    private List<Data> data;

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public List<Data> getData() {
        return this.data;
    }

    public class Data {
        private int id;

        private int city_id;

        private String name;

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return this.id;
        }

        public void setCity_id(int city_id) {
            this.city_id = city_id;
        }

        public int getCity_id() {
            return this.city_id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

    }
}
