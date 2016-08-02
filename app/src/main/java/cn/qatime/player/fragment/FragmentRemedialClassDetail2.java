package cn.qatime.player.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.qatime.player.R;
import cn.qatime.player.bean.RemedialClassDetailBean;

public class FragmentRemedialClassDetail2 extends Fragment {
    private TextView text;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_remedial_class_detail2, container, false);
        initview(view);
        return view;
    }
    private void initview(View view) {
        text = (TextView) view.findViewById(R.id.text);
    }
    public void setData(RemedialClassDetailBean data) {
        RemedialClassDetailBean.Data bean = data.getData();
        text.setText("名称："+bean.getName()+"\n科目类型："+bean.getSubject()+"\n授课老师："+bean.getTeacher().getName());
    }
}
