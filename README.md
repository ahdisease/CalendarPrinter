# Calendar Printer
This repo is an attempt to create a simple CLI calendar event generator using the ICalendar standard. An example has been copied from the ICalendar [website](https://icalendar.org/):

```
BEGIN:VCALENDAR
VERSION:2.0
PRODID:-//ZContent.net//Zap Calendar 1.0//EN
CALSCALE:GREGORIAN
METHOD:PUBLISH
BEGIN:VEVENT
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
END:VEVENT
END:VCALENDAR
```

This example represents all accepted base properties of an .ics file using the ICalendar standard. 

## Current To Do:
- [X] Create Calendar Event model
- [ ] Add all properties to Calendar Event
    - [X] SUMMARY - Title of the Event
    - [X] UID - Unique identifier with UUID format
    - [ ] SEQUENCE
    - [ ] STATUS
    - [ ] TRANSP
    - [ ] RRULE
    - [X] DTSTART - Timestamp indicating when an event begins
    - [ ] DTEND
    - [X] DTSTAMP - Timestamp indicating when an event was created
    - [ ] CATEGORIES
    - [ ] LOCATION
    - [ ] GEO
    - [ ] DESCRIPTION
    - [ ] URL
- [X] Create File Writing Class
- [ ] Add customization of all properties to Calendar File writer
    - [ ] VERSION
    - [ ] PRODID
    - [ ] CALSCALE
    - [ ] METHOD
- [ ] Create UI to Generate Calendar Events