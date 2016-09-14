package cn.qatime.player.utils;

import android.content.Context;

import java.util.List;

import cn.qatime.player.ApplicationTest;
import libraryextra.bean.ImageBucket;


/**
 * @author Tianhaoranly
 * @date 2016/9/13 13:40
 * @Description:
 */
public class AlbumHelperTest extends ApplicationTest {

    private AlbumHelper helper;
    private Context context;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        helper = AlbumHelper.getHelper();
        context = getContext();
        helper.init(context);
    }

    public void testInit() throws Exception {
        List<ImageBucket> imagesBucketList = helper.getImagesBucketList(false);
        assertEquals("20",imagesBucketList.size()+"");
    }
}