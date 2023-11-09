package com.ahdisease.calendarprinter.model;

public record GeoCoordinate(double latitude, double longitude) {

    @Override
    public String toString() {
        return String.format("%.6f;%.6f", latitude, longitude);
    }
}
