package com.ahdisease.calendarprinter.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalendarEventTests {
    private final ZonedDateTime FIRST_DAY_OF_SPRING_DATE = ZonedDateTime.of(2023 , 03, 21, 0, 0, 0,0, ZoneId.of("EST",ZoneId.SHORT_IDS));
    private final ZonedDateTime OCTOBER_22_3PM = ZonedDateTime.of(2023,10,22,15,0,0,0,ZoneId.of("EST",ZoneId.SHORT_IDS));
    private final ZonedDateTime FIRST_WEEKLY_MEETING_TIME = ZonedDateTime.of(2023,10,23,9,0,0,0,ZoneId.of("EST",ZoneId.SHORT_IDS));
    private CalendarEvent event1;
    private CalendarEvent event2;
    private CalendarEvent event3;

    @BeforeEach
    public void resetEvents() {
        event1 = new CalendarEvent("Spring Begins",FIRST_DAY_OF_SPRING_DATE,FIRST_DAY_OF_SPRING_DATE.plusDays(1),false,true,null,new String[]{"HOLIDAY","SEASON"},"N/A");
        event2 = new CalendarEvent("Spanish Club Meeting",OCTOBER_22_3PM,OCTOBER_22_3PM.plusHours(2),false,false, "es",new String[]{"EDUCATION", "BEGINNER"},"Classroom 102");
        event3 = new CalendarEvent("Rencontre hebdomadaire", FIRST_WEEKLY_MEETING_TIME, FIRST_WEEKLY_MEETING_TIME.plusMinutes(30),true,false,"fr",null, "Salle de réunion au 1er étage");

    }

    @Test
    public void constructor_generates_valid_UID() {
        //ARRANGE
        // set up regex pattern
        String UID_REGEX = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}";
        Pattern uidPattern = Pattern.compile(UID_REGEX);

        //ACT
        // create calendar event
        CalendarEvent firstDayOfSpring = event1;

        //ASSERT
        // validate uid
        Matcher uidMatcher = uidPattern.matcher(firstDayOfSpring.getUuid().toString());
        Assertions.assertTrue(uidMatcher.find(), "Expected contructor to generate valid UID");
    }
    @Test
    public void toString_returns_expected_value() {
        //ARRANGE
        // create expected calendar event string
        String[] expectedToStringLinesEvent1 = new String[] {
                "BEGIN:VEVENT",
                "SUMMARY:Spring Begins",
                "UID:",
                "SEQUENCE:0",
                "STATUS:CONFIRMED",
                "TRANSP:TRANSPARENT",
                "DTSTART:20230321T050000Z",
                "DTEND:20230322T050000Z",
                "DTSTAMP",
                "CATEGORIES:HOLIDAY,SEASON",
                "LOCATION:N/A",
                "END:VEVENT"
        };

        // create regex Pattern for UID validation
        Pattern uidToStringPattern = Pattern.compile("UID:[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}");


        // create calendar event
        CalendarEvent firstDayOfSpring = event1;

        //ACT
        String iCalendarString = firstDayOfSpring.toString();
        String[] iCalendarLines = iCalendarString.split("\n");

        //ASSERT
        Assertions.assertEquals(expectedToStringLinesEvent1.length, iCalendarLines.length, "New CalendarEvent object should have expected number of string lines: " + expectedToStringLinesEvent1.length);

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

            Assertions.assertEquals(expectedToStringLinesEvent1[i],iCalendarLines[i],"Expected line of String to match: " + i);
        }
    }

    @Test
    public void confirmEvent_changes_status_to_confirmed() {
        //ARRANGE
        // create calendar event
        CalendarEvent tentativeEvent = new CalendarEvent("Spring Begins",FIRST_DAY_OF_SPRING_DATE,FIRST_DAY_OF_SPRING_DATE.plusDays(1),true, true, null, null,null);
        String[] eventString = tentativeEvent.toString().split("\n");

        // find index of STATUS property
        int statusIndex = getPropertyLineIndexFromToString(eventString,"STATUS");
        if (statusIndex < 0) {
            Assertions.fail("Unable to identify STATUS in CalendarEvent String");
        }

        //ACT
        String statusLineAfterCreation = eventString[statusIndex];

        tentativeEvent.confirmEvent();
        eventString = tentativeEvent.toString().split("\n");
        String statusLineAfterConfirmation = eventString[statusIndex];

        //ASSERT
        Assertions.assertEquals("STATUS:TENTATIVE",statusLineAfterCreation, "Expected CalendarEvent to be generated as TENTATIVE");
        Assertions.assertEquals("STATUS:CONFIRMED",statusLineAfterConfirmation, "Expected status to change after confirmation");
    }
    @Test
    public void cancelEvent_changes_status_to_cancelled() {
        //ARRANGE
        // create calendar event
        CalendarEvent tentativeEvent = new CalendarEvent("Spring Begins",FIRST_DAY_OF_SPRING_DATE,FIRST_DAY_OF_SPRING_DATE.plusDays(1),true,true, null, null,null);
        String[] eventString = tentativeEvent.toString().split("\n");

        // find index of STATUS property
        int statusIndex = getPropertyLineIndexFromToString(eventString,"STATUS");
        if (statusIndex < 0) {
            Assertions.fail("Unable to identify STATUS in CalendarEvent String");
        }

        //ACT
        String statusLineAfterCreation = eventString[statusIndex];

        tentativeEvent.cancelEvent();
        eventString = tentativeEvent.toString().split("\n");
        String statusLineAfterCancellation = eventString[statusIndex];

        //ASSERT
        Assertions.assertEquals("STATUS:TENTATIVE",statusLineAfterCreation, "Expected CalendarEvent to be generated as TENTATIVE");
        Assertions.assertEquals("STATUS:CANCELLED",statusLineAfterCancellation, "Expected status to change after cancellation");
    }
    
    @Test
    public void allCategoriesToString_returns_empty_string_if_language_and_category_array_are_invalid() {
        //ARRANGE
        CalendarEvent nullCategoriesEvent = new CalendarEvent("Spring Begins",FIRST_DAY_OF_SPRING_DATE,FIRST_DAY_OF_SPRING_DATE.plusDays(1),false,true,null,null,null);
        CalendarEvent blankLanguageEvent = new CalendarEvent("Spring Begins",FIRST_DAY_OF_SPRING_DATE,FIRST_DAY_OF_SPRING_DATE.plusDays(1),false,true,"",null,null);
        CalendarEvent emptyArrayEvent = new CalendarEvent("Spring Begins",FIRST_DAY_OF_SPRING_DATE,FIRST_DAY_OF_SPRING_DATE.plusDays(1),false,true,null,new String[]{},null);
        
        //ACT
        int nullCategoriesPropertyIndex = getPropertyLineIndexFromToString(nullCategoriesEvent,"CATEGORIES");
        int blankLanguagePropertyIndex = getPropertyLineIndexFromToString(blankLanguageEvent,"CATEGORIES");
        int emptyArrayPropertyIndex = getPropertyLineIndexFromToString(emptyArrayEvent,"CATEGORIES");

        //ASSERT
        Assertions.assertEquals(-1, nullCategoriesPropertyIndex, "No CATEGORIES property expected when languageCategory and categories are both null");
        Assertions.assertEquals(-1, blankLanguagePropertyIndex, "No CATEGORIES property expected when languageCategory is blank and categories is null");
        Assertions.assertEquals(-1, emptyArrayPropertyIndex, "No CATEGORIES property expected when languageCategory null and categories is empty");

    }

    @Test
    public void allCategoriesToString_returns_expected_results() {
        //ARRANGE
        String[] springDayStrings = event1.toString().split("\n");
        String[] spanishClubStrings = event2.toString().split("\n");
        String[] weeklyMeetingStrings = event3.toString().split("\n");

        //ACT
        String springDayCATEGORIES = springDayStrings[getPropertyLineIndexFromToString(springDayStrings,"CATEGORIES")];
        String spanishClubCATEGORIES = spanishClubStrings[getPropertyLineIndexFromToString(spanishClubStrings,"CATEGORIES")];
        String weeklyMeetingCATEGORIES = weeklyMeetingStrings[getPropertyLineIndexFromToString(weeklyMeetingStrings,"CATEGORIES")];

        //ASSERT
        Assertions.assertEquals("CATEGORIES:HOLIDAY,SEASON",springDayCATEGORIES,"Categories without language should return comma delimited list of categories array");
        Assertions.assertEquals("CATEGORIES:EDUCATION,BEGINNER,es",spanishClubCATEGORIES,"All categories should be listed, with languageCategory listed last");
        Assertions.assertEquals("CATEGORIES:fr", weeklyMeetingCATEGORIES, "Language code expected in CATEGORIES string");
    }

    @Test
    public void constructor_throws_exception_when_startDate_is_null() {
        //ARRANGE

        //ACT & ASSERT
        try {
            CalendarEvent nullEvent = new CalendarEvent(null,null,null,false,false,null,null,null);
        } catch (IllegalArgumentException e) {
            return;
        }
        Assertions.fail("startDate cannot be null");
        //ASSERT
    }

    //helper methods
    private int getPropertyLineIndexFromToString(CalendarEvent event, String propertyName) {
        String[] eventStrings = event.toString().split("\n");

        for (int i = 0; i < eventStrings.length; i++) {
            if (eventStrings[i].startsWith(propertyName)) {
                return i;
            }
        }
        return -1;
    }

    private int getPropertyLineIndexFromToString(String[] eventStrings, String propertyName) {
        for (int i = 0; i < eventStrings.length; i++) {
            if (eventStrings[i].startsWith(propertyName)) {
                return i;
            }
        }
        return -1;
    }


}
