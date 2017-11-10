package cn.qatime.player.bean;

import java.io.Serializable;
import java.util.List;

import libraryextra.bean.ChatTeamBean;
import libraryextra.bean.TeacherBean;

/**
 * @author lungtify
 * @Time 2017/7/28 14:18
 * @Describe
 */

public class ExclusiveLessonPlayBean implements Serializable {

    /**
     * status : 1
     * data : {"customized_group":{"id":6,"name":"专属课之打秀海1","publicizes_url":{"app_info":"http://testing.qatime.cn/assets/groups/biology/app_info_default-ec859ff6298dc84ccd94ca0853ca9afd.png","list":"http://testing.qatime.cn/assets/groups/biology/list_default-388af7fe5d20628cfac3ba3fd5db6cd4.png","info":"http://testing.qatime.cn/assets/groups/biology/info_default-d66757088f78c5c5b966968d829208d0.png"},"subject":"生物","grade":"高三","status":"teaching","teacher_name":"解","price":15,"current_price":15,"sell_type":"charge","view_tickets_count":0,"events_count":5,"closed_events_count":0,"objective":"打死秀海1","suit_crowd":"I have a dream，One day，we will be flaying！","description":"<p>动词打次动动打次！动词打次动动打次！动词打次动动打次！动词打次动动打次！动词打次动动打次！动词打次动动打次！<\/p><p>动词打次动动打次！动词打次动动打次！动词打次动动打次！<br><\/p><p><br><\/p>","icons":{"refund_any_time":true,"coupon_free":true,"cheap_moment":false,"join_cheap":false,"free_taste":false},"start_at":1501689600,"end_at":1501776000,"teacher":{"id":2875,"name":"解","nick_name":"数学老师","avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/cec5bef74f3bc73b6f307341db4938c3.jpg","ex_big_avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/ex_big_cec5bef74f3bc73b6f307341db4938c3.jpg","login_mobile":"18600694947","email":null,"is_guest":false,"teaching_years":"within_three_years","category":"高中","subject":"数学","grade_range":[],"gender":"male","birthday":null,"province":null,"city":null,"school":null,"school_name":"","school_id":null,"desc":""},"offline_lessons":[{"id":16,"name":"线下，我们来打秀海1","class_date":"2017-08-04","start_time":"12:00","end_time":"12:30","status":"ready","replayable":false,"class_address":"805"},{"id":17,"name":"线下，我们来打秀海2","class_date":"2017-08-04","start_time":"17:00","end_time":"18:00","status":"ready","replayable":false,"class_address":"805"}],"scheduled_lessons":[{"id":13,"name":"我们来打秀海1","class_date":"2017-08-03","start_time":"18:00","end_time":"19:00","status":"init","replayable":false},{"id":14,"name":"我们来打秀海2","class_date":"2017-08-04","start_time":"11:00","end_time":"12:00","status":"ready","replayable":false},{"id":15,"name":"我们来打秀海3","class_date":"2017-08-04","start_time":"18:00","end_time":"19:30","status":"ready","replayable":false}],"chat_team":{"announcement":null,"team_id":"85754484","team_announcements":[],"accounts":[{"accid":"11415677f751dedc9a6dd91732cb01cc","name":"数学老师","icon":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/small_cec5bef74f3bc73b6f307341db4938c3.jpg"},{"accid":"06fca71991be76996df0d6c766c75c02","name":"111","icon":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/small_1a7fa9676cce0ec97873c2a3d481d0e2.png"},{"accid":"f443180ab1b20afa3f3cc510b78d0776","name":"王晓龙","icon":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/small_318714b08938204dd11a57172da90218.jpg"},{"accid":"0ba06bd120840dc0423db9d4cb861e99","name":"信雅壮","icon":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/small_16e2b0bef7e49fec9efb945cdf5894f0.jpg"}]},"board_pull_stream":"http://flv412c6bef.live.126.net/live/538d77961d5b4d57881b55cab4f4dbbf.flv?netease=flv412c6bef.live.126.net","camera_pull_stream":"http://flv412c6bef.live.126.net/live/8b68b644bdf54f3486e7de27f4335a8c.flv?netease=flv412c6bef.live.126.net"},"ticket":{"id":842,"used_count":0,"buy_count":5,"lesson_price":"3.0","status":"active","type":"LiveStudio::BuyTicket"}}
     */

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
        /**
         * customized_group : {"id":6,"name":"专属课之打秀海1","publicizes_url":{"app_info":"http://testing.qatime.cn/assets/groups/biology/app_info_default-ec859ff6298dc84ccd94ca0853ca9afd.png","list":"http://testing.qatime.cn/assets/groups/biology/list_default-388af7fe5d20628cfac3ba3fd5db6cd4.png","info":"http://testing.qatime.cn/assets/groups/biology/info_default-d66757088f78c5c5b966968d829208d0.png"},"subject":"生物","grade":"高三","status":"teaching","teacher_name":"解","price":15,"current_price":15,"sell_type":"charge","view_tickets_count":0,"events_count":5,"closed_events_count":0,"objective":"打死秀海1","suit_crowd":"I have a dream，One day，we will be flaying！","description":"<p>动词打次动动打次！动词打次动动打次！动词打次动动打次！动词打次动动打次！动词打次动动打次！动词打次动动打次！<\/p><p>动词打次动动打次！动词打次动动打次！动词打次动动打次！<br><\/p><p><br><\/p>","icons":{"refund_any_time":true,"coupon_free":true,"cheap_moment":false,"join_cheap":false,"free_taste":false},"start_at":1501689600,"end_at":1501776000,"teacher":{"id":2875,"name":"解","nick_name":"数学老师","avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/cec5bef74f3bc73b6f307341db4938c3.jpg","ex_big_avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/ex_big_cec5bef74f3bc73b6f307341db4938c3.jpg","login_mobile":"18600694947","email":null,"is_guest":false,"teaching_years":"within_three_years","category":"高中","subject":"数学","grade_range":[],"gender":"male","birthday":null,"province":null,"city":null,"school":null,"school_name":"","school_id":null,"desc":""},"offline_lessons":[{"id":16,"name":"线下，我们来打秀海1","class_date":"2017-08-04","start_time":"12:00","end_time":"12:30","status":"ready","replayable":false,"class_address":"805"},{"id":17,"name":"线下，我们来打秀海2","class_date":"2017-08-04","start_time":"17:00","end_time":"18:00","status":"ready","replayable":false,"class_address":"805"}],"scheduled_lessons":[{"id":13,"name":"我们来打秀海1","class_date":"2017-08-03","start_time":"18:00","end_time":"19:00","status":"init","replayable":false},{"id":14,"name":"我们来打秀海2","class_date":"2017-08-04","start_time":"11:00","end_time":"12:00","status":"ready","replayable":false},{"id":15,"name":"我们来打秀海3","class_date":"2017-08-04","start_time":"18:00","end_time":"19:30","status":"ready","replayable":false}],"chat_team":{"announcement":null,"team_id":"85754484","team_announcements":[],"accounts":[{"accid":"11415677f751dedc9a6dd91732cb01cc","name":"数学老师","icon":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/small_cec5bef74f3bc73b6f307341db4938c3.jpg"},{"accid":"06fca71991be76996df0d6c766c75c02","name":"111","icon":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/small_1a7fa9676cce0ec97873c2a3d481d0e2.png"},{"accid":"f443180ab1b20afa3f3cc510b78d0776","name":"王晓龙","icon":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/small_318714b08938204dd11a57172da90218.jpg"},{"accid":"0ba06bd120840dc0423db9d4cb861e99","name":"信雅壮","icon":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/small_16e2b0bef7e49fec9efb945cdf5894f0.jpg"}]},"board_pull_stream":"http://flv412c6bef.live.126.net/live/538d77961d5b4d57881b55cab4f4dbbf.flv?netease=flv412c6bef.live.126.net","camera_pull_stream":"http://flv412c6bef.live.126.net/live/8b68b644bdf54f3486e7de27f4335a8c.flv?netease=flv412c6bef.live.126.net"}
         * ticket : {"id":842,"used_count":0,"buy_count":5,"lesson_price":"3.0","status":"active","type":"LiveStudio::BuyTicket"}
         */

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
            /**
             * id : 6
             * name : 专属课之打秀海1
             * publicizes_url : {"app_info":"http://testing.qatime.cn/assets/groups/biology/app_info_default-ec859ff6298dc84ccd94ca0853ca9afd.png","list":"http://testing.qatime.cn/assets/groups/biology/list_default-388af7fe5d20628cfac3ba3fd5db6cd4.png","info":"http://testing.qatime.cn/assets/groups/biology/info_default-d66757088f78c5c5b966968d829208d0.png"}
             * subject : 生物
             * grade : 高三
             * status : teaching
             * teacher_name : 解
             * price : 15
             * current_price : 15
             * sell_type : charge
             * view_tickets_count : 0
             * events_count : 5
             * closed_events_count : 0
             * objective : 打死秀海1
             * suit_crowd : I have a dream，One day，we will be flaying！
             * description : <p>动词打次动动打次！动词打次动动打次！动词打次动动打次！动词打次动动打次！动词打次动动打次！动词打次动动打次！</p><p>动词打次动动打次！动词打次动动打次！动词打次动动打次！<br></p><p><br></p>
             * icons : {"refund_any_time":true,"coupon_free":true,"cheap_moment":false,"join_cheap":false,"free_taste":false}
             * start_at : 1501689600
             * end_at : 1501776000
             * teacher : {"id":2875,"name":"解","nick_name":"数学老师","avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/cec5bef74f3bc73b6f307341db4938c3.jpg","ex_big_avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/ex_big_cec5bef74f3bc73b6f307341db4938c3.jpg","login_mobile":"18600694947","email":null,"is_guest":false,"teaching_years":"within_three_years","category":"高中","subject":"数学","grade_range":[],"gender":"male","birthday":null,"province":null,"city":null,"school":null,"school_name":"","school_id":null,"desc":""}
             * offline_lessons : [{"id":16,"name":"线下，我们来打秀海1","class_date":"2017-08-04","start_time":"12:00","end_time":"12:30","status":"ready","replayable":false,"class_address":"805"},{"id":17,"name":"线下，我们来打秀海2","class_date":"2017-08-04","start_time":"17:00","end_time":"18:00","status":"ready","replayable":false,"class_address":"805"}]
             * scheduled_lessons : [{"id":13,"name":"我们来打秀海1","class_date":"2017-08-03","start_time":"18:00","end_time":"19:00","status":"init","replayable":false},{"id":14,"name":"我们来打秀海2","class_date":"2017-08-04","start_time":"11:00","end_time":"12:00","status":"ready","replayable":false},{"id":15,"name":"我们来打秀海3","class_date":"2017-08-04","start_time":"18:00","end_time":"19:30","status":"ready","replayable":false}]
             * chat_team : {"announcement":null,"team_id":"85754484","team_announcements":[],"accounts":[{"accid":"11415677f751dedc9a6dd91732cb01cc","name":"数学老师","icon":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/small_cec5bef74f3bc73b6f307341db4938c3.jpg"},{"accid":"06fca71991be76996df0d6c766c75c02","name":"111","icon":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/small_1a7fa9676cce0ec97873c2a3d481d0e2.png"},{"accid":"f443180ab1b20afa3f3cc510b78d0776","name":"王晓龙","icon":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/small_318714b08938204dd11a57172da90218.jpg"},{"accid":"0ba06bd120840dc0423db9d4cb861e99","name":"信雅壮","icon":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/small_16e2b0bef7e49fec9efb945cdf5894f0.jpg"}]}
             * board_pull_stream : http://flv412c6bef.live.126.net/live/538d77961d5b4d57881b55cab4f4dbbf.flv?netease=flv412c6bef.live.126.net
             * camera_pull_stream : http://flv412c6bef.live.126.net/live/8b68b644bdf54f3486e7de27f4335a8c.flv?netease=flv412c6bef.live.126.net
             */

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
            private List<OfflineLessonsBean> offline_lessons;
            private List<ScheduledLessonsBean> scheduled_lessons;

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

            public List<OfflineLessonsBean> getOffline_lessons() {
                return offline_lessons;
            }

            public void setOffline_lessons(List<OfflineLessonsBean> offline_lessons) {
                this.offline_lessons = offline_lessons;
            }

            public List<ScheduledLessonsBean> getScheduled_lessons() {
                return scheduled_lessons;
            }

            public void setScheduled_lessons(List<ScheduledLessonsBean> scheduled_lessons) {
                this.scheduled_lessons = scheduled_lessons;
            }

            public static class PublicizesUrlBean {
                /**
                 * app_info : http://testing.qatime.cn/assets/groups/biology/app_info_default-ec859ff6298dc84ccd94ca0853ca9afd.png
                 * list : http://testing.qatime.cn/assets/groups/biology/list_default-388af7fe5d20628cfac3ba3fd5db6cd4.png
                 * info : http://testing.qatime.cn/assets/groups/biology/info_default-d66757088f78c5c5b966968d829208d0.png
                 */

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
                /**
                 * refund_any_time : true
                 * coupon_free : true
                 * cheap_moment : false
                 * join_cheap : false
                 * free_taste : false
                 */

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

            public static class OfflineLessonsBean {
                /**
                 * id : 16
                 * name : 线下，我们来打秀海1
                 * class_date : 2017-08-04
                 * start_time : 12:00
                 * end_time : 12:30
                 * status : ready
                 * replayable : false
                 * class_address : 805
                 */

                private int id;
                private String name;
                private String class_date;
                private String start_time;
                private String end_time;
                private String status;
                private boolean replayable;
                private String class_address;

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

                public String getClass_date() {
                    return class_date;
                }

                public void setClass_date(String class_date) {
                    this.class_date = class_date;
                }

                public String getStart_time() {
                    return start_time;
                }

                public void setStart_time(String start_time) {
                    this.start_time = start_time;
                }

                public String getEnd_time() {
                    return end_time;
                }

                public void setEnd_time(String end_time) {
                    this.end_time = end_time;
                }

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }

                public boolean isReplayable() {
                    return replayable;
                }

                public void setReplayable(boolean replayable) {
                    this.replayable = replayable;
                }

                public String getClass_address() {
                    return class_address;
                }

                public void setClass_address(String class_address) {
                    this.class_address = class_address;
                }
            }

            public static class ScheduledLessonsBean {
                /**
                 * id : 13
                 * name : 我们来打秀海1
                 * class_date : 2017-08-03
                 * start_time : 18:00
                 * end_time : 19:00
                 * status : init
                 * replayable : false
                 */

                private int id;
                private String name;
                private String class_date;
                private String start_time;
                private String end_time;
                private String status;
                private boolean replayable;

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

                public String getClass_date() {
                    return class_date;
                }

                public void setClass_date(String class_date) {
                    this.class_date = class_date;
                }

                public String getStart_time() {
                    return start_time;
                }

                public void setStart_time(String start_time) {
                    this.start_time = start_time;
                }

                public String getEnd_time() {
                    return end_time;
                }

                public void setEnd_time(String end_time) {
                    this.end_time = end_time;
                }

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }

                public boolean isReplayable() {
                    return replayable;
                }

                public void setReplayable(boolean replayable) {
                    this.replayable = replayable;
                }
            }
        }

        public static class TicketBean {
            /**
             * id : 842
             * used_count : 0
             * buy_count : 5
             * lesson_price : 3.0
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
