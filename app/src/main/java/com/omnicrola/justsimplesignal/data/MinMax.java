package com.omnicrola.justsimplesignal.data;

import lombok.Getter;

@Getter
public class MinMax {

    private int min;
    private int max;

    public MinMax() {
        this.min = Integer.MAX_VALUE;
        this.max = Integer.MIN_VALUE;
    }

    public void update(int newValue) {
        min = Math.min(min, newValue);
        max = Math.max(max, newValue);
    }

}
