package cn.qatime.player.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import libraryextra.adapter.ViewHolder;
import libraryextra.utils.DensityUtils;
import libraryextra.utils.GifHelper;
import libraryextra.view.TagViewPager;

public class BiaoQingView extends RelativeLayout {
    private EditText edit;
    private TagViewPager viewPager;
    private List<Bitmap> bitmapList = new ArrayList<>();

    private Handler hd = new Handler();
    private ImageView emoji;
    private Runnable r1;
    private List<List<Map<String, Integer>>> listmap;
    private List<GridView> gv;
    private KeyEvent delete;
    private LayoutParams params;

    private String[][] biaoqingTags = new String[3][28];

    public BiaoQingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public BiaoQingView(Context context) {
        super(context);
    }

    public BiaoQingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void initEmoji() {
        emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BiaoQingView.this.getVisibility() == GONE) {//如果隐藏 打开
                    BiaoQingView.this.setVisibility(View.VISIBLE);
                    closeInput();
                } else {
                    edit.requestFocus();
                    openInput();
                }
            }
        });
        edit.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                openInput();
                return false;
            }
        });
        initViewPager();
        initData();
//        initGv();
    }

    private void initViewPager() {
        viewPager = new TagViewPager(getContext());
        params = new LayoutParams(LayoutParams.MATCH_PARENT, DensityUtils.dp2px(getContext(), 180));
        viewPager.setLayoutParams(params);
        this.addView(viewPager);
        viewPager.init(R.drawable.shape_biaoqing_tag_select, R.drawable.shape_biaoqing_tag_nomal, 16, 8, 2, 40);
        viewPager.setAutoNext(false, 0);
        viewPager.setOnGetView(new TagViewPager.OnGetView() {
            @Override
            public View getView(ViewGroup container, final int page) {
                GridView mGridView = new GridView(edit.getContext());
                mGridView.setLayoutParams(params);
                mGridView.setGravity(Gravity.CENTER);
                mGridView.setPadding(0,20,0,0);
                mGridView.setBackgroundColor(Color.WHITE);
                mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
                mGridView.setNumColumns(7);
                mGridView.setAdapter(new BaseAdapter() {

                    public ViewHolder viewHolder;

                    @Override
                    public int getCount() {
                        return biaoqingTags[page].length;
                    }

                    @Override
                    public Object getItem(int position) {
                        return biaoqingTags[page][position];
                    }

                    @Override
                    public long getItemId(int position) {
                        return position;
                    }

                    @TargetApi(Build.VERSION_CODES.KITKAT)
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        viewHolder = ViewHolder.get(edit.getContext(), convertView, parent, R.layout.item_emoji_page, position);
                        if (biaoqingTags[page][position] != null) {
                            try {
                                viewHolder.setImageResource(R.id.emoji_image, Integer.parseInt(R.mipmap.class.getDeclaredField(biaoqingTags[page][position]).get(null).toString()));
                            } catch (IllegalAccessException | NoSuchFieldException e) {
                                e.printStackTrace();
                            }
                        }
                        return viewHolder.getConvertView();
                    }
                });
                mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position < 21) {
                            edit.append(getEmotionContent(biaoqingTags[viewPager.getCurrentItem()][position]));
                        } else if (position == 27) {
                            //动作按下
                            int action = KeyEvent.ACTION_DOWN;
                            //code:删除，其他code也可以，例如 code = 0
                            int code = KeyEvent.KEYCODE_DEL;
                            KeyEvent event = new KeyEvent(action, code);
                            edit.onKeyDown(KeyEvent.KEYCODE_DEL, event); //抛给系统处理了
                        } else if (viewPager.getCurrentItem() != 2) {
                            edit.append(getEmotionContent(biaoqingTags[viewPager.getCurrentItem()][position]));
                        }
                    }
                });
                container.addView(mGridView);
                return mGridView;
            }
        });
        viewPager.setAdapter(3);
    }


    private void initData() {
        int position = 1;

        try {
            for (int i = 0; i < 3; i++) {
                for (int j = 27 * i; j < 27 * (i + 1); j++) {
                    biaoqingTags[i][j - 27 * i] = "em_" + position;
                    if (j != 27) {
                        position++;
                    }
                }
                biaoqingTags[i][27] = "emoji_delete";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SpannableString getEmotionContent(String resId) {
        String emoji = "[" + resId + "]";
        SpannableString spannableString = new SpannableString(emoji);
        int size = (int) edit.getTextSize();
        GifHelper helper = new GifHelper();
        int id = 0;
        try {
            id = Integer.parseInt(R.mipmap.class.getDeclaredField(resId).get(null).toString());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        InputStream is = getResources().openRawResource(id);
        helper.read(is);
        Bitmap bitmap = helper.getImage();
        Bitmap scaleBitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);
        ImageSpan span = new ImageSpan(getContext(), scaleBitmap);
        spannableString.setSpan(span, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        bitmapList.add(bitmap);
        bitmapList.add(scaleBitmap);
        return spannableString;
    }

    public void init(EditText edit, ImageView emoji) {
        this.edit = edit;
        this.emoji = emoji;
        BiaoQingView.this.setVisibility(View.GONE);//初始隐藏
        initEmoji();
    }

    /**
     * 关闭输入法
     */
    public void closeInput() {
        if (BiaoQingView.this.getVisibility() == View.VISIBLE) {
            emoji.setImageResource(R.mipmap.keybord);
        }
        InputMethodManager inputMethodManager = (InputMethodManager) this
                .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(edit.getWindowToken(), 0);
    }

    /**
     * 输入法打开
     */

    public void openInput() {
        emoji.setImageResource(R.mipmap.biaoqing);
        BiaoQingView.this.setVisibility(View.GONE);
        InputMethodManager inputMethodManager = (InputMethodManager) this.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        // 接受软键盘输入的编辑文本或其它视图
        inputMethodManager.showSoftInput(edit, 0);
    }

    public void closeEmojiAndInput() {
        emoji.setImageResource(R.mipmap.biaoqing);
        closeInput();
    }
}
