package org.nwolfhub.model;

import java.util.List;

public class Section {
    public String name;
    public List<Unit> units;

    public Section() {
    }

    public Section(String name) {
        this.name = name;
    }

    public Section(String name, List<Unit> units) {
        this.name = name;
        this.units = units;
    }

    public String getName() {
        return name;
    }

    public Section setName(String name) {
        this.name = name;
        return this;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public Section setUnits(List<Unit> units) {
        this.units = units;
        return this;
    }
}
