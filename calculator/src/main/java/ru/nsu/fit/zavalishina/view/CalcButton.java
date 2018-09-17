package ru.nsu.fit.zavalishina.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalcButton extends MyButton {

    public CalcButton(final String text, final CalcTextField textField, final Color color, Font font) {
        super(text, color, font);

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int pos = textField.getCaretPosition();
                if (textField.isExecuted && Character.isDigit(text.charAt(0))) {
                    textField.setText("");
                }
                textField.isExecuted = false;
                StringBuffer string = new StringBuffer(textField.getText());
                string.insert(pos, text);
                textField.setText(string.toString());
                textField.setCaretPosition(pos + text.length());
            }
        });
    }

}
