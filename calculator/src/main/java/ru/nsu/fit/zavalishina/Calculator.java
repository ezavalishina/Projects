package ru.nsu.fit.zavalishina;

import ru.nsu.fit.zavalishina.controller.Controller;
import ru.nsu.fit.zavalishina.model.Model;
import ru.nsu.fit.zavalishina.view.View;

/**
 * Hello world!
 *
 */
public class Calculator
{
    private static View view;
    private static Controller controller;
    private static Model model;

    public static void main( String[] args )
    {
        model = new Model();
        controller = new Controller(model);
        view = new View(controller, model);
        view.setVisible(true);

    }
}
