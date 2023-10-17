package com.ahdisease.calendarprinter;

import com.ahdisease.calendarprinter.model.CalendarEvent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.List;

public class CalendarFileWriter {
    //constants
    private final String DEFAULT_WRITE_DIRECTORY = "ics_calendar_files\\";

    //instance variables
    private final File workingFile;
    private final FileWriter writer;
    private final List<CalendarEvent> events = new ArrayList<>();

    public CalendarFileWriter(String fileName) throws IllegalArgumentException {

        File directory = new File(DEFAULT_WRITE_DIRECTORY);
        //confirm directory exists
        if (!directory.isDirectory()) {
            directory.mkdir();
        }

        //create file object with file and directory
        workingFile = new File(directory, fileName);

        //try to generate FileWriter
        try {
            writer = new FileWriter(workingFile);
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid file name");
        }
    }


    public CalendarFileWriter(String fileName, String writeDirectory) throws IllegalArgumentException {
        File directory = new File(writeDirectory);
        if (!directory.isDirectory() && !directory.isFile()) {
            directory.mkdirs();
        }

        //create file object with file and directory
        File file = new File(directory + fileName);
        if (!file.canWrite()) {
            throw new InvalidPathException(file.getPath(), "No write access");
        }
        workingFile = new File(directory, fileName);

        try {
            writer = new FileWriter(file);
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid file name");
        }
    }

    public void addCalendarEvent(CalendarEvent newEvent) throws IllegalArgumentException {
        if (newEvent == null) {
            throw new IllegalArgumentException("Calendar event cannot be empty");
        }

        if (getNumberOfCalendarEvents() == 0) {
            events.add(newEvent);
            return;
        }

        events.stream()
                .map(storedEvent -> storedEvent.getUuid())
                .forEach(uuid -> {
                    if (uuid.equals(newEvent.getUuid())) {
                        throw new IllegalArgumentException("Calendar event already added");
                    }
                });

        events.add(newEvent);
    }

    public File writeEventsToFile() throws IOException {
        if (getNumberOfCalendarEvents() == 0) {
            throw new IllegalStateException("No calendar events have been added to writer");
        }

        String iCalendarString = toString();

        writer.write(iCalendarString);
        writer.flush();
        writer.close();


        return workingFile;
    }

    // Overrides


    @Override
    public String toString() {
        //TODO research how to include other calendar apps and generate this section based on user selection
        StringBuilder iCalendarFormat = new StringBuilder();
        iCalendarFormat.append("BEGIN:VCALENDAR\n");
        iCalendarFormat.append("VERSION:2.0\n");
        iCalendarFormat.append("PRODID:-//ZContent.net//Zap Calendar 1.0//EN\n");
        iCalendarFormat.append("CALSCALE:GREGORIAN\n");
        iCalendarFormat.append("METHOD:PUBLISH\n");

        for (CalendarEvent event : events) {
            iCalendarFormat.append(event);
        }

        iCalendarFormat.append("\nEND:VCALENDAR");
        return iCalendarFormat.toString();
    }

    public int getNumberOfCalendarEvents() {
        return events.size();
    }
}
