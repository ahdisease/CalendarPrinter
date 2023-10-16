package com.ahdisease.calendarprinter.model;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class CalendarEvent {
    /*
    SUMMARY:Abraham Lincoln
    UID:c7614cff-3549-4a00-9152-d25cc1fe077d
    SEQUENCE:0
    STATUS:CONFIRMED
    TRANSP:TRANSPARENT
    RRULE:FREQ=YEARLY;INTERVAL=1;BYMONTH=2;BYMONTHDAY=12
    DTSTART:20080212
    DTEND:20080213
    DTSTAMP:20150421T141403
    CATEGORIES:U.S. Presidents,Civil War People
    LOCATION:Hodgenville\, Kentucky
    GEO:37.5739497;-85.7399606
    DESCRIPTION:Born February 12\, 1809\nSixteenth President (1861-1865)\n\n\n
     \nhttp://AmericanHistoryCalendar.com
    URL:http://americanhistorycalendar.com/peoplecalendar/1,328-abraham-lincol
 n
     */
    //constants
    private final String PATTERN_FORMAT = "yyyyMMdd'T'HHmmssz";


    //instance variables
    private String summary;
    private UUID uuid;
    private ZonedDateTime startDate;
    private ZonedDateTime createdDate;

    public CalendarEvent(String summary, ZonedDateTime startDate) {
        uuid = UUID.randomUUID();
        this.summary = summary;
        this.startDate = startDate;
        this.createdDate = ZonedDateTime.now();
    }

    private String DateToUTCString(ZonedDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_FORMAT)
                .withZone(ZoneId.of("Z"));
        return formatter.format(date);
    }

    // overrides
    @Override
    public String toString() {
        StringBuilder eventText= new StringBuilder("BEGIN:VEVENT");
        eventText.append("\nSUMMARY:" + summary );
        eventText.append("\nUID:" + uuid);
        eventText.append("\nDTSTART:" + DateToUTCString(startDate));
        eventText.append("\nDTSTAMP:" + DateToUTCString(createdDate));

        eventText.append("\nEND:VEVENT");
        return eventText.toString();
    }

    //getters

    public String getSummary() {
        return summary;
    }

    public UUID getUuid() {
        return uuid;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }
}
