package cz.uhk.fim.rssreader.gui;

import cz.uhk.fim.rssreader.utils.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class MainFrame extends JFrame {

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
        JTextField txtInputField = new JTextField();
        JButton btnSave = new JButton("Save");
        JLabel lblError = new JLabel("Prazdne pole!!!",JLabel.CENTER);
        lblError.setBackground(Color.RED);
        lblError.setOpaque(true);
        //Pridani hornich itemů
        controlPanel.add(btnLoad, BorderLayout.WEST);
        controlPanel.add(txtInputField, BorderLayout.CENTER);
        controlPanel.add(btnSave, BorderLayout.EAST);

        //listener tlacitka Load
        btnLoad.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Load clicked");
                if (validateInput(txtInputField.getText())==false){
                    controlPanel.add(lblError, BorderLayout.NORTH);
                    controlPanel.updateUI();
                }else {controlPanel.remove(lblError);
                controlPanel.updateUI();}

            }
        });
        //listener tlacitka Save
        btnSave.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Save clicked");
            }
        });

        //horni kontrolní panel
        add(controlPanel, BorderLayout.NORTH);

        //dolni text panel
        JPanel textPanel = new JPanel(new BorderLayout());

        JTextArea txtContent = new JTextArea();

        textPanel.add(txtContent, BorderLayout.CENTER);

        add(new JScrollPane(textPanel), BorderLayout.CENTER);

        try {
            txtContent.setText(FileUtils.loadStringFromFile("rss.xml"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean validateInput(String in) {
        System.out.println("validating " + in);
        if (in.trim().equals("")) {
            System.out.println("empty");
            return false;
        } else {
            return true;
        }
    }
}
