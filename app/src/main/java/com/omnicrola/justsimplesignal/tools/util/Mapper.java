package com.omnicrola.justsimplesignal.tools.util;


import java.util.List;

public interface Mapper<T, K> {

    K map(T in);

    List<K> map(List<T> in);
}
