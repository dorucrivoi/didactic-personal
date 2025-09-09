package com.example.dp.model.professor.entity;

import com.example.dp.model.schoolclass.entity.SchoolClass;
import jakarta.persistence.*;
import org.hibernate.envers.Audited;

import java.util.Objects;
import java.util.Set;

@Entity(name="PROFESSOR")
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
    @Column(name="CODE")
    private String code;

    @ManyToMany(cascade = CascadeType.ALL)
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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