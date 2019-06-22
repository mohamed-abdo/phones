package com.softieas.phones.domain.entities;

import com.softieas.phones.domain.models.PhoneStatus;
import com.softieas.phones.domain.models.RejectionReason;

import javax.persistence.*;


@Entity
@Table(name = "Phone")
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
    private PhoneStatus status;

    @Column(name = "rejection_code", length = 50)
    private RejectionReason rejection;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "imported_file_id", nullable = false)
    private ImportedFile importedFile;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getOriginalNumber() {
        return originalNumber;
    }

    public void setOriginalNumber(String originalNumber) {
        this.originalNumber = originalNumber;
    }

    public PhoneStatus getStatus() {
        return status;
    }

    public void setStatus(PhoneStatus status) {
        this.status = status;
    }

    public RejectionReason getRejection() {
        return rejection;
    }

    public void setRejection(RejectionReason rejection) {
        this.rejection = rejection;
    }

    public ImportedFile getImportedFile() {
        return importedFile;
    }

    public void setImportedFile(ImportedFile importedFile) {
        this.importedFile = importedFile;
    }

}
