package cn.qatime.player.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragment;

/**
 * @author lungtify
 * @Time 2017/8/1 10:59
 * @Describe 详情下的公告
 */

public class FragmentAnnouncements extends BaseFragment {

    private View inflate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_announcements, null);
        return inflate;
    }

    public void setAnnouncements(String announcement) {
        TextView desc = (TextView) inflate.findViewById(R.id.describe);
        desc.setText(announcement);
    }
}
