package cn.qatime.player.flow.screen;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author luntify
 * @date 2017/12/12 13:48
 * @Description: Ⅳ.朗读短文
 */

public class FlowReadScreen extends BaseScreen implements Parcelable {
    public int index;

    public FlowReadScreen(int index, int passed) {
        this.index = index;
        this.passed = passed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.index);
    }

    protected FlowReadScreen(Parcel in) {
        super(in);
        this.index = in.readInt();
    }

    public static final Creator<FlowReadScreen> CREATOR = new Creator<FlowReadScreen>() {
        @Override
        public FlowReadScreen createFromParcel(Parcel source) {
            return new FlowReadScreen(source);
        }

        @Override
        public FlowReadScreen[] newArray(int size) {
            return new FlowReadScreen[size];
        }
    };
}
