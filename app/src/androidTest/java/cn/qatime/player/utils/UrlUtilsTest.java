package cn.qatime.player.utils;

import junit.framework.Assert;

import java.util.HashMap;
import java.util.Map;

import cn.qatime.player.ApplicationTest;

/**
 * @author Tianhaoranly
 * @date 2016/9/13 10:33
 * @Description:
 */
public class UrlUtilsTest extends ApplicationTest{

    public void testGetUrl() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("11","12");
        map.put("13","14");
        map.put("15","16");
        map.put("17","18");
        String url = UrlUtils.getUrl("www.baidu.com", map);
        Assert.assertEquals("www.baidu.com?11=12&13=14&15=16&17=18",url);
    }
}