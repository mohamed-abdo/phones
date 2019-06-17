package com.softieas.phones.domain.entities;

import com.softieas.phones.domain.models.ImportStatus;
import com.softieas.phones.domain.models.RejectionReason;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Invalid_Phone")
public class InvalidPhone {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "number", length = 20, nullable = false, unique = true)
    private String number;

    @Column(name = "status_code",length = 10, nullable = false)
    private ImportStatus status;

    @Column(name = "rejection_code",length = 50)
    private RejectionReason rejection;
}
