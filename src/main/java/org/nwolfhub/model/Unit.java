package org.nwolfhub.model;

import java.util.List;

public class Unit {
    public String name;
    public String description;
    public List<Field> fields;

    public Unit() {
    }

    public Unit(String name, String description, List<Field> fields) {
        this.name = name;
        this.description = description;
        this.fields = fields;
    }

    public String getName() {
        return name;
    }

    public Unit setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Unit setDescription(String description) {
        this.description = description;
        return this;
    }

    public List<Field> getFields() {
        return fields;
    }

    public Unit setFields(List<Field> fields) {
        this.fields = fields;
        return this;
    }
}
