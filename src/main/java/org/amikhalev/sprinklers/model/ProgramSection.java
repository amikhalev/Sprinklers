package org.amikhalev.sprinklers.model;

import javax.persistence.*;

/**
 * Created by alex on 4/28/15.
 */
@Entity
@Table(name = "ProgramSections")
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
    private double time;

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
        this.program = program;
    }

    public SectionModel getSectionModel() {
        return sectionModel;
    }

    public void setSectionModel(SectionModel sectionModel) {
        this.sectionModel = sectionModel;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }
}
