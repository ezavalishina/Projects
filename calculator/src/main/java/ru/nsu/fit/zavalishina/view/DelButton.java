package ru.nsu.fit.zavalishina.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DelButton extends MyButton {
    public DelButton(final JTextField textField, Color color, Font font) {
        super("←", color, font);

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textField.getText().length() != 0) {
                    int pos = textField.getCaretPosition();
                    String string = textField.getText().substring(0, pos - 1) + textField.getText().substring(pos, textField.getText().length());
                    textField.setText(string);
                    textField.setCaretPosition(pos - 1);
                }
            }
        });
    }
}
