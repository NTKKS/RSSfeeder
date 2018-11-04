package cz.uhk.fim.rssreader.gui;

import cz.uhk.fim.rssreader.model.RSSItem;

import javax.swing.*;
import java.awt.*;

public class CardView extends JPanel {

    public static final int ITEM_WIDTH = 180;
    public static final int COMPONENT_WIDTH = 160;
    public static final int HEIGHT = 1;

    final String startHtml = "<html><p style='width: " + COMPONENT_WIDTH +" px'>";
    final String endHtml = "</p></html>";

    public CardView(RSSItem item){
        setLayout(new WrapLayout());
        setSize(ITEM_WIDTH,HEIGHT);
        setTitle(item.getTitle());
        setBackground(Color.RED);
        setDescription(item.getDescription());
        setInfo(String.format("%s - %s", item.getPubDate(), item.getAuthor()));
        genColor(item);
    }

    private void  setTitle(String title){
        JLabel lblTitle = new JLabel();
        lblTitle.setSize(COMPONENT_WIDTH,HEIGHT);
        lblTitle.setFont(new Font("Courier", Font.BOLD, 12));
        lblTitle.setText(String.format("%s%s%s", startHtml, title, endHtml));
        add(lblTitle);
    }

    private void setDescription(String description){
        JLabel lblDescr = new JLabel();
        lblDescr.setSize(COMPONENT_WIDTH,HEIGHT);
        lblDescr.setFont(new Font("Courier", Font.PLAIN, 11));
        lblDescr.setText(String.format("%s%s%s", startHtml, description, endHtml));
        add(lblDescr);
    }

    private void setInfo(String info){
        JLabel lblInfo = new JLabel();
        lblInfo.setSize(COMPONENT_WIDTH,HEIGHT);
        lblInfo.setFont(new Font("Courier", Font.ITALIC, 10));
        lblInfo.setForeground(Color.LIGHT_GRAY);
        lblInfo.setText(String.format("%s%s%s", startHtml, info, endHtml));
        add(lblInfo);
    }

    private void genColor(RSSItem item){
        String text = item.getDescription().replaceAll("[^\\p{L}\\p{Z}]","").replaceAll("\\s","");
        System.out.println(text);
        char txt = text.charAt(0);
        System.out.println(txt);
        int cislo = txt;
        System.out.println(cislo);
        Color color = new Color(0xffffff);
    }
}
