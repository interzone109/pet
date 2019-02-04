package ua.squirrel.user.service.store.storage;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.squirrel.user.entity.store.storage.Storage;

public interface StorageRepository extends JpaRepository<Storage, Long> {

}
