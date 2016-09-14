package cn.qatime.player.utils;

import android.content.Context;

import java.io.File;

import cn.qatime.player.ApplicationTest;


/**
 * @author Tianhaoranly
 * @date 2016/9/13 14:09
 * @Description:
 */
public class DataCleanUtilsTestWithContext extends ApplicationTest {


    private Context context;
    private String totalCacheSize;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getContext();

    }

    public void testGetTotalCacheSize() throws Exception {
        //这个context获取的值不确定,放在clearcache中测试
        testClearAllCache();

    }


    public void testClearAllCache() throws Exception {
        totalCacheSize = DataCleanUtils.getTotalCacheSize(getContext());
        assertNotSame("0.00KB", totalCacheSize);
        DataCleanUtils.clearAllCache(context);
        totalCacheSize = DataCleanUtils.getTotalCacheSize(getContext());
        assertEquals(totalCacheSize, "0.00KB");
    }

    public void testGetFormatSize()  {
        String formatSize = DataCleanUtils.getFormatSize(1024);
        String formatSize1 = DataCleanUtils.getFormatSize(1024*1024);
        String formatSize2 = DataCleanUtils.getFormatSize(1024*1024*1024);
        assertEquals("1.00KB",formatSize);
        assertEquals("1.00MB",formatSize1);
        assertEquals("1.00GB",formatSize2);
    }

    public void testGetFloderSize() throws Exception {
        long folderSize = DataCleanUtils.getFolderSize(new File(""));
        assertEquals(0,folderSize);
    }
}