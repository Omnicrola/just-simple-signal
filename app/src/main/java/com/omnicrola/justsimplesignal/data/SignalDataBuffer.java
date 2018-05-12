package com.omnicrola.justsimplesignal.data;


import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SignalDataBuffer {

    private static final int MAX_BUFFER = 20;

    private Map<Integer, ArrayDeque<CellSignal>> signals = new HashMap<>();
    private Map<Integer, MinMax> minMax = new HashMap<>();

    public void insert(List<CellSignal> signals) {
        for (CellSignal signal : signals) {
            insert(signal);
        }
    }

    public void insert(CellSignal signal) {
        updateBuffer(signal);
        updateMinMax(signal);
    }

    private void updateBuffer(CellSignal signal) {
        int signalId = signal.getSignalId();
        if (!signals.containsKey(signalId)) {
            signals.put(signalId, new ArrayDeque<CellSignal>());
        }
        ArrayDeque<CellSignal> signalQueue = signals.get(signalId);
        signalQueue.addLast(signal);
        if (signalQueue.size() > MAX_BUFFER) {
            signalQueue.removeFirst();
        }
    }

    private void updateMinMax(CellSignal signal) {
        int signalId = signal.getSignalId();
        if (!minMax.containsKey(signalId)) {
            minMax.put(signalId, new MinMax());
        }
        minMax.get(signalId).update(signal.getStrengthInDb());
    }

    public Set<Integer> getChannels() {
        return signals.keySet();
    }

    public List<CellSignal> getDataFromCannel(int channel) {
        return new ArrayList<>(signals.get(channel));
    }

    public CellSignal getCurrent(int channel) {
        return signals.get(channel).peekLast();
    }

    public MinMax getMinMax(int channel) {
        return minMax.get(channel);
    }
}
