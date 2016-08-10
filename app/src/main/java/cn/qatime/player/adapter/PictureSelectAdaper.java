package cn.qatime.player.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.bean.ImageItem;

/**
 * @author luntify
 * @date 2016/8/10 21:03
 * @Description
 */
public class PictureSelectAdaper extends BaseAdapter {
    private final List<ImageItem> list;
    private final Context context;

    public PictureSelectAdaper(Context context, List<ImageItem> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_picture_select, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == 0) {
            Glide.with(context).load("").placeholder(R.mipmap.camera).crossFade().into(holder.image);
        } else {
            Glide.with(context).load("file://" + list.get(position - 1).thumbnailPath).placeholder(R.mipmap.default_image).crossFade().into(holder.image);
        }
        return convertView;
    }

    public class ViewHolder {
        public final ImageView image;
        public final View root;

        public ViewHolder(View root) {
            image = (ImageView) root.findViewById(R.id.image);
            this.root = root;
        }
    }
}
