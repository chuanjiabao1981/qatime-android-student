package cn.qatime.player.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.bean.MembersBean;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.transformation.GlideCircleTransform;

/**
 * @author luntify
 * @date 2016/8/9 17:15
 * @Description 直播-成员列表
 */
public class FragmentNEVideoPlayerAdapter extends CommonAdapter<MembersBean.DataBean.Members> {
    private Context context;

    public FragmentNEVideoPlayerAdapter(Context context, List<MembersBean.DataBean.Members> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
        this.context = context;
    }

    @Override
    public void convert(ViewHolder holder, MembersBean.DataBean.Members item, int position) {
        if (item == null) return;
        if (item.isOwner()) {
            ((TextView) holder.getView(R.id.name)).setTextColor(0xffC4483C);
            ((TextView) holder.getView(R.id.role)).setTextColor(0xffC4483C);
            ((TextView) holder.getView(R.id.role)).setText(R.string.teacher_translate);
        } else {
            ((TextView) holder.getView(R.id.name)).setTextColor(0xff666666);
            ((TextView) holder.getView(R.id.role)).setTextColor(0xff999999);
            ((TextView) holder.getView(R.id.role)).setText(R.string.student_translate);
        }

        holder.setText(R.id.name, TextUtils.isEmpty(item.getName()) ? "无名" : item.getName());
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
