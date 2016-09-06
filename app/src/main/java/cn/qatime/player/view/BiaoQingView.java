package cn.qatime.player.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.annotation.NonNull;
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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.utils.DensityUtils;
import libraryextra.view.TagViewPager;

public class BiaoQingView extends RelativeLayout {
    private EditText content;
    private TagViewPager viewPager;


    private Handler hd = new Handler();
    private ImageView emoji;

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
        viewPager = new TagViewPager(getContext());
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, (int) (DensityUtils.dp2px(getContext(), 180)));
        viewPager.setLayoutParams(params);
        viewPager.setVisibility(View.GONE);
        this.addView(viewPager);
        emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeInput();
                hd.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (viewPager.getVisibility() == GONE) {
                            viewPager.setVisibility(View.VISIBLE);
                            emoji.setImageResource(R.mipmap.keybord);
                        } else {
                            viewPager.setVisibility(View.GONE);
                            emoji.setImageResource(R.mipmap.biaoqing);
                        }

                    }
                }, 100);
            }
        });
        content.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                openInput();
                content.requestFocus();
                emoji.setImageResource(R.mipmap.biaoqing);
                viewPager.setVisibility(View.GONE);
                return false;
            }
        });
        List<List<Map<String, Integer>>> listmap = initData();
        initViewPager(listmap);
    }

    private void initViewPager(final List<List<Map<String, Integer>>> listmap) {
        viewPager.init(R.drawable.shape_photo_tag_select, R.drawable.shape_photo_tag_nomal, 16, 8, 2, 40);
        viewPager.setAutoNext(false, 0);
        viewPager.setOnGetView(new TagViewPager.OnGetView() {
            @Override
            public View getView(ViewGroup container, int position) {
                final GridView gv = new GridView(getContext());
                gv.setNumColumns(7);
                gv.setAdapter(new CommonAdapter<Map<String, Integer>>(getContext(), listmap.get(position), R.layout.list_emoji_page) {

                    @Override
                    public void convert(ViewHolder holder, final Map<String, Integer> item, final int position) {
                        ImageView view = holder.getView(R.id.emoji_image);
                        if (item.get("image") != null) {
                            int resId = item.get("image");
                            view.setImageResource(resId);
                            view.setEnabled(true);
                        } else {
                            view.setEnabled(false);
                        }
                        view.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // TODO: 2016/9/5 加入到eidttext
                                if (position < 21) {
                                    content.append(getEmotionContent(item.get("image")));
                                } else if (position == 27) {
                                    //动作按下
                                    int action = KeyEvent.ACTION_DOWN;
                                    //code:删除，其他code也可以，例如 code = 0
                                    int code = KeyEvent.KEYCODE_DEL;
                                    KeyEvent event = new KeyEvent(action, code);
                                    content.setPressed(true);
                                    content.onKeyDown(KeyEvent.KEYCODE_DEL, event); //抛给系统处理了
                                } else if (viewPager.getCurrentItem() != 2) {
                                    content.append(getEmotionContent(item.get("image")));
                                }
                            }
                        });
                    }
                });
                gv.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.WRAP_CONTENT));
                gv.setGravity(Gravity.CENTER);
                container.addView(gv);
                return gv;
            }
        });
        viewPager.setAdapter(3);
    }

    @NonNull
    private List<List<Map<String, Integer>>> initData() {
        final List<Map<String, Integer>> listitems1 = new ArrayList<>();
        final List<Map<String, Integer>> listitems2 = new ArrayList<>();
        final List<Map<String, Integer>> listitems3 = new ArrayList<>();
        try {
            for (int i = 1; i <= 28; i++) {
                Map<String, Integer> listitem1 = new HashMap<>();
                if (i != 28) {
                    listitem1.put("image",
                            Integer.parseInt(R.mipmap.class.getDeclaredField("em_" + i).get(null).toString()));
                } else {
                    listitem1.put("image", R.mipmap.left_arrow);
                }
                listitems1.add(listitem1);
            }
            for (int i = 28; i <= 55; i++) {
                Map<String, Integer> listitem2 = new HashMap<>();
                if (i != 55) {
                    listitem2.put("image", Integer.parseInt(R.mipmap.class.getDeclaredField("em_" + i).get(null).toString()));
                } else {
                    listitem2.put("image", R.mipmap.left_arrow);
                }
                listitems2.add(listitem2);
            }

            for (int i = 55; i <= 82; i++) {
                Map<String, Integer> listitem3 = new HashMap<>();
                if (i <= 75) {
                    listitem3.put("image", Integer.parseInt(R.mipmap.class.getDeclaredField("em_" + i).get(null).toString()));
                } else if (i == 82) {
                    listitem3.put("image", R.mipmap.left_arrow);
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
        final List<List<Map<String, Integer>>> listmap = new ArrayList();
        listmap.add(listitems1);
        listmap.add(listitems2);
        listmap.add(listitems3);
        return listmap;
    }

    public SpannableString getEmotionContent(int resId) {
        String emoji = "[" + getResources().getResourceName(resId).replace("cn.qatime.player:mipmap/", "") + "]";
        SpannableString spannableString = new SpannableString("" + emoji);
        Resources res = getResources();
        int size = (int) content.getTextSize();
        Bitmap bitmap = BitmapFactory.decodeResource(res, resId);
        Bitmap scaleBitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);
        ImageSpan span = new ImageSpan(getContext(), scaleBitmap);
        spannableString.setSpan(span, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public void init(EditText edit, ImageView emoji) {
        this.content = edit;
        this.emoji = emoji;
        initEmoji();
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
}