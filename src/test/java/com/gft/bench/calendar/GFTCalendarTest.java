package com.gft.bench.calendar;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Iterator;

public class GFTCalendarTest {
    public GFTCalendarTest() {
    }


    @Test
    public void shouldReturnTheInitialDate() {
        GFTCalendar gftCalendar = new GFTCalendar();
        gftCalendar.setInitialDate(LocalDate.of(2016, 10, 19));

        Iterator<LocalDate> iterator = gftCalendar.iterator();
        LocalDate localDate2 = iterator.next();

        Assert.assertEquals(LocalDate.of(2016, 10, 21), localDate2);
    }

    @Test
    public void shouldProduceIndependentIterators() {
        GFTCalendar gftCalendar1 = new GFTCalendar();
        gftCalendar1.setInitialDate(LocalDate.of(2016, 10, 19));

        Iterator<LocalDate> iterator1 = gftCalendar1.iterator();
        Iterator<LocalDate> iterator2 = gftCalendar1.iterator();
        LocalDate localDate1 = iterator1.next();
        LocalDate localDate2 = iterator2.next();

        Assert.assertEquals(localDate1, localDate2);
    }
}
