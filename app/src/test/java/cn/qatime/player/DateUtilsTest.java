package cn.qatime.player.utils;

import org.junit.Test;

import java.util.Date;

import libraryextra.utils.DateUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Tianhaoranly
 * @date 2016/9/13 10:18
 * @Description:
 */
public class DateUtilsTest {

    @Test
    public void testGetMonthDays() {
//第10个月  9+1
        int monthDays = DateUtils.getMonthDays(2016, 9);
        assertEquals(31, monthDays);
    }

    @Test
    public void testGetFirstDayWeek() throws Exception {
        //第10个月  9+1
        int monthDays = DateUtils.getFirstDayWeek(2016, 9);
        assertEquals(6, monthDays);
    }

    @Test
    public void testGetTimeShowString() throws Exception {
        String timeShowString = DateUtils.getTimeShowString(new Date(999999l).getTime(), true);
        assertEquals("1970-01-01",timeShowString);
        String timeShowString1 = DateUtils.getTimeShowString(new Date(999999l).getTime(), false);
        assertEquals("1970-01-01 08:16",timeShowString1);
    }

    @Test
    public void testGetTodayTimeBucket() throws Exception {
        //上午 08:16
        String timeShowString = DateUtils.getTodayTimeBucket(new Date(999999l));
        assertEquals("上午 08:16",timeShowString);
    }

    @Test
    public void testIsSameWeekDates() throws Exception {
        boolean timeShowString = DateUtils.isSameWeekDates(new Date(), new Date());
        assertTrue(timeShowString);
    }

    @Test
    public void testGetWeekOfDate() throws Exception {
        String weekOfDate = DateUtils.getWeekOfDate(new Date());
        assertEquals("星期二",weekOfDate);
    }
}