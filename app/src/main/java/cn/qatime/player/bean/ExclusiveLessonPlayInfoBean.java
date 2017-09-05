package cn.qatime.player.bean;

import java.util.List;

import libraryextra.bean.ChatTeamBean;

/**
 * Created by lenovo on 2017/9/5.
 */

public class ExclusiveLessonPlayInfoBean {

    private int status;
    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {


        private CustomizedGroupBean customized_group;
        private TicketBean ticket;

        public CustomizedGroupBean getCustomized_group() {
            return customized_group;
        }

        public void setCustomized_group(CustomizedGroupBean customized_group) {
            this.customized_group = customized_group;
        }

        public TicketBean getTicket() {
            return ticket;
        }

        public void setTicket(TicketBean ticket) {
            this.ticket = ticket;
        }

        public static class CustomizedGroupBean {


            private int id;
            private String name;
            private PublicizesUrlBean publicizes_url;
            private String subject;
            private String grade;
            private String status;
            private String teacher_name;
            private int price;
            private int current_price;
            private String sell_type;
            private int view_tickets_count;
            private int events_count;
            private int closed_events_count;
            private String objective;
            private String suit_crowd;
            private String description;
            private IconsBean icons;
            private int start_at;
            private int end_at;
            private TeacherBean teacher;
            private ChatTeamBean chat_team;
            private String board_pull_stream;
            private String camera_pull_stream;

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

            public PublicizesUrlBean getPublicizes_url() {
                return publicizes_url;
            }

            public void setPublicizes_url(PublicizesUrlBean publicizes_url) {
                this.publicizes_url = publicizes_url;
            }

            public String getSubject() {
                return subject;
            }

            public void setSubject(String subject) {
                this.subject = subject;
            }

            public String getGrade() {
                return grade;
            }

            public void setGrade(String grade) {
                this.grade = grade;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getTeacher_name() {
                return teacher_name;
            }

            public void setTeacher_name(String teacher_name) {
                this.teacher_name = teacher_name;
            }

            public int getPrice() {
                return price;
            }

            public void setPrice(int price) {
                this.price = price;
            }

            public int getCurrent_price() {
                return current_price;
            }

            public void setCurrent_price(int current_price) {
                this.current_price = current_price;
            }

            public String getSell_type() {
                return sell_type;
            }

            public void setSell_type(String sell_type) {
                this.sell_type = sell_type;
            }

            public int getView_tickets_count() {
                return view_tickets_count;
            }

            public void setView_tickets_count(int view_tickets_count) {
                this.view_tickets_count = view_tickets_count;
            }

            public int getEvents_count() {
                return events_count;
            }

            public void setEvents_count(int events_count) {
                this.events_count = events_count;
            }

            public int getClosed_events_count() {
                return closed_events_count;
            }

            public void setClosed_events_count(int closed_events_count) {
                this.closed_events_count = closed_events_count;
            }

            public String getObjective() {
                return objective;
            }

            public void setObjective(String objective) {
                this.objective = objective;
            }

            public String getSuit_crowd() {
                return suit_crowd;
            }

            public void setSuit_crowd(String suit_crowd) {
                this.suit_crowd = suit_crowd;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public IconsBean getIcons() {
                return icons;
            }

            public void setIcons(IconsBean icons) {
                this.icons = icons;
            }

            public int getStart_at() {
                return start_at;
            }

            public void setStart_at(int start_at) {
                this.start_at = start_at;
            }

            public int getEnd_at() {
                return end_at;
            }

            public void setEnd_at(int end_at) {
                this.end_at = end_at;
            }

            public TeacherBean getTeacher() {
                return teacher;
            }

            public void setTeacher(TeacherBean teacher) {
                this.teacher = teacher;
            }

            public ChatTeamBean getChat_team() {
                return chat_team;
            }

            public void setChat_team(ChatTeamBean chat_team) {
                this.chat_team = chat_team;
            }

            public String getBoard_pull_stream() {
                return board_pull_stream;
            }

            public void setBoard_pull_stream(String board_pull_stream) {
                this.board_pull_stream = board_pull_stream;
            }

            public String getCamera_pull_stream() {
                return camera_pull_stream;
            }

            public void setCamera_pull_stream(String camera_pull_stream) {
                this.camera_pull_stream = camera_pull_stream;
            }

        }

        public static class PublicizesUrlBean {

            private String app_info;
            private String list;
            private String info;

            public String getApp_info() {
                return app_info;
            }

            public void setApp_info(String app_info) {
                this.app_info = app_info;
            }

            public String getList() {
                return list;
            }

            public void setList(String list) {
                this.list = list;
            }

            public String getInfo() {
                return info;
            }

            public void setInfo(String info) {
                this.info = info;
            }
        }

        public static class IconsBean {


            private boolean refund_any_time;
            private boolean coupon_free;
            private boolean cheap_moment;
            private boolean join_cheap;
            private boolean free_taste;

            public boolean isRefund_any_time() {
                return refund_any_time;
            }

            public void setRefund_any_time(boolean refund_any_time) {
                this.refund_any_time = refund_any_time;
            }

            public boolean isCoupon_free() {
                return coupon_free;
            }

            public void setCoupon_free(boolean coupon_free) {
                this.coupon_free = coupon_free;
            }

            public boolean isCheap_moment() {
                return cheap_moment;
            }

            public void setCheap_moment(boolean cheap_moment) {
                this.cheap_moment = cheap_moment;
            }

            public boolean isJoin_cheap() {
                return join_cheap;
            }

            public void setJoin_cheap(boolean join_cheap) {
                this.join_cheap = join_cheap;
            }

            public boolean isFree_taste() {
                return free_taste;
            }

            public void setFree_taste(boolean free_taste) {
                this.free_taste = free_taste;
            }
        }

        public static class TeacherBean {


            private int id;
            private String name;
            private String nick_name;
            private String avatar_url;
            private String ex_big_avatar_url;
            private String login_mobile;
            private String email;
            private boolean is_guest;
            private String teaching_years;
            private String category;
            private String subject;
            private String gender;
            private String birthday;
            private int province;
            private int city;
            private int school;
            private String school_name;
            private int school_id;
            private String desc;
            private List<String> grade_range;

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

            public String getNick_name() {
                return nick_name;
            }

            public void setNick_name(String nick_name) {
                this.nick_name = nick_name;
            }

            public String getAvatar_url() {
                return avatar_url;
            }

            public void setAvatar_url(String avatar_url) {
                this.avatar_url = avatar_url;
            }

            public String getEx_big_avatar_url() {
                return ex_big_avatar_url;
            }

            public void setEx_big_avatar_url(String ex_big_avatar_url) {
                this.ex_big_avatar_url = ex_big_avatar_url;
            }

            public String getLogin_mobile() {
                return login_mobile;
            }

            public void setLogin_mobile(String login_mobile) {
                this.login_mobile = login_mobile;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public boolean isIs_guest() {
                return is_guest;
            }

            public void setIs_guest(boolean is_guest) {
                this.is_guest = is_guest;
            }

            public String getTeaching_years() {
                return teaching_years;
            }

            public void setTeaching_years(String teaching_years) {
                this.teaching_years = teaching_years;
            }

            public String getCategory() {
                return category;
            }

            public void setCategory(String category) {
                this.category = category;
            }

            public String getSubject() {
                return subject;
            }

            public void setSubject(String subject) {
                this.subject = subject;
            }

            public String getGender() {
                return gender;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }

            public String getBirthday() {
                return birthday;
            }

            public void setBirthday(String birthday) {
                this.birthday = birthday;
            }

            public int getProvince() {
                return province;
            }

            public void setProvince(int province) {
                this.province = province;
            }

            public int getCity() {
                return city;
            }

            public void setCity(int city) {
                this.city = city;
            }

            public int getSchool() {
                return school;
            }

            public void setSchool(int school) {
                this.school = school;
            }

            public String getSchool_name() {
                return school_name;
            }

            public void setSchool_name(String school_name) {
                this.school_name = school_name;
            }

            public int getSchool_id() {
                return school_id;
            }

            public void setSchool_id(int school_id) {
                this.school_id = school_id;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public List<String> getGrade_range() {
                return grade_range;
            }

            public void setGrade_range(List<String> grade_range) {
                this.grade_range = grade_range;
            }
        }
        public static class TicketBean {
            /**
             * id : 834
             * used_count : 0
             * buy_count : 2
             * lesson_price : 50.0
             * status : active
             * type : LiveStudio::BuyTicket
             */

            private int id;
            private int used_count;
            private int buy_count;
            private String lesson_price;
            private String status;
            private String type;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getUsed_count() {
                return used_count;
            }

            public void setUsed_count(int used_count) {
                this.used_count = used_count;
            }

            public int getBuy_count() {
                return buy_count;
            }

            public void setBuy_count(int buy_count) {
                this.buy_count = buy_count;
            }

            public String getLesson_price() {
                return lesson_price;
            }

            public void setLesson_price(String lesson_price) {
                this.lesson_price = lesson_price;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }
    }
}
