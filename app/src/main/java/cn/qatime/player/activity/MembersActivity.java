package cn.qatime.player.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.bean.MemberListBean;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.transformation.GlideCircleTransform;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.PinyinUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * Created by lenovo on 2017/9/5.
 */

public class MembersActivity extends BaseActivity {
    private List<MemberListBean.DataBean.MembersBean> list = new ArrayList<>();
    private CommonAdapter<MemberListBean.DataBean.MembersBean> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);
        setTitles("成员列表");
        ListView listView = (ListView) findViewById(R.id.listview);
        adapter = new CommonAdapter<MemberListBean.DataBean.MembersBean>(this, list, R.layout.item_fragment_nevideo_player) {
            @Override
            public void convert(ViewHolder holder, MemberListBean.DataBean.MembersBean item, int position) {
                if (item.isOwner()) {
                    ((TextView) holder.getView(R.id.name)).setTextColor(0xffC4483C);
                    ((TextView) holder.getView(R.id.role)).setTextColor(0xffC4483C);
                    ((TextView) holder.getView(R.id.role)).setText(R.string.teacher_translate);
                } else {
                    ((TextView) holder.getView(R.id.name)).setTextColor(0xff666666);
                    ((TextView) holder.getView(R.id.role)).setTextColor(0xff999999);
                    ((TextView) holder.getView(R.id.role)).setText(R.string.student_translate);
                }

                holder.setText(R.id.name, TextUtils.isEmpty(item.getStudent_name()) ? "无名" : item.getStudent_name());
                if (item.getStudent_avatar() != null) {
                    Glide.with(MembersActivity.this).load(item.getStudent_avatar().getUrl()).placeholder(R.mipmap.error_header).fitCenter().crossFade().transform(new GlideCircleTransform(MembersActivity.this)).dontAnimate().into((ImageView) holder.getView(R.id.image));
                }

            }
        };
        listView.setAdapter(adapter);

        int id = getIntent().getIntExtra("id", 0);
        String type = getIntent().getStringExtra("type");
        if (id != 0) {
            initData(type, id);
        }
    }

    private void initData(String type, int id) {
        String url = "";
        switch (type) {
            case "custom":
                url = UrlUtils.urlCourses + "%d/members";
                break;
            case "interactive":
                url = UrlUtils.urlInteractCourses + "%d/members";
                break;
            case "exclusive":
                url = UrlUtils.urlExclusiveLesson + "/%d/members";

                break;
            case "video":
                url = UrlUtils.urlVideoCourses + "%d/members";

                break;
        }
        DaYiJsonObjectRequest requestMember = new DaYiJsonObjectRequest(String.format(url, id), null,
                new VolleyListener(MembersActivity.this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        MemberListBean bean = JsonUtils.objectFromJson(response.toString(), MemberListBean.class);
                        if (bean != null) {
                            setData(bean);
                        }
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
        addToRequestQueue(requestMember);
    }


    public void setData(MemberListBean data) {
        if (data.getData() != null && data.getData().getMembers() != null) {
            list.clear();
            list.addAll(data.getData().getMembers());
            if (data.getData().getTeachers() != null) {
                for (MemberListBean.DataBean.TeachersBean teachersBean : data.getData().getTeachers()) {
                    MemberListBean.DataBean.MembersBean bean = new MemberListBean.DataBean.MembersBean();
                    bean.setOwner(true);
                    bean.setStudent_name(teachersBean.getName());
                    MemberListBean.DataBean.MembersBean.StudentAvatarBean avatar = new MemberListBean.DataBean.MembersBean.StudentAvatarBean();
                    avatar.setUrl(teachersBean.getAvatar_url());
                    bean.setStudent_avatar(avatar);
                    list.add(bean);
                }
            }
            for (MemberListBean.DataBean.MembersBean item : list) {
                if (item == null) continue;
//                if (!StringUtils.isNullOrBlanK(accounts.getOwner())) {
//                    if (accounts.getOwner().equals(item.getAccid())) {
//                        item.setOwner(true);
//                    } else {
//                        item.setOwner(false);
//                    }
//                }
                if (StringUtils.isNullOrBlanK(item.getStudent_name())) {
                    item.setFirstLetters("");
                } else {
                    item.setFirstLetters(PinyinUtils.getPinyinFirstLetters(item.getStudent_name()));
                }
            }
            Collections.sort(list, new Comparator<MemberListBean.DataBean.MembersBean>() {
                @Override
                public int compare(MemberListBean.DataBean.MembersBean lhs, MemberListBean.DataBean.MembersBean rhs) {
                    int x = 0;
                    if (lhs.isOwner() && !rhs.isOwner()) {
                        x = -3;
                    } else if (!lhs.isOwner() && rhs.isOwner()) {
                        x = 3;
                    } else if (lhs.isOwner() && rhs.isOwner()) {
                        x = -3;
                    }

                    int y = lhs.getFirstLetters().compareTo(rhs.getFirstLetters());
                    if (x == 0) {
                        return y;
                    }
                    return x;
//                    return lhs.getFirstLetters().compareTo(rhs.getFirstLetters());
                }
            });
            adapter.notifyDataSetChanged();
        }
    }
}
