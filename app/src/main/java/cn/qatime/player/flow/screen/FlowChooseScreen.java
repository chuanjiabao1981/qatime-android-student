package cn.qatime.player.flow.screen;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author luntify
 * @date 2017/12/11 11:22
 * @Description: Ⅰ.听后选择
 */

public class FlowChooseScreen extends BaseScreen implements Parcelable {
    public int index;

    public FlowChooseScreen(int index, int passed) {
        this.index = index;
        this.passed = passed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.index);
    }

    protected FlowChooseScreen(Parcel in) {
        this.index = in.readInt();
    }

    public static final Parcelable.Creator<FlowChooseScreen> CREATOR = new Parcelable.Creator<FlowChooseScreen>() {
        @Override
        public FlowChooseScreen createFromParcel(Parcel source) {
            return new FlowChooseScreen(source);
        }

        @Override
        public FlowChooseScreen[] newArray(int size) {
            return new FlowChooseScreen[size];
        }
    };

}
