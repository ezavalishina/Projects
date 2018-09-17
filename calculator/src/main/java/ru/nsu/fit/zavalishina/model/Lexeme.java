package ru.nsu.fit.zavalishina.model;

public class Lexeme {
    private LexemeType type;
    private String value;

    public Lexeme() {
        type = LexemeType.EOF;
        value = null;
    }

    public Lexeme(LexemeType type, String value) {
        this.type = type;
        this.value = value;
    }

    public void setType(LexemeType type) {
        this.type = type;
    }

    public LexemeType getType() {
        return type;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
