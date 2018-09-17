package ru.nsu.fit.zavalishina.controller;

import ru.nsu.fit.zavalishina.model.CalcPanelException;
import ru.nsu.fit.zavalishina.model.Model;

public class Controller {
    private Model model;
    private String result = "";

    public Controller(Model model){
        this.model = model;
    }

    public String execute(String string) throws CalcPanelException {
        try {
            result = model.execute(string);
        } catch (CalcPanelException ex) {
            throw ex;
        }
        return result;
    }
}
