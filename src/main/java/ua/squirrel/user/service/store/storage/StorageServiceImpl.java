package ua.squirrel.user.service.store.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.squirrel.user.entity.store.storage.Storage;

@Service
public class StorageServiceImpl implements StorageService {

	private StorageRepository storageRepository;
	
	@Autowired
	public StorageServiceImpl(StorageRepository storageRepository) {
		this.storageRepository = storageRepository;
	}


	@Override
	public Storage save(Storage storage) {
		return storageRepository.save(storage);
	}

}
