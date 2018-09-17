package ru.nsu.fit.zavalishina.model;

import java.io.IOException;

public class Parser {
    private Lexer lexer;
    private Lexeme current;

    public Parser(Lexer lexer) throws IOException, LexerException {
        this.lexer = lexer;
        current = lexer.getLexeme();
    }

    public double calculate() throws ParseException, IOException, LexerException {
        double temp = parseExpr();

        if (current.getType() != LexemeType.EOF)
            throw new ParseException("EOF not found!");

        return temp;
    }

    private double parseAtom() throws IOException, LexerException, ParseException {
        if (current.getType() == LexemeType.NUMBER) {
            double temp = Double.parseDouble(current.getValue());
            current = lexer.getLexeme();

            return temp;
        }
        else if (current.getType() == LexemeType.OPEN) {
            current = lexer.getLexeme();
            double temp = parseExpr();

            if (current.getType() != LexemeType.CLOSE)
                throw new ParseException(") is missed");


            current = lexer.getLexeme();

            return temp;
        }

        throw new ParseException("Parse error...");
    }

    private double parsePower() throws ParseException, IOException, LexerException {
        double temp = parseAtom();

        if (current.getType() == LexemeType.POWER) {
            current = lexer.getLexeme();
            temp = (double)Math.pow(temp, parseFactor());
        }

        return temp;
    }

    private double parseFactor() throws ParseException, IOException, LexerException {
        if (current.getType() == LexemeType.MINUS) {
            current = lexer.getLexeme();
            return -parsePower();
        }

        return parsePower();
    }

    private double parseTerm() throws ParseException, IOException, LexerException {
        double temp = parseFactor();

        while ((current.getType() == LexemeType.DIV) || (current.getType() == LexemeType.MULT)) {
            if (current.getType() == LexemeType.DIV) {
                current = lexer.getLexeme();

                if (Integer.parseInt(current.getValue()) == 0)
                    throw new ParseException("Division by zero!");

                temp /= parseFactor();
            }
            else {
                current = lexer.getLexeme();
                temp *= parseFactor();
            }
        }

        return temp;
    }

    private double parseExpr() throws ParseException, IOException, LexerException {
        double temp = parseTerm();

        while ((current.getType() == LexemeType.PLUS) || (current.getType() == LexemeType.MINUS)) {
            if (current.getType() == LexemeType.PLUS) {
                current = lexer.getLexeme();
                temp += parseTerm();
            }
            else {
                current = lexer.getLexeme();
                temp -= parseTerm();
            }
        }

        return temp;
    }
}
