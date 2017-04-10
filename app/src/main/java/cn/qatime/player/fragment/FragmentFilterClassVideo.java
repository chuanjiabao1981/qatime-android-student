package cn.qatime.player.fragment;

import cn.qatime.player.base.BaseFragment;

/**
 * @author Tianhaoranly
 * @date 2017/4/10 18:16
 * @Description:
 */
public class FragmentFilterClassVideo extends BaseFragment {
    private String grade;
    private String subject;

    public BaseFragment setArguments(String grade, String subject) {
        this.grade = grade;
        this.subject = subject;
        return this;
    }
}
