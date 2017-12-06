package cn.qatime.player.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragment;

/**
 * @author luntify
 * @date 2017/12/5 17:18
 * @Description:
 */

public class FragmentExamUnSubmit extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_exam_unsubmit, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initOver = true;
    }

    private void initView() {

    }

    public void onShow() {
        if (initOver) {
            initData(1);
        } else {
            super.onShow();
        }
    }

    private void initData(int type) {

    }
}
