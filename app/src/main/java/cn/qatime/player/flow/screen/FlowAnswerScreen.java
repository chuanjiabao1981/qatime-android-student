package cn.qatime.player.flow.screen;

/**
 * @author luntify
 * @date 2017/12/12 13:48
 * @Description: Ⅱ.听后回答
 */

public class FlowAnswerScreen extends BaseScreen {
    public int index;

    public FlowAnswerScreen(int index, int passed) {
        this.index = index;
        this.passed = passed;
    }
}
