package com.softieas.phones.domain.entities;

import com.softieas.phones.domain.models.ImportStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Phone")
public class Phone {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "number", length = 20, nullable = false, unique = true)
    private String number;

    @Column(name = "status",length = 10, nullable = false)
    private ImportStatus status;
}
