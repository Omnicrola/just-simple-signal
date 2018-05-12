package com.omnicrola.justsimplesignal.activities.fragments;


import android.app.Activity;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Debug;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.omnicrola.justsimplesignal.R;
import com.omnicrola.justsimplesignal.data.CellSignal;
import com.omnicrola.justsimplesignal.data.MinMax;
import com.omnicrola.justsimplesignal.data.SignalDataBuffer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

class CellSignalViewAdapter {

    private static int[] colors = new int[]{Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA, Color.CYAN, Color.YELLOW};

    private final SignalDataBuffer dataQueue;
    private CellSignalFragment cellSignalFragment;
    private boolean hasNotInitialized;

    CellSignalViewAdapter(CellSignalFragment cellSignalFragment) {
        this.cellSignalFragment = cellSignalFragment;
        dataQueue = new SignalDataBuffer();
        hasNotInitialized = true;
    }

    void update(List<CellSignal> signals) {
        dataQueue.insert(signals);
        updateDisplay();
    }

    private void updateDisplay() {
        Activity activity = cellSignalFragment.getActivity();

        LineChart lineChart = activity.findViewById(R.id.cell_output);
        if (lineChart != null) {
            initChart(lineChart);

            updateData(lineChart);

            lineChart.invalidate();
            lineChart.requestLayout();
        }
    }

    private void updateLabels(int channel) {
        Activity activity = cellSignalFragment.getActivity();
        TextView currentSignal = activity.findViewById(R.id.signal_current);
        TextView minSignal = activity.findViewById(R.id.signal_min);
        TextView maxSignal = activity.findViewById(R.id.signal_max);

        int strengthInDb = dataQueue.getCurrent(channel).getStrengthInDb();
        MinMax minMax = dataQueue.getMinMax(channel);

        currentSignal.setText(String.format(Locale.US, "%s dbm", strengthInDb));
        minSignal.setText(String.format(Locale.US, "%s dbm", minMax.getMin()));
        maxSignal.setText(String.format(Locale.US, "%s dbm", minMax.getMax()));
    }

    private void initChart(LineChart lineChart) {
        if (hasNotInitialized) {
            YAxis axisRight = lineChart.getAxisRight();
            axisRight.setEnabled(false);

            YAxis leftAxis = lineChart.getAxisLeft();
            leftAxis.setValueFormatter(new DbmAxisValueFormatter());
            leftAxis.setAxisMinimum(-120);
            leftAxis.setAxisMaximum(-90);

            lineChart.getXAxis().setDrawLabels(false);

            hasNotInitialized = false;
        }
    }

    private void updateData(LineChart lineChart) {
        Set<Integer> channels = dataQueue.getChannels();
        for (int channel : channels) {
            List<CellSignal> signals = dataQueue.getDataFromCannel(channel);
            String label = String.valueOf(channel);

            List<Entry> entrySet = mapDataEntries(signals);
            setData(lineChart, entrySet, label, colors[0]);
            updateLabels(channel);
        }
    }

    private List<Entry> mapDataEntries(List<CellSignal> signals) {
        ArrayList<Entry> entries = new ArrayList<>();
        int index = 0;
        for (CellSignal signal : signals) {
            entries.add(new Entry(index++, signal.getStrengthInDb()));
        }
        return entries;
    }

    private void setData(LineChart lineChart, List<Entry> values, String label, int color) {

        if (dataSetExists(lineChart, label)) {
            LineDataSet dataSet = getDataSet(lineChart, label);
            dataSet.setValues(values);
            lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();
        } else {
            LineDataSet dataSet = new LineDataSet(values, label);

            dataSet.setDrawIcons(false);

            dataSet.setColor(color);
            dataSet.setCircleColor(color);
            dataSet.setLineWidth(1f);
            dataSet.setCircleRadius(3f);
            dataSet.setDrawCircleHole(false);
            dataSet.setValueTextSize(9f);
            dataSet.setDrawFilled(true);
            dataSet.setFormLineWidth(1f);
            dataSet.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            dataSet.setFormSize(15.f);

            dataSet.setDrawValues(false);

            dataSet.setFillColor(color);

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(dataSet); // add the datasets

            // create a data object with the datasets
            LineData data = new LineData(dataSets);

            // set data
            lineChart.setData(data);
        }
    }

    private LineDataSet getDataSet(LineChart lineChart, String label) {
        return (LineDataSet) lineChart.getData().getDataSetByLabel(label, true);
    }

    private boolean dataSetExists(LineChart lineChart, String label) {
        LineData data = lineChart.getData();
        if (data == null) {
            return false;
        }
        LineDataSet dataSet = (LineDataSet) data.getDataSetByLabel(label, true);
        if (dataSet == null) {
            return false;
        }
        return true;
    }
}
