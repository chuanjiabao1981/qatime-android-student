package cn.qatime.player.bean;

import java.io.Serializable;
import java.util.List;

import libraryextra.bean.TeacherBean;
import libraryextra.utils.StringUtils;

/**
 * @author lungtify
 * @Time 2017/3/21 14:20
 * @Describe 精选内容
 */

public class EssenceContentBean implements Serializable {


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


        private int index;
        private String logo_url;
        private String tag_one;
        private int target_id;
        private String target_type;
        private String title;
        private String type;
        //        @SerializedName(value = "essenceCourse", alternate = {"live_studio_course","live_studio_video_course","live_studio_interactive_course","live_studio_customized_group"})
        private EssenceContentCourseBean essenceCourse;
        private EssenceContentCourseBean live_studio_course;
        private EssenceContentCourseBean live_studio_video_course;
        private EssenceContentCourseBean live_studio_interactive_course;
        private EssenceContentCourseBean live_studio_customized_group;

        public EssenceContentCourseBean getEssenceCourse() {
            if (live_studio_course != null) {
                essenceCourse = live_studio_course;
            } else if (live_studio_video_course != null) {
                essenceCourse = live_studio_video_course;
            } else if (live_studio_interactive_course != null) {
                essenceCourse = live_studio_interactive_course;
            } else if (live_studio_customized_group != null) {
                essenceCourse = live_studio_customized_group;
            }
            return essenceCourse;
        }

        public void setEssenceCourse(EssenceContentCourseBean essenceCourse) {
            this.essenceCourse = essenceCourse;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getLogo_url() {
            return logo_url;
        }

        public void setLogo_url(String logo_url) {
            this.logo_url = logo_url;
        }

        public String getTag_one() {
            return tag_one;
        }

        public void setTag_one(String tag_one) {
            this.tag_one = tag_one;
        }

        public int getTarget_id() {
            return target_id;
        }

        public void setTarget_id(int target_id) {
            this.target_id = target_id;
        }

        public String getTarget_type() {
            return target_type;
        }

        public void setTarget_type(String target_type) {
            this.target_type = target_type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public static class EssenceContentCourseBean {

            private String grade;
            private int id;
            private String name;
            private String subject;
            private String teacher_name;
            private List<TeacherBean> teachers;

            public String getTeacher_name() {
                if (!StringUtils.isNullOrBlanK(teacher_name)) {
                    return teacher_name;
                } else {
                    if (teachers != null && teachers.size() > 0) {
                        return teachers.get(0).getName();
                    } else {
                        return "";
                    }
                }
            }

            public void setTeacher_name(String teacher_name) {
                this.teacher_name = teacher_name;
            }

            public String getGrade() {
                return grade;
            }

            public void setGrade(String grade) {
                this.grade = grade;
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

            public String getSubject() {
                return subject;
            }

            public void setSubject(String subject) {
                this.subject = subject;
            }



        }
    }
}
