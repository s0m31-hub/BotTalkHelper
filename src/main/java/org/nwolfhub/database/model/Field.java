package org.nwolfhub.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(schema = "bottalk")
public class Field {
    @Id
    public String name;
    public String type;
    public Boolean required;
    @Column(length = 4096)
    public String description;

    public Field() {
    }

    public Field(String name, String type, Boolean required, String description) {
        this.name = name;
        this.type = type;
        this.required = required;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public Field setName(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public Field setType(String type) {
        this.type = type;
        return this;
    }

    public Boolean getRequired() {
        return required;
    }

    public Field setRequired(Boolean required) {
        this.required = required;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Field setDescription(String description) {
        this.description = description;
        return this;
    }
}
