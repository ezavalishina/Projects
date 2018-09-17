package ru.nsu.fit.zavalishina.model;

import java.io.IOException;
import java.io.StringReader;

public class Model {

    public String execute(String expression) throws CalcPanelException {
        try {
            Lexer lexer = new Lexer(new StringReader(expression));
            Parser parser = new Parser(lexer);

            double result = parser.calculate();

            if ((int)result == result) {
                return String.valueOf((int)result);
            } else {
                return String.valueOf(result);
            }

        } catch (IOException | LexerException | ParseException e) {
            throw new CalcPanelException();
        }
    }
}
