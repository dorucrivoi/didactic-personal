package com.example.dp.audit;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.data.domain.Auditable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class AuditRepository<T extends Auditable> {

    @PersistenceContext
    private EntityManager entityManager;

    public Number getRevisionNumber(LocalDateTime time) {
        final AuditReader auditReader = getAuditReader();
        return auditReader.getRevisionNumberForDate(time);
    }

    @SuppressWarnings("unchecked")
    public List<T> getEntitiesAtRevision(Class<T> entityClass, LocalDateTime dateTime) {
        final AuditReader auditReader = getAuditReader();
        Number revisionNumber = getRevisionNumber(dateTime);

        return auditReader.createQuery()
                .forEntitiesAtRevision(entityClass, revisionNumber)
                .getResultList();
    }

    private AuditReader getAuditReader() {
        return AuditReaderFactory.get(entityManager);
    }

}
