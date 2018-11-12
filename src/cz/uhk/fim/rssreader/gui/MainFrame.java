package cz.uhk.fim.rssreader.gui;

import cz.uhk.fim.rssreader.model.RSSItem;
import cz.uhk.fim.rssreader.model.RSSList;
import cz.uhk.fim.rssreader.model.RSSSource;
import cz.uhk.fim.rssreader.utils.FileUtils;
import cz.uhk.fim.rssreader.utils.RSSParser;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {

    private static final String VALIDATION_TYPE = "VALIDATION_TYPE";
    private static final String IO_LOAD_TYPE = "IO_LOAD_TYPE";
    private static final String IO_SAVE_TYPE = "IO_SAVE_TYPE";


    private JLabel lblErrorMessage;
    private JTextArea txtContent = new JTextArea();
    private JTextField txtInputField;
    private RSSList rssList;
    private JScrollPane myScrollPane;


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

        JPanel contentPanel = new JPanel(new WrapLayout());

        try {
            //rssList = new RSSParser().getParsedRSS("rss.xml");
            rssList = new RSSParser().getParsedRSS("https://www.zive.cz/rss");
            for (RSSItem item : rssList.getAllItems()) {
                contentPanel.add(new CardView(item));
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        add(myScrollPane = new JScrollPane(contentPanel), BorderLayout.CENTER);
        myScrollPane.getVerticalScrollBar().setUnitIncrement(10);

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<RSSSource> sources = new ArrayList<>();
                sources.add(new RSSSource("Živě.cz", "https://fjdslfsjkd"));
                sources.add(new RSSSource("dafadfs", "https://fjdslfsjkd"));
                sources.add(new RSSSource("4654646", "https://fjdslfsjkd"));
                FileUtils.saveSources(sources);
            }
        });

        btnLoad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    List<RSSSource> sources = FileUtils.loadSources();
                    for (RSSSource s: sources
                         ) {
                        System.out.println(s.getName()+";"+s.getSource());
                    }
                } catch (IOException e1) {
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
