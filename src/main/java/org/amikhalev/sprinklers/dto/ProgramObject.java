package org.amikhalev.sprinklers.dto;

/**
 * Created by alex on 5/5/15.
 */
public class ProgramObject {
    private Integer id;
    private String name;
    private String schedule;
    private Boolean enabled;

    public ProgramObject() {
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

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
