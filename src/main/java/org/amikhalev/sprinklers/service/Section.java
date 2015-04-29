package org.amikhalev.sprinklers.service;

/**
 * Created by alex on 4/21/15.
 */
public abstract class Section {
    private transient Integer id;
    private transient String name;

    public Section(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public abstract void initialize();

    public abstract void cleanup();

    public abstract void on();

    public abstract void off();
}
