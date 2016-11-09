package cn.qatime.player.activity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import libraryextra.view.SideBar;

/**
 * @author Tianhaoranly
 * @date 2016/11/9 19:54
 * @Description:
 */
public class CitySelectActivity extends BaseActivity {
    private TextView currentCity;
    private TextView textDialog;
    private ListView listView;
    private SideBar sidebar;

    private void assignViews() {
        currentCity = (TextView) findViewById(R.id.current_city);
        textDialog = (TextView) findViewById(R.id.text_dialog);
        listView = (ListView) findViewById(R.id.listView);
        sidebar = (SideBar) findViewById(R.id.sidebar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_select);
        assignViews();
        initView();
        initData();
    }

    private void initData() {
        // TODO: 2016/11/9 访问网络获取城市列表
//        list.clear();
//        list.addAll(accounts);
//        for (Announcements.DataBean.MembersBean item : list) {
//            if (StringUtils.isNullOrBlanK(item.getName())) {
//                item.setFirstLetter("");
//            } else {
//                item.setFirstLetter(StringUtils.getPYIndexStr(item.getName().substring(0, 1)));
//            }
//        }
//        Collections.sort(list, new Comparator<Announcements.DataBean.MembersBean>() {
//            @Override
//            public int compare(Announcements.DataBean.MembersBean lhs, Announcements.DataBean.MembersBean rhs) {
//                return lhs.getFirstLetter().compareTo(rhs.getFirstLetter());
//            }
//        });
//        hd.postDelayed(runnable, 200);
    }

    private void initView() {
//
//        listView = (ListView) view.findViewById(R.id.listview);
//        adapter = new FragmentNEVideoPlayerAdapter4(this, list, R.layout.item_fragment_nevideo_player4);
//        listView.setAdapter(adapter);
//        sidebar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
//            @Override
//            public void onTouchingLetterChanged(String s) {
//                if (s.equals("#")) {
//                    listView.setSelection(0);
//                } else {
//                    int position = adapter.getPositionByLetter(s);
//                    if (position >= 0) {
//                        listView.setSelection(position);
//                    }
//                }
//            }
//        });
    }
}
