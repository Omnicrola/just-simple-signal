package com.omnicrola.justsimplesignal.data;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CellSignal {

    private long timestamp;
    private int strengthInDb;
    private int signalId;

}
