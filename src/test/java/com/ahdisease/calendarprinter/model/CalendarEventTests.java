package com.ahdisease.calendarprinter.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class CalendarEventTests {

    @Test
    public void toString_returns_expected_value() {
        //arrange
            //create expected calendar event string
        String[] expectedICalendarLines = new String[] {
                "BEGIN:VEVENT",
                "SUMMARY:Spring Begins",
                "UID:",
                "DATEST:20230321T050000Z",
                "END:VEVENT"
        };

            //create calendar event with
        ZonedDateTime dateTime = ZonedDateTime.of(2023 , 03, 21, 0, 0, 0,0, ZoneId.of("EST",ZoneId.SHORT_IDS));
        CalendarEvent firstDayOfSpring = new CalendarEvent("Spring Begins", dateTime);

        //act
        String iCalendarString = firstDayOfSpring.toString();
        String[] iCalendarLines = iCalendarString.split("\n");

        //assert
        Assertions.assertEquals(expectedICalendarLines.length, iCalendarLines.length);

        for (int i = 0; i < iCalendarLines.length; i++) {
            if(iCalendarLines[i].startsWith("UID")) {
                //todo implement regex checking
                continue;
            }

            Assertions.assertEquals(expectedICalendarLines[i],iCalendarLines[i]);
        }
    }

}
