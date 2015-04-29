package org.amikhalev.sprinklers.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 4/21/15.
 */
@Entity
@Table(name = "Programs")
public class Program {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "schedule", nullable = false)
    private String schedule;

    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;

    @OneToMany(mappedBy = "program", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OrderColumn(name = "index")
    private List<ProgramSection> sections = new ArrayList<ProgramSection>();

    public Program() {
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<ProgramSection> getSections() {
        return sections;
    }

    public void setSections(List<ProgramSection> sections) {
        this.sections = sections;
        sections.stream().forEach(section -> section.setProgram(this));
    }

    @Override
    public String toString() {
        return "Program{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", schedule='" + schedule + '\'' +
                ", enabled=" + enabled +
                ", sections=" + sections +
                '}';
    }
}
