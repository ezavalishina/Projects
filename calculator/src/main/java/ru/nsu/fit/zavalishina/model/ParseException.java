package ru.nsu.fit.zavalishina.model;

public class ParseException extends Exception{
    public ParseException() {
        super();
    }

    public ParseException(String err) {
        super(err);
    }
}
