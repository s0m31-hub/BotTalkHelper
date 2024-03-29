package org.nwolfhub.database.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(schema = "bottalk")
public class Section {
    @Id
    public String name;

    public Section() {
    }

    public Section(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public Section setName(String name) {
        this.name = name;
        return this;
    }
}
