package cn.qatime.player.adapter;

import android.content.Context;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.bean.RemedialClassDetailBean;
import libraryextra.utils.StringUtils;

/**
 * @author luntify
 * @date 2016/8/9 17:15
 * @Description 直播-成员列表
 */
public class FragmentNEVideoPlayerAdapter4 extends CommonAdapter<RemedialClassDetailBean.Accounts> {
    private Map<String, Integer> letterMap = new HashMap<String, Integer>();

    public FragmentNEVideoPlayerAdapter4(Context context, List<RemedialClassDetailBean.Accounts> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    public void clearLetterMap() {
        letterMap.clear();
    }

    @Override
    public void convert(ViewHolder holder, RemedialClassDetailBean.Accounts item, int position) {
        if (!letterMap.containsKey(item.getFirstLetter())) {
            letterMap.put(item.getFirstLetter(), position);
        }
//        if (position == letterMap.get(item.getFirstLetter()) && !StringUtils.isNullOrBlanK(item.getFirstLetter())) {
//            holder.getView(R.id.top).setVisibility(View.VISIBLE);
//        } else {
//            holder.getView(R.id.top).setVisibility(View.GONE);
//        }
//        holder.setText(R.id.top, item.getFirstLetter());
//        if (position > 5) {
//            ((TextView)holder.getView(R.id.name)).setTextColor(0xff6c6c6c);
//            ((TextView)holder.getView(R.id.role)).setTextColor(0xff6c6c6c);
//        } else {
//            ((TextView)holder.getView(R.id.name)).setTextColor(0xffed0000);
//            ((TextView)holder.getView(R.id.role)).setTextColor(0xffed0000);
//        }
        holder.setText(R.id.name, item.getName());
    }

    public int getPositionByLetter(String s) {
        Integer value = letterMap.get(s);
        if (StringUtils.isNullOrBlanK(value)) {
            return -1;
        } else {
            return value.intValue();
        }
    }
}
