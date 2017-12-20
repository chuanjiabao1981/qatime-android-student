package cn.qatime.player.bean.exam;

import java.io.Serializable;
import java.util.List;

/**
 * @author luntify
 * @date 2017/12/14 17:05
 * @Description:
 */

public class Categories implements Serializable {

    /**
     * id : 1
     * paper_id : 1
     * name : Ⅰ听后选择
     * description :
     * duration : 540
     * read_time : null
     * play_times : null
     * interval_time : null
     * waiting_time : null
     * topics_count : 8
     * score : 12
     * created_at : 2017-12-13T16:25:22.874+08:00
     * updated_at : 2017-12-13T16:25:22.874+08:00
     * type : Exam::ListenSelection
     * topics : [{"id":1,"paper_id":1,"category_id":1,"package_topic_id":null,"group_topic_id":null,"name":"01-02题","section_name":null,"title":null,"attach":"#<ActionDispatch::Http::UploadedFile:0x0000000c441d38>","topics_count":3,"duration":null,"score":3,"answer":null,"answer_attach":null,"type":"Exam::GroupTopic","status":"pending","topics":[{"id":2,"paper_id":1,"category_id":null,"package_topic_id":null,"group_topic_id":1,"name":"01题","section_name":null,"title":"第1题","attach":null,"topics_count":1,"duration":null,"score":1,"answer":null,"answer_attach":null,"type":"Exam::SingleChoiceTopic","status":"pending","topics":[],"topic_options":[{"id":1,"topic_id":2,"name":"A","title":"AAAAA","correct":false,"created_at":"2017-12-13T16:25:22.905+08:00","updated_at":"2017-12-13T18:01:14.323+08:00"},{"id":2,"topic_id":2,"name":"B","title":"BBBBB","correct":false,"created_at":"2017-12-13T16:25:22.907+08:00","updated_at":"2017-12-13T18:01:14.325+08:00"},{"id":3,"topic_id":2,"name":"C","title":"CCCCC","correct":false,"created_at":"2017-12-13T16:25:22.909+08:00","updated_at":"2017-12-13T18:01:14.326+08:00"}],"created_at":"2017-12-13T16:25:22.893+08:00","updated_at":"2017-12-13T18:01:14.321+08:00"},{"id":3,"paper_id":1,"category_id":null,"package_topic_id":null,"group_topic_id":1,"name":"02题","section_name":null,"title":"第2题","attach":null,"topics_count":1,"duration":null,"score":1,"answer":null,"answer_attach":null,"type":"Exam::SingleChoiceTopic","status":"pending","topics":[],"topic_options":[{"id":4,"topic_id":3,"name":"A","title":"AAAAA","correct":false,"created_at":"2017-12-13T16:25:22.914+08:00","updated_at":"2017-12-13T18:01:14.331+08:00"},{"id":5,"topic_id":3,"name":"B","title":"BBBBB","correct":false,"created_at":"2017-12-13T16:25:22.915+08:00","updated_at":"2017-12-13T18:01:14.332+08:00"},{"id":6,"topic_id":3,"name":"C","title":"CCCCC","correct":false,"created_at":"2017-12-13T16:25:22.916+08:00","updated_at":"2017-12-13T18:01:22.592+08:00"}],"created_at":"2017-12-13T16:25:22.911+08:00","updated_at":"2017-12-13T18:01:14.330+08:00"}],"topic_options":[],"created_at":"2017-12-13T16:25:22.880+08:00","updated_at":"2017-12-13T18:01:14.315+08:00"},{"id":4,"paper_id":1,"category_id":1,"package_topic_id":null,"group_topic_id":null,"name":"03-04题","section_name":null,"title":null,"attach":null,"topics_count":3,"duration":null,"score":3,"answer":null,"answer_attach":null,"type":"Exam::GroupTopic","status":"pending","topics":[{"id":5,"paper_id":1,"category_id":null,"package_topic_id":null,"group_topic_id":4,"name":"03题","section_name":null,"title":"第3题","attach":null,"topics_count":1,"duration":null,"score":1,"answer":null,"answer_attach":null,"type":"Exam::SingleChoiceTopic","status":"pending","topics":[],"topic_options":[{"id":7,"topic_id":5,"name":"A","title":"AAAAA","correct":false,"created_at":"2017-12-13T16:25:22.923+08:00","updated_at":"2017-12-13T18:01:51.807+08:00"},{"id":8,"topic_id":5,"name":"B","title":"BBBBB","correct":false,"created_at":"2017-12-13T16:25:22.924+08:00","updated_at":"2017-12-13T18:01:51.809+08:00"},{"id":9,"topic_id":5,"name":"C","title":"CCCCC","correct":false,"created_at":"2017-12-13T16:25:22.925+08:00","updated_at":"2017-12-13T18:01:51.810+08:00"}],"created_at":"2017-12-13T16:25:22.920+08:00","updated_at":"2017-12-13T18:01:51.805+08:00"},{"id":6,"paper_id":1,"category_id":null,"package_topic_id":null,"group_topic_id":4,"name":"04题","section_name":null,"title":"第4题","attach":null,"topics_count":1,"duration":null,"score":1,"answer":null,"answer_attach":null,"type":"Exam::SingleChoiceTopic","status":"pending","topics":[],"topic_options":[{"id":10,"topic_id":6,"name":"A","title":"AAAAA","correct":false,"created_at":"2017-12-13T16:25:22.931+08:00","updated_at":"2017-12-13T18:01:51.815+08:00"},{"id":11,"topic_id":6,"name":"B","title":"BBBBB","correct":false,"created_at":"2017-12-13T16:25:22.932+08:00","updated_at":"2017-12-13T18:01:51.817+08:00"},{"id":12,"topic_id":6,"name":"C","title":"CCCCC","correct":false,"created_at":"2017-12-13T16:25:22.933+08:00","updated_at":"2017-12-13T18:01:51.818+08:00"}],"created_at":"2017-12-13T16:25:22.928+08:00","updated_at":"2017-12-13T18:01:51.814+08:00"}],"topic_options":[],"created_at":"2017-12-13T16:25:22.918+08:00","updated_at":"2017-12-13T18:01:51.799+08:00"},{"id":7,"paper_id":1,"category_id":1,"package_topic_id":null,"group_topic_id":null,"name":"05-06题","section_name":null,"title":null,"attach":null,"topics_count":3,"duration":null,"score":3,"answer":null,"answer_attach":null,"type":"Exam::GroupTopic","status":"pending","topics":[{"id":8,"paper_id":1,"category_id":null,"package_topic_id":null,"group_topic_id":7,"name":"05题","section_name":null,"title":"第5题","attach":null,"topics_count":1,"duration":null,"score":1,"answer":null,"answer_attach":null,"type":"Exam::SingleChoiceTopic","status":"pending","topics":[],"topic_options":[{"id":13,"topic_id":8,"name":"A","title":"AAAAA","correct":false,"created_at":"2017-12-13T16:25:22.939+08:00","updated_at":"2017-12-13T18:02:16.462+08:00"},{"id":14,"topic_id":8,"name":"B","title":"BBBBB","correct":false,"created_at":"2017-12-13T16:25:22.941+08:00","updated_at":"2017-12-13T18:02:16.464+08:00"},{"id":15,"topic_id":8,"name":"C","title":"CCCCC","correct":false,"created_at":"2017-12-13T16:25:22.942+08:00","updated_at":"2017-12-13T18:02:16.466+08:00"}],"created_at":"2017-12-13T16:25:22.937+08:00","updated_at":"2017-12-13T18:02:16.459+08:00"},{"id":9,"paper_id":1,"category_id":null,"package_topic_id":null,"group_topic_id":7,"name":"06题","section_name":null,"title":"第6题","attach":null,"topics_count":1,"duration":null,"score":1,"answer":null,"answer_attach":null,"type":"Exam::SingleChoiceTopic","status":"pending","topics":[],"topic_options":[{"id":16,"topic_id":9,"name":"A","title":"AAAAA","correct":false,"created_at":"2017-12-13T16:25:22.948+08:00","updated_at":"2017-12-13T18:02:16.475+08:00"},{"id":17,"topic_id":9,"name":"B","title":"BBBBB","correct":false,"created_at":"2017-12-13T16:25:22.949+08:00","updated_at":"2017-12-13T18:02:16.476+08:00"},{"id":18,"topic_id":9,"name":"C","title":"CCCCC","correct":false,"created_at":"2017-12-13T16:25:22.950+08:00","updated_at":"2017-12-13T18:02:16.478+08:00"}],"created_at":"2017-12-13T16:25:22.945+08:00","updated_at":"2017-12-13T18:02:16.473+08:00"}],"topic_options":[],"created_at":"2017-12-13T16:25:22.935+08:00","updated_at":"2017-12-13T18:02:16.452+08:00"},{"id":10,"paper_id":1,"category_id":1,"package_topic_id":null,"group_topic_id":null,"name":"07-08题","section_name":null,"title":null,"attach":null,"topics_count":3,"duration":null,"score":3,"answer":null,"answer_attach":null,"type":"Exam::GroupTopic","status":"pending","topics":[{"id":11,"paper_id":1,"category_id":null,"package_topic_id":null,"group_topic_id":10,"name":"07题","section_name":null,"title":"第7题","attach":null,"topics_count":1,"duration":null,"score":1,"answer":null,"answer_attach":null,"type":"Exam::SingleChoiceTopic","status":"pending","topics":[],"topic_options":[{"id":19,"topic_id":11,"name":"A","title":"AAAAA","correct":false,"created_at":"2017-12-13T16:25:22.957+08:00","updated_at":"2017-12-13T18:03:12.377+08:00"},{"id":20,"topic_id":11,"name":"B","title":"BBBBB","correct":false,"created_at":"2017-12-13T16:25:22.958+08:00","updated_at":"2017-12-13T18:03:12.380+08:00"},{"id":21,"topic_id":11,"name":"C","title":"CCCCC","correct":false,"created_at":"2017-12-13T16:25:22.960+08:00","updated_at":"2017-12-13T18:03:12.383+08:00"}],"created_at":"2017-12-13T16:25:22.955+08:00","updated_at":"2017-12-13T18:03:12.374+08:00"},{"id":12,"paper_id":1,"category_id":null,"package_topic_id":null,"group_topic_id":10,"name":"08题","section_name":null,"title":"第8题","attach":null,"topics_count":1,"duration":null,"score":1,"answer":null,"answer_attach":null,"type":"Exam::SingleChoiceTopic","status":"pending","topics":[],"topic_options":[{"id":22,"topic_id":12,"name":"A","title":"AAAAA","correct":false,"created_at":"2017-12-13T16:25:22.966+08:00","updated_at":"2017-12-13T18:03:12.394+08:00"},{"id":23,"topic_id":12,"name":"B","title":"BBBBB","correct":false,"created_at":"2017-12-13T16:25:22.967+08:00","updated_at":"2017-12-13T18:03:12.399+08:00"},{"id":24,"topic_id":12,"name":"C","title":"CCCCC","correct":false,"created_at":"2017-12-13T16:25:22.968+08:00","updated_at":"2017-12-13T18:03:18.795+08:00"}],"created_at":"2017-12-13T16:25:22.963+08:00","updated_at":"2017-12-13T18:03:12.389+08:00"}],"topic_options":[],"created_at":"2017-12-13T16:25:22.952+08:00","updated_at":"2017-12-13T18:03:12.370+08:00"}]
     */

    private int id;
    private int paper_id;
    private String name;
    private String description;
    private int duration;
    private int read_time;
    private int play_times;
    private int interval_time;
    private int waiting_time;
    private int topics_count;
    private int score;
    private String type;
    private List<Topics> topics;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPaper_id() {
        return paper_id;
    }

    public void setPaper_id(int paper_id) {
        this.paper_id = paper_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getRead_time() {
        return read_time;
    }

    public void setRead_time(int read_time) {
        this.read_time = read_time;
    }

    public int getPlay_times() {
        return play_times;
    }

    public void setPlay_times(int play_times) {
        this.play_times = play_times;
    }

    public int getInterval_time() {
        return interval_time;
    }

    public void setInterval_time(int interval_time) {
        this.interval_time = interval_time;
    }

    public int getWaiting_time() {
        return waiting_time;
    }

    public void setWaiting_time(int waiting_time) {
        this.waiting_time = waiting_time;
    }

    public int getTopics_count() {
        return topics_count;
    }

    public void setTopics_count(int topics_count) {
        this.topics_count = topics_count;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Topics> getTopics() {
        return topics;
    }

    public void setTopics(List<Topics> topics) {
        this.topics = topics;
    }
}
