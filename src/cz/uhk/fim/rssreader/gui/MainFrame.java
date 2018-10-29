package cz.uhk.fim.rssreader.gui;

import cz.uhk.fim.rssreader.utils.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class MainFrame extends JFrame {

    private static final String VALIDATION_TYPE = "VALIDATION_TYPE";
    private static final String IO_LOAD_TYPE = "IO_LOAD_TYPE";
    private static final String IO_SAVE_TYPE = "IO_SAVE_TYPE";


    private JLabel lblErrorMessage;
    private JTextArea txtContent = new JTextArea();
    private JTextField txtInputField;


    public MainFrame() {
        init();
    }

    private void init() {
        setTitle("RSS Reader");
        setSize(800, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initContentUI();
    }

    private void initContentUI() {
        //horni panel
        JPanel controlPanel = new JPanel(new BorderLayout());

        //horní itemy
        JButton btnLoad = new JButton("Load");
        txtInputField = new JTextField();
        JButton btnSave = new JButton("Save");

        lblErrorMessage = new JLabel();
        lblErrorMessage.setBackground(Color.RED);
        lblErrorMessage.setOpaque(true);
        lblErrorMessage.setHorizontalAlignment(SwingConstants.CENTER);

        //Pridani hornich itemů
        controlPanel.add(btnLoad, BorderLayout.WEST);
        controlPanel.add(txtInputField, BorderLayout.CENTER);
        controlPanel.add(btnSave, BorderLayout.EAST);
        controlPanel.add(lblErrorMessage, BorderLayout.NORTH);

        //horni kontrolní panel
        add(controlPanel, BorderLayout.NORTH);

        //dolni text panel
        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.add(txtContent, BorderLayout.CENTER);
        add(new JScrollPane(textPanel), BorderLayout.CENTER);

        btnLoad.addActionListener(e -> {
            if (validateInput()) {
                try {
                    txtContent.setText(FileUtils.loadStringFromFile(txtInputField.getText()));
                } catch (IOException e1) {
                    showErrorMessage(IO_LOAD_TYPE);
                    e1.printStackTrace();
                }
            }
        });


        btnSave.addActionListener(e -> {
            if (validateInput()) {
                try {
                    FileUtils.saveStringToFile(txtInputField.getText(), txtContent.getText().getBytes(StandardCharsets.UTF_8));
                } catch (IOException e1) {
                    showErrorMessage(IO_SAVE_TYPE);
                    e1.printStackTrace();
                }
            }
        });
    }

    private boolean validateInput() {
        lblErrorMessage.setVisible(false);
        if (txtInputField.getText().trim().isEmpty()) {
            showErrorMessage(VALIDATION_TYPE);
            return false;
        }
        return true;
    }

    private void showErrorMessage(String type) {
        String message;
        switch (type) {
            case VALIDATION_TYPE:
                message = "Chyba! Pole nesmí být prázdné!";
                break;
            case IO_LOAD_TYPE:
                message = "Chyba při načítání souboru.";
                break;
            case IO_SAVE_TYPE:
                message = "Chyba při ukládání souboru.";
                break;
            default:
                message = "Bůh ví, co se stalo.";
                break;
        }
        lblErrorMessage.setText(message);
        lblErrorMessage.setVisible(true);
    }
}
