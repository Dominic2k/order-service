package org.datpham.orderservice.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity {
    private boolean isDeleted;
    private Instant createdDate;
    private Instant updatedDate;
    private String createdBy;
    private String updatedBy;
}
