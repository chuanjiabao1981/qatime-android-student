package cn.qatime.player.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.activity.MainActivity;
import cn.qatime.player.activity.MessageFragmentActivity;
import cn.qatime.player.activity.RemedialClassDetailActivity;
import cn.qatime.player.activity.TeacherDataActivity;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.bean.ClassRecommendBean;
import cn.qatime.player.bean.TeacherRecommendBean;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;
import libraryextra.view.TagViewPager;

public class HomeMainPageF extends BaseFragment implements View.OnClickListener {


    private TagViewPager tagViewpagerImg;
    private TagViewPager tagViewpagerSubject;
    private ImageView refreshTeacher;
    private GridView gridviewTeacher;
    private View allClass;
    private GridView gridviewClass;
    private List<GridView> gvSub;
    private int page = 1;
    private List<ClassRecommendBean.DataBean> listRecommendClass = new ArrayList<>();
    private BaseAdapter classAdapter;
    private ImageView message;
    private ArrayList<TeacherRecommendBean.DataBean> listRecommendTeacher = new ArrayList<>();
    private CommonAdapter<TeacherRecommendBean.DataBean> teacherAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_main_page, container, false);
        assignViews(view);
        return view;
    }

    private void assignViews(View view) {
        tagViewpagerImg = (TagViewPager) view.findViewById(R.id.tag_viewpager_img);
        tagViewpagerSubject = (TagViewPager) view.findViewById(R.id.tag_viewpager_subject);
        refreshTeacher = (ImageView) view.findViewById(R.id.refresh_teacher);
        gridviewTeacher = (GridView) view.findViewById(R.id.gridview_teacher);
        allClass = view.findViewById(R.id.all_class);
        gridviewClass = (GridView) view.findViewById(R.id.gridview_class);
        message = (ImageView) view.findViewById(R.id.message);

        initTagImg();
        initTagViewpagerSubject();
        initGridTeacher();
        initGridClass();
        refreshTeacher.setOnClickListener(this);
        allClass.setOnClickListener(this);
        message.setOnClickListener(this);

    }

    private void initTagImg() {
        final int imageIds[] = {R.mipmap.home_ad, R.mipmap.home_ad, R.mipmap.home_ad};
        tagViewpagerImg.init(R.drawable.shape_photo_tag_select, R.drawable.shape_photo_tag_nomal, 16, 8, 2, 40);
        tagViewpagerImg.setAutoNext(true, 4000);
//        viewPager.setId(1252);
        tagViewpagerImg.setOnGetView(new TagViewPager.OnGetView() {
            @Override
            public View getView(ViewGroup container, int position) {
                ImageView iv = new ImageView(getActivity());
                iv.setClickable(true);
                iv.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                iv.setId(position);
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                iv.setImageResource(imageIds[position]);
                container.addView(iv);
                return iv;
            }
        });
        tagViewpagerImg.setAdapter(imageIds.length, 0);
    }

    private void initTagViewpagerSubject() {
        final List<String> strings = Arrays.asList(getResources().getStringArray(R.array.subject));
        tagViewpagerSubject.init(0, 0, 16, 8, 2, 40);
        tagViewpagerSubject.setAutoNext(false, 0);
//        viewPager.setId(1252);
        tagViewpagerSubject.setOnGetView(new TagViewPager.OnGetView() {
            @Override
            public View getView(ViewGroup container, final int position) {
                GridView grid = new GridView(getContext());
                grid.setNumColumns(5);
                grid.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.MATCH_PARENT));
                grid.setPadding(0, 20, 0, 0);
                grid.setBackgroundColor(Color.WHITE);
                grid.setGravity(Gravity.CENTER);
                grid.setAdapter(new CommonAdapter<String>(getContext(), strings, R.layout.item_grid_subject) {
                    @Override
                    public int getCount() {
                        return (strings.size() - 10 * position) > 10 ? 10 : (strings.size() - 10 * position);
                    }

                    @Override
                    public void convert(ViewHolder holder, String item, int positionG) {
                        String s = strings.get(position * 10 + positionG);
                        holder.setText(R.id.subject_text, s);
                    }
                });
                grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int positionG, long id) {
                        String s = strings.get(position * 10 + positionG);
                        MainActivity mainActivity = (MainActivity) getActivity();
                        mainActivity.setCurrentPosition(1, s);
                    }
                });
                container.addView(grid);
                return grid;
            }
        });
        Logger.e("size" + (strings.size() - 1) / 10 + 1);
        tagViewpagerSubject.setAdapter((strings.size() - 1) / 10 + 1, 0);
    }

    private void initGridTeacher() {
        initTeacherData();
        teacherAdapter = new CommonAdapter<TeacherRecommendBean.DataBean>(getContext(), listRecommendTeacher, R.layout.item_grid_teacher) {
            @Override
            public int getCount() {
                return listRecommendTeacher.size();
            }

            @Override
            public void convert(ViewHolder holder, TeacherRecommendBean.DataBean item, int position) {
                Glide.with(getActivity()).load(item.getTeacher().getAvatar_url()).placeholder(R.mipmap.personal_information_head).centerCrop().crossFade().dontAnimate().into(((ImageView) holder.getView(R.id.teacher_img)));
                holder.setText(R.id.teacher_text, item.getTeacher().getName());// TODO: 2016/11/1 getName为空 复用之前的item显示之前的名称
            }
        };
        gridviewTeacher.setAdapter(teacherAdapter);
        gridviewTeacher.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), TeacherDataActivity.class);
                intent.putExtra("teacherId", listRecommendTeacher.get(position).getTeacher().getId());
                startActivity(intent);
            }
        });
    }

    private void initTeacherData() {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(page));
        map.put("per_page", String.valueOf(5));
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlRecommend + "index_teacher_recommend" + "/items", map), null,
                new VolleyListener(getActivity()) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        TeacherRecommendBean teacherRecommendBean = JsonUtils.objectFromJson(response.toString(), TeacherRecommendBean.class);
                        if (teacherRecommendBean != null && teacherRecommendBean.getData() != null && teacherRecommendBean.getData().size() > 0) {
                            page++;
                            listRecommendTeacher.clear();
                            listRecommendTeacher.addAll(teacherRecommendBean.getData());
                            teacherAdapter.notifyDataSetChanged();
                        } else {
                            if (page != 1) {
                                page = 1;
                                initGridTeacher();
                            } else {
                                Toast.makeText(getActivity(), "没有更多推荐信息", Toast.LENGTH_SHORT).show();
                            }
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
        addToRequestQueue(request);
    }

    private void initGridClass() {
        initClassData();
        classAdapter = new CommonAdapter<ClassRecommendBean.DataBean>(getContext(), listRecommendClass, R.layout.item_class_recommend) {
            @Override
            public int getCount() {
                return listRecommendClass.size();
            }

            @Override
            public void convert(ViewHolder holder, ClassRecommendBean.DataBean item, int position) {
                Glide.with(getActivity()).load(item.getLive_studio_course().getPublicize()).placeholder(R.mipmap.photo).centerCrop().crossFade().dontAnimate().into(((ImageView) holder.getView(R.id.class_recommend_img)));
                holder.setText(R.id.title, item.getLive_studio_course().getName());
                holder.setText(R.id.grade, item.getLive_studio_course().getGrade());
                holder.setText(R.id.subject, item.getLive_studio_course().getSubject());
                holder.setText(R.id.count, item.getLive_studio_course().getBuy_tickets_count() + "人已购");

            }
        };
        gridviewClass.setAdapter(classAdapter);
        gridviewClass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), RemedialClassDetailActivity.class);
                if (listRecommendClass.get(position) != null) {
                    intent.putExtra("id", listRecommendClass.get(position).getLive_studio_course().getId());
                }
                startActivity(intent);
            }
        });
    }

    private void initClassData() {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(1));
        map.put("per_page", String.valueOf(6));
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlRecommend + "index_live_studio_course_recommend" + "/items", map), null,
                new VolleyListener(getActivity()) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        ClassRecommendBean classRecommendBean = JsonUtils.objectFromJson(response.toString(), ClassRecommendBean.class);
                        if (classRecommendBean != null && classRecommendBean.getData() != null) {
                            listRecommendClass.addAll(classRecommendBean.getData());
                            classAdapter.notifyDataSetChanged();
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
        addToRequestQueue(request);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all_class:
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.setCurrentPosition(1, "全部");
                break;
            case R.id.refresh_teacher:
                initTeacherData();
                break;
            case R.id.message:
                Intent intent = new Intent(getActivity(), MessageFragmentActivity.class);
                startActivity(intent);
                break;
        }
    }
}
