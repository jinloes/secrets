package com.jinloes.secrets.model;

import java.util.UUID;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.data.domain.Auditable;

/**
 * Base class for audited entities.
 */
@MappedSuperclass
public abstract class AuditedEntity implements Auditable<UUID, UUID> {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private UUID id;
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdDate;
    private UUID createdBy;
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime modifiedDate;
    private UUID modifiedBy;

    @Override
    public UUID getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public DateTime getCreatedDate() {
        return createdDate;
    }

    @Override
    public void setCreatedDate(DateTime creationDate) {
        this.createdDate = creationDate;
    }

    @Override
    public UUID getLastModifiedBy() {
        return modifiedBy;
    }

    @Override
    public void setLastModifiedBy(UUID modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @Override
    public DateTime getLastModifiedDate() {
        return modifiedDate;
    }

    @Override
    public void setLastModifiedDate(DateTime lastModifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @Override
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public boolean isNew() {
        return false;
    }

    @PrePersist
    protected void onCreate() {
        createdDate = new DateTime();
    }
}
