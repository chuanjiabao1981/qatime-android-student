package cn.qatime.player.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.utils.LogUtils;
import cn.qatime.player.view.VerticalSlide;

public class FragmentNEVideoPlayer3 extends BaseFragment {

    private ImageView point3;
    private ImageView point1;
    private ImageView point2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_nevideo_player3, null);
        point1 = (ImageView) view.findViewById(R.id.point1);
        point2 = (ImageView) view.findViewById(R.id.point2);
        point3 = (ImageView) view.findViewById(R.id.point3);
        VerticalSlide dragLayout = (VerticalSlide) view.findViewById(R.id.dragLayout);
        dragLayout.setOnPageChangeListener(new VerticalSlide.OnPageChangeListener() {
            @Override
            public void onChange(int page) {
                if (page == 0) {
                    point1.setImageResource(R.mipmap.point_select);
                    point2.setImageResource(R.mipmap.point_default);
                    point3.setImageResource(R.mipmap.point_default);
                } else if (page == 1) {
                    point1.setImageResource(R.mipmap.point_default);
                    point2.setImageResource(R.mipmap.point_select);
                    point3.setImageResource(R.mipmap.point_default);
                } else {
                    point1.setImageResource(R.mipmap.point_default);
                    point2.setImageResource(R.mipmap.point_default);
                    point3.setImageResource(R.mipmap.point_select);
                }
            }
        });
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.first, new FragmentNEVideoPlayer31());
        transaction.replace(R.id.second, new FragmentNEVideoPlayer32());
        transaction.replace(R.id.three, new FragmentNEVideoPlayer33());
        transaction.commit();
        return view;
    }
}
