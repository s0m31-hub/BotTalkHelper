package org.nwolfhub.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(schema = "bottalk", name = "saved")
public class PreparedMessage {
    @Id
    public Long id;
    @Column(length = 4096)
    public String text;
    public Long owner;
    public boolean global;
    public String name;
    public PreparedMessage() {}

    public PreparedMessage(Long id, String text, Long owner, boolean global) {
        this.id = id;
        this.text = text;
        this.owner = owner;
        this.global = global;
    }

    public PreparedMessage(Long id, String name, String text, Long owner, boolean global) {
        this.id = id;
        this.text = text;
        this.owner = owner;
        this.global = global;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public PreparedMessage setId(Long id) {
        this.id = id;
        return this;
    }

    public String getText() {
        return text;
    }

    public PreparedMessage setText(String text) {
        this.text = text;
        return this;
    }

    public Long getOwner() {
        return owner;
    }

    public PreparedMessage setOwner(Long owner) {
        this.owner = owner;
        return this;
    }

    public boolean isGlobal() {
        return global;
    }

    public PreparedMessage setGlobal(boolean global) {
        this.global = global;
        return this;
    }

    public String getName() {
        return name;
    }

    public PreparedMessage setName(String preview) {
        this.name = preview;
        return this;
    }
}
