package cn.qatime.player.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.bean.CityBean;
import libraryextra.utils.StringUtils;
import libraryextra.view.GridViewForScrollView;

/**
 * @author Tianhaoranly
 * @date 2016/11/10 9:49
 * @Description:
 */
public abstract class CitySelectAdapter extends BaseAdapter {

    private int VIEW_TYPE_COUNT;
    private List<CityBean.Data> list;
    private int[] itemLayoutId;
    private Context context;
    private ArrayList<CityBean.Data> listLately;
    private Map<String, Integer> letterMap;

    /**
     * @param context
     * @param letterMap
     * @param listLately   最近
     * @param list         全部
     * @param itemLayoutId 最近 全国 城市
     */
    public CitySelectAdapter(Context context, HashMap<String, Integer> letterMap, ArrayList<CityBean.Data> listLately, ArrayList<CityBean.Data> list, int... itemLayoutId) {
        VIEW_TYPE_COUNT = itemLayoutId.length;
        this.listLately = listLately;
        this.list = list;
        this.letterMap = letterMap;
        this.itemLayoutId = itemLayoutId;
        this.context = context;
    }


    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public int getCount() {
        return list.size() + 2;
    }

    @Override
    public Object getItem(int position) {
        if (position == 0) {
            return null;
        } else if (position == 1) {
            return null;
        } else {
            return list.get(position - 2);
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        int viewType = getItemViewType(position);
        if (viewType == 0) {//最近
            viewHolder = ViewHolder.get(context, convertView, parent, itemLayoutId[0], position);
                GridViewForScrollView grid = viewHolder.getView(R.id.grid_city_lately);
                grid.setAdapter(new CommonAdapter<CityBean.Data>(context, listLately, R.layout.item_city_single) {
                    @Override
                    public void convert(ViewHolder holder, CityBean.Data item, int position) {
                        holder.setText(R.id.city_name, item.getName());
                    }
                });
                grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        setCityName(listLately.get(position));
                    }
                });
        } else if (viewType == 1) {//全国
            viewHolder = ViewHolder.get(context, convertView, parent, itemLayoutId[1], position);
            viewHolder.getView(R.id.city_name).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CityBean.Data _default = new CityBean.Data("全国");
                    _default.setWorkstation_id(-1);
                    setCityName(_default);
                }
            });
        } else {//城市
            viewHolder = ViewHolder.get(context, convertView, parent, itemLayoutId[2], position);
            CityBean.Data item = list.get(position - 2);
            viewHolder.setText(R.id.city_latter, item.getFirstLetter());
            viewHolder.setText(R.id.city_name, item.getName());
            if (position > 2) {
                if (list.get(position - 3).getFirstLetter().equals(item.getFirstLetter())) {
                    viewHolder.getView(R.id.city_latter).setVisibility(View.GONE);
                } else {
                    viewHolder.getView(R.id.city_latter).setVisibility(View.VISIBLE);
                }
            }
            if(position == 2){
                viewHolder.getView(R.id.city_latter).setVisibility(View.VISIBLE);
            }
            viewHolder.getView(R.id.city_name).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setCityName(list.get(position - 2));
                }
            });
        }
        return viewHolder.getConvertView();
    }

    @Override
    public int getItemViewType(int position) {
        return position < (VIEW_TYPE_COUNT - 1) ? position : (VIEW_TYPE_COUNT - 1);
    }

    public int getPositionByLetter(String s) {
        Integer value = letterMap.get(s);
        if (StringUtils.isNullOrBlanK(value)) {
            return -1;
        } else {
            return value.intValue();
        }
    }

    public String getLetterByPosition(int position) {
        if (position == 0) {
            return "最近";
        } else if (position == 1) {
            return "全国";
        } else {
            return list.get(position - 2).getFirstLetter();
        }
    }

    public abstract void setCityName(CityBean.Data data);
}
