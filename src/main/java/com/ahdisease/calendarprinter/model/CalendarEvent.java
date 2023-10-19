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
    //enums
    private enum Status {TENTATIVE, CONFIRMED, CANCELLED};
    //constants
    private final String PATTERN_FORMAT = "yyyyMMdd'T'HHmmssz";


    //instance variables
    // SUMMARY property acts as a title to the event
    private String summary;
    // UID property is a unique identifier with the UUID format (128 bit value)
    private UUID uuid;
    // SEQUENCE property indicates the number of revisions to the event
    private int sequence = 0;
    // DTSTART property indicates the UTC time at which the event begins
    private ZonedDateTime startDate;
    // DTSTAMP property indicates the UTC time at which the event was created
    private ZonedDateTime createdDate;
    // STATUS property indicates whether the event is TENTATIVE, CONFIRMED, or CANCELLED
    private Status status;
    // TRANSP property can be set to TRANSPARENT or OPAQUE
    //      TRANSPARENT - does not consume time on a calendar (e.g. a Holiday or an event from another calendar shared for reference only)
    //      OPAQUE - consumes time on a calendar, allowing the event to be detected by free-busy time searches (e.g. a confirmed meeting)
    private boolean transparent;

    //TODO instead of making lots of complex constructors, I should build one all-access constructor and a factory class
    // so it's clear what kind of event is being created (e.g. Holiday, Optional Meeting, et cetra) but there's only one
    // constructor to test
    public CalendarEvent(String summary, ZonedDateTime startDate) {
        uuid = UUID.randomUUID();
        this.summary = summary;
        this.startDate = startDate;
        this.createdDate = ZonedDateTime.now();
        this.status = Status.CONFIRMED;
    }

    public CalendarEvent(String summary, ZonedDateTime startDate, boolean tentativeEvent, boolean transparent) {
        uuid = UUID.randomUUID();
        this.summary = summary;
        this.startDate = startDate;
        this.createdDate = ZonedDateTime.now();
        if (tentativeEvent) {
            this.status = Status.TENTATIVE;
        } else {
            this.status = Status.CONFIRMED;
        }
        this.transparent = transparent;
    }

    private String DateToUTCString(ZonedDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_FORMAT)
                .withZone(ZoneId.of("Z"));
        return formatter.format(date);
    }

    public void confirmEvent() {
        status = Status.CONFIRMED;
        transparent = false;
    }

    public void cancelEvent() {
        status = Status.CANCELLED;
        transparent = true;
    }

    // overrides
    @Override
    public String toString() {
        //TODO should this be built from a template file?
        // If so, this functionality should be moved to CalendarFileWriter
        StringBuilder eventText= new StringBuilder("BEGIN:VEVENT");

        eventText.append("\nSUMMARY:" + summary );
        eventText.append("\nUID:" + uuid);
        eventText.append("\nSEQUENCE:" + sequence);
        eventText.append("\nSTATUS:" + status.name());
        eventText.append("\nTRANSP:" + (transparent ? "TRANSPARENT" : "OPAQUE"));
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

    public int getSequence() {
        return sequence;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }
}
