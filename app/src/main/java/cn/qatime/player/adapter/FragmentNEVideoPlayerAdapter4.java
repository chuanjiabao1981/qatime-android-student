package cn.qatime.player.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.bean.Announcements;
import libraryextra.transformation.GlideCircleTransform;
import libraryextra.utils.StringUtils;

/**
 * @author luntify
 * @date 2016/8/9 17:15
 * @Description 直播-成员列表
 */
public class FragmentNEVideoPlayerAdapter4 extends CommonAdapter<Announcements.DataBean.MembersBean> {
    private Map<String, Integer> letterMap = new HashMap<String, Integer>();
    private Context context;

    public FragmentNEVideoPlayerAdapter4(Context context, List<Announcements.DataBean.MembersBean> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
        this.context = context;
    }

    public void clearLetterMap() {
        letterMap.clear();
    }

    @Override
    public void convert(ViewHolder holder, Announcements.DataBean.MembersBean item, int position) {
        if (!letterMap.containsKey(item.getFirstLetter())) {
            letterMap.put(item.getFirstLetter(), position);
        }
        holder.setText(R.id.name, item.getName());
        Glide.with(context).load(item.getIcon()).placeholder(R.mipmap.error_header).fitCenter().crossFade().transform(new GlideCircleTransform(context)).dontAnimate().into((ImageView) holder.getView(R.id.image));
//        if (position == 0) {
//            ((TextView) holder.getView(R.id.name)).setTextColor(0xffbe0b0b);
//            ((TextView) holder.getView(R.id.role)).setTextColor(0xffbe0b0b);
//            holder.setText(R.id.role, context.getString(R.string.teacher_translate));
//        }
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
