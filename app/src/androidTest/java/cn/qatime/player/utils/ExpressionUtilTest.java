package cn.qatime.player.utils;

import junit.framework.Assert;

import cn.qatime.player.ApplicationTest;

/**
 * @author Tianhaoranly
 * @date 2016/9/13 11:16
 * @Description:
 */
public class ExpressionUtilTest extends ApplicationTest{


    public void testGetExpressionString() throws Exception {
        String expressionString = ExpressionUtil.getExpressionString("ssdcd", "d");
        Assert.assertEquals("ssc",expressionString);
    }
}