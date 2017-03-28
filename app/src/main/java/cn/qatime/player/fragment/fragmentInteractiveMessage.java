package cn.qatime.player.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragment;

/**
 * @author lungtify
 * @Time 2017/3/28 11:23
 * @Describe
 */
public class FragmentInteractiveMessage extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return View.inflate(getActivity(), R.layout.fragment_interactive_message,null);
    }
}
