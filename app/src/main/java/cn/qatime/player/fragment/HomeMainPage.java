package cn.qatime.player.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.orhanobut.logger.Logger;

import java.util.Arrays;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragment;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.view.TagViewPager;

public class HomeMainPage extends BaseFragment {


    private TagViewPager tagViewpagerImg;
    private TagViewPager tagViewpagerSubject;
    private ImageView refreshTeacher;
    private GridView gridviewTeacher;
    private ImageView refreshClass;
    private GridView gridviewClass;
    private List<GridView> gvSub;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_main_page, container, false);
        assignViews(view);
        return view;
    }

    private void assignViews(View view) {
        tagViewpagerImg = (TagViewPager) view.findViewById(R.id.tag_viewpager_img);
        tagViewpagerSubject = (TagViewPager) view.findViewById(R.id.tag_viewpager_subject);
        refreshTeacher = (ImageView) view.findViewById(R.id.refresh_teacher);
        gridviewTeacher = (GridView) view.findViewById(R.id.gridview_teacher);
        refreshClass = (ImageView) view.findViewById(R.id.refresh_class);
        gridviewClass = (GridView) view.findViewById(R.id.gridview_class);

        initTagImg();
        initTagViewpagerSubject();
        initGridTeacher();
        initGridClass();

    }

    private void initTagImg() {
        final int imageIds[] = {R.mipmap.photo, R.mipmap.photo, R.mipmap.photo};
        tagViewpagerImg.init(R.drawable.shape_photo_tag_select, R.drawable.shape_photo_tag_nomal, 16, 8, 2, 40);
        tagViewpagerImg.setAutoNext(true, 4000);
//        viewPager.setId(1252);
        tagViewpagerImg.setOnGetView(new TagViewPager.OnGetView() {
            @Override
            public View getView(ViewGroup container, int position) {
                ImageView iv = new ImageView(getActivity());
                iv.setClickable(true);
                iv.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                iv.setId(position);
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                iv.setImageResource(imageIds[position]);
                container.addView(iv);
                return iv;
            }
        });
        tagViewpagerImg.setAdapter(imageIds.length, 0);
    }

    private void initTagViewpagerSubject() {
        final List<String> strings = Arrays.asList(getResources().getStringArray(R.array.subject));
        tagViewpagerSubject.init(0, 0, 16, 8, 2, 40);
        tagViewpagerSubject.setAutoNext(false, 0);
//        viewPager.setId(1252);
        tagViewpagerSubject.setOnGetView(new TagViewPager.OnGetView() {
            @Override
            public View getView(ViewGroup container, final int position) {
                GridView grid = new GridView(getContext()) {
                    @Override
                    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                    }
                };
                grid.setNumColumns(5);
                grid.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.MATCH_PARENT));
                grid.setPadding(0, 20, 0, 0);
                grid.setBackgroundColor(Color.WHITE);
                grid.setGravity(Gravity.CENTER);
                grid.setAdapter(new CommonAdapter<String>(getContext(), strings, R.layout.item_subject) {
                    @Override
                    public int getCount() {
                        return (strings.size() - 10 * position) > 10 ? 10 : (strings.size() - 10 * position);
                    }

                    @Override
                    public void convert(ViewHolder holder, String item, int positionG) {
                        Logger.e("position" + position);
                        String s = strings.get(position * 10 + positionG);
                        holder.setText(R.id.subject_text, s);
                    }
                });
                grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Logger.e("onItemClick");
                    }
                });
                container.addView(grid);
                return grid;
            }
        });
        Logger.e("size" + (strings.size() - 1) / 10 + 1);
        tagViewpagerSubject.setAdapter((strings.size() - 1) / 10 + 1, 0);
    }

    private void initGridTeacher() {
        final List<String> strings = Arrays.asList(getResources().getStringArray(R.array.subject));
        gridviewTeacher.setAdapter(new CommonAdapter<String>(getContext(), strings, R.layout.item_subject) {
            @Override
            public int getCount() {
                return 5;
            }

            @Override
            public void convert(ViewHolder holder, String item, int position) {
                String s = strings.get(position);
                holder.setText(R.id.subject_text, s);
            }
        });
        gridviewTeacher.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Logger.e("onItemClick");
            }
        });
    }

    private void initGridClass() {
        final List<String> strings = Arrays.asList(getResources().getStringArray(R.array.subject));
        gridviewClass.setAdapter(new CommonAdapter<String>(getContext(), strings, R.layout.item_class_recommend) {
            @Override
            public int getCount() {
                return 8;
            }

            @Override
            public void convert(ViewHolder holder, String item, int position) {
            }
        });
        gridviewClass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Logger.e("onItemClick");
            }
        });
        View view = gridviewClass.getAdapter().getView(0, null, gridviewClass);
        view.measure(0, 0);
        ViewGroup.LayoutParams layoutParams = gridviewClass.getLayoutParams();
        layoutParams.height = view.getMeasuredHeight() * 4;
        gridviewClass.setLayoutParams(layoutParams);
    }


}
