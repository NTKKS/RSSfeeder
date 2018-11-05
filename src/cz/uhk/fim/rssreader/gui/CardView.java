package cz.uhk.fim.rssreader.gui;

import cz.uhk.fim.rssreader.model.RSSItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CardView extends JPanel {

    public static final int ITEM_WIDTH = 180;
    public static final int COMPONENT_WIDTH = 160;
    public static final int HEIGHT = 1;

    final String startHtml = "<html><p style='width: " + COMPONENT_WIDTH +" px'>";
    final String endHtml = "</p></html>";
    private Color textColor;

    public CardView(RSSItem item){
        setLayout(new WrapLayout());
        setSize(ITEM_WIDTH,HEIGHT);
        setBackground(genColor(item));
        setInverseColor(genColor(item));
        setTitle(item.getTitle());
        setDescription(item.getDescription());
        setInfo(String.format("%s%s", item.getPubDate(), item.getAuthor()));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount()==2){
                    new DetailFrame(item).setVisible(true);
                }
            }
        });
    }

    private void  setTitle(String title){
        JLabel lblTitle = new JLabel();
        lblTitle.setSize(COMPONENT_WIDTH,HEIGHT);
        lblTitle.setFont(new Font("Courier", Font.BOLD, 12));
        lblTitle.setForeground(textColor);
        lblTitle.setText(String.format("%s%s%s", startHtml, title.subSequence(0,Math.min(50,title.length()))+"...", endHtml));
        add(lblTitle);
    }

    private void setDescription(String description){
        JLabel lblDescr = new JLabel();
        lblDescr.setSize(COMPONENT_WIDTH,HEIGHT);
        lblDescr.setFont(new Font("Courier", Font.PLAIN, 11));
        lblDescr.setForeground(textColor);
        lblDescr.setText(String.format("%s%s%s", startHtml, description.subSequence(0,Math.min(100,description.length()))+" ...", endHtml));
        add(lblDescr);
    }

    private void setInfo(String info){
        JLabel lblInfo = new JLabel();
        lblInfo.setSize(COMPONENT_WIDTH,HEIGHT);
        lblInfo.setFont(new Font("Courier", Font.ITALIC, 10));
        lblInfo.setForeground(Color.LIGHT_GRAY);
        lblInfo.setText(String.format("%s%s<br />%s%s", startHtml, info.substring(0,29),info.substring(29), endHtml));
        add(lblInfo);
    }

    private Color genColor(RSSItem item){
        String text = item.getDescription();
        Color color = Color.decode(String.valueOf(text.hashCode()));
        return color;
    }

    private void setInverseColor(Color bgColor){
        textColor = new Color(255-bgColor.getRed(),255-bgColor.getGreen(),255-bgColor.getBlue());
    }
}
