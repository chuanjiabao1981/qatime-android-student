package cn.qatime.player.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.qatime.player.R;
import libraryextra.adapter.ViewHolder;

/**
 * Created by lenovo on 2017/8/30.
 */

public abstract class MyExpandableListViewAdapter<T> extends BaseExpandableListAdapter implements CompoundButton.OnCheckedChangeListener {
    private final Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            notifyDataSetChanged();
            super.handleMessage(msg);
        }
    };
    private ExpandableListView expandableListView;
    private Context context;
    private int childLayouId;
    private List<String> groupData;
    private List<List<T>> childData;

    private Set<T> selectedSet = new HashSet<>();
    private boolean show;
    private boolean singleMode;
    private boolean selectAll;
    private SelectChangeListener mListener;

    /*供外界更新数据的方法*/
    public void refresh() {
        handler.sendMessage(new Message());
        //必须重新伸缩之后才能更新数据
        expandableListView.collapseGroup(0);
        expandableListView.expandGroup(0);
    }

    public MyExpandableListViewAdapter(Context context, ExpandableListView expandableListView,int childLayouId, List<String> groupData, List<List<T>> childData,boolean singleMode) {
        this.context = context;
        this.childLayouId = childLayouId;
        this.groupData = groupData;
        this.childData = childData;
        this.singleMode = singleMode;
        this.expandableListView = expandableListView;

    }


    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    //获取分组个数
    @Override
    public int getGroupCount() {
        int ret = 0;
        if (groupData != null) {
            ret = groupData.size();
        }
        return ret;
    }

    //获取groupPosition分组，子列表数量
    @Override
    public int getChildrenCount(int groupPosition) {
        int ret = 0;
        if (childData != null) {
            ret = childData.get(groupPosition).size();
        }
        return ret;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childData.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private ViewHolder getGroupViewHolder(int position, View convertView, ViewGroup parent) {
        return ViewHolder.get(context, convertView, parent, R.layout.item_group_expandablelistview, position);
    }

    private ViewHolder getChildViewHolder(int position, View convertView, ViewGroup parent) {
        return ViewHolder.get(context, convertView, parent, R.layout.item_file_upload_manager, position);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder holder = getGroupViewHolder(groupPosition, convertView,
                parent);

        if (isExpanded) {
//            holder.img.setImageResource(R.drawable.img_bottom);
        } else {
//            holder.img.setImageResource(R.drawable.img_right);
        }
        holder.setText(R.id.tv_group_name, groupData.get(groupPosition));
        holder.setText(R.id.tv_group_num, "(" + childData.get(groupPosition).size() + ")");
        return holder.getConvertView();
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder holder = getChildViewHolder(groupPosition, convertView,
                parent);
        T item = childData.get(groupPosition).get(childPosition);
        convert(holder, item,groupPosition,childPosition);


        holder.getView(R.id.checkedView).setTag(item);
        if (show) {
            holder.getView(R.id.checkedView).setVisibility(View.VISIBLE);
            if (selectedSet.contains(item)) {
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

    public abstract void convert(ViewHolder holder, T item, int groupPosition, int childPosition);

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }

    public boolean isCheckboxShow() {
        return show;
    }

    public void showCheckbox(boolean show) {
        this.show = show;
        refresh();
    }

    public void selectAll(boolean b) {
        this.selectAll = b;
        selectedSet.clear();
        if (b) {
            for (List<T> list : childData) {
                selectedSet.addAll(list);
            }
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
                    if(mListener!=null){
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
            for (List<T> list : childData) {
                if (list.indexOf(item) != -1) {
                    selectedSet.add(item);
                }
            }
        } else {
            selectedSet.remove(item);
        }
        refresh();
    }

    public interface SelectChangeListener<T> {
        void update(T item, boolean isChecked);
    }
}
