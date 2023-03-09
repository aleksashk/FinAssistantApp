package com.aleksandrphilimonov.practice.converter;

public interface Converter <S, T>{
    T convert(S source);
}
