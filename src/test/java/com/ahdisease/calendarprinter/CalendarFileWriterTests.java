package com.ahdisease.calendarprinter;

import com.ahdisease.calendarprinter.model.CalendarEvent;
import org.junit.jupiter.api.*;

import java.io.File;
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
            Assertions.fail("Null should not be accepted");
        } catch (IllegalArgumentException error) {
            Assertions.assertEquals("Calendar event cannot be empty", error.getMessage());
        }
    }

    @Test
    public void addCalendarEvent_throws_IllegalArgumentException_if_event_is_already_added() {
        //ARRANGE

        //ACT & ASSERT
        try {
            writer.addCalendarEvent(eventOne);
            writer.addCalendarEvent(eventOne);
            Assertions.fail("Calendar event should not be addable twice.");
        } catch (IllegalArgumentException error) {
            Assertions.assertEquals("Calendar event already added", error.getMessage());
        }
    }

    //TODO implement toString testing

    @Test
    public void writeEventsToFile_throws_IllegalStateException_when_it_does_not_contain_events() {
        //ARRANGE

        //ACT & ASSERT
        try {
            writer.writeEventsToFile();
            Assertions.fail("CalendarFileWriter should not write files when no CalendarEvents have been added");
        } catch (IllegalStateException error) {
            Assertions.assertEquals("No calendar events have been added to writer", error.getMessage());
        }
    }

    @Test
    public void writeEventsToFile_changes_file() {
        //ARRANGE
        File testFile = new File("ics_calendar_files\\test_file.ics");
        long lastModifiedTime = testFile.lastModified();
        writer.addCalendarEvent(eventOne);
        //ACT & ASSERT
        try {
            writer.writeEventsToFile();
            long testLastModifiedTime = testFile.lastModified();
            Assertions.assertNotEquals(lastModifiedTime,testLastModifiedTime,"File '.\\ics_calendar_files\\test_file.ics' should be modified when given valid input");
        } catch (Exception error) {
            Assertions.fail("CalendarFileWriter should write to file.");
        }
    }

    @AfterEach
    public void delete_test_file_ics() {
        File testFile = new File("ics_calendar_files\\test_file.ics");
        if (testFile.exists()) {
            testFile.delete();

        }
    }


}
