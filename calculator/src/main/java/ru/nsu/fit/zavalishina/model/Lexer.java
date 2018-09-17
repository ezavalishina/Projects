package ru.nsu.fit.zavalishina.model;

import java.io.IOException;
import java.io.Reader;

public class Lexer {
    private Reader reader;
    private char current;
    private StringBuilder value = new StringBuilder();
    private boolean dot = false;

    public Lexer(Reader reader) throws IOException {
        this.reader = reader;
        current = (char) reader.read();
    }

    Lexeme getLexeme() throws IOException, LexerException {
        Lexeme result = new Lexeme();

        while (Character.isWhitespace(current))
            next();

        switch (current) {
            case ' ':
                break;

            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                value.setLength(0);
                value.append(current);

                while (true) {
                    next();

                    if (Character.isWhitespace(current))
                        continue;

                    if (Character.isDigit(current)) {
                        value.append(current);
                        continue;
                    }
                    if (current == '.' || current == ',') {
                        if (dot) {
                            throw new LexerException("Неправильное дробное число: " + current);
                        } else {
                            dot = true;
                            value.append('.');
                            continue;
                        }
                    }
                    result.setType(LexemeType.NUMBER);
                    result.setValue(value.toString());
                    dot = false;

                    return result;
                }

            case '+':
                result.setType(LexemeType.PLUS);
                result.setValue("+");

                break;

            case '-':
                result.setType(LexemeType.MINUS);
                result.setValue("-");

                break;

            case '(':
                result.setType(LexemeType.OPEN);
                result.setValue("(");
                break;

            case ')':
                result.setType(LexemeType.CLOSE);
                result.setValue(")");
                break;

            case '/':
                result.setType(LexemeType.DIV);
                result.setValue("/");
                break;

            case '*':
                result.setType(LexemeType.MULT);
                result.setValue("*");
                break;

            case '^':
                result.setType(LexemeType.POWER);
                result.setValue("^");
                break;

            case '\uFFFF':
                result.setType(LexemeType.EOF);
                result.setValue("EOF");
                break;

            default:
                throw new LexerException("Неизвестный символ: " + current);
        }

        next();

        return result;
    }

    private int next() throws IOException {
        int result = reader.read();
        current = (char) result;

        return result;
    }
}

