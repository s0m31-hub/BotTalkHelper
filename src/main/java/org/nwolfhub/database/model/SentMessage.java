package org.nwolfhub.database.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class SentMessage {
    @Id
    public String id;
    @Column(length = 4096)
    public String text;
    public Long from;
    @ElementCollection
    @CollectionTable(name = "edit_history")
    public List<String> editHistory;

    public SentMessage() {}

    public SentMessage(String id, String text, Long from) {
        this.id = id;
        this.text = text;
        this.from = from;
        this.editHistory = new ArrayList<String>();
    }

    public SentMessage(String id, String text, Long from, List<String> editHistory) {
        this.id = id;
        this.text = text;
        this.from = from;
        this.editHistory = editHistory;
    }

    public String getId() {
        return id;
    }

    public SentMessage setId(String id) {
        this.id = id;
        return this;
    }

    public String getText() {
        return text;
    }

    public SentMessage setText(String text) {
        this.text = text;
        return this;
    }

    public Long getFrom() {
        return from;
    }

    public SentMessage setFrom(Long from) {
        this.from = from;
        return this;
    }

    public List<String> getEditHistory() {
        return editHistory;
    }

    public SentMessage setEditHistory(List<String> editHistory) {
        this.editHistory = editHistory;
        return this;
    }
}
