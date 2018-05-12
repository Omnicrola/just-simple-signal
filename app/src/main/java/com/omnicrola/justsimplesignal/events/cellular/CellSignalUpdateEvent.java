package com.omnicrola.justsimplesignal.events.cellular;

import com.omnicrola.justsimplesignal.data.CellSignal;

import java.util.List;

import lombok.Getter;

@Getter
public class CellSignalUpdateEvent {
    private List<CellSignal> signals;

    public CellSignalUpdateEvent(List<CellSignal> signals) {
        this.signals = signals;
    }
}
