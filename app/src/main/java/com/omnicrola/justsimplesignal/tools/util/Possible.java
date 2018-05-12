package com.omnicrola.justsimplesignal.tools.util;

public final class Possible<T> {
    private T value;

    private Possible(T value) {
        this.value = value;
    }

    public static <T> Possible<T> empty() {
        return new Possible<>(null);
    }

    public static <T> Possible<T> of(T value) {
        return new Possible<>(value);
    }

    public T get() {
        return this.value;
    }

    public boolean isPresent() {
        return this.value != null;
    }
}
