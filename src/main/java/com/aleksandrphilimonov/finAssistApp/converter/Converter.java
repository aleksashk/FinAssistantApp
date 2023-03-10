package com.aleksandrphilimonov.finAssistApp.converter;

public interface Converter<S, T> {
    T convert(S source);
}
