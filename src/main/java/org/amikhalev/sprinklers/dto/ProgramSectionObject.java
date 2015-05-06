package org.amikhalev.sprinklers.dto;

/**
 * Created by alex on 5/5/15.
 */
public class ProgramSectionObject {
    private Integer id;
    private Double time;
    private Integer section;

    public ProgramSectionObject() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getTime() {
        return time;
    }

    public void setTime(Double time) {
        this.time = time;
    }

    public Integer getSection() {
        return section;
    }

    public void setSection(Integer section) {
        this.section = section;
    }
}
