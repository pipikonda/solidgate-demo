package com.solidgatedemo.utils;

public record Result<T>(T result) {

    public static final Result<String> OK = new Result<>("ok");
    public static final Result<Object> EMPTY = new Result<>(null);

}

