package cn.qatime.player.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

import cn.qatime.player.R;
import libraryextra.bean.ImageItem;
import libraryextra.utils.DensityUtils;
import libraryextra.utils.ScreenUtils;
import libraryextra.utils.StringUtils;

/**
 * @author luntify
 * @date 2017/8/16 19:44
 * @Description:
 */

public class QuestionEditAdapter extends BaseAdapter {
    private final Context context;
    private final List<ImageItem> list;
    private OnEventListener listener;

    public QuestionEditAdapter(Context context, List<ImageItem> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size() >= 5 ? list.size() : list.size() + 1;
    }

    @Override
    public ImageItem getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
//        if (convertView == null) {
        convertView = View.inflate(context, R.layout.item_question_edit, null);
        holder = new ViewHolder(convertView);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
        int width = ScreenUtils.getScreenWidth(context) - DensityUtils.dp2px(context, 30);
        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(width / 5, width / 5 - 10);
        holder.rootView.setLayoutParams(param);

        if (position > 4) {
            ImageItem item = getItem(position);
            if (StringUtils.isNullOrBlanK(item.thumbnailPath)) {
                item.thumbnailPath = item.imagePath;
            } else {
                File file = new File(item.thumbnailPath);
                if (!file.exists()) {
                    item.thumbnailPath = item.imagePath;
                }
            }
            holder.delete.setVisibility(View.VISIBLE);
            Glide.with(context).load("file://" + item.thumbnailPath).placeholder(R.mipmap.default_image).crossFade().centerCrop().into(holder.image);
        } else {
            if (position == list.size()) {
                holder.delete.setVisibility(View.GONE);
//                holder.image.setImageResource(R.mipmap.question_add_image);
                Glide.with(context).load("").placeholder(R.mipmap.question_add_image).crossFade().centerCrop().into(holder.image);

            } else {
                ImageItem item = getItem(position);
                if (StringUtils.isNullOrBlanK(item.thumbnailPath)) {
                    item.thumbnailPath = item.imagePath;
                } else {
                    File file = new File(item.thumbnailPath);
                    if (!file.exists()) {
                        item.thumbnailPath = item.imagePath;
                    }
                }
                holder.delete.setVisibility(View.VISIBLE);
                Glide.with(context).load("file://" + item.thumbnailPath).placeholder(R.mipmap.default_image).crossFade().centerCrop().into(holder.image);
            }
        }
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onDelete(position);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        private final ImageView image;
        private final ImageView delete;
        private final View rootView;

        public ViewHolder(View rootView) {
            this.image = (ImageView) rootView.findViewById(R.id.image);
            this.delete = (ImageView) rootView.findViewById(R.id.delete);
            this.rootView = rootView;
        }
    }

    public void setOnEventListener(OnEventListener listener) {
        this.listener = listener;
    }

    public interface OnEventListener {
        void onDelete(int position);
    }
}
