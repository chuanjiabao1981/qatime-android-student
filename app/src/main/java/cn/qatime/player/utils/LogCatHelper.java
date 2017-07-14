package cn.qatime.player.utils;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;

import libraryextra.utils.StringUtils;

/**
 * @author lungtify
 * @Time 2017/6/29 10:52
 * @Describe 写日志到本地
 */

public class LogCatHelper {
    private static LogCatHelper instance;
    private String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/qatime";
    private FileOutputStream fos;
    private BufferedReader mReader;

    public static LogCatHelper getInstance(String path) {
        if (instance == null) {
            instance = new LogCatHelper(path);
        }
        return instance;
    }

    private LogCatHelper(String path) {
        if (!StringUtils.isNullOrBlanK(path)) {
            dirPath = path;
        }
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public void log(String tag) {
        File file = new File(dirPath, "qatime_student.log");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(file, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        StringBuilder builder = new StringBuilder();
        builder.append(getFormatTime())
                .append(" ")
                .append(getSimpleClassName(trace[3].getClassName()))
                .append(".")
                .append(trace[3].getMethodName())
                .append(" ")
                .append("(")
                .append(trace[3].getFileName())
                .append(":")
                .append(trace[3].getLineNumber())
                .append(")")
                .append(" ");
        if (!StringUtils.isNullOrBlanK(tag)) {
            builder.append(tag);
        }
        try {
            mReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(builder.toString().getBytes())), 1024);
            String line;
            while ((line = mReader.readLine()) != null) {
                if (line.length() == 0) {
                    continue;
                }
                if (fos != null) {
                    fos.write(line.getBytes());
                    fos.write("\r\n".getBytes());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (mReader != null) {
                    mReader.close();
                    mReader = null;
                }
                if (fos != null) {
                    fos.close();
                    fos = null;
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    private String getSimpleClassName(String name) {
        int lastIndex = name.lastIndexOf(".");
        return name.substring(lastIndex + 1);
    }

    private String getFormatTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(System.currentTimeMillis());
    }
}
