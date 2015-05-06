package org.amikhalev.sprinklers.dto;

/**
 * Created by alex on 5/5/15.
 */
public class SectionObject {
    private Integer id;
    private String name;
    private String className;
    private String data;

    public SectionObject() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
