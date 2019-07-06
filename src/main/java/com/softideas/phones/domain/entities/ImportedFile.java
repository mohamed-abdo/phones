package com.softideas.phones.domain.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "Imported_File")
@Getter
@Setter
public class ImportedFile extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID fileRef;


    @Column(name = "checksum", length = 50, nullable = false)
    private String checksum;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "importedFile")
    private Set<Phone> phones;
}
