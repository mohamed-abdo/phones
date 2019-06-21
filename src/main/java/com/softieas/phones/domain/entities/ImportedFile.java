package com.softieas.phones.domain.entities;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Imported_File")
public class ImportedFile extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID fileRef;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH, mappedBy = "importedFile")
    private List<Phone> phones;

    public UUID getFileRef() {
        return fileRef;
    }

    public void setFileRef(UUID fileRef) {
        this.fileRef = fileRef;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }
}
