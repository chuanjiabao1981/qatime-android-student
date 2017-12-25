package cn.qatime.player.bean.exam;

import java.io.Serializable;
import java.util.List;

/**
 * @author luntify
 * @date 2017/12/14 16:59
 * @Description:
 */

public class Topics implements Serializable {

    /**
     * id : 2
     * paper_id : 1
     * category_id : null
     * package_topic_id : null
     * group_topic_id : 1
     * name : 01题
     * section_name : null
     * title : 第1题
     * attach : null
     * topics_count : 1
     * duration : null
     * score : 1
     * answer : null
     * answer_attach : null
     * type : Exam::SingleChoiceTopic
     * status : pending
     * topics : []
     * topic_options : [{"id":1,"topic_id":2,"name":"A","title":"AAAAA","correct":false,"created_at":"2017-12-13T16:25:22.905+08:00","updated_at":"2017-12-13T18:01:14.323+08:00"},{"id":2,"topic_id":2,"name":"B","title":"BBBBB","correct":false,"created_at":"2017-12-13T16:25:22.907+08:00","updated_at":"2017-12-13T18:01:14.325+08:00"},{"id":3,"topic_id":2,"name":"C","title":"CCCCC","correct":false,"created_at":"2017-12-13T16:25:22.909+08:00","updated_at":"2017-12-13T18:01:14.326+08:00"}]
     * created_at : 2017-12-13T16:25:22.893+08:00
     * updated_at : 2017-12-13T18:01:14.321+08:00
     */

    private int id;
    //    private int paper_id;
//    private Object category_id;
//    private Object package_topic_id;
//    private int group_topic_id;
//    private String name;
//    private Object section_name;
    private String title;
    private Attach attach;
    private int topics_count;
    //    private Object duration;
//    private int score;
//    private Object answer;
//    private Object answer_attach;
    private String type;
    private String status;
    private List<Topics> topics;
    private List<TopicOptions> topic_options;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

//    public int getPaper_id() {
//        return paper_id;
//    }
//
//    public void setPaper_id(int paper_id) {
//        this.paper_id = paper_id;
//    }

//    public Object getCategory_id() {
//        return category_id;
//    }
//
//    public void setCategory_id(Object category_id) {
//        this.category_id = category_id;
//    }
//
//    public Object getPackage_topic_id() {
//        return package_topic_id;
//    }
//
//    public void setPackage_topic_id(Object package_topic_id) {
//        this.package_topic_id = package_topic_id;
//    }

//    public int getGroup_topic_id() {
//        return group_topic_id;
//    }
//
//    public void setGroup_topic_id(int group_topic_id) {
//        this.group_topic_id = group_topic_id;
//    }

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }

//    public Object getSection_name() {
//        return section_name;
//    }
//
//    public void setSection_name(Object section_name) {
//        this.section_name = section_name;
//    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Attach getAttach() {
        return attach;
    }

    public void setAttach(Attach attach) {
        this.attach = attach;
    }

    public int getTopics_count() {
        return topics_count;
    }

    public void setTopics_count(int topics_count) {
        this.topics_count = topics_count;
    }

//    public Object getDuration() {
//        return duration;
//    }
//
//    public void setDuration(Object duration) {
//        this.duration = duration;
//    }
//
//    public int getScore() {
//        return score;
//    }
//
//    public void setScore(int score) {
//        this.score = score;
//    }

//    public Object getAnswer() {
//        return answer;
//    }
//
//    public void setAnswer(Object answer) {
//        this.answer = answer;
//    }
//
//    public Object getAnswer_attach() {
//        return answer_attach;
//    }
//
//    public void setAnswer_attach(Object answer_attach) {
//        this.answer_attach = answer_attach;
//    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Topics> getTopics() {
        return topics;
    }

    public void setTopics(List<Topics> topics) {
        this.topics = topics;
    }

    public List<TopicOptions> getTopic_options() {
        return topic_options;
    }

    public void setTopic_options(List<TopicOptions> topic_options) {
        this.topic_options = topic_options;
    }

}
