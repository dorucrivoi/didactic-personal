package com.example.dp.model.professor.entity;

import com.example.dp.model.schoolclass.entity.SchoolClass;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.util.Objects;
import java.util.Set;

@Entity(name="PROFESSOR")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Audited
public class Professor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private Long id;

    @Column(name="NAME")
    private String name;
    @Column(name="DISCIPLINE_CODE")
    private String disciplineCode;
    @Column(name="PROFESSOR_CODE")
    private String professorCode;

   // @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToMany
    @JoinTable(
            name = "CLASS_PROFESSOR",
            joinColumns = @JoinColumn(name = "PROFESSOR_ID"),
            inverseJoinColumns = @JoinColumn(name = "CLASS_ID")
    )
    private Set<SchoolClass> classes;

    public Set<SchoolClass> getClasses() {
        return classes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisciplineCode() {
        return disciplineCode;
    }

    public void setDisciplineCode(String disciplineCode) {
        this.disciplineCode = disciplineCode;
    }

    public void setClasses(Set<SchoolClass> classes) {
        this.classes = classes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Professor professor)) return false;
        return Objects.equals(id, professor.id) &&
                Objects.equals(name, professor.name) &&
                Objects.equals(disciplineCode, professor.disciplineCode) &&
                Objects.equals(classes, professor.classes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, disciplineCode, classes);
    }
// Getters and setters
}