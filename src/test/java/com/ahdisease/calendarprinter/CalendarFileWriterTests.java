package com.ahdisease.calendarprinter;

import com.ahdisease.calendarprinter.model.CalendarEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class CalendarFileWriterTests {
    private CalendarFileWriter writer;
    private CalendarEvent eventOne = null;
    private CalendarEvent eventTwo = null;

    @BeforeEach
    public void setup() {
        //reset
        try {
            writer = new CalendarFileWriter("test_file.ics");
        } catch (IllegalArgumentException e) {
            Assertions.fail();
        }

        if (eventOne == null || eventTwo == null) {
            // create calendar event
            ZonedDateTime eventOneDate = ZonedDateTime.of(2023, 03, 21, 0, 0, 0, 0, ZoneId.of("EST", ZoneId.SHORT_IDS));
            ZonedDateTime eventTwoDate = ZonedDateTime.of(2023, 10, 15, 0, 0, 0, 0, ZoneId.of("EST", ZoneId.SHORT_IDS));

            eventOne = new CalendarEvent("Spring Begins", eventOneDate);
            eventTwo = new CalendarEvent("Tests first written", eventTwoDate);
        }
    }

    @Test
    public void addCalendarEvent_increases_list_size_when_new_event_added() {
        //ARRANGE

        //ACT & ASSERT
        Assertions.assertEquals(0, writer.getNumberOfCalendarEvents(), "New CalendarFileWriter should not contain any events");
        writer.addCalendarEvent(eventOne);
        Assertions.assertEquals(1, writer.getNumberOfCalendarEvents(), "Valid event addition should increase number of events in CalendarFileWriter");
        writer.addCalendarEvent(eventTwo);
        Assertions.assertEquals(2, writer.getNumberOfCalendarEvents(), "Valid event addition should increase number of events in CalendarFileWriter");

    }

    @Test
    public void addCalendarEvent_throws_IllegalArgumentException_if_event_is_null() {
        //ARRANGE

        //ACT & ASSERT
        try {
            writer.addCalendarEvent(null);
        } catch (IllegalArgumentException error) {
            Assertions.assertEquals("Calendar event cannot be empty", error.getMessage());
            return;
        }
        Assertions.fail("Null should not be accepted");
    }

    @Test
    public void addCalendarEvent_throws_IllegalArgumentException_if_event_is_already_added() {
        //ARRANGE

        //ACT & ASSERT
        try {
            writer.addCalendarEvent(eventOne);
            writer.addCalendarEvent(eventOne);
        } catch (IllegalArgumentException error) {
            Assertions.assertEquals("Calendar event already added", error.getMessage());
            return;
        }
        Assertions.fail("Calendar event should not be addable twice.");
    }


}
