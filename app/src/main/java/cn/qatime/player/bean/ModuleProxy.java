package cn.qatime.player.bean;

/**
 * @author lungtify
 * @Time 2017/2/10 11:58
 * @Describe 会话窗口提供给子模块的代理接口。
 */

public interface ModuleProxy {
    // 应当收起输入区
    void shouldCollapseInputPanel();

    boolean isLongClickEnabled();
}
