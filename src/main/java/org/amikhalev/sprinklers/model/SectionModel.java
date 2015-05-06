package org.amikhalev.sprinklers.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.amikhalev.sprinklers.model.converters.SectionConverter;
import org.amikhalev.sprinklers.service.Section;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by alex on 4/28/15.
 */
@Entity
@Table(name = "Sections")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
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
    @JsonIgnore
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

    @JsonValue
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
        programSections.stream().forEach(programSection -> programSection.setSectionModel(this));
        this.programSections = programSections;
    }

    public void addProgramSection(ProgramSection programSection) {
        if (!programSections.contains(programSection)) {
            programSections.add(programSection);
            programSection.setSectionModel(this);
        }
    }

    public boolean removeProgramSection(ProgramSection programSection) {
        if (programSections.remove(programSection)) {
            programSection.setSectionModel(null);
            return true;
        } else {
            return false;
        }
    }
}
