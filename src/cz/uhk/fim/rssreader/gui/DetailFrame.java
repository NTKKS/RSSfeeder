package cz.uhk.fim.rssreader.gui;

import cz.uhk.fim.rssreader.model.RSSItem;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DetailFrame extends JFrame {

    private RSSItem item;

    public DetailFrame(RSSItem item){
        this.item = item;
        init();
    }

    private void init(){
        setUndecorated(true);
        setSize(600, 400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isRightMouseButton(e)){
                    dispose();
                }
            }
        });

        initContentUI();
    }
    private void initContentUI(){
        JPanel content = new JPanel();
        add(content);
        JLabel lblTitle = new JLabel(item.getTitle());
        content.add(lblTitle);
        JLabel lblDescription = new JLabel(item.getDescription());
        lblDescription.setHorizontalAlignment(SwingConstants.LEFT);
        content.add(lblDescription);
        JLabel lblLink = new JLabel(item.getLink());
        content.add(lblLink);
        JLabel lblDateAuthor = new JLabel(item.getPubDate()+" "+item.getAuthor());
        content.add(lblDateAuthor);
    }
}
