package cn.qatime.player.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.orhanobut.logger.Logger;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.utils.GifHelper;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.utils.DensityUtils;
import libraryextra.view.TagViewPager;

public class BiaoQingView extends RelativeLayout {
    private EditText content;
    private TagViewPager viewPager;
    private List<Bitmap> bitmapList = new ArrayList<>();

    private Handler hd = new Handler();
    private ImageView emoji;
    private Runnable r1;
    private List<List<Map<String, Integer>>> listmap;
    private List<GridView> gv;
    private KeyEvent delete;

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
        r1 = new Runnable() {

            @Override
            public void run() {
                viewPager.setVisibility(View.VISIBLE);
                emoji.setImageResource(R.mipmap.keybord);
            }

        };
        emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getVisibility() == GONE) {
                    hd.postDelayed(r1, 50);
                    closeInput();
                } else {
                    viewPager.setVisibility(View.GONE);
                    emoji.setImageResource(R.mipmap.biaoqing);
                    content.requestFocus();
                    openInput();
                }
            }
        });
        content.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                content.requestFocus();
                openInput();
                emoji.setImageResource(R.mipmap.biaoqing);
                viewPager.setVisibility(View.GONE);
                return false;
            }
        });
        initData();
        initGv();
        initViewPager();
    }

    private void initViewPager() {
        viewPager = new TagViewPager(getContext());
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, DensityUtils.dp2px(getContext(), 180));
        viewPager.setLayoutParams(params);
        viewPager.setId(R.id.viewPager);
        viewPager.setVisibility(View.GONE);
        this.addView(viewPager);
        viewPager.init(R.drawable.shape_biaoqing_tag_select, R.drawable.shape_biaoqing_tag_nomal, 16, 8, 2, 40);
        viewPager.setAutoNext(false, 0);
        viewPager.setOnGetView(new TagViewPager.OnGetView() {
            @Override
            public View getView(ViewGroup container, int position) {
                container.addView(gv.get(position));
                return gv.get(position);
            }
        });
        viewPager.setAdapter(3);
    }

    private void initGv() {
        int action = KeyEvent.ACTION_DOWN;
        //code:删除，其他code也可以，例如 code = 0
        int code = KeyEvent.KEYCODE_DEL;
        delete = new KeyEvent(action, code);
        gv = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            GridView grid = new GridView(getContext());
            grid.setNumColumns(7);
            grid.setSelector(new ColorDrawable(Color.TRANSPARENT));
            grid.setPadding(0,20,0,0);
            grid.setBackgroundColor(Color.WHITE);
            grid.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.WRAP_CONTENT));
            grid.setGravity(Gravity.CENTER);
            grid.setAdapter(new CommonAdapter<Map<String, Integer>>(getContext(), listmap.get(i), R.layout.list_emoji_page) {

                @Override
                public void convert(ViewHolder holder, Map<String, Integer> item, int position) {
                    ImageView view = holder.getView(R.id.emoji_image);
                    if (item.get("image") != null) {
                        int resId = item.get("image");
//                        if (position == 27) {
                            view.setImageResource(resId);
//                        } else {
//                            Glide.with(getContext()).load(resId).dontAnimate().crossFade().into(view);
////                                view.setImageResource(resId);
//                        }
                    }
                }
            });
            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position < 21) {
                        content.append(getEmotionContent(listmap.get(viewPager.getCurrentItem()).get(position).get("image")));
                    } else if (position == 27) {
                        //动作按下
                        content.setPressed(true);
                        content.onKeyDown(KeyEvent.KEYCODE_DEL, delete); //抛给系统处理了
                    } else if (viewPager.getCurrentItem() != 2) {
                        content.append(getEmotionContent(listmap.get(viewPager.getCurrentItem()).get(position).get("image")));
                    }
                }
            });
            gv.add(grid);
        }
    }

    private void initData() {
        List<Map<String, Integer>> listitems1 = new ArrayList<>();
        List<Map<String, Integer>> listitems2 = new ArrayList<>();
        List<Map<String, Integer>> listitems3 = new ArrayList<>();
        try {
            for (int i = 1; i <= 28; i++) {
                Map<String, Integer> listitem1 = new HashMap<>();
                if (i != 28) {
                    listitem1.put("image",
                            Integer.parseInt(R.mipmap.class.getDeclaredField("em_" + i).get(null).toString()));
                } else {
                    listitem1.put("image", R.mipmap.emoji_delete);
                }
                listitems1.add(listitem1);
            }
            for (int i = 28; i <= 55; i++) {
                Map<String, Integer> listitem2 = new HashMap<>();
                if (i != 55) {
                    listitem2.put("image", Integer.parseInt(R.mipmap.class.getDeclaredField("em_" + i).get(null).toString()));
                } else {
                    listitem2.put("image", R.mipmap.emoji_delete);
                }
                listitems2.add(listitem2);
            }

            for (int i = 55; i <= 82; i++) {
                Map<String, Integer> listitem3 = new HashMap<>();
                if (i <= 75) {
                    listitem3.put("image", Integer.parseInt(R.mipmap.class.getDeclaredField("em_" + i).get(null).toString()));
                } else if (i == 82) {
                    listitem3.put("image", R.mipmap.emoji_delete);
                } else {
                    listitem3.put("image", null);
                }
                listitems3.add(listitem3);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        listmap = new ArrayList();
        listmap.add(listitems1);
        listmap.add(listitems2);
        listmap.add(listitems3);
    }

    public SpannableString getEmotionContent(int resId) {
        String emoji = "[" + getResources().getResourceName(resId).replace("cn.qatime.player:mipmap/", "") + "]";
        SpannableString spannableString = new SpannableString(emoji);
        int size = (int) content.getTextSize();
        GifHelper helper = new GifHelper();
        InputStream is = getResources().openRawResource(resId);
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
        this.content = edit;
        this.emoji = emoji;
        initEmoji();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        listmap.clear();
        gv.clear();
        //回收bitmap
        Logger.e("BiaoQingView回收Bitmp");
        for (Bitmap bitmap : bitmapList) {
            bitmap.recycle();
        }
    }

    /**
     * 关闭输入法
     */
    public void closeInput() {
        InputMethodManager inputMethodManager = (InputMethodManager) this
                .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(content.getWindowToken(), 0);
    }

    /**
     * 打开输入发
     */

    public void openInput() {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        // 接受软键盘输入的编辑文本或其它视图
        inputMethodManager.showSoftInput(content, 0);
    }

    public void closeEmojiAndInput() {
        viewPager.setVisibility(View.GONE);
        emoji.setImageResource(R.mipmap.biaoqing);
        closeInput();
    }
}
