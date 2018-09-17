package ru.nsu.fit.zavalishina.view;

import javax.swing.*;
import java.awt.*;

public class MyButton extends JButton {
    public MyButton(String text, Color color, Font font) {
        super(text);
        setFont(font);
        setBackground(color);
        setBorderPainted(false);
        setFocusPainted(false);
    }
}
