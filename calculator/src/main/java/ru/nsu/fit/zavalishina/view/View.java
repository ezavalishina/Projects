package ru.nsu.fit.zavalishina.view;

import ru.nsu.fit.zavalishina.controller.Controller;
import ru.nsu.fit.zavalishina.model.CalcPanelException;
import ru.nsu.fit.zavalishina.model.Model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static java.lang.System.exit;

public final class View extends JFrame {
    private JToolBar toolBar;
    private Controller controller;
    private Model model;
    private final CalcTextField textField;
    private Font font = new Font("Dialog", Font.PLAIN, 35);
    private Color equalColor = new Color(21, 84, 232);
    private Color numPadColor = Color.LIGHT_GRAY;
    private Color operationsButtonColor = Color.GRAY;
    private Color removeColor = new Color(210, 75, 41);
    private final MyButton equalButton;

    public View(final Controller controller, Model model) {
        super("Калькулятор");
        this.controller = controller;
        this.model = model;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        toolBar = new JToolBar();
        add(toolBar, BorderLayout.PAGE_START);
        final JButton exitButton = new JButton("Выход");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exit(0);
            }
        });
        toolBar.add(exitButton);
        JButton aboutButton = new JButton("About");
        aboutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAbout();
            }
        });
        toolBar.add(aboutButton);

        JPanel textFieldPanel = new JPanel();
        textFieldPanel.setBackground(numPadColor);
        textField = new CalcTextField(15);
        textField.setHorizontalAlignment(JTextField.RIGHT);
        textField.setFont(font);
        textFieldPanel.add(textField);
        textField.requestFocus(true);

        JPanel bracketPanel = new JPanel();
        bracketPanel.setBackground(numPadColor);
        GridLayout layout = new GridLayout(1, 4);
        bracketPanel.setLayout(layout);

        bracketPanel.add(new CalcButton("(", textField, operationsButtonColor, font));
        bracketPanel.add(new CalcButton(")", textField, operationsButtonColor, font));
        bracketPanel.add(new CButton(textField, removeColor, font));

        final DelButton del = new DelButton(textField, removeColor, font);
        bracketPanel.add(del);

        JPanel numPanel = new JPanel();
        numPanel.setBackground(numPadColor);
        GridLayout numLayout = new GridLayout(4, 3);
        numPanel.setLayout(numLayout);

        for (int i = 1; i < 10; i++) {
            numPanel.add(new CalcButton(String.valueOf(i), textField, numPadColor, font));
        }

        numPanel.add(new CalcButton(".", textField, numPadColor, font));
        numPanel.add(new CalcButton("0", textField, numPadColor, font));

        equalButton = new MyButton("=", equalColor, font);
        equalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textField.setText(clickEqual(textField.getText()));
            }
        });
        numPanel.add(equalButton);

        JPanel operationsPanel = new JPanel();
        GridLayout operationsLayout = new GridLayout(5, 1);
        operationsPanel.setLayout(operationsLayout);

        operationsPanel.add(new CalcButton("+", textField, operationsButtonColor, font));
        operationsPanel.add(new CalcButton("-", textField, operationsButtonColor, font));
        operationsPanel.add(new CalcButton("*", textField, operationsButtonColor, font));
        operationsPanel.add(new CalcButton("/", textField, operationsButtonColor, font));
        operationsPanel.add(new CalcButton("^", textField, operationsButtonColor, font));

        JPanel numOperPanel = new JPanel();
        GridBagLayout numOperLayout = new GridBagLayout();
        numOperPanel.setLayout(numOperLayout);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 3.0 / 4.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridy = 0;
        gbc.insets.top = 1;
        gbc.insets.bottom = -2;
        numOperLayout.setConstraints(numPanel, gbc);
        gbc.insets.top = 0;
        gbc.insets.bottom = -2;
        gbc.weightx = 1.0 / 4;
        gbc.weighty = 0.98 / 5;
        gbc.gridy = 0;
        gbc.gridx = 1;
        numOperLayout.setConstraints(operationsPanel, gbc);
        numOperPanel.add(numPanel);
        numOperPanel.add(operationsPanel);

        JPanel mainPanel = new JPanel();
        GridBagLayout gbl = new GridBagLayout();
        mainPanel.setLayout(gbl);
        mainPanel.setBackground(numPadColor);
        add(mainPanel);
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 1.0;
        c.weighty = 0.02;
        c.fill = GridBagConstraints.BOTH;
        c.gridy = 0;
        c.insets.top = 4;
        gbl.setConstraints(textFieldPanel, c);
        c.insets.top = 1;
        c.insets.bottom = -2;
        c.weightx = 1.0;
        c.weighty = 0.98 / 5.0;
        c.gridy = 1;
        gbl.setConstraints(bracketPanel, c);
        c.weightx = 1.0;
        c.weighty = 0.98 / 5.0 * 4.0;
        c.gridy = 2;
        c.gridx = 0;
        gbl.setConstraints(numOperPanel, c);

        mainPanel.add(textFieldPanel);
        mainPanel.add(bracketPanel);
        mainPanel.add(numOperPanel);

        textField.addKeyListener(new MyKeyListener());
    }

    private String clickEqual(String string) {
        String solve = "";
        try {
            solve = controller.execute(string);
            textField.isExecuted = true;
        } catch (CalcPanelException ex) {
            JOptionPane.showMessageDialog(this, "Введены некорректные данные!", "Ошибка!", JOptionPane.ERROR_MESSAGE);
            return string;
        }
        return solve;
    }

    private void showAbout(){
        JOptionPane.showMessageDialog(this, "Тестовое задание для Focus Start by Elena Zavalishina.\n\n" +
                "Строковый калькулятор для десятичных чисел с поддержкой\n" +
                "    • бинарных операций + - * /\n" +
                "    • унарных операций - ^\n" +
                "    • произвольной вложенности скобок", "About", JOptionPane.INFORMATION_MESSAGE);
    }

    private class MyKeyListener implements KeyListener {
        boolean isClicked = false;
        boolean shiftClicked = false;
        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            char symbol = e.getKeyChar();
            if (Character.isLetter(symbol)) {
                isClicked = true;
            } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                equalButton.doClick();
            } else if ( e.getKeyCode() == KeyEvent.VK_SHIFT) {
                shiftClicked = true;
            }
        }


        @Override
        public void keyReleased(KeyEvent e) {
            if (shiftClicked) {
                shiftClicked = false;
            }
            if (isClicked) {
                if (textField.getText().length() != 0) {
                    int pos = textField.getCaretPosition();
                    String string = textField.getText().substring(0, pos - 1) + textField.getText().substring(pos, textField.getText().length());
                    textField.setText(string);
                    textField.setCaretPosition(pos - 1);
                    isClicked = false;
                }
            }
        }
    }
}
