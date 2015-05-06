package org.amikhalev.sprinklers.model;

import com.google.common.base.Objects;

import javax.persistence.*;

/**
 * Created by alex on 4/28/15.
 */
@Entity
@Table(name = "ProgramSections")
//@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
public class ProgramSection {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue
    private Integer id;

    @Column(name = "index")
    private Integer index;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id")
    private Program program;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "section_id")
    private SectionModel sectionModel;

    @Column
    private Double time;

    public ProgramSection() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        if (program != null && !program.containsSection(this)) {
            throw new IllegalArgumentException("Parent program must contain this section");
        }
        this.program = program;
    }

    public SectionModel getSectionModel() {
        return sectionModel;
    }

    public void setSectionModel(SectionModel sectionModel) {
        if (Objects.equal(this.sectionModel, sectionModel))
            return;
        if (this.sectionModel != null)
            this.sectionModel.removeProgramSection(this);
        if (sectionModel != null)
            sectionModel.addProgramSection(this);
        this.sectionModel = sectionModel;
    }

    public Double getTime() {
        return time;
    }

    public void setTime(Double time) {
        this.time = time;
    }
}
