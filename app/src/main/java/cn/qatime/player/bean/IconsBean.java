package cn.qatime.player.bean;

public class IconsBean {
    /**
     * free_taste : false
     * coupon_free : true
     * cheap_moment : false
     * refund_any_time : true
     * join_cheap : false
     */

    private boolean free_taste;
    private boolean coupon_free;
    private boolean cheap_moment;
    private boolean refund_any_time;
    private boolean join_cheap;

    public boolean isFree_taste() {
        return free_taste;
    }

    public void setFree_taste(boolean free_taste) {
        this.free_taste = free_taste;
    }

    public boolean isCoupon_free() {
        return coupon_free;
    }

    public void setCoupon_free(boolean coupon_free) {
        this.coupon_free = coupon_free;
    }

    public boolean isCheap_moment() {
        return cheap_moment;
    }

    public void setCheap_moment(boolean cheap_moment) {
        this.cheap_moment = cheap_moment;
    }

    public boolean isRefund_any_time() {
        return refund_any_time;
    }

    public void setRefund_any_time(boolean refund_any_time) {
        this.refund_any_time = refund_any_time;
    }

    public boolean isJoin_cheap() {
        return join_cheap;
    }

    public void setJoin_cheap(boolean join_cheap) {
        this.join_cheap = join_cheap;
    }
}