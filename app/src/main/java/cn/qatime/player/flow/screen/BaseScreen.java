package cn.qatime.player.flow.screen;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author luntify
 * @date 2017/12/18 10:11
 * @Description:
 */

public class BaseScreen implements Parcelable {
    public int passed;//已做题数量
    public int max;//每类型的小题个数


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.passed);
        dest.writeInt(this.max);
    }

    public BaseScreen() {
    }

    protected BaseScreen(Parcel in) {
        this.passed = in.readInt();
        this.max = in.readInt();
    }

}
