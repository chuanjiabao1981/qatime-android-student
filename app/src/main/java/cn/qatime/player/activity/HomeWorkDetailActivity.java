package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.bean.HomeWorkItemBean;
import cn.qatime.player.bean.HomeworkDetailBean;
import cn.qatime.player.bean.MyHomeWorksBean;
import cn.qatime.player.bean.StudentHomeWorksBean;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;
import libraryextra.view.ListViewForScrollView;

/**
 * Created by lenovo on 2017/9/11.
 */

public class HomeWorkDetailActivity extends BaseActivity {

    private StudentHomeWorksBean.DataBean item;
    private TextView homeworkTitle;
    private TextView createTime;
    private SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private CommonAdapter<HomeworkDetailBean> adapter;
    private List<HomeworkDetailBean> list;
    private HomeworkDetailBean doingItem;
    private List<HomeWorkItemBean> answerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_detail);
        initView();
        initData();
    }

    private void doHomework() {
        if (answerList.size() <= 0) {
            Toast.makeText(this, "请先添加答案", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("task_items_attributes", getContentString());
        JSONObject obj = new JSONObject(map);
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.PATCH, UrlUtils.urlLiveStudio + "student_homeworks/" + item.getId(), obj,
                new VolleyListener(this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        setResult(Constant.RESPONSE);
                        finish();
                    }

                    @Override
                    protected void onError(JSONObject response) {

                    }

                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }
                }, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
            }
        });
        addToRequestQueue(request);
    }

    private String getContentString() {
        List<MyHomeWorksBean.DataBean.ItemsBean> items = item.getHomework().getItems();
        if (answerList.size() < items.size()) {//如果提交时没做完
//            List<Integer> ids = new ArrayList<>();
//            for (MyHomeWorksBean.DataBean.ItemsBean itemsBean : items) {//循环拿到所有的作业Id
//                ids.add(itemsBean.getId());
//            }
//            List<Integer> answerIds = new ArrayList<>();
//            for (HomeWorkItemBean homeWorkItemBean : answerList) { //循环拿到已经做的id
//                answerIds.add(homeWorkItemBean.parent_id);
//            }
//            ids.removeAll(answerIds);//去掉所有的已经做过的id（拿到没做的id）
//            for (Integer id : ids) {//循环为没有做过的添加答案
//                HomeWorkItemBean itemBean = new HomeWorkItemBean();
//                itemBean.parent_id = id;
//                itemBean.content="未做";
//                answerList.add(itemBean);
//            }

            for (MyHomeWorksBean.DataBean.ItemsBean itemsBean : items) {
                HomeWorkItemBean itemBean = new HomeWorkItemBean();
                itemBean.parent_id = itemsBean.getId();
                if(!answerList.contains(itemBean)){
                    itemBean.content="未做";
                    answerList.add(itemBean);
                }

            }

        }


        StringBuilder sb = new StringBuilder("[");
        for (HomeWorkItemBean homeWorkItemBean : answerList) {
            sb.append("{\"parent_id\":")
                    .append(homeWorkItemBean.parent_id)
                    .append(",\"body\":\"")
                    .append(homeWorkItemBean.content)
                    .append("\"},");
        }
        sb.setCharAt(sb.length() - 1, ']');
        return sb.toString();
    }

    private void initData() {
        item = (StudentHomeWorksBean.DataBean) getIntent().getSerializableExtra("item");
        if (item.getStatus().equals("pending")) {
            setRightText("提交", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doHomework();
                }
            });
            findViewById(R.id.right_text).setVisibility(View.VISIBLE);
        }
        homeworkTitle.setText(item.getTitle());
        setTitles(item.getTitle());
        long time = item.getHomework().getCreated_at() * 1000L;
        createTime.setText("创建时间"+parse.format(new Date(time)));
        //融合
        List<MyHomeWorksBean.DataBean.ItemsBean> homeworks = item.getHomework().getItems();
        List<StudentHomeWorksBean.DataBean.ItemsBean> items = item.getItems();
        List<StudentHomeWorksBean.DataBean.CorrectionBean.ItemsBean> corrections = null;
        if (item.getCorrection() != null) {
            corrections = item.getCorrection().getItems();
        }
        //答案结果以及批改一一对应的写法
//        for (MyHomeWorksBean.DataBean.ItemsBean homework : homeworks) {
//            HomeworkDetailBean homeworkDetailBean = new HomeworkDetailBean();
//            homeworkDetailBean.homework = homework;
//            if (items != null) {
//                for (StudentHomeWorksBean.DataBean.ItemsBean itemsBean : items) {
//                    if (itemsBean.getParent_id() == homework.getId()) {
//                        homeworkDetailBean.answer = itemsBean;
//                        if (corrections != null) {
//                            for (StudentHomeWorksBean.DataBean.CorrectionBean.ItemsBean correction : corrections) {
//                                if (correction.getParent_id() == itemsBean.getId()) {
//                                    homeworkDetailBean.correction = correction;
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//            list.add(homeworkDetailBean);
//        }


        //答案结果以及批改按顺序取值
        for (MyHomeWorksBean.DataBean.ItemsBean homework : homeworks) {
            HomeworkDetailBean homeworkDetailBean = new HomeworkDetailBean();
            homeworkDetailBean.homework = homework;
            list.add(homeworkDetailBean);
        }
        for (int i = 0; i < homeworks.size(); i++) {
            if (items != null && i < items.size()) {
                list.get(i).answer = items.get(i);
            }
            if (corrections != null && i < corrections.size()) {
                list.get(i).correction = corrections.get(i);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.REQUEST && resultCode == Constant.RESPONSE) {
            HomeWorkItemBean item = (HomeWorkItemBean) data.getSerializableExtra("item");
            if (item != null) {
                doingItem.answer = new StudentHomeWorksBean.DataBean.ItemsBean();
                doingItem.answer.setBody(item.content);
                answerList.remove(item);
                answerList.add(item);
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void initView() {
        homeworkTitle = (TextView) findViewById(R.id.homework_title);
        createTime = (TextView) findViewById(R.id.create_time);

        list = new ArrayList<>();
        ListViewForScrollView listView = (ListViewForScrollView) findViewById(R.id.list);
        listView.setEmptyView(View.inflate(this, R.layout.empty_view, null));
        adapter = new CommonAdapter<HomeworkDetailBean>(this, list, R.layout.item_homework_detail) {

            @Override
            public void convert(ViewHolder holder, final HomeworkDetailBean item, int position) {
                String num = position + 1 + "";
                if (position < 10) {
                    num = 0 + num;
                }
                holder.setText(R.id.num, num)
                        .setText(R.id.title, item.homework.getBody());

                TextView doHomeWork = holder.getView(R.id.do_homework);
                if (HomeWorkDetailActivity.this.item.getStatus().equals("submitted") || HomeWorkDetailActivity.this.item.getStatus().equals("resolved")) {
                    doHomeWork.setVisibility(View.GONE);
                    View answerLayout = holder.getView(R.id.answer_layout);
                    answerLayout.setVisibility(View.VISIBLE);
                    if (item.answer != null) {//已做并且答案不为空    有id
                        if (!StringUtils.isNullOrBlanK(item.answer.getBody())) {//内容不为空
                            holder.setText(R.id.answer_content, item.answer.getBody());
                        } else {
                            holder.setText(R.id.answer_content, "无");
                        }
                    } else {//已做但是答案为空  理论上不会出现   answer为空id为空
                        holder.setText(R.id.answer_content, "无");
                    }
                    if (HomeWorkDetailActivity.this.item.getStatus().equals("resolved")) {
                        View correctionLayout = holder.getView(R.id.correction_layout);
                        correctionLayout.setVisibility(View.VISIBLE);
                        if (item.correction != null) {//已批改并且结果不为空    有id
                            if (!StringUtils.isNullOrBlanK(item.correction.getBody())) {//内容不为空
                                holder.setText(R.id.correction_content, item.correction.getBody());
                            } else {
                                holder.setText(R.id.correction_content, "无");
                            }
                        } else {//已批改的状态但是没有批改结果  理论上不会出现   correction为空id为空
                            holder.setText(R.id.correction_content, "无");
                        }
                    }
                } else {
                    if (item.answer != null) {//已做但是并未提交
                        doHomeWork.setText("重做");
                        View answerLayout = holder.getView(R.id.answer_layout);
                        answerLayout.setVisibility(View.VISIBLE);
                        if (!StringUtils.isNullOrBlanK(item.answer.getBody())) {//内容不为空
                            holder.setText(R.id.answer_content, item.answer.getBody());
                        } else {
                            holder.setText(R.id.answer_content, "无");
                        }
                    }
                    doHomeWork.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            doingItem = item;
                            Intent intent = new Intent(HomeWorkDetailActivity.this, HomeWorkItemEditActivity.class);
                            intent.putExtra("parent_id", doingItem.homework.getId());
                            startActivityForResult(intent, Constant.REQUEST);
                        }
                    });
                }
            }
        };
        listView.setAdapter(adapter);
    }
}
