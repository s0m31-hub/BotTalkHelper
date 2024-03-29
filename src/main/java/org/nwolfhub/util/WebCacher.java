package org.nwolfhub.util;

import jakarta.annotation.PostConstruct;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.nwolfhub.database.model.Field;
import org.nwolfhub.database.model.Section;
import org.nwolfhub.database.model.Unit;
import org.nwolfhub.database.repositories.FieldRepository;
import org.nwolfhub.database.repositories.SectionRepository;
import org.nwolfhub.database.repositories.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.xsoup.Xsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class WebCacher {
    private Document botapi;
    private Document tdapi;
    private final FieldRepository fieldRepository;
    private final SectionRepository sectionRepository;
    private final UnitRepository unitRepository;
    public WebCacher(FieldRepository fieldRepository, SectionRepository sectionRepository, UnitRepository unitRepository) throws IOException {
        botapi = Jsoup.connect("https://core.telegram.org/bots/api").get();
        tdapi = Jsoup.connect("https://corefork.telegram.org/methods").get();
        this.fieldRepository = fieldRepository;
        this.sectionRepository = sectionRepository;
        this.unitRepository = unitRepository;
    }

    @PostConstruct
    public void reCache() {
        List<Element> elements = Xsoup.compile("//*[@id=\"dev_page_content\"]").evaluate(botapi).getElements().get(0).children();
        Section lastSection=null;
        Unit lastUnit = null;
        List<Section> sections =  new ArrayList<>();
        List<Unit> units = new ArrayList<>();
        List<Field> fields = new ArrayList<>();
        for(Element e:elements) {
            if(e.is("h3")) {
                if (lastSection != null) {
                    sections.add(lastSection);
                }
                lastSection = new Section(e.text());
            } else if(e.is("h4")) {
                if(lastUnit!=null) {
                    units.add(lastUnit);
                }
                lastUnit = new Unit().setName(e.text());
            } else if(e.is("p")) {
                if(lastUnit!=null && lastUnit.description==null) {
                    lastUnit.setDescription(e.text());
                }
            } else if(e.is("table")) {
                List<Element> basicFields = e.children();
                for(Element fieldElement:basicFields) {
                    if(fieldElement.is("tbody")) {
                        List<Field> fields2 = new ArrayList<>();
                        for(Element element:fieldElement.children()) {
                            if(element.is("tr")) {
                                Field field = new Field();
                                for(Element finallyFinal:element.children()) {
                                    if(finallyFinal.is("td")) {
                                        if (field.name == null) field.setName(finallyFinal.text());
                                        else if (field.type == null) field.setType(finallyFinal.text());
                                        else if (field.required == null && (finallyFinal.text().equalsIgnoreCase("Optional") || finallyFinal.text().equalsIgnoreCase("Yes")))
                                            field.setRequired(finallyFinal.text().equalsIgnoreCase("yes"));
                                        else field.setDescription(finallyFinal.text());
                                    }
                                }
                                fields2.add(field);
                                fields.add(field);
                            }
                        }
                        Objects.requireNonNull(lastUnit).setFields(fields2);
                    }
                }
            }
        }
        fieldRepository.saveAll(fields);
        unitRepository.saveAll(units);
        sectionRepository.saveAll(sections);
        System.out.println("Recaching complete");
    }
}
