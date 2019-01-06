package com.gdn.helper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Calendar;
import java.util.Date;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DateHelper.class)
public class DateHelperTest {

    private static final int AMOUNT_OF_DAY_ADDED = 2;
    private static final int HOUR = 16;

    @Before
    public void setUp() throws Exception {
        PowerMockito.mockStatic(DateHelper.class);
    }

    @Test
    public void setDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, AMOUNT_OF_DAY_ADDED);
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        given(DateHelper.setDay(AMOUNT_OF_DAY_ADDED)).willReturn(calendar.getTime());
        Date expectedResponse = DateHelper.setDay(AMOUNT_OF_DAY_ADDED);
        assertThat(expectedResponse, equalTo(calendar.getTime()));
    }

    @Test
    public void setTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, HOUR);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        given(DateHelper.setTime(HOUR)).willReturn(calendar.getTime());
        Date expectedResponse = DateHelper.setTime(HOUR);
        assertThat(expectedResponse, equalTo(calendar.getTime()));
    }

}