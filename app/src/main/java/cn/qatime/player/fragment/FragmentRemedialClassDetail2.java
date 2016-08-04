package cn.qatime.player.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.bean.RemedialClassDetailBean;
import cn.qatime.player.utils.LogUtils;

public class FragmentRemedialClassDetail2 extends BaseFragment {
    private TextView text;
    private ImageView image;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_remedial_class_detail2, container, false);
        initview(view);
        return view;
    }


    private void initview(View view) {
        text = (TextView) view.findViewById(R.id.text);
        image = (ImageView) view.findViewById(R.id.image);
    }

    public void setData(RemedialClassDetailBean data) {
//        text.setText("老师姓名：" +  "\n昵称：" );
//        Glide.with(this).load(data.getData().getTeacher().getSmall_avatar_url()).placeholder(R.mipmap.ic_launcher).crossFade().into(image);
    }
}
