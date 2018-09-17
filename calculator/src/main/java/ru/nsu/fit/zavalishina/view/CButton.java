package ru.nsu.fit.zavalishina.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CButton extends MyButton {
    public CButton(final CalcTextField textField, Color color, Font font) {
        super("C", color, font);

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textField.getText().length() != 0) {
                    textField.setText("");
                }
            }
        });
    }
}
