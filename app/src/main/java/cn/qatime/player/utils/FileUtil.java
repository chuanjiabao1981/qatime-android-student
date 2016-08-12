package cn.qatime.player.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

/**
 * @author luntify
 * @date 2016/8/12 10:07
 * @Description
 */
public class FileUtil {


    /**
     * 把数据写入文�?
     *
     * @param is       数据�?
     * @param path     文件路径
     * @param recreate 如果文件存在，是否需要删除重�?
     * @return 是否写入成功
     */
    public static boolean writeFile(InputStream is, String path, boolean recreate) {
        boolean res = false;
        File f = new File(path);
        FileOutputStream fos = null;
        try {
            if (recreate && f.exists()) {
                f.delete();
            }
            if (!f.exists() && null != is) {
                File parentFile = new File(f.getParent());
                parentFile.mkdirs();
                int count = -1;
                byte[] buffer = new byte[1024];
                fos = new FileOutputStream(f);
                while ((count = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, count);
                }
                res = true;
            }
        } catch (Exception e) {
            LogUtils.e(e);
        } finally {
            try {
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    public static String readFile(String path) {
        FileInputStream inStream = null;
        try {
            inStream = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];

        int length = -1;

        try {
            while ((length = inStream.read(buffer)) != -1) {
                bos.write(buffer, 0, length);
            }
            bos.close();
            inStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bos.toString();
    }
}
