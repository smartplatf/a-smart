package org.anon.smart.d2cache.store.fileStore.disk;

import org.anon.smart.d2cache.store.StoreConfig;

public interface DiskStoreConfig extends StoreConfig {
	public String baseDirectory();
}
