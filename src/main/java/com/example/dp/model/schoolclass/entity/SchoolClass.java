package com.example.dp.model.schoolclass.entity;

import jakarta.persistence.*;
import com.example.dp.model.professor.entity.Professor;
import org.hibernate.envers.Audited;
import java.util.Objects;
import java.util.Set;

@Entity(name = "SCHOOL_CLASS")
@Audited
public class SchoolClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "class_code")
    private String classCode;

    @Column(name = "class_year")
    private int classYear;

    @ManyToMany(mappedBy = "classes")
    private Set<Professor> professors;

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

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public int getClassYear() {
        return classYear;
    }

    public void setClassYear(int classYear) {
        this.classYear = classYear;
    }

    public Set<Professor> getProfessors() {
        return professors;
    }

    public void setProfessors(Set<Professor> professors) {
        this.professors = professors;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Professor)) return false;
        Professor other = (Professor) o;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}