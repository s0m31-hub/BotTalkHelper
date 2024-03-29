package org.nwolfhub.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.nwolfhub.model.Field;
import org.nwolfhub.model.Section;
import org.nwolfhub.model.Unit;
import us.codecraft.xsoup.Xsoup;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class WebCacher {
    private Document docs;
    public HashMap<String, Section> sections;
    public HashMap<String, Unit> units;
    public HashMap<String, Field> fields;
    public WebCacher() throws IOException {
        docs = Jsoup.connect("https://core.telegram.org/bots/api").get();
        sections = new HashMap<>();
        units = new HashMap<>();
        fields = new HashMap<>();
    }

    public void reCache() {
        List<Element> elements = Xsoup.compile("//*[@id=\"dev_page_content\"]").evaluate(docs).getElements().get(0).children();
        Section lastSection=null;
        Unit lastUnit = null;
        for(Element e:elements) {
            if(e.is("h3")) {
                if (lastSection != null) {
                    sections.put(lastSection.getName(), lastSection);
                }
                lastSection = new Section(e.text());
            } else if(e.is("h4")) {
                if(lastUnit!=null) {
                    units.put(lastUnit.getName(), lastUnit);
                }
                lastUnit = new Unit().setName(e.text());
            } else if(e.is("table")) {
                List<Element> basicFields = e.children();
                for(Element fieldElement:basicFields) {

                }
            }
        }
    }
}
