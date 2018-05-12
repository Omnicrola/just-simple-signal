package com.omnicrola.justsimplesignal.services.signal.cellular;


import android.telephony.CellInfo;

class UnknownCellTypeException extends RuntimeException {
    UnknownCellTypeException(CellInfo cellInfo) {
        super("Unknown CellInfo type: " + cellInfo.getClass().getName());
    }
}
