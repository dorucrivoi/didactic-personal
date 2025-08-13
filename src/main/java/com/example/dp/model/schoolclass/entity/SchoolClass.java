package com.example.dp.model.schoolclass.entity;

import jakarta.persistence.*;
import com.example.dp.model.professor.entity.Professor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.util.Set;

@Entity(name = "SCHOOL_CLASS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
//    @ManyToMany
//    @JoinTable(
//        name = "class_professor",
//        joinColumns = @JoinColumn(name = "class_id"),
//        inverseJoinColumns = @JoinColumn(name = "professor_id")
//    )
//    private Set<Professor> professors;

//    @ManyToMany
//    @JoinTable(
//            name = "CLASS_PROFESSOR",
//            joinColumns = @JoinColumn(name = "CLASS_ID"),
//            inverseJoinColumns = @JoinColumn(name = "PROFESSOR_ID")
//    )
//    private Set<Professor> professors;

    // Getters and setters
}