package cn.qatime.player.flow.screen;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author luntify
 * @date 2017/12/12 13:48
 * @Description: Ⅲ.听后转述
 */

public class FlowRePostScreen extends BaseScreen implements Parcelable {
    public int index;

    public FlowRePostScreen(int index, int passed) {
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

    protected FlowRePostScreen(Parcel in) {
        super(in);
        this.index = in.readInt();
    }

    public static final Creator<FlowRePostScreen> CREATOR = new Creator<FlowRePostScreen>() {
        @Override
        public FlowRePostScreen createFromParcel(Parcel source) {
            return new FlowRePostScreen(source);
        }

        @Override
        public FlowRePostScreen[] newArray(int size) {
            return new FlowRePostScreen[size];
        }
    };
}
