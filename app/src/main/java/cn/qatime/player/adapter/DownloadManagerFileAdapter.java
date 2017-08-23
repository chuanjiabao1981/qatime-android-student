package cn.qatime.player.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.qatime.player.R;
import libraryextra.adapter.ViewHolder;
import libraryextra.utils.DataCleanUtils;

/**
 * Created by lenovo on 2017/8/22.
 */

public class DownloadManagerFileAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener {
    private Context context;
    private int itemLayoutId;
    private List<File> list;
    private boolean show;
    private boolean selectAll;
    private Set<Integer> selectedSet = new HashSet<>();
    private SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SelectChangeListener mListener;

    public DownloadManagerFileAdapter(Context context, List<File> list, int itemLayoutId) {
        this.context = context;
        this.list = list;
        this.itemLayoutId = itemLayoutId;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public File getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = getViewHolder(position, convertView,
                parent);
        holder.setText(R.id.name, getItem(position).getName());
        holder.setText(R.id.size, DataCleanUtils.getFormatSize(getItem(position).length()));
        holder.setText(R.id.time, "下载时间:" + parse.format(new Date(getItem(position).lastModified())));
        if (show) {
            holder.getView(R.id.checkedView).setVisibility(View.VISIBLE);
            if (selectedSet.contains(position)) {
                ((CheckBox) holder.getView(R.id.checkedView)).setChecked(true);
            } else {
                ((CheckBox) holder.getView(R.id.checkedView)).setChecked(false);
            }
        } else {
            holder.getView(R.id.checkedView).setVisibility(View.GONE);
        }
        holder.getView(R.id.checkedView).setTag(position);
        ((CheckBox) holder.getView(R.id.checkedView)).setOnCheckedChangeListener(this);
        return holder.getConvertView();

    }

    private ViewHolder getViewHolder(int position, View convertView, ViewGroup parent) {
        return ViewHolder.get(context, convertView, parent, itemLayoutId, position);
    }

    public boolean isCheckboxShow() {
        return show;
    }

    public void showCheckbox(boolean show) {
        this.show = show;
        notifyDataSetChanged();
    }

    public void selectAll(boolean b) {
        this.selectAll = b;
        selectedSet.clear();
        if (b) {
            for (int i = 0; i < list.size(); i++) {
                selectedSet.add(i);
            }
        }
        notifyDataSetChanged();
    }

    public boolean isSelectAll() {
        return selectAll;
    }

    public List<File> getSelectedList() {
        List<File> list = new ArrayList<>();
        for (Integer integer : selectedSet) {
            list.add(this.list.get(integer));
        }
        return list;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Integer position = (Integer) buttonView.getTag();
        if (isChecked) {
            selectedSet.add(position);
        } else {
            selectedSet.remove((position));
        }
        if (mListener != null) {
            mListener.update(selectedSet.size());
        }
    }

    public SelectChangeListener getSelectListener() {
        return mListener;
    }

    public void setSelectListener(SelectChangeListener Listener) {
        this.mListener = Listener;
    }

    public void removeFile() {
        for (File file : getSelectedList()) {
            file.delete();
            list.remove(file);
        }
        selectedSet.clear();
        notifyDataSetChanged();
    }

    public interface SelectChangeListener {
        void update(int count);
    }
}
