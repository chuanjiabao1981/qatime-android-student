package cn.qatime.player.flow.screen;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author luntify
 * @date 2017/12/12 13:48
 * @Description: Ⅲ.听后记录
 */

public class FlowRecordScreen extends BaseScreen implements Parcelable {
    public int index;

    public FlowRecordScreen(int index, int passed) {
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

    protected FlowRecordScreen(Parcel in) {
        super(in);
        this.index = in.readInt();
    }

    public static final Creator<FlowRecordScreen> CREATOR = new Creator<FlowRecordScreen>() {
        @Override
        public FlowRecordScreen createFromParcel(Parcel source) {
            return new FlowRecordScreen(source);
        }

        @Override
        public FlowRecordScreen[] newArray(int size) {
            return new FlowRecordScreen[size];
        }
    };
}
