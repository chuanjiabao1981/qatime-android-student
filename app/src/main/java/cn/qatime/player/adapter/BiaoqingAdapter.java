package cn.qatime.player.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.bean.BiaoQingData;

/**
 * @author lungtify
 * @Time 2016/12/12 14:54
 * @Describe 表情
 */
public class BiaoqingAdapter extends BaseAdapter {
    private final List<BiaoQingData> list;
    private final Context context;

    public BiaoqingAdapter(Context context, List<BiaoQingData> list) {
        this.context = context;
        this.list = list;

    }

    @Override
    public int getCount() {
        return list.size();
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
            convertView = View.inflate(context, R.layout.item_emoji_page, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.emojiimage.setImageResource(list.get(position).getResourceId());
        return convertView;
    }

    public List<BiaoQingData> getList() {
        return list;
    }

    public class ViewHolder {
        final ImageView emojiimage;
        public final View root;

        public ViewHolder(View root) {
            emojiimage = (ImageView) root.findViewById(R.id.emoji_image);
            this.root = root;
        }
    }
}
