package cn.qatime.player.bean;

import java.util.List;

/**
 * Created by lenovo on 2017/8/29.
 */

public class MyFilesBean {


    private int status;
    private List<DataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 2
         * name : null
         * type : Resource::PictureFile
         * file_size : 19637.0
         * ext_name : png
         */

        private int id;
        private String name;
        private String type;
        private String file_size;
        private String ext_name;
        private String file_url;

        public String getFile_url() {
            return file_url;
        }

        public void setFile_url(String file_url) {
            this.file_url = file_url;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getFile_size() {
            return file_size;
        }

        public void setFile_size(String file_size) {
            this.file_size = file_size;
        }

        public String getExt_name() {
            return ext_name;
        }

        public void setExt_name(String ext_name) {
            this.ext_name = ext_name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            DataBean dataBean = (DataBean) o;

            return id == dataBean.id;
        }

        @Override
        public int hashCode() {
            return 1;
        }
    }
}
