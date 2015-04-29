package org.amikhalev.sprinklers.model;

import org.amikhalev.sprinklers.converters.SectionConverter;
import org.amikhalev.sprinklers.service.Section;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by alex on 4/28/15.
 */
@Entity
@Table(name = "Sections")
public class SectionModel {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "section")
    @Convert(converter = SectionConverter.class)
    private Section section;

    @OneToMany(mappedBy = "sectionModel", fetch = FetchType.LAZY)
    private Set<ProgramSection> programSections = new HashSet<>();

    public SectionModel() {
    }

    @PostLoad
    private void populateSection() {
        section.setId(this.getId());
        section.setName(this.getName());
    }

    @PreUpdate
    private void readSection() {
        this.setId(section.getId());
        this.setName(section.getName());
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

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public Set<ProgramSection> getProgramSections() {
        return programSections;
    }

    public void setProgramSections(Set<ProgramSection> programSections) {
        this.programSections = programSections;
    }
}
