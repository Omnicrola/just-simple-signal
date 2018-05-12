package com.omnicrola.justsimplesignal.services.signal.cellular;

import android.util.Log;

import com.omnicrola.justsimplesignal.data.CellSignal;
import com.omnicrola.justsimplesignal.events.cellular.CellSignalUpdateEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.TimerTask;


class SampleCellSignalTask extends TimerTask {
    private CellSignalSampler cellSignalSampler;

    SampleCellSignalTask(CellSignalSampler cellSignalSampler) {
        this.cellSignalSampler = cellSignalSampler;
    }

    @Override
    public void run() {
        Log.d(getClass().getSimpleName(), "Running task...");
        List<CellSignal> signals = this.cellSignalSampler.sample();
        EventBus.getDefault().post(new CellSignalUpdateEvent(signals));
    }
}
