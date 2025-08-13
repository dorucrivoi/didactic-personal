package com.example.dp.audit;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;
import org.springframework.data.annotation.Id;

@Entity
@RevisionEntity
@Table(name = "REVINFO")
@Getter
@Setter
public class RevInfo {

    @Id
    @GeneratedValue
    @RevisionNumber
    private int rev;

    @RevisionTimestamp
    private long revtstmp;

    public int getRev() {
        return rev;
    }

    public void setRev(int rev) {
        this.rev = rev;
    }

    public long getRevtstmp() {
        return revtstmp;
    }

    public void setRevtstmp(long revtstmp) {
        this.revtstmp = revtstmp;
    }
}
