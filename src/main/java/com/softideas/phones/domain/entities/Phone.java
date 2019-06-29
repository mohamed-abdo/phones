package com.softideas.phones.domain.entities;

import com.softideas.phones.domain.models.RejectionReason;
import com.softideas.phones.domain.models.PhoneNumberStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Table(name = "Phone")
@Getter
@Setter
public class Phone extends Auditable {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "number", length = 20, nullable = false)
    private String number;

    @Column(name = "original_number", length = 20, nullable = false)
    private String originalNumber;

    @Column(name = "status", length = 10, nullable = false)
    private PhoneNumberStatus status;

    @Column(name = "rejection_code", length = 50)
    private RejectionReason rejection;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "imported_file_id", nullable = false)
    private ImportedFile importedFile;

}