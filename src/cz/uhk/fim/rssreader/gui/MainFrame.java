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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class MainFrame extends JFrame {

    private static final String VALIDATION_TYPE = "VALIDATION_TYPE";
    private static final String IO_LOAD_TYPE = "IO_LOAD_TYPE";
    private static final String IO_SAVE_TYPE = "IO_SAVE_TYPE";


    private JLabel lblErrorMessage;
    private RSSList rssList;
    private JScrollPane myScrollPane;
    List<RSSSource> sources;
    JComboBox cmbList = new JComboBox();
    JPanel contentPanel;


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
        //nacti zdroje
        try {
            sources = FileUtils.loadSources();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //horni panel
        JPanel controlPanel = new JPanel(new BorderLayout());
        JPanel buttons = new JPanel(new FlowLayout());

        //horní itemy
        JButton btnAdd = new JButton("Add");
        JButton btnEdit = new JButton("Edit");
        JButton btnRemove = new JButton("Remove");

        lblErrorMessage = new JLabel();
        lblErrorMessage.setBackground(Color.RED);
        lblErrorMessage.setOpaque(true);
        lblErrorMessage.setHorizontalAlignment(SwingConstants.CENTER);

        //pridani hornich itemů
        controlPanel.add(cmbList, BorderLayout.NORTH);
        controlPanel.add(buttons);
        buttons.add(btnAdd, BorderLayout.CENTER);
        buttons.add(btnEdit, BorderLayout.CENTER);
        buttons.add(btnRemove, BorderLayout.CENTER);

        //pridani horniho panelu
        add(controlPanel, BorderLayout.NORTH);

        contentPanel = new JPanel(new WrapLayout());

        loadSources();
        updateSources();
        updateCardView();

        cmbList.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.paramString().endsWith("=SELECTED")) {
                    cmbList.setSelectedItem(e.getItem());
                    updateCardView();
                }
            }
        });

        add(myScrollPane = new JScrollPane(contentPanel), BorderLayout.CENTER);
        myScrollPane.getVerticalScrollBar().setUnitIncrement(10);


        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cmbList.getItemCount() > 0) {
                    initAddDialog(sources.get(cmbList.getSelectedIndex()));
                }
            }
        });
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initAddDialog(null);
            }
        });
        btnRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cmbList.getItemCount() > 0) {
                    int index = cmbList.getSelectedIndex();
                    cmbList.removeItem(cmbList.getItemAt(index));
                    sources.remove(index);
                    FileUtils.saveSources(sources);
                    if (cmbList.getItemCount()==0){
                        lblErrorMessage.setVisible(false);
                    }
                }
            }
        });
    }

    private void initAddDialog(RSSSource src) {
        //vytvor dialog
        JDialog dlgAdd = new JDialog();
        dlgAdd.setVisible(true);
        dlgAdd.setSize(260, 175);
        dlgAdd.setLocationRelativeTo(null);
        dlgAdd.setTitle("Add new RSS item");
        dlgAdd.setLayout(new BorderLayout());
        //pridej itemy
        JTextField txtName = new JTextField();
        JLabel lblName = new JLabel("Název:");
        JTextField txtLink = new JTextField();
        JLabel lblLink = new JLabel("Link:");
        JButton btnOK = new JButton("OK");
        JButton btnCancel = new JButton("Cancel");
        if (!(src == null)) {
            dlgAdd.setTitle("Edit RSS item");
            txtName.setText(src.getName());
            txtLink.setText(src.getSource());
        }
        JPanel pnlFields = new JPanel();
        pnlFields.setLayout(new GridLayout(4,1));
        pnlFields.setBorder(BorderFactory.createEmptyBorder(5,30,0,30));
        dlgAdd.add(pnlFields,BorderLayout.NORTH);
        pnlFields.add(lblName);
        pnlFields.add(txtName);
        pnlFields.add(lblLink);
        pnlFields.add(txtLink);
        JPanel pnlBtns = new JPanel();
        dlgAdd.add(pnlBtns,BorderLayout.CENTER);
        pnlBtns.setLayout(new FlowLayout(FlowLayout.RIGHT));
        pnlBtns.setBorder(BorderFactory.createEmptyBorder(0,0,0,25));
        pnlBtns.add(btnOK);
        pnlBtns.add(btnCancel);
        dlgAdd.add(lblErrorMessage,BorderLayout.SOUTH);
        lblErrorMessage.setVisible(false);
        //listenery
        btnOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateInput(txtName.getText()) &&
                        validateInput(txtLink.getText())) {
                    if (src != null) {
                        sources.get(cmbList.getSelectedIndex()).setName(txtName.getText().replaceAll(";", ""));
                        sources.get(cmbList.getSelectedIndex()).setSource(txtLink.getText().replaceAll(";", ""));
                    } else {
                        sources.add(new RSSSource(txtName.getText().replaceAll(";", ""), txtLink.getText().replaceAll(";", "")));
                    }
                    FileUtils.saveSources(sources);
                    loadSources();
                    updateSources();
                    cmbList.setSelectedIndex(cmbList.getItemCount() - 1);
                    updateCardView();
                    dlgAdd.dispose();
                }
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dlgAdd.dispose();
            }
        });
    }

    private void updateSources() {
        if (!sources.isEmpty()) {
            cmbList.removeAllItems();
            for (int i = 0; i < sources.size(); i++) {
                cmbList.addItem(sources.get(i).getName());
            }
        }
    }

    private void loadSources() {
        try {
            FileUtils.loadSources();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateCardView() {
        contentPanel.removeAll();
        contentPanel.updateUI();
        for (RSSSource src : sources
        ) {

            if (src.getName().equals(cmbList.getSelectedItem())) {
                try {
                    rssList = new RSSParser().getParsedRSS(src.getSource());
                    for (RSSItem item : rssList.getAllItems()) {
                        contentPanel.add(new CardView(item));
                    }
                } catch (FileNotFoundException e) {
                    contentPanel.add(lblErrorMessage, BorderLayout.SOUTH);
                    showErrorMessage(IO_LOAD_TYPE);
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    private boolean validateInput(String text) {
        lblErrorMessage.setVisible(false);
        if (text.trim().isEmpty()) {
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

//TODO - dialog: - 2 fieldy (název, link) - pro oba validace (validateInput - upravit metodu pro potřeby kódu)
//      - validace polí "název" a "link" na přítomnost středníku (replaceAll(";", "");)
// - přidat do/upravit GUI - tlačítka "Add", "Edit", "Remove/Delete" - pro CRUD akce se sources
//      - přidat ComboBox pro výběr zdroje feedu - pouze název feedu (bez linku)
// - tlačítko "Load" - volitelně - buď automatická změna při výběru v ComboBoxu nebo výběr v ComboBoxu a pak Load
// - aplikace bude fungovat jak pro lokální soubor, tak pro online feed z internetu
// - aplikace v žádném případě nespadne na hubu - otestovat a ošetřit
// - funkční ukládání a načítání konfigurace
// - při spuštění aplikace se automaticky načte první záznam z konfigurace
//          - pokud konfigurace existuje nebo není prázdná -> nutno kontrolovat