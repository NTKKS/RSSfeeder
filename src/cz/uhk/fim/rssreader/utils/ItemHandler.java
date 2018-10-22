package cz.uhk.fim.rssreader.utils;

import cz.uhk.fim.rssreader.model.RSSItem;
import cz.uhk.fim.rssreader.model.RSSList;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class ItemHandler extends DefaultHandler {

    private static final String ITEM = "item";
    private static final String TITLE = "title";
    private static final String LINK = "link";
    private static final String DESCRIPTION = "description";
    private static final String PUB_DATE = "pubDate";
    private static final String AUTHOR = "dc:creator";

    private RSSList rssList;
    private RSSItem rssItem;

    private boolean title;
    private boolean link;
    private boolean description;
    private boolean pubDate;
    private boolean author;

    public ItemHandler(RSSList rssList) {
        this.rssList = rssList;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (qName.equalsIgnoreCase(ITEM)) {
            rssItem = new RSSItem();
        }

        if (qName.equalsIgnoreCase(TITLE) && rssItem != null) {
            title = true;
        }
        if (qName.equalsIgnoreCase(LINK) && rssItem != null) {
            link = true;
        }
        if (qName.equalsIgnoreCase(DESCRIPTION) && rssItem != null) {
            description = true;
        }
        if (qName.equalsIgnoreCase(PUB_DATE) && rssItem != null) {
            pubDate = true;
        }
        if (qName.equalsIgnoreCase(AUTHOR) && rssItem != null) {
            author = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (qName.equalsIgnoreCase(ITEM)){
            rssList.addItem(rssItem);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        if (title){
            rssItem.setTitle(new String(ch, start, length));
            title = false;
        }
        if (link){
            rssItem.setLink(new String(ch, start, length));
            link = false;
        }
        if (description){
            rssItem.setDescription(new String(ch, start, length));
            description = false;
        }
        if (pubDate){
            rssItem.setPubDate(new String(ch, start, length));
            pubDate = false;
        }
        if (author){
            rssItem.setAuthor(new String(ch, start, length));
            author = false;
        }
    }
}
