package cn.qatime.player.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.bean.Announcements;
import libraryextra.bean.ChatTeamBean;
import libraryextra.transformation.GlideCircleTransform;
import libraryextra.utils.StringUtils;

/**
 * @author luntify
 * @date 2016/8/9 17:15
 * @Description 直播-成员列表
 */
public class FragmentNEVideoPlayerAdapter4 extends CommonAdapter<ChatTeamBean.Accounts> {
    private Map<String, Integer> letterMap = new HashMap<String, Integer>();
    private Context context;

    public FragmentNEVideoPlayerAdapter4(Context context, List<ChatTeamBean.Accounts> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
        this.context = context;
    }

    public void clearLetterMap() {
        letterMap.clear();
    }

    @Override
    public void convert(ViewHolder holder, ChatTeamBean.Accounts item, int position) {
        if (item == null) return;
        if (!letterMap.containsKey(item.getFirstLetters())) {
            letterMap.put(item.getFirstLetters(), position);
        }
//        if (item.isOwner()) {
//            ((TextView) holder.getView(R.id.name)).setTextColor(0xffff5842);
//            ((TextView) holder.getView(R.id.role)).setTextColor(0xffff5842);
//            ((TextView) holder.getView(R.id.role)).setText(R.string.teacher_translate);
//        } else {
        ((TextView) holder.getView(R.id.name)).setTextColor(0xff666666);
        ((TextView) holder.getView(R.id.role)).setTextColor(0xff999999);
        ((TextView) holder.getView(R.id.role)).setText(R.string.student_translate);
//        }

        holder.setText(R.id.name, item.getName());
        Glide.with(context).load(item.getIcon()).placeholder(R.mipmap.error_header).fitCenter().crossFade().transform(new GlideCircleTransform(context)).dontAnimate().into((ImageView) holder.getView(R.id.image));
//        if (position == 0) {
//            ((TextView) holder.getView(R.id.name)).setTextColor(0xffbe0b0b);
//            ((TextView) holder.getView(R.id.role)).setTextColor(0xffbe0b0b);
//            holder.setText(R.id.role, context.getString(R.string.teacher_translate));
//        }
    }

//    public int getPositionByLetter(String s) {
//        Integer value = letterMap.get(s);
//        if (StringUtils.isNullOrBlanK(value)) {
//            return -1;
//        } else {
//            return value;
//        }
//    }
}
