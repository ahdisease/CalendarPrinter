package com.ahdisease.calendarprinter.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalendarEventTests {


    @Test
    public void constructor_generates_valid_UID() {
        //ARRANGE
        // set up regex pattern
        String UID_REGEX = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}";
        Pattern uidPattern = Pattern.compile(UID_REGEX);

        //ACT
        // create calendar event
        ZonedDateTime dateTime = ZonedDateTime.of(2023 , 03, 21, 0, 0, 0,0, ZoneId.of("EST",ZoneId.SHORT_IDS));
        CalendarEvent firstDayOfSpring = new CalendarEvent("Spring Begins", dateTime);

        //ASSERT
        // validate uid
        Matcher uidMatcher = uidPattern.matcher(firstDayOfSpring.getUuid().toString());
        Assertions.assertTrue(uidMatcher.find(), "Expected contructor to generate valid UID");
    }
    @Test
    public void toString_returns_expected_value() {
        //ARRANGE
        // create expected calendar event string
        String[] expectedICalendarLines = new String[] {
                "BEGIN:VEVENT",
                "SUMMARY:Spring Begins",
                "UID:",
                "DTSTART:20230321T050000Z",
                "DTSTAMP",
                "END:VEVENT"
        };

        // create regex Pattern for UID validation
        Pattern uidToStringPattern = Pattern.compile("UID:[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}");


        // create calendar event
        ZonedDateTime dateTime = ZonedDateTime.of(2023 , 03, 21, 0, 0, 0,0, ZoneId.of("EST",ZoneId.SHORT_IDS));
        CalendarEvent firstDayOfSpring = new CalendarEvent("Spring Begins", dateTime);

        //ACT
        String iCalendarString = firstDayOfSpring.toString();
        String[] iCalendarLines = iCalendarString.split("\n");

        //ASSERT
        Assertions.assertEquals(expectedICalendarLines.length, iCalendarLines.length, "New CalendarEvent object should have expected number of string lines: " + expectedICalendarLines.length);

        for (int i = 0; i < iCalendarLines.length; i++) {
            if(iCalendarLines[i].startsWith("UID")) {
                Matcher uidToStringMatcher = uidToStringPattern.matcher(iCalendarLines[i]);
                Assertions.assertTrue(uidToStringMatcher.find(),"Expected UID line to match format");
                continue;
            }

            if(iCalendarLines[i].startsWith("DTSTAMP")) {
                // DTSTAMP is necessary to be considered a valid VEVENT but is for information only.
                continue;
            }

            Assertions.assertEquals(expectedICalendarLines[i],iCalendarLines[i],"Expected line of String to match: " + i);
        }
    }

}
