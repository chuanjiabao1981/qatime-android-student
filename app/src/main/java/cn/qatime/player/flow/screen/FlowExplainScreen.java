package cn.qatime.player.flow.screen;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author luntify
 * @date 2017/12/11 11:22
 * @Description: 单选题页面
 */

public class FlowExplainScreen extends BaseScreen implements Parcelable {
    private int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public FlowExplainScreen(int index, int passed) {
        super();
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

    protected FlowExplainScreen(Parcel in) {
        this.index = in.readInt();
    }

    public static final Creator<FlowExplainScreen> CREATOR = new Creator<FlowExplainScreen>() {
        @Override
        public FlowExplainScreen createFromParcel(Parcel source) {
            return new FlowExplainScreen(source);
        }

        @Override
        public FlowExplainScreen[] newArray(int size) {
            return new FlowExplainScreen[size];
        }
    };
}
