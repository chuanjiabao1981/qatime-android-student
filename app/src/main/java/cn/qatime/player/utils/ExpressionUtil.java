package cn.qatime.player.utils;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;

import com.orhanobut.logger.Logger;

import java.lang.reflect.Field;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.qatime.player.R;
import cn.qatime.player.view.AnimatedImageSpan;
import cn.qatime.player.view.GifDrawable;

public class ExpressionUtil {
    public static String emoji = "\\[em_\\d{1,2}\\]";

    /**
     * 对spannableString经行正则判断，如果匹配成功，就用表情图片替代字符串
     *
     * @param spannableString
     * @param patten
     * @param start
     */
    public static String dealExpression(String spannableString, Pattern patten, int start) throws Exception {
        Matcher matcher = patten.matcher(spannableString);
        while (matcher.find()) {
            String key = matcher.group();
            if (matcher.start() < start) {
                continue;
            }
            spannableString = spannableString.replace(key, "");
            // String imagekey = "b"
            // + key.substring(key.indexOf("#") + 1, key.lastIndexOf("#"));
//            String imagekey = key.replace("[", "").replace("]", "");
//            Field field = R.mipmap.class.getDeclaredField(imagekey);
//            int resId = Integer.parseInt(field.get(null).toString());
//            if (resId != 0) {
//                Bitmap bitmap = BitmapFactory.decodeResource(
//                        context.getResources(), resId);
//                ImageSpan imageSpan = new ImageSpan(context, bitmap);
//                int end = matcher.start() + key.length();
//                spannableString.setSpan(imageSpan, matcher.start(), end,
//                        Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//                if (end < matcher.end()) {
//
//                }
        }
        return spannableString;

    }

    /**
     * 得到一个SpanableString对象，通过传入的字符串,并进行正则判断
     *
     * @param context
     * @param string
     * @param zhengze
     * @return
     */
    public static SpannableString getExpressionString(Context context,
                                                      String string, String zhengze,
                                                      Hashtable<Integer, GifDrawable> cache, GifDrawable.UpdateListener listener) {
        SpannableString spannableString = new SpannableString(string);
        Pattern pattern = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);
        try {
            // dealExpression(context, spannableString, pattern, 0);
            dealExpression_gif(context, spannableString, pattern, cache, listener);
        } catch (Exception e) {
            Logger.e(e.toString());
        }
        return spannableString;

    }

    public static void dealExpression_gif(Context context,
                                          SpannableString spannableString, Pattern patten,
                                          Hashtable<Integer, GifDrawable> cache, GifDrawable.UpdateListener listener)
            throws NoSuchFieldException,
            IllegalAccessException, IllegalArgumentException {
        Matcher matcher = patten.matcher(spannableString);
        while (matcher.find()) {
            String key = matcher.group();
            String imagkey = key.replace("[", "").replace("]", "");
            Field field = R.mipmap.class.getDeclaredField(imagkey);
            int resId = Integer.parseInt(field.get(null).toString());
            if (resId != 0) {
                GifDrawable gifDrawable = null;
                if (cache.containsKey(resId)) {
                    gifDrawable = cache.get(resId);
                } else {
                    gifDrawable = new GifDrawable(context, context
                            .getResources().openRawResource(resId), listener);
                    cache.put(resId, gifDrawable);
                }
                AnimatedImageSpan imageSpan = new AnimatedImageSpan(gifDrawable);
                spannableString.setSpan(imageSpan, matcher.start(), matcher.start() + key.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }
        }
    }


    /**
     * 得到一个SpanableString对象，通过传入的字符串,并进行正则判断
     *
     * @param str
     * @return
     */
    public static String getExpressionString(String str, String zhengze) {
//        SpannableString spannableString = new SpannableString(str);
        Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE); // 通过传入的正则表达式来生成一个pattern
        try {
            return dealExpression(str, sinaPatten, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }
}
