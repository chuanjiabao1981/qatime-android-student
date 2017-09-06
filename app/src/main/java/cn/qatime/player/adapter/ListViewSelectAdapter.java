package cn.qatime.player.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.qatime.player.R;
import libraryextra.adapter.ViewHolder;

/**
 * Created by lenovo on 2017/8/22.
 */

public abstract class ListViewSelectAdapter<T> extends BaseAdapter implements CompoundButton.OnCheckedChangeListener {
    private Context context;
    private int itemLayoutId;
    private List<T> list;
    private boolean show;
    private boolean selectAll;
    private Set<T> selectedSet = new HashSet<>();
    private SelectChangeListener mListener;
    private final boolean singleMode;

    public ListViewSelectAdapter(Context context, List<T> list, int itemLayoutId, boolean singleMode) {
        this.context = context;
        this.list = list;
        this.itemLayoutId = itemLayoutId;
        this.singleMode = singleMode;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public T getItem(int position) {
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
        convert(holder, getItem(position), position);


        holder.getView(R.id.checkedView).setTag(getItem(position));
        if (show) {
            holder.getView(R.id.checkedView).setVisibility(View.VISIBLE);
            if (selectedSet.contains(getItem(position))) {
                ((CheckBox) holder.getView(R.id.checkedView)).setChecked(true);
            } else {
                ((CheckBox) holder.getView(R.id.checkedView)).setChecked(false);
            }
        } else {
            holder.getView(R.id.checkedView).setVisibility(View.GONE);
        }
        ((CheckBox) holder.getView(R.id.checkedView)).setOnCheckedChangeListener(this);
        return holder.getConvertView();

    }

    public abstract void convert(ViewHolder holder, T item, int position);

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
            selectedSet.addAll(list);
        }
    }

    public boolean isSelectAll() {
        return selectAll;
    }

    public List<T> getSelectedList() {
        List<T> list = new ArrayList<>();
        for (T t : selectedSet) {
            list.add(t);
        }
        return list;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        T tag = (T) buttonView.getTag();

        if (isChecked && !selectedSet.contains(tag)) {
            if (singleMode) {
                for (T t : selectedSet) {
                    if (mListener != null) {
                        mListener.update(t, false);
                    }
                }
            }
            selectedSet.add(tag);
            if (mListener != null) {
                mListener.update(tag, isChecked);
            }
        } else if (!isChecked && selectedSet.contains(tag)) {
            selectedSet.remove((tag));
            if (mListener != null) {
                mListener.update(tag, isChecked);
            }
        }

    }

    public SelectChangeListener getSelectListener() {
        return mListener;
    }

    public void setSelectListener(SelectChangeListener Listener) {
        this.mListener = Listener;
    }

    public void updateItem(T item, boolean isChecked) {
        if (isChecked) {
            if (singleMode) {
                selectedSet.clear();
            }
            if (list.indexOf(item) != -1) {
                selectedSet.add(item);
            }
        } else {
            selectedSet.remove(item);
        }
        notifyDataSetChanged();
    }

    public interface SelectChangeListener<T> {
        void update(T item, boolean isChecked);
    }
}
