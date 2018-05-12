package com.omnicrola.justsimplesignal.activities.fragments;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.Locale;


class DbmAxisValueFormatter implements IAxisValueFormatter {
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
         return String.format(Locale.US, "%.0f dBm", value);
    }
}
