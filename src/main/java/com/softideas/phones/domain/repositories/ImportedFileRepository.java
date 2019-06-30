package com.softideas.phones.domain.repositories;

import com.softideas.phones.domain.entities.ImportedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ImportedFileRepository extends JpaRepository<ImportedFile, UUID> {
}
