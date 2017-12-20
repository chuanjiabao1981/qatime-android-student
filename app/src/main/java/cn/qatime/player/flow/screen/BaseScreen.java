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
    public int passed;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.passed);
    }

    public BaseScreen() {
    }

    protected BaseScreen(Parcel in) {
        this.passed = in.readInt();
    }

    public static final Parcelable.Creator<BaseScreen> CREATOR = new Parcelable.Creator<BaseScreen>() {
        @Override
        public BaseScreen createFromParcel(Parcel source) {
            return new BaseScreen(source);
        }

        @Override
        public BaseScreen[] newArray(int size) {
            return new BaseScreen[size];
        }
    };
}
