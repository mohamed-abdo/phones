package com.softideas.phones.domain.repositories;

import com.softideas.phones.domain.entities.ImportedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ImportedFileRepository extends JpaRepository<ImportedFile, UUID> {
    @Query("select f from ImportedFile f where f.checksum=:checksum")
    Optional<ImportedFile> findByChecksum(@Param(value = "checksum") String checksum);
}
