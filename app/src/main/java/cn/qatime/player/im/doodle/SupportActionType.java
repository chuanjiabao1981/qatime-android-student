package cn.qatime.player.im.doodle;

import android.util.SparseArray;

import cn.qatime.player.im.doodle.action.MyEraser;


public class SupportActionType {
    private final int DEFAULT_TYPE_KEY_ERASER = -1;

    public SupportActionType() {
        // 默认支持类型
        supportTypes.put(DEFAULT_TYPE_KEY_ERASER, MyEraser.class);
    }

    public static SupportActionType getInstance() {
        return SupportActionTypeHolder.instance;
    }

    private SparseArray<Class> supportTypes = new SparseArray<>();

    public void addSupportActionType(int type, Class cz) {
        supportTypes.put(type, cz);
    }

    public Class getActionClass(int type) {
        return supportTypes.get(type);
    }

    int getEraserType() {
        return DEFAULT_TYPE_KEY_ERASER;
    }

    private static class SupportActionTypeHolder {
        public static final SupportActionType instance = new SupportActionType();
    }
}
