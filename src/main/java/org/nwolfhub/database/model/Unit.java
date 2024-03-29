package org.nwolfhub.database.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(schema = "bottalk")
public class Unit {
    @Id
    public String name;
    @Column(length = 4096)
    public String description;
    @ManyToMany
    @JoinTable(schema = "bottalk", name = "field_mappings", joinColumns = @JoinColumn(name = "name"), inverseJoinColumns = @JoinColumn(name = "field_name", referencedColumnName = "name"))
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

    public Unit addField(Field field) {
        this.fields.add(field);
        return this;
    }
}
