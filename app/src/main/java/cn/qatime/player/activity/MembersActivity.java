package cn.qatime.player.activity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.adapter.FragmentNEVideoPlayerAdapter;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.bean.ExclusiveLessonPlayInfoBean;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.bean.ChatTeamBean;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.PinyinUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * Created by lenovo on 2017/9/5.
 */

public class MembersActivity extends BaseActivity {
    private List<ChatTeamBean.Accounts> list = new ArrayList<>();
    private FragmentNEVideoPlayerAdapter adapter;
    private Handler hd = new Handler();
    private int id;
    private ExclusiveLessonPlayInfoBean playInfo;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            adapter.notifyDataSetChanged();
            hd.removeCallbacks(this);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);
        setTitles("成员列表");
        ListView listView = (ListView) findViewById(R.id.listview);
        adapter = new FragmentNEVideoPlayerAdapter(this, list, R.layout.item_fragment_nevideo_player);
        listView.setAdapter(adapter);

        id = getIntent().getIntExtra("courseId", 0);
        initData();

    }

    private void initData() {

        DaYiJsonObjectRequest requestMember = new DaYiJsonObjectRequest(UrlUtils.urlExclusiveLesson + "/" + id + "/play", null,
                new VolleyListener(MembersActivity.this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        playInfo = JsonUtils.objectFromJson(response.toString(), ExclusiveLessonPlayInfoBean.class);
                        if (playInfo != null) {
                            setData(playInfo.getData().getCustomized_group().getChat_team().getAccounts());
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

    /**
     * @param accounts
     */
    public void setData(List<ChatTeamBean.Accounts> accounts) {
        if (accounts != null) {
            list.clear();
            list.addAll(accounts);
            for (ChatTeamBean.Accounts item : list) {
                if (item == null) continue;
//                if (!StringUtils.isNullOrBlanK(accounts.getOwner())) {
//                    if (accounts.getOwner().equals(item.getAccid())) {
//                        item.setOwner(true);
//                    } else {
//                        item.setOwner(false);
//                    }
//                }
                if (StringUtils.isNullOrBlanK(item.getName())) {
                    item.setFirstLetters("");
                } else {
                    item.setFirstLetters(PinyinUtils.getPinyinFirstLetters(item.getName()));
                }
            }
            Collections.sort(list, new Comparator<ChatTeamBean.Accounts>() {
                @Override
                public int compare(ChatTeamBean.Accounts lhs, ChatTeamBean.Accounts rhs) {
//                    int x = 0;
//                    if (lhs.isOwner() && !rhs.isOwner()) {
//                        x = -3;
//                    } else if (!lhs.isOwner() && rhs.isOwner()) {
//                        x = 3;
//                    } else if (lhs.isOwner() && rhs.isOwner()) {
//                        x = -3;
//                    }

//                    int y = lhs.getFirstLetters().compareTo(rhs.getFirstLetters());
//                    if (x == 0) {
//                        return y;
//                    }
//                    return x;
                    return lhs.getFirstLetters().compareTo(rhs.getFirstLetters());
                }
            });
            hd.postDelayed(runnable, 200);
        }
    }
}
