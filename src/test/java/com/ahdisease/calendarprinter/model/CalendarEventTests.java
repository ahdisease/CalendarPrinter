package com.ahdisease.calendarprinter.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalendarEventTests {
    private final ZonedDateTime FIRST_DAY_OF_SPRING_DATE = ZonedDateTime.of(2023 , 03, 21, 0, 0, 0,0, ZoneId.of("EST",ZoneId.SHORT_IDS));
    private final ZonedDateTime OCTOBER_22_3PM = ZonedDateTime.of(2023,10,22,15,0,0,0,ZoneId.of("EST",ZoneId.SHORT_IDS));
    private CalendarEvent event1;
    private CalendarEvent event2;

    @BeforeEach
    public void resetEvents() {
        event1 = new CalendarEvent("Spring Begins",FIRST_DAY_OF_SPRING_DATE,FIRST_DAY_OF_SPRING_DATE.plusDays(1),false,true,null,new String[]{"HOLIDAY","SEASON"});
        event2 = new CalendarEvent("Spanish Club Meeting",OCTOBER_22_3PM,OCTOBER_22_3PM.plusHours(2),false,false, "es",new String[]{"EDUCATION", "BEGINNER"});
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
        String[] expectedICalendarLines1 = new String[] {
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
        Assertions.assertEquals(expectedICalendarLines1.length, iCalendarLines.length, "New CalendarEvent object should have expected number of string lines: " + expectedICalendarLines1.length);

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

            Assertions.assertEquals(expectedICalendarLines1[i],iCalendarLines[i],"Expected line of String to match: " + i);
        }
    }

    @Test
    public void confirmEvent_changes_status_to_confirmed() {
        //ARRANGE
        // create calendar event
        CalendarEvent tentativeEvent = new CalendarEvent("Spring Begins",FIRST_DAY_OF_SPRING_DATE,FIRST_DAY_OF_SPRING_DATE.plusDays(1),true, true, null, null);

        String[] eventStringLines = tentativeEvent.toString().split("\n");
        String statusLineAfterCreation = null;
        int statusIndex = -1;
        for (int i = 0; i < eventStringLines.length; i++) {
            if (eventStringLines[i].startsWith("STATUS:")) {
                statusLineAfterCreation = eventStringLines[i];
                statusIndex=i;
            }
        }
        if (statusLineAfterCreation == null) {
            Assertions.fail("Unable to identify STATUS in CalendarEvent String");
        }
        //ACT
        tentativeEvent.confirmEvent();
        eventStringLines = tentativeEvent.toString().split("\n");
        String statusLineAfterConfirmation = eventStringLines[statusIndex];

        //ASSERT
        Assertions.assertEquals("STATUS:TENTATIVE",statusLineAfterCreation, "Expected CalendarEvent to be generated as TENTATIVE");
        Assertions.assertEquals("STATUS:CONFIRMED",statusLineAfterConfirmation, "Expected status to change after confirmation");
    }
    @Test
    public void cancelEvent_changes_status_to_cancelled() {
        //ARRANGE
        // create calendar event
        CalendarEvent tentativeEvent = new CalendarEvent("Spring Begins",FIRST_DAY_OF_SPRING_DATE,FIRST_DAY_OF_SPRING_DATE.plusDays(1),true,true, null, null);

        String[] eventStringLines = tentativeEvent.toString().split("\n");
        String statusLineAfterCreation = null;
        int statusIndex = -1;
        for (int i = 0; i < eventStringLines.length; i++) {
            if (eventStringLines[i].startsWith("STATUS:")) {
                statusLineAfterCreation = eventStringLines[i];
                statusIndex=i;
            }
        }
        if (statusLineAfterCreation == null) {
            Assertions.fail("Unable to identify STATUS in CalendarEvent String");
        }
        //ACT
        tentativeEvent.cancelEvent();
        eventStringLines = tentativeEvent.toString().split("\n");
        String statusLineAfterCancellation = eventStringLines[statusIndex];

        //ASSERT
        Assertions.assertEquals("STATUS:TENTATIVE",statusLineAfterCreation, "Expected CalendarEvent to be generated as TENTATIVE");
        Assertions.assertEquals("STATUS:CANCELLED",statusLineAfterCancellation, "Expected status to change after cancellation");
    }
    
    @Test
    public void allCategoriesToString_returns_empty_string_if_language_and_category_array_are_invalid() {
        //ARRANGE
        CalendarEvent firstDayOfSpringNullCategories = new CalendarEvent("Spring Begins",FIRST_DAY_OF_SPRING_DATE,FIRST_DAY_OF_SPRING_DATE.plusDays(1),false,true,null,null);
        CalendarEvent firstDayOfSpringBlankLanguage = new CalendarEvent("Spring Begins",FIRST_DAY_OF_SPRING_DATE,FIRST_DAY_OF_SPRING_DATE.plusDays(1),false,true,"",null);
        CalendarEvent firstDayOfSpringEmptyArray = new CalendarEvent("Spring Begins",FIRST_DAY_OF_SPRING_DATE,FIRST_DAY_OF_SPRING_DATE.plusDays(1),false,true,null,new String[]{});
        
        //ACT
        String nullCategoriesPropertyString = getPropertyStringFromToString(firstDayOfSpringNullCategories,"CATEGORIES");
        String blankLanguagePropertyString = getPropertyStringFromToString(firstDayOfSpringBlankLanguage,"CATEGORIES");
        String emptyArrayPropertyString = getPropertyStringFromToString(firstDayOfSpringEmptyArray,"CATEGORIES");

        //ASSERT
        Assertions.assertEquals("", nullCategoriesPropertyString, "No CATEGORIES property expected when languageCategory and categories are both null");
        Assertions.assertEquals("", blankLanguagePropertyString, "No CATEGORIES property expected when languageCategory is blank and categories is null");
        Assertions.assertEquals("", nullCategoriesPropertyString, "No CATEGORIES property expected when languageCategory null and categories is empty");

    }

    private String getPropertyStringFromToString(CalendarEvent event, String propertyName) {
        String[] eventStrings = event.toString().split("\n");
        try {
            return Arrays.stream(eventStrings).filter(line -> { return line.startsWith(propertyName);}).findAny().get();
        } catch (NoSuchElementException e) {
            return "";
        } catch (Exception e) {
            Assertions.fail();
            return "Unexpected exception: " + e.getMessage();
        }
    }

}
