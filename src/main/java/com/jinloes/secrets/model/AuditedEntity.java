package com.jinloes.secrets.model;

import java.util.Objects;
import java.util.UUID;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Auditable;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Base class for audited entities.
 */
public abstract class AuditedEntity implements Auditable<String, String> {
    @Id
    @Field("_id")
    private String id;
    private DateTime createdDate;
    private String createdBy;
    private DateTime modifiedDate;
    private String modifiedBy;

    @Override
    public String getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(String createdBy) {
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
    public String getLastModifiedBy() {
        return modifiedBy;
    }

    @Override
    public void setLastModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @Override
    public DateTime getLastModifiedDate() {
        return modifiedDate;
    }

    @Override
    public void setLastModifiedDate(DateTime lastModifiedDate) {
        this.modifiedDate = lastModifiedDate;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean isNew() {
        return false;
    }
}
