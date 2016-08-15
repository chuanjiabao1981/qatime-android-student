package cn.qatime.player.utils;

import org.apache.http.NameValuePair;

import java.security.MessageDigest;
import java.util.List;

/**
 * @author luntify
 * @date 2016/8/15 11:45
 * @Description
 */
public class SignUtil {
    public static String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }

        sb.append("key=");
        sb.append("8hJno18aMonxkkHI14bLPBZfK3sZ6Fsf");

        LogUtils.e("拼接字符串 ----------------------------- ", sb.toString());
        String appSign = toMD5(sb.toString()).toUpperCase();
        LogUtils.e("签名  --------------- ", appSign);
        return appSign;
    }

    public static String toMD5(String plainText) {
        try {
            //生成实现指定摘要算法的 MessageDigest 对象。
            MessageDigest md = MessageDigest.getInstance("MD5");
            //使用指定的字节数组更新摘要。
            md.update(plainText.getBytes());
            //通过执行诸如填充之类的最终操作完成哈希计算。
            byte b[] = md.digest();
            //生成具体的md5密码到buf数组
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            return buf.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
