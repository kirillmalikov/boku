package com.boku.boku.command;

public interface Command<T> {

    void execute(T parameter) throws Exception;
}
