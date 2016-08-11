package cn.qatime.player.bean;

import java.io.Serializable;

public class Profile implements Serializable{

    private int status;

    private Data data;

    public void setStatus(int status){
        this.status = status;
    }
    public int getStatus(){
        return this.status;
    }
    public void setData(Data data){
        this.data = data;
    }
    public Data getData(){
        return this.data;
    }

    public String getToken(){
        return this.data!=null&&this.data.remember_token!=null?this.data.remember_token:"";
    }


    public class Data implements Serializable{
        private String remember_token;

        private User user;

        public void setRemember_token(String remember_token){
            this.remember_token = remember_token;
        }
        public String getRemember_token(){
            return this.remember_token;
        }
        public void setUser(User user){
            this.user = user;
        }
        public User getUser(){
            return this.user;
        }

    }public class User implements Serializable{
        private int id;

        private String name;

        private String nick_name;

        private String small_avatar_url;

        public void setId(int id){
            this.id = id;
        }
        public int getId(){
            return this.id;
        }
        public void setName(String name){
            this.name = name;
        }
        public String getName(){
            return this.name;
        }
        public void setNick_name(String nick_name){
            this.nick_name = nick_name;
        }
        public String getNick_name(){
            return this.nick_name;
        }
        public void setSmall_avatar_url(String small_avatar_url){
            this.small_avatar_url = small_avatar_url;
        }
        public String getSmall_avatar_url(){
            return this.small_avatar_url;
        }

    }
}
