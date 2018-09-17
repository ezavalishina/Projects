package ru.nsu.fit.zavalishina.model;

import java.io.IOException;

public class CalcPanelException extends Exception {
    public CalcPanelException() {
        super("Введенные данные некорректны!");
    }
}
