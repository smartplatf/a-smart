package org.anon.smart.d2cache.store.fileStore.hadoop;

import org.anon.smart.d2cache.store.StoreConfig;

public interface HadoopStoreConfig extends StoreConfig {
	
	public String baseDirectory();
	public String getFSDefaultName();
	public String getHadoopTempDir();
	public String getDFSReplication();
}
