package cn.qatime.player.flow;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author luntify
 * @date 2017/12/11 11:22
 * @Description: 单选题页面
 */

public class FlowRadioScreen implements Parcelable {
private int index;

    public FlowRadioScreen(int index) {
        this.index = index;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.index);
    }

    protected FlowRadioScreen(Parcel in) {
        this.index = in.readInt();
    }

    public static final Parcelable.Creator<FlowRadioScreen> CREATOR = new Parcelable.Creator<FlowRadioScreen>() {
        @Override
        public FlowRadioScreen createFromParcel(Parcel source) {
            return new FlowRadioScreen(source);
        }

        @Override
        public FlowRadioScreen[] newArray(int size) {
            return new FlowRadioScreen[size];
        }
    };
}
