package com.ahdisease.calendarprinter;

import com.ahdisease.calendarprinter.model.CalendarEvent;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CalendarFileWriter {
    //constants
    private String writeDirectory = ".\\ics_calendar_files";

    //instance variables
    private final FileWriter writer;
    private final List<CalendarEvent> events = new ArrayList<>();

    public CalendarFileWriter(String fileName) throws IllegalArgumentException {
        try {
            writer = new FileWriter(fileName);
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid file name");
        }
    }

    public void addCalendarEvent(CalendarEvent newEvent) throws IllegalArgumentException {
        if (newEvent == null) {
            throw new IllegalArgumentException("Calendar event cannot be empty");
        }

        if (events.size() == 0) {
            events.add(newEvent);
            return;
        }

        events.stream()
            .map(storedEvent->storedEvent.getUuid())
            .forEach(uuid -> {
                if (uuid.equals(newEvent.getUuid())) {
                    throw new IllegalArgumentException("Calendar event already added");
                }
            });

        events.add(newEvent);
    }

    public int getNumberOfCalendarEvents() {
        return events.size();
    }
}
