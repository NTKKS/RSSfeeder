package cz.uhk.fim.rssreader.gui;

import cz.uhk.fim.rssreader.model.RSSItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class CardView extends JPanel {

    public static final int ITEM_WIDTH = 180;
    public static final int COMPONENT_WIDTH = 160;
    public static final int HEIGHT = 1;

    final String startHtml = "<html><p style='width: " + COMPONENT_WIDTH + " px'>";
    final String endHtml = "</p></html>";
    private Color textColor;

    public CardView(RSSItem item) {
        setLayout(new WrapLayout());
        setSize(ITEM_WIDTH, HEIGHT);
        setBackground(genColor(item));
        textColor = inverseColor(genColor(item));
        setTitle(item.getTitle());
        setDescription(item.getDescription());
        setInfo(String.format("%s<br />%s", item.getPubDate(), item.getAuthor()));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
                    new DetailFrame(item,genColor(item),inverseColor(genColor(item))).setVisible(true);
                }
            }
        });
    }

    private void setTitle(String title) {
        JLabel lblTitle = new JLabel();
        lblTitle.setSize(COMPONENT_WIDTH, HEIGHT);
        lblTitle.setFont(new Font("Courier", Font.BOLD, 12));
        lblTitle.setForeground(textColor);
        lblTitle.setText(String.format("%s%s%s", startHtml, title.subSequence(0, Math.min(50, title.length())) + "...", endHtml));
        add(lblTitle);
    }

    private void setDescription(String description) {
        JLabel lblDescr = new JLabel();
        lblDescr.setSize(COMPONENT_WIDTH, HEIGHT);
        lblDescr.setFont(new Font("Courier", Font.PLAIN, 11));
        lblDescr.setForeground(textColor);
        lblDescr.setText(String.format("%s%s%s", startHtml, description.subSequence(0, Math.min(100, description.length())) + " ...", endHtml));
        add(lblDescr);
    }

    private void setInfo(String info) {
        JLabel lblInfo = new JLabel();
        lblInfo.setSize(COMPONENT_WIDTH, HEIGHT);
        lblInfo.setFont(new Font("Courier", Font.ITALIC, 10));
        lblInfo.setText(String.format("%s%s%s", startHtml, info, endHtml));
        if (textColor == Color.WHITE) {
            lblInfo.setForeground(Color.LIGHT_GRAY);
        } else {
            lblInfo.setForeground(Color.DARK_GRAY);
        }
        add(lblInfo);
    }

    private Color genColor(RSSItem item) {
        String text = item.getDescription();
        Color color = Color.decode(String.valueOf(text.hashCode()));
        return color;
    }

    private Color inverseColor(Color bgColor) {
        double r, g, b, l;
        r = bgColor.getRed();
        g = bgColor.getGreen();
        b = bgColor.getBlue();
        ArrayList<Double> color = new ArrayList<>();
        color.add(r);
        color.add(g);
        color.add(b);

        for (int i = 0; i < color.size(); i++) {
            color.set(i, (color.get(i) / 255.0));
            if (color.get(i) <= 0.03928) {
                color.set(i, (color.get(i) / 12.92));
            } else {
                color.set(i, (Math.pow(((color.get(i) + 0.055) / 1.055), 2.4)));
            }
        }

        l = 0.2126 * color.get(0) + 0.7152 * color.get(1) + 0.0722 * color.get(2);

        if (l > 0.179) {
            return Color.BLACK;
        } else {
            return Color.WHITE;
        }
    }
}
