package cn.qatime.player.im.model;


public enum FullScreenType {
    /**
     * 全屏模式关闭
     */
    CLOSE(0),
    /**
     * 全屏模式开启
     */
    OPEN(1);

    private int value;

    FullScreenType(int value){
        this.value = value;
    }

    public static FullScreenType statusOfValue(int status) {
        for (FullScreenType e : values()) {
            if (e.getValue() == status) {
                return e;
            }
        }
        return CLOSE;
    }

    public int getValue(){
        return value;
    }
}
