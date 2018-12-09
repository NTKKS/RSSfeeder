package cz.uhk.fim.rssreader.gui;

import cz.uhk.fim.rssreader.model.RSSItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class DetailFrame extends JFrame {

    public static final int DETAIL_WIDTH = 600;
    public static final int DETAIL_HEIGHT = 400;
    public static final int CONTENT_WIDTH = (DETAIL_WIDTH/2);
    final String startHtml = "<html><p style='width: " + CONTENT_WIDTH +" px;text-align:center;margin-top:10px'>";
    final String startHtmlDesc = "<html><p style='width: " + CONTENT_WIDTH +" px;text-align:justify;padding:30px 0 80px 0'>";
    final String endHtml = "</p></html>";

    private RSSItem item;
    private Color detailColor;
    private Color textColor;

    public DetailFrame(RSSItem item, Color detailColor, Color textColor){
        dispose();
        this.item = item;
        this.detailColor = detailColor;
        this.textColor = textColor;
        init();
    }

    private void init(){
        setUndecorated(true);
        setSize(DETAIL_WIDTH, DETAIL_HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isRightMouseButton(e)){
                    dispose();
                }
                //otevri odkaz v prohlizeci
                else if (SwingUtilities.isLeftMouseButton(e)&&e.isControlDown()){
                        Desktop desktop = java.awt.Desktop.getDesktop();
                    URI oURL = null;
                    try {
                        oURL = new URI(item.getLink());
                    } catch (URISyntaxException e1) {
                        e1.printStackTrace();
                    }
                    try {
                        desktop.browse(oURL);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    dispose();
                }
            }
        });

        initContentUI();
    }
    private void initContentUI(){
        JPanel content = new JPanel();
        content.setBackground(textColor == Color.BLACK ? detailColor.brighter() : detailColor.darker());
        add(content);

        JLabel lblTitle = new JLabel(item.getTitle());
        lblTitle.setText(String.format("%s%s%s", startHtml, lblTitle.getText(), endHtml));
        lblTitle.setFont(new Font("Arial",Font.BOLD,20));
        lblTitle.setForeground(textColor);
        content.add(lblTitle);

        JLabel lblDescription = new JLabel(item.getDescription());
        System.out.println(lblDescription.getText());
        lblDescription.setText(String.format("%s%s%s", startHtmlDesc,lblDescription.getText(),endHtml));
        lblDescription.setFont(new Font("Arial",Font.PLAIN,12));
        lblDescription.setForeground(textColor);
        content.add(lblDescription);

        JLabel lblLink = new JLabel(item.getLink());
        lblLink.setText(String.format("%s%s%s", startHtml,lblLink.getText(),endHtml));
        lblLink.setFont(new Font("Arial",Font.ITALIC,10));
        lblLink.setForeground(textColor);//getGray(textColor));
        content.add(lblLink);

        JLabel lblDateAuthor = new JLabel(item.getPubDate()+" "+item.getAuthor());
        lblDateAuthor.setText(String.format("%s%s%s", startHtml,lblDateAuthor.getText(),endHtml));
        lblDateAuthor.setFont(new Font("Arial",Font.ITALIC,10));
        lblDateAuthor.setForeground(getGray(textColor));
        content.add(lblDateAuthor);
    }

    public Color getGray(Color textColor){
        return textColor == Color.WHITE ? Color.LIGHT_GRAY : Color.DARK_GRAY;
    }
}
