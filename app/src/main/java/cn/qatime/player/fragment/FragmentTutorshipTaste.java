package cn.qatime.player.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.activity.NEVideoPlayerActivity;
import cn.qatime.player.activity.RemedialClassDetailActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.bean.TutorialClassBean;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

public class FragmentTutorshipTaste extends BaseFragment {
    private PullToRefreshListView listView;
    private java.util.List<TutorialClassBean.Data> list = new ArrayList<>();
    private CommonAdapter<TutorialClassBean.Data> adapter;
    private int page = 1;
    private SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutorship_taste, container, false);
        initview(view);
        return view;
    }

    private void initview(View view) {
        listView = (PullToRefreshListView) view.findViewById(R.id.list);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setEmptyView(View.inflate(getActivity(), R.layout.empty_view, null));
        listView.getLoadingLayoutProxy(true, false).setPullLabel(getResourceString(R.string.pull_to_refresh));
        listView.getLoadingLayoutProxy(false, true).setPullLabel(getResourceString(R.string.pull_to_load));
        listView.getLoadingLayoutProxy(true, false).setRefreshingLabel(getResourceString(R.string.refreshing));
        listView.getLoadingLayoutProxy(false, true).setRefreshingLabel(getResourceString(R.string.loading));
        listView.getLoadingLayoutProxy(true, false).setReleaseLabel(getResourceString(R.string.release_to_refresh));
        listView.getLoadingLayoutProxy(false, true).setReleaseLabel(getResourceString(R.string.release_to_load));

        adapter = new CommonAdapter<TutorialClassBean.Data>(getActivity(), list, R.layout.item_fragment_personal_my_tutorship5) {
            @Override
            public void convert(ViewHolder helper, final TutorialClassBean.Data item, int position) {
                /**
                 * 已购买的已在获取数据时候排除，当前只填充试听的课程（已试听。试听中）
                 */
                String status = item.getStatus();

                boolean isTeaching = "teaching".equals(status);//是否是开课中

                //进入状态
                helper.getView(R.id.enter).setVisibility(isTeaching ? View.VISIBLE : View.GONE);//进入播放器按钮显示或隐藏
                helper.getView(R.id.enter).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), NEVideoPlayerActivity.class);
                        intent.putExtra("camera", item.getCamera());
                        intent.putExtra("board", item.getBoard());
                        intent.putExtra("id", item.getId());
                        intent.putExtra("sessionId", item.getChat_team_id());
                        startActivity(intent);
                    }
                });

                //试听状态
                TextView taste = helper.getView(R.id.taste);
                if (!item.isTasted()) {//tasted为true为已经试听
                    taste.setText("试听中");
                    taste.setBackgroundColor(0xffff9966);
                    helper.getView(R.id.enter).setEnabled(true);//按钮是否能被点击
                } else {
                    taste.setText("已试听");
                    taste.setBackgroundColor(0xffcccccc);
                    helper.getView(R.id.enter).setEnabled(false);//按钮是否能被点击
                }


                Glide.with(getActivity()).load(item.getPublicize()).placeholder(R.mipmap.photo).centerCrop().crossFade().into((ImageView) helper.getView(R.id.image));
                helper.setText(R.id.name, item.getName());
                helper.setText(R.id.subject, item.getSubject());
                helper.setText(R.id.teacher, "/" + item.getTeacher_name());
                helper.setText(R.id.grade, item.getGrade());

                if ("init".equals(status) || "published".equals(status) || "ready".equals(status)) {
                    helper.getView(R.id.teaching_time).setVisibility(View.VISIBLE);
                    helper.getView(R.id.class_over).setVisibility(View.GONE);
                    helper.getView(R.id.progress).setVisibility(View.GONE);
                    try {
                        long time = System.currentTimeMillis() - parse.parse(item.getPreview_time()).getTime();
                        int value = 0;
                        if (time > 0) {
                            value = (int) (time / (1000 * 3600 * 24));
                        }
                        helper.setText(R.id.teaching_time, getResourceString(R.string.item_to_start) + value + getResourceString(R.string.item_day));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else if ("completed".equals(status) || "finished".equals(status)) {
                    helper.getView(R.id.class_over).setVisibility(View.VISIBLE);
                    helper.getView(R.id.teaching_time).setVisibility(View.GONE);
                    helper.getView(R.id.progress).setVisibility(View.GONE);
                } else {
                    helper.getView(R.id.progress).setVisibility(View.VISIBLE);
                    helper.getView(R.id.teaching_time).setVisibility(View.GONE);
                    helper.getView(R.id.class_over).setVisibility(View.GONE);
                    helper.setText(R.id.progress, "进度" + item.getCompleted_lesson_count() + "/" + item.getPreset_lesson_count());
                }
            }
        };
        listView.setAdapter(adapter);

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                initData(1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                initData(2);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), RemedialClassDetailActivity.class);
                intent.putExtra("id", list.get(position - 1).getId());
                startActivity(intent);
            }
        });
        listView.getRefreshableView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO  长按点击事件
                return true;
            }
        });
    }

    public void onShow() {
        if (!isLoad) {
            initData(1);
        }
    }

    /**
     * @param type 1刷新
     *             2加载更多
     */
    private void initData(final int type) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(page));
        map.put("per_page", "10");
        map.put("cate", "taste");

        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlMyRemedialClass + BaseApplication.getUserId() + "/courses", map), null,
                new VolleyListener(getActivity()) {

                    @Override
                    protected void onSuccess(JSONObject response) {
                        isLoad = true;
                        Logger.e(response.toString());
                        if (type == 1) {
                            list.clear();
                        }
                        String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                        listView.getLoadingLayoutProxy(true, false).setLastUpdatedLabel(label);
                        listView.onRefreshComplete();

                        try {
                            TutorialClassBean data = JsonUtils.objectFromJson(response.toString(), TutorialClassBean.class);
                            if (data != null) {
                                for (TutorialClassBean.Data item : data.getData()) {
                                    if (item.isIs_tasting() || item.isTasted()) {//只显示试听
                                        list.add(item);
                                    }
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onError(JSONObject response) {
                        String label = DateUtils.formatDateTime(
                                getActivity(),
                                System.currentTimeMillis(),
                                DateUtils.FORMAT_SHOW_TIME
                                        | DateUtils.FORMAT_SHOW_DATE
                                        | DateUtils.FORMAT_ABBREV_ALL);
                        // Update the LastUpdatedLabel
                        listView.getLoadingLayoutProxy(false, true)
                                .setLastUpdatedLabel(label);
                        listView.onRefreshComplete();
                    }

                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }

                }, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
                listView.onRefreshComplete();
            }
        });
        addToRequestQueue(request);
    }
}
